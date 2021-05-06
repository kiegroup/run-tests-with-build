run-tests-with-build-kogito-runtimes
------------------------------------

This module executes [kogito-runtimes](https://github.com/kiegroup/kogito-runtimes) tests.

This module relies on automatic Maven modules resolution. The goal here is to invoke just so-called
**leaf** Maven projects to enforce usage of previously built and published maven dependencies from repository. By
invoking just leaf projects, they are detached from the overall Maven reactor build - when compared to running
project from the parent.

That's why there might be discrepancy between real structure of repository and what is executed.

More information in parent project [README.md](../README.md).

Execution
---------
Tests are started from a project sources archive. By default, we use latest kogito build version. You can override this using `-Dkogito.download.build.version` property.
Whole build including modules resolution supports custom Maven settings limited to `-s settings.xml -Dmaven.repo.local=`.

For running tests in quarkus native mode use  `-Dnative` property.

Note
----
Module executions is based on maven module filtering using maven property `${maven.modules.resolution.reactor.filtering}`.