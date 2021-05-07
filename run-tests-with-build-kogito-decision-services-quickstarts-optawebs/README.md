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

`$PROJECT_SETTINGS` is the settings.xml with the repository which contains project dependencies

`$QUICKSTARTS_URL` redhat distribution decision-services-quickstarts.zip url

`-Dintegration-tests={true|false}` run integration cypress tests on docker

`-Dcontainer.runtime={docker|podman}` change runtime

In your $PROJECT_SETTINGS file, you might want to provide the mirror repository and/or proxied npm repository for your environment.

---------------------------------------
Issues

`Unknown host ...` -  check if you are connected to the Kerberos

`Artifact was not found at ...` - check if the repository of problem dependency was correct or if it already contains the artifact.