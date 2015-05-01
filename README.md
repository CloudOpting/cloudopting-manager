# cloudopting-deploy-services
This is the master repository for the CloudOpting platform orchestrator

This is a Spring Boot application subdivided into different modules

The following tables explains the modules and the subdivision

| component | codename | scope | interface documentation |
| --- | --- | --- | --- |
| [bpmn-component](https://github.com/CloudOpting/cloudopting-deploy-services/tree/master/bpmn-component "bpmn-component") | | This component contains the BPMN engine, the processes and the related classes of the Activiti BPMN engine | |
| [cloud-component](https://github.com/CloudOpting/cloudopting-deploy-services/tree/master/cloud-component "cloud-component") | | This component contains the classes that interact with the cloud environment and use the JClouds library | |
| [docker-component](https://github.com/CloudOpting/cloudopting-deploy-services/tree/master/docker-component "docker-component") | | This component contains the classes to talk REST to the python component that interact with docker (NOTE: the docker python component will be in a separate repository from this one) | |
| [rest-component](https://github.com/CloudOpting/cloudopting-deploy-services/tree/master/rest-component "rest-component") | | This component contains the Spring Boot REST entrypoint for all the platform features | |
| [tosca-component](https://github.com/CloudOpting/cloudopting-deploy-services/tree/master/tosca-component "tosca-component") | | This component contains the TOSCA parser thsat manages all the interactions with the TOSCA world | |

All other folders are legacy code kept for reference.
