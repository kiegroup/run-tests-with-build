run-tests-with-build-optaplanner
------------------------------------
Operates on Optaplanner product build archive
Does not run turtle tests

Execution
-------------------------------------
`mvn -fae verify -s $PROJECT_SETTINGS -Doptaplanner.download.build.version=${OPTAPLANNER_BUILD_VERSION}`

where `$PROJECT_SETTINGS` is the settings.xml with the repository which contains ${OPTAPLANNER_BUILD_VERSION} archive