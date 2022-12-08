# Optawebs Parent

## Description
This module serves as an invocation point for tests of OptaWeb repositories.

## Usage
Project pom defines profiles that can invoke tests for a particular delivery option.
Download of artifacts is provided by means described in [Module structure](#module-structure).

### Project-sources testing

* openshift certification
  ```
  mvn clean verify \
  -DskipTests -Dtest.type=sources-zip \
  -Ddownload.sources.url=http://link.to/source.zip \
  -Dopenshift 
  -Dopenshift.api-url=https://api.crc.testing:6443\
  -Dopenshift.user=developer
  -Dopenshift.password=developer
  -Dsettings.xml.file=../settings/settings.xml
  ```
### Sources-zip testing
```
mvn clean verify \
-Dtest.type=sources-zip \
-Ddownload.sources.url=http://link.to/source.zip
```

### Additional configuration
* `-Dintegration-tests={true|false}` run integration cypress tests on docker
* `-Dcontainer.runtime={docker|podman}` change runtime
* `-Dskip.invoker.tests={true|false}` skip JUnit tests during build of the modules 

## Module structure
Each of sub-modules contains configuration for Maven Invoker related settings.

Download configuration of particular deliverable is provided by modules
* [run-tests-with-build-sources-zip-download](../run-tests-with-build-sources-zip-download)
  * Used for downloads from arbitrary url.
* [run-tests-with-build-project-sources-dependency-get](../run-tests-with-build-project-sources-dependency-get)
  * Used for tar.gz archives with the classifier project-sources from nexus repository.
