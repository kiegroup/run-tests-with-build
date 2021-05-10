# run-tests-with-build

TODO

---
Note: `run-tests-with-build` project is dependent on https://github.com/baldimir/unpack-build-maven-plugin

### KIE OSGI tests

```
Parameters:
FUSE_VERSIONS_PROPERTY_FILE ... Property file with Fuse versions and binaries
```

### Camel in container tests

```
Parameters:
BXMS_DELIVERABLES_PROPERTIES ... List of deliverables
FUSE_VERSIONS_PROPERTY_FILE ... Property file with Fuse versions and binaries
eap.url ... URL with EAP distribution file
```

### Kogito and OptaPlanner modules
There is a specific parent module for the repositories:
* [run-tests-with-build-kogito-runtimes-parent](./run-tests-with-build-kogito-runtimes-parent)
* [run-tests-with-build-kogito-examples-parent](./run-tests-with-build-kogito-examples-parent)
* [run-tests-with-build-optaplanner-parent](./run-tests-with-build-optaplanner-parent)
* [run-tests-with-build-optaplanner-quickstarts-parent](./run-tests-with-build-optaplanner-quickstarts-parent)
* [run-tests-with-build-optawebs-parent](./run-tests-with-build-optawebs-parent)
#### Approach

Kogito modules fall into two categories regarding what is tested and how downloaded:

1. Tests for already built and published maven artifacts covered in modules:
* **\*\*/\*-project-sources-test**

2. Tests for zips with project sources covered in modules:
* **\*\*/\*-sources-zip-test**
* **\*\*/\*-quickstarts-zip-test**

#### Testing Previously Built Maven Artifacts using the original project tests

* The main goal is here to verify functionality and integrity of the maven artifacts after the build process.
* During the repository build whole project is packaged into project-sources.tar.gz archive and this is published in
  nexus.
* Using maven-dependency plugin this artifact can be downloaded and extracted easily.
* Maven GAV coordinates are similar to:
  ```xml
  <artifactItem>
    <groupId>org.kie.kogito</groupId>
    <artifactId>kogito-runtimes</artifactId>
    <version>${kogito.download.build.version}</version>
    <classifier>project-sources</classifier>
    <type>tar.gz</type>
  </artifactItem>
  ```
* To achieve the desired level of separation (not to build everything from downloaded sources), only leaf projects are
  to be invoked during the build (those projects that do not have children modules). Reasoning here is that building a
  parent project puts all its modules (possibly dependent between each other) into the same Maven reactor, thus not
  using the previously built and published artifacts in a repository.
* For this purpose the [resolve-includes groovy script](#resolve-includes-groovy-script) is provided. This script is
  supposed to be invoked after sources download, but before Maven invoker invocation.

#### Testing zips with sources

* In this case the main focus is on building the project as a whole, thus checking the build itself, not anything else.
* Approach here is to download and extract the archive with sources and pass respective pom.xml into Invoker.
* Here it is desirable to invoke a top-level pom.xml, so no need for any resolving script at all.

### Resolve Includes Groovy Script

* Relies on Maven invoker
  plugin [selectorScript property](https://maven.apache.org/plugins/maven-invoker-plugin/run-mojo.html#selectorScript)
  to alter the way how invoker processes the full repository. Writes such a groovy script (denoted by
  property `invokerScriptName`)
  with body `return false`, which is executed for each pom.xml specified to Maven invoker using `pomIncludes`
  configuration.
* Gives configuration options to scope the invoker build just to a profile defined in the original project pom.xml,
  which allows for smooth selective execution of the modules.
    * Be careful when specifying a profile to be invoked by Maven invoker. Such setting does not change the scope of
      invocation, does not alter what modules from the project are being invoked. It just invokes the profile in all
      those projects that are already included, so it will try to invoke also modules that don't have the profile
      specified.
    * If the project module enables such a filtering, let's say a profile in parent pom activated by `-Dnative` having
      in
      `modules` element just modules that define some build profile which is again activated by the same property, it
      needs to be accompanied by advanced maven modules resolution by also altering `mavenReactorFiltering`
      passed to the script.
* Paths relative to folder denoted by `basedir` property.
* Logs placed into folder denoted by`logdir` location.
* Resolution is done using an internal maven exec:exec command.
    * Artifact resolution might take some time based on environment, timeout is set by `mavenExecutionTimeoutMs`
      property.
    * To alter Maven build two properties are available `mavenRepoLocal` and `mavenSettings` expecting local filesystem
      paths.
