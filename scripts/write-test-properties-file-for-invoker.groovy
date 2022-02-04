import java.util.stream.Collectors

/**
 * Contents of test.properties file to be written. Contains key=value entries.
 * If inputListAsString argument is not an empty string, only subset of system properties is written,
 * based on the provided comma separated list of properties.
 * @param inputListAsString subset of system properties to be written to file, empty string to include all
 * @return String with contents of the file
 */
private String getTestPropertiesFileContents(String inputListAsString) {
    Properties systemProperties = session.userProperties
    Set<String> resultingProperties = systemProperties.keySet()

    def listOfPropertiesToWrite = inputListAsString.split(',').toList().collect { it.trim() }
    if (listOfPropertiesToWrite.size() > 0) {
        resultingProperties.removeIf({ it -> !listOfPropertiesToWrite.contains(it) })
    }
    return resultingProperties.stream()
            .map(it -> "$it=${session.userProperties[it]}")
            .collect(Collectors.joining("\n"))
}
/**
 * Method that writes test properties file next to each pom.xml file. The name of the newly
 * created file is the first parameter.
 * The search for pom.xml files starts at location denoted by basedir configuration option.
 * @param fileName of the newly created file.
 * @param contentsToWrite Contents of the file to be written
 */
private void writeTestProperties(String fileName, String contentsToWrite) {
    log.info("Contents of properties file being written:\n$contentsToWrite")
    new File(basedir).eachFileRecurse { it ->
        if (it.name.equalsIgnoreCase("pom.xml")) {
            def testPropsFile = new File(it.getParent(), fileName)
            log.info("Writing properties as $testPropsFile")
            testPropsFile.write(contentsToWrite)
        }
    }
}

String contentsToWrite = getTestPropertiesFileContents(listedProperties)
writeTestProperties(testPropertiesFileName, contentsToWrite)
