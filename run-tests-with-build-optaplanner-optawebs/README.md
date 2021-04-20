    run-tests-with-build-optaplanner-optawebs
------------------------------------
Run tests OptaWeb backend, frontend and standalone, including integration

Execution
-------------------------------------
`mvn verify -Doptaweb=$OPTAWEB_NAME -s $PROJECT_SETTINGS -Doptaplanner.download.build.version=$OPTAPLANNER_BUILD_VERSION`

where 

`$OPTAWEB_NAME` is a profile name {vehicle-routing|employee-rostering} 

`$PROJECT_SETTINGS` is the settings.xml with the repository which contains ${OPTAPLANNER_BUILD_VERSION} tar.gz

`$OPTAPLANNER_BUILD_VERSION` redhat distribution artifact tar.gz

`-Pintegration-tests` run integration cypress tests on docker

`-Dcontainer.runtime={docker|podman}` change runtime

---------------------------------------
Issues

`Unknown host ...` -  check if you are connected to the Kerberos

`Artifact was not found at ...` - check if the repository of problem dependency was correct or if it already contains the artifact.