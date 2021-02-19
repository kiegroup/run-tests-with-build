run-tests-with-build-kogito-runtimes
------------------------------------

This module executes [kogito-runtimes](https://github.com/kiegroup/kogito-runtimes) integration tests modules with specific build version.
  * [kogito-runtimes/integration-tests](https://github.com/kiegroup/kogito-runtimes/tree/master/integration-tests)
  * [kogito-runtimes/kogito-quarkus-extension](https://github.com/kiegroup/kogito-runtimes/tree/master/kogito-quarkus-extension)

Execution
---------
Tests are started from a project sources archive. By default, we use latest kogito build version. You can override this using `-Dkogito.download.build.version` property.

For running tests in quarkus native mode use  `-Dnative` property.

Note
----
Module is under development. Not all tests are executed currently. See the exclusions in the pom.xml.