run-tests-with-build-kogito-runtimes
------------------------------------

This module executes [kogito-runtimes](https://github.com/kiegroup/kogito-runtimes) tests. Unfortunately there is no synchronization mechanism. If the **kogito-runtimes** project structure will be changed, it needs to by manually adapted also in this module.

Execution
---------
Tests are started from a project sources archive. By default, we use latest kogito build version. You can override this using `-Dkogito.download.build.version` property.

For running tests in quarkus native mode use  `-Dnative` property.

Note
----
Module is under development. Not all tests are executed currently. See the exclusions in the pom.xml.

[KOGTIO-4404](https://issues.redhat.com/browse/KOGITO-4404) is blocking for:
  * [integration-tests-kogito-plugin](https://github.com/kiegroup/kogito-runtimes/tree/master/integration-tests/integration-tests-kogito-plugin)
  * [integration-tests-springboot](https://github.com/kiegroup/kogito-runtimes/tree/master/integration-tests/integration-tests-springboot)
