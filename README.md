# cloudopting-manager
This is the master repository for the CloudOpting platform orchestrator

This is a Spring Boot application subdivided into different modules

The following tables explains the modules and the subdivision

| component | codename | scope | interface documentation |
| --- | --- | --- | --- |
| [bpmn-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/bpmn-component "bpmn-component") | | This component contains the BPMN engine, the processes and the related classes of the Activiti BPMN engine | |
| [cloud-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/cloud-component "cloud-component") | | This component contains the classes that interact with the cloud environment and use the JClouds library | |
| [database-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/database-component "database-component") | | This component contains the classes to save entities on the database, that will be PostgreSQL | |
| [docker-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/docker-component "docker-component") | | This component contains the classes to talk REST to the python component that interact with docker (NOTE: the docker python component will be in a separate repository from this one) | |
| [rest-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/rest-component "rest-component") | | This component contains the Spring Boot REST entrypoint for all the platform features | |
| [storage-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/storage-component "storage-component") | | This component contains the jackrabbit classes to help manage the storage of files | |
| [tosca-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/tosca-component "tosca-component") | | This component contains the TOSCA parser thsat manages all the interactions with the TOSCA world | |
| [wordpress-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/wordpress-component "wordpress-component") | | This component contains the code necessary to interact through API with wordpress for the public view of the catalogue | |
| [webui-component](https://github.com/CloudOpting/cloudopting-manager/tree/master/webui-component "webui-component") | | This component contains the web interface of the project | |

All other folders are legacy code kept for reference.
