# Kogito Examples Parent

## Description
This module serves as an invocation point for tests of kogito-examples repository.

## Usage
Project pom defines profiles that can invoke tests for a particular delivery option.
Download of artifacts is provided by means described in [Module structure](#module-structure).

### Project-sources testing
```
mvn clean verify \
-Dtest.type=project-sources \
-Dproject.sources.artifact=kogito-examples \
-Ddownload.sources.version=<VERSION>
```
### Sources-zip testing
```
mvn clean verify \
-Dtest.type=sources-zip \
-Ddownload.sources.url=http://link.to/source.zip
```
### Quickstarts-zip testing
```
mvn clean verify \
-Dtest.type=quickstarts-zip \
-Ddownload.sources.url=http://link.to/quickstarts.zip
```
### Scm-checkout testing
```
mvn clean verify \
-Dtest.type=scm-checkout \
-Drepository.url=https://github.com/kiegroup/kogito-examples \
-Dsources.revision=1.11.x
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