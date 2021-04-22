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

`-Dmaven.integration-tests={true|false}` equal to `-Pintegration-tests`, escape      
warnings "The requested profile "integration-tests" could not be activated because it does not exist."

`-Dcontainer.runtime={docker|podman}` change runtime

To set up $PROJECT_SETTINGS file, provide there the mirror repository and npm repository e.g: 
https://gist.githubusercontent.com/dupliaka/8c65bcbc6eb4eb931ea7124b1daa2228/raw/faa452c141fa1fa247ea724bf0006cf915ac4ad6/settings.xml

---------------------------------------
Issues

`Unknown host ...` -  check if you are connected to the Kerberos

`Artifact was not found at ...` - check if the repository of problem dependency was correct or if it already contains the artifact.