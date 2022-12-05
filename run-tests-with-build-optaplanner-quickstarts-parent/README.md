# OptaPlanner Quickstarts Parent

## Description
This module serves as an invocation point for tests of optaplanner-quickstarts-parent repository.

## Usage
Project pom defines profiles that can invoke tests for a particular delivery option.
Download of artifacts is provided by means described in [Module structure](#module-structure).

### Sources-zip testing
```
mvn clean verify \
-Dtest.type=sources-zip \
-Ddownload.sources.url=http://link.to/source.zip
```

### Scm-checkout testing
```
mvn clean verify \
-Dtest.type=scm-checkout \
-Drepository.url=https://github.com/kiegroup/optaplanner-quickstarts \
-Dsources.revision=8.11.x
```

### Hello-world testing
```
mvn clean verify \
-Dtest.type=sources-zip \
-Ddownload.sources.url=http://link.to/source.zip
-Dquickstart-baseline
```

## Passing system properties
### Static
Any system properties that needs to be passed to particular invoked project, and are known
in advance, they can be configured to invoker-maven-plugin directly in its configuration. An
example is using the `-Dfull` property by default in the configuration:
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-invoker-plugin</artifactId>
  <configuration>
    <properties>
      <full>true</full>
    </properties>
  </configuration>
</plugin>
```
### Dynamic and optional system properties
If more variability is needed in terms of setting system properties for invoked build - there's an
optional step that can be added to the `*-test` maven module build section.
The following step will make sure that system properties are passed to invoked project
using `invoker-maven-plugin`'s configuration option `<testPropertiesFile>`.
```xml
<plugin>
  <groupId>org.codehaus.gmavenplus</groupId>
  <artifactId>gmavenplus-plugin</artifactId>
  <executions>
    <execution>
      <id>write-test-properties-file-for-invoker</id>
      <phase>generate-resources</phase>
      <goals>
        <goal>execute</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```
By default, all properties passed to run-tests-with-build are passed - this can be changed by
the configuration property `invoker.test.properties.list`, which is a comma-separated list
of system properties that are passed (but only if they are actally passed to overall run-tests-with-build maven build).
This will help you to filter out run-tests-with-build internally used properties (e.g. `-Dtest.type`).
```xml
<properties>
  <invoker.test.properties.list>quarkus.platform.group-id</invoker.test.properties.list>
</properties>
```

## Module structure
Each of sub-modules contains configuration for Maven Invoker related settings.

Download configuration of particular deliverable is provided by modules
* [run-tests-with-build-sources-zip-download](../run-tests-with-build-sources-zip-download)
  * Used for downloads from arbitrary url.
* [run-tests-with-build-project-sources-dependency-get](../run-tests-with-build-project-sources-dependency-get)
  * Used for tar.gz archives with the classifier project-sources from nexus repository.
* [run-tests-with-build-scm-checkout](../run-tests-with-build-scm-checkout)
  * Used for git repo checkout using repository URL and revision.
