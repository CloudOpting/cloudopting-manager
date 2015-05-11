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

## Development environment configuration
The following description helps setting up a CentOS 7 development environment.

Java 1.8 is needed (OpenJDK can work for the moment, no quirks have discovered to require SUN java)

yum install java-1.8.0-openjdk-devel

In this way you get JDK and not only JRE.

### Setup of Eclipse environment
Is adviced to start from Sprint Tool Suite environment, but if preferred also a simple eclipse environment can do the job (you have to install the STS plugin yourselves:
Here is a list of useful plugin is adviced to have installed in the Eclipse environment:
 * STS
 * Activiti plugin

With Eclipse you will than import the GitHub repository:
 * Open the GIT perspective
 * Clone the repo from CloudOpting account
 * you will find the cloned repo in the home/git folder (typically)

Now is time to import the project using maven import
 * point to the main pom.xml in the cloudopting-manager and import the project from it
 * all the modules will get imported

Prepare the run configuration as in the image below, this this configuration you will build the whole project to be able to check the working of the modifiers done
![Setting the run configuration](https://raw.githubusercontent.com/CloudOpting/cloudopting-manager/master/documentation/runconfig.png)

## Processes
### Service Publishing Process
The workflow that a Service Provider has to follow to publish a Service in the platform is represented in BPMN notation
in the following picture
![Service Publishing Process](https://raw.githubusercontent.com/CloudOpting/cloudopting-manager/master/documentation/ServicePublishingProcess.png)

### Deployment Process
The workflow below explains the deployment process the is run by the Activiti engine

![Service Publishing Process](https://raw.githubusercontent.com/CloudOpting/cloudopting-manager/master/documentation/orchestration-process.bpmn20.png)


## Architecture
The overall architecture of the system is defined in the diagram below
![Architectural diagram](https://raw.githubusercontent.com/CloudOpting/cloudopting-manager/master/documentation/architecture.png)

As can be seen there is a core component represente by the CloudOpting manager that is the element implemented in this repository.

### Technologies used
This application is coded in java.

The framework used is [Spring](https://spring.io/) and in particular Spinr-boot since is a headless application.

It has a BPMN engine embedded, and the project opted for [Activiti](http://activiti.org/)

Other libraries used are:
 * [Freemarker]()
 * [JGraphT]()
 * [JClouds]()
