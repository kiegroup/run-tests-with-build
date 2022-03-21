import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Executes given maven command
 * @param mvnCommand String containing the command
 * @return stdout of the execution as list lines
 */
private Stream<String> mvnExecute(String mvnCommand) {
    def out = new StringBuilder(), err = new StringBuilder()
    log.info("Resolving Maven reactor projects for command '$mvnCommand'")
    log.info("Waiting up to $mavenExecutionTimeoutMs ms for resolution to end.")
    def stdoutLogFile = "mvn-out.log"
    log.info("Standard output will be available in ${Path.of(logdir).resolve(stdoutLogFile).toString()} after the build finishes.")
    // combine session.userProperties and System.getenv and pass into the process
    Closure<Map.Entry> formatProp = { it -> "$it.key=$it.value" }
    List envPropsList = session.userProperties.collect(formatProp) + System.getenv().collect(formatProp)
    log.debug("Passing env properties: ${envPropsList.toString()}")
    def mvnExec = mvnCommand
            .execute(envPropsList, new File(basedir)
                    .listFiles({ d, f -> Files.isDirectory(d.toPath().resolve(f)) } as FilenameFilter)[0])
    mvnExec.consumeProcessOutput(out, err)
    mvnExec.waitForOrKill(Long.valueOf(mavenExecutionTimeoutMs))
    logDebug(stdoutLogFile, out.toString().lines().collect(Collectors.toList()))
    if (mvnExec.exitValue() != 0) {
        def stderrLogFile = "err.log"
        log.error("Maven build unsuccessful with code ${mvnExec.exitValue()}, " +
                "see ${Path.of(logdir).resolve(stdoutLogFile).toString()} and/or ${Path.of(logdir).resolve(stderrLogFile).toString()} for details.")
        logDebug(stderrLogFile, err.toString().lines().collect(Collectors.toList()))
        throw new RuntimeException("Mvn command ended with error code ${mvnExec.exitValue()}")
    }
    return out.toString().lines()
}

/**
 * Trim path prefix up to given basedir.
 * @param value absolute path to be trimmed
 * @param prefix absolute path to be used as prefix
 * @return relative path between the two
 */
private String trimPrefix(Path value, Path prefix) {
    return prefix.relativize(value).toString();
}

/**
 * Return a list of string paths to pom project directories that are supposed to be leaf ones - no sub-modules.
 * @param projects string paths for all the projects within reactor
 * @return list of strings for paths that don't have any children in the list
 */
private List<String> resolveLeafProjects(Stream<String> projects) {
    List<String> lines = projects.map({ it -> trimPrefix(Path.of(it), new File(basedir).toPath()) }).collect(Collectors.toList()).sort()
    List<String> leafProjects = new ArrayList<>()
    def logFile = "leaf-projects.log"
    log.info("Starting leaf projects resolution, results will be in ${Path.of(logdir).resolve(logFile).toString()}")
    for (int i = 0; i < lines.size(); i++) {
        String current = lines[i]
        if (lines.size() > i + 1) {
            String next = lines[i + 1]
            if (!Path.of(next).startsWith(current)) {
                log.debug("Adding $current as a leaf project, it has no child")
                leafProjects.add(current)
            } else {
                log.debug("Skipping '$current' project, cause it has child '$next'")
            }
        } else {
            log.debug("Adding $current as a leaf project, cause no other project is in reactor.")
            leafProjects.add(current)
        }
    }
    logDebug(logFile, leafProjects)
    return leafProjects
}

/**
 * Log into given file contents of the provided list of paths.
 * @param fileName target dir to be created within logdir (passed from config)
 * @param projectList list of paths
 */
private void logDebug(String fileName, ArrayList<String> projectList) {
    def logdirPath = Path.of(logdir)
    if (!Files.isDirectory(logdirPath)) {
        Files.createDirectory(logdirPath)
    }
    logdirPath.resolve(fileName).toFile().write projectList.stream().map({ it ->
        it + System.lineSeparator()
    }).collect(Collectors.joining())
}

/**
 * Recursively find all folders that contain pom.xml
 * @return list of string paths
 */
private List<String> findAllDirsWithPom() {
    File base = new File(basedir)
    List<File> poms = new ArrayList<>()
    base.traverse(type: groovy.io.FileType.FILES, nameFilter: "pom.xml") { it -> poms.add(it) }
    return poms.stream().map({ pom -> trimPrefix(pom.toPath().getParent().toAbsolutePath(), base.toPath()) }).collect(Collectors.toList())
}

/**
 * Write a invokerScriptName named file into given locations. Write contents return value.
 * @param locations list of string paths
 * @param value value to be written (false/true)
 */
private void writeInvokerScripts(List<String> locations, String value) {
    locations.forEach({ location ->
        def filePath = Path.of(basedir).resolve(location).resolve(invokerScriptName).toString()
        new File(filePath).write "return $value"
    })
}

/**
 * Get Maven command string including possible arguments passed from configuration.x`
 * Tailor the mvn command used to inherit settings of the invoking maven build.
 * @return mvn command string
 */
private String getMvnCommand() {
    String mvnCommandToRun = "mvn -q -am exec:exec -Dexec.executable=pwd -Dexec.args=\"\""
    if (mavenReactorFiltering) {
        mvnCommandToRun = mvnCommandToRun + " $mavenReactorFiltering"
    }
    if (mavenRepoLocal) {
        mvnCommandToRun = mvnCommandToRun + " -Dmaven.repo.local=$mavenRepoLocal"
    }
    if (mavenSettings && Files.exists(Path.of(mavenSettings))) {
        mvnCommandToRun = mvnCommandToRun + " -s $mavenSettings"
    }
    if (mavenAdditionalOptions) {
        mvnCommandToRun = mvnCommandToRun + " $mavenAdditionalOptions"
    }
    return mvnCommandToRun
}

/**
 * Method to prevent execution of provided projects by adding invoker-specific script into project folder.
 * @param toExclude list of projects to exclude
 */
private void excludeFromInvokerExecution(List toExclude) {
    def ignoredLog = "ignored-interim-poms.log"
    log.info("Ignored projects are logged as ${Path.of(logdir).resolve(ignoredLog).toString()} .")
    logDebug(ignoredLog, toExclude)
    writeInvokerScripts(toExclude, "false")
}

/**
 * Copy invoker.properties into each project dir provided.
 * @param projects list of relative paths to particular projects
 */
private void copyInvokerPropertiesToProjects(List<String> projects) {
    def basedirPath = Path.of(basedir)
    def invokerPropertiesFileName = "invoker.properties"
    Path invokerPropertiesFile = basedirPath.resolve(invokerPropertiesFileName)
    if (Files.exists(invokerPropertiesFile)) {
        log.info("Copying invoker.properties file into all leaf projects. (from ${invokerPropertiesFile.toString()})")
        projects.each { relativePath -> Files.copy(invokerPropertiesFile, basedirPath.resolve(relativePath).resolve(invokerPropertiesFileName)) }
    } else {
        log.info("Not using invoker.properties. No invoker.properties file found in ${invokerPropertiesFile.toString()}")
    }
}

private List<String> resolveProjectExplicitFiltering(ArrayList<String> leafProjects) {
    List<String> projects = mavenProjectExplicitFiltering.split(',')
    List<String> filteredLeafProjects = new ArrayList<>()
    List<String> filteredOutLeafProjects = new ArrayList<>()
    String filteringTYpe = mavenProjectExplicitFilteringType

    def logFile = "filter-projects.log"
    log.info("Starting explicit filtering projects resolution, results will be in ${Path.of(logdir).resolve(logFile).toString()}")

    if (projects && !projects.empty) {
        for (int i = 0; i < leafProjects.size(); i++) {
            String current = leafProjects[i]
            for (int j = 0; j < projects.size(); j++) {
                String filter = projects[j]

                if (Path.of(current).endsWith(filter)) {
                    if (filteringTYpe.equals("in")) {
                        filteredLeafProjects.add(current)
                        log.debug("Adding $current as a selected project for tests")
                    } else if (filteringTYpe.equals("out")) {
                        filteredOutLeafProjects.add(current)
                        log.debug("Skipping '$current' project, cause it has been filtered out")
                    }
                }
            }
        }
    }

    if (filteringTYpe.equals("in")) {
        logDebug(logFile, filteredLeafProjects)
        return filteredLeafProjects
    } else if (filteringTYpe.equals("out")) {
        logDebug(logFile, filteredOutLeafProjects)
        return leafProjects.minus(filteredOutLeafProjects)
    } else {
        logDebug(logFile, filteredOutLeafProjects)
        return leafProjects
    }
}

Stream<String> projectsInReactor = mvnExecute(getMvnCommand())
List leafProjects = resolveLeafProjects(projectsInReactor)
List dirsWithPoms = findAllDirsWithPom()
List filteredLeafProjects = resolveProjectExplicitFiltering(leafProjects)
List toExclude = dirsWithPoms.minus(filteredLeafProjects)
copyInvokerPropertiesToProjects(leafProjects)
excludeFromInvokerExecution(toExclude)