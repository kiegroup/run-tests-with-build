run-tests-with-build-kogito-decision-services-quickstarts-optawebs
------------------------------------
Run tests OptaWeb backend, frontend and standalone, including integration delivered as part of decision-services-quickstarts zip

Currently, these optawebs are delivered in decision-services-quickstarts zip
- [optaweb-employee-rostering](https://github.com/kiegroup/optaweb-employee-rostering)
- [optaweb-vehicle-routing](https://github.com/kiegroup/optaweb-vehicle-routing)

Execution
-------------------------------------
`mvn verify -Doptaweb=$OPTAWEB_NAME -s $PROJECT_SETTINGS -Dkogito.decision.services.quickstarts.url=$QUICKSTARTS_URL`

where 

`$OPTAWEB_NAME` is a profile name {vehicle-routing|employee-rostering} 

`$PROJECT_SETTINGS` is the settings.xml with the repository which contains ${OPTAPLANNER_BUILD_VERSION} tar.gz 

`$QUICKSTARTS_URL` redhat distribution decision-services-quickstarts.zip url

`-Dintegration-tests={true|false}` run integration cypress tests on docker

`-Dcontainer.runtime={docker|podman}` change runtime

To set up $PROJECT_SETTINGS file, provide there the mirror repository and npm repository e.g: 
https://gist.githubusercontent.com/dupliaka/8c65bcbc6eb4eb931ea7124b1daa2228/raw/faa452c141fa1fa247ea724bf0006cf915ac4ad6/settings.xml

---------------------------------------
Issues

`Unknown host ...` -  check if you are connected to the Kerberos

`Artifact was not found at ...` - check if the repository of problem dependency was correct or if it already contains the artifact.