# OptaPlanner Parent

## Description
This module serves as an invocation point for tests of OptaPlanner repository.

## Usage
Project pom defines profiles that can invoke tests for a particular delivery option.
Download of artifacts is provided by means described in [Module structure](#module-structure).

### Project-sources testing
```
mvn clean verify \
-Dtest.type=project-sources \
-Dproject.sources.artifact=optaplanner \
-Doptaplanner.download.build.version=<VERSION>
```
### Sources-zip testing
```
mvn clean verify \
-Dtest.type=sources-zip \
-Ddownload.source.url=http://link.to/source.zip
```

## Module structure
Each of sub-modules contains configuration for Maven Invoker related settings.

Download configuration of particular deliverable is provided by modules
* [run-tests-with-build-sources-zip-download](../run-tests-with-build-sources-zip-download)
  * Used for downloads from arbitrary url.
* [run-tests-with-build-project-sources-dependency-get](../run-tests-with-build-project-sources-dependency-get)
  * Used for tar.gz archives with the classifier project-sources from nexus repository.
