run-tests-with-build-kogito-decision-services-quickstarts
---------------------------------------------------------

Run kogito-examples and optaplanner-quickstarst tests delivered as part of decision-services-quickstarts zip

- [kogito-examples](https://github.com/kiegroup/kogito-examples) - just chosen submodules
- [optaplanner-quickstarts](https://github.com/kiegroup/optaplanner-quickstarts)

Execution
---------
The url of the decision-services-quickstarts zip needs to be specified by `-Dkogito.decision.services.quickstarts.url` property.

For running tests in quarkus native mode use  `-Dnative` property.

Note
----
Module is under development. Not all tests are executed currently. See the exclusions in the pom.xml.
