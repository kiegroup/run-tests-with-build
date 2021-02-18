
run-tests-with-build-kogito-apps
------------------------------------

This module executes [kogito-apps](https://github.com/kiegroup/kogito-apps) integration tests module with specific build version.
* [kogito-apps/apps-integration-tests](https://github.com/kiegroup/kogito-apps/tree/master/apps-integration-tests)

Execution
---------
Tests are started from a project sources archive. User need to specify the archive location `-Dsources.url`

For example

`mvn clean install -Dsources.url=<mvn.repository.url>/org/kie/kogito/kogito-apps/<version>/kogito-apps-<version>-project-sources.tar.gz`

Note
----
Module is under development. Not all tests are executed currently. See the exclusions in the pom.xml.