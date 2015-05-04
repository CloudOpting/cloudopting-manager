package eu.cloudopting.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.cloudopting.domain.Applications;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Daniel P
 *
 * Simple tests for jackson deserializers.
 */
public class TestJacksonDeserializer {

        String postJson = "{ \"id\": 1051702783, \"applicationName\": \"Clearo\", \"applicationDescription\": \"\", \"applicationToscaTemplate\": {<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Definitions id=\"Clearo\" name=\"\" targetNamespace=\"http://tempuri.org\"\n" +
            "\txmlns=\"http://docs.oasis-open.org/tosca/ns/2011/12\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"\n" +
            "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txmlns:co=\"http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes\"\n" +
            "\txsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12 TOSCA-v1.0.xsd \n" +
            "\thttp://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes ./Types/CloudOptingTypes.xsd\">\n" +
            "\t<NodeType name=\"VMhost\">\n" +
            "\t\t<documentation>This is the VM description, we need to collect the SLA\n" +
            "\t\t\tinformation (that is the set of CPU+RAM+DISK) that the VM need to\n" +
            "\t\t\thave for the service (this information is just a label for the end\n" +
            "\t\t\tuser but translate to data for the system)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"co:VMhostProperties\"\n" +
            "\t\t\ttype=\"co:tVMhostProperties\" />\n" +
            "\t\t<Interfaces>\n" +
            "\t\t\t<Interface name=\"http://tempuri.org\">\n" +
            "\t\t\t\t<Operation name=\"Install\">\n" +
            "\t\t\t\t\t<documentation>The parameters to ask to the end user to execute the\n" +
            "\t\t\t\t\t\t\"install\" operation of this node\n" +
            "\t\t\t\t\t</documentation>\n" +
            "\t\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t\t<InputParameter name=\"co:SLA.co:Chosen\" type=\"co:SLA\" />\n" +
            "\t\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t</Operation>\n" +
            "\t\t\t</Interface>\n" +
            "\t\t</Interfaces>\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"DockerContainer\">\n" +
            "\t\t<documentation>This is the Docker Container (the Docker host is\n" +
            "\t\t\talready installed in the VM image)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"Apache\">\n" +
            "\t\t<documentation>This is the Apache server (we should not ask anything\n" +
            "\t\t\tto the end user about apache, but we need to set the properties)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"ApacheVirtualHost\">\n" +
            "\t\t<documentation>This is the Apache Virtual Host and here we have things\n" +
            "\t\t\tto ask to the user\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t\t<Interfaces>\n" +
            "\t\t\t<Interface name=\"http://tempuri.org\">\n" +
            "\t\t\t\t<Operation name=\"Install\">\n" +
            "\t\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t\t<InputParameter name=\"VHostName\" type=\"xs:string\" />\n" +
            "\t\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t</Operation>\n" +
            "\t\t\t</Interface>\n" +
            "\t\t</Interfaces>\n" +
            "\t</NodeType>\n" +
            "\t<ServiceTemplate id=\"Clearo\" name=\"\"\n" +
            "\t\tsubstitutableNodeType=\"QName\" targetNamespace=\"http://tempuri.org\">\n" +
            "\n" +
            "\t\t<TopologyTemplate>\n" +
            "\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t<NodeTemplate id=\"ClearoVM\" maxInstances=\"1\"\n" +
            "\t\t\t\tminInstances=\"1\" name=\"\" type=\"VMhost\">\n" +
            "\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<Properties>\n" +
            "\t\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t\t<co:VMhostProperties>\n" +
            "\t\t\t\t\t\t<co:SLAsProperties>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Big City\" id=\"BigCity\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>2</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>2</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>10000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>10</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Small City\" id=\"SmallCity\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>1</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>1</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>5000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>5</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Region\" id=\"Region\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>4</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>4</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>20000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>20</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:vmImage></co:vmImage>\n" +
            "\t\t\t\t\t\t</co:SLAsProperties>\n" +
            "\t\t\t\t\t</co:VMhostProperties>\n" +
            "\t\t\t\t</Properties>\n" +
            "\t\t\t</NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"DockerContainer\" id=\"ClearoApacheDC\"></NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"Apache\" id=\"ClearoApache\"></NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"ApacheVirtualHost\" id=\"ClearoApacheVH\">\n" +
            "\t\t\t\t<Properties>\n" +
            "\t\t\t\t\t<co:ApacheVirtualHostproperties>\n" +
            "\t\t\t\t\t\t<co:VHostName><?getInput VHostName?></co:VHostName>\n" +
            "\t\t\t\t\t</co:ApacheVirtualHostproperties>\n" +
            "\t\t\t\t</Properties>\n" +
            "\t\t\t</NodeTemplate>\n" +
            "\t\t</TopologyTemplate>\n" +
            "\t\t<Plans targetNamespace=\"http://tempuri.org\">\n" +
            "\t\t\t<Plan id=\"idvalue5\" name=\"\" planLanguage=\"http://tempuri.org\"\n" +
            "\t\t\t\tplanType=\"http://tempuri.org\">\n" +
            "\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<Precondition expressionLanguage=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t<InputParameter name=\"\" required=\"yes\" type=\"\" />\n" +
            "\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t<OutputParameters>\n" +
            "\t\t\t\t\t<OutputParameter name=\"\" required=\"yes\" type=\"\" />\n" +
            "\t\t\t\t</OutputParameters>\n" +
            "\t\t\t\t<PlanModel>\n" +
            "\t\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t</PlanModel>\n" +
            "\t\t\t</Plan>\n" +
            "\t\t</Plans>\n" +
            "\t</ServiceTemplate>\n" +
            "\t<NodeTypeImplementation nodeType=\"QName\" name=\"NCName\"></NodeTypeImplementation>\n" +
            "</Definitions>\n" +
            "}, \"applicationVersion\": \"\", \"customizations\": {  \"id\": 760843691,  \"customizationToscaFile\": {<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Definitions id=\"Clearo\" name=\"\" targetNamespace=\"http://tempuri.org\"\n" +
            "\txmlns=\"http://docs.oasis-open.org/tosca/ns/2011/12\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"\n" +
            "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txmlns:co=\"http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes\"\n" +
            "\txsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12 TOSCA-v1.0.xsd \n" +
            "\thttp://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes ./Types/CloudOptingTypes.xsd\">\n" +
            "\t<NodeType name=\"VMhost\">\n" +
            "\t\t<documentation>This is the VM description, we need to collect the SLA\n" +
            "\t\t\tinformation (that is the set of CPU+RAM+DISK) that the VM need to\n" +
            "\t\t\thave for the service (this information is just a label for the end\n" +
            "\t\t\tuser but translate to data for the system)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"co:VMhostProperties\"\n" +
            "\t\t\ttype=\"co:tVMhostProperties\" />\n" +
            "\t\t<Interfaces>\n" +
            "\t\t\t<Interface name=\"http://tempuri.org\">\n" +
            "\t\t\t\t<Operation name=\"Install\">\n" +
            "\t\t\t\t\t<documentation>The parameters to ask to the end user to execute the\n" +
            "\t\t\t\t\t\t\"install\" operation of this node\n" +
            "\t\t\t\t\t</documentation>\n" +
            "\t\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t\t<InputParameter name=\"co:SLA.co:Chosen\" type=\"co:SLA\" />\n" +
            "\t\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t</Operation>\n" +
            "\t\t\t</Interface>\n" +
            "\t\t</Interfaces>\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"DockerContainer\">\n" +
            "\t\t<documentation>This is the Docker Container (the Docker host is\n" +
            "\t\t\talready installed in the VM image)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"Apache\">\n" +
            "\t\t<documentation>This is the Apache server (we should not ask anything\n" +
            "\t\t\tto the end user about apache, but we need to set the properties)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"ApacheVirtualHost\">\n" +
            "\t\t<documentation>This is the Apache Virtual Host and here we have things\n" +
            "\t\t\tto ask to the user\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t\t<Interfaces>\n" +
            "\t\t\t<Interface name=\"http://tempuri.org\">\n" +
            "\t\t\t\t<Operation name=\"Install\">\n" +
            "\t\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t\t<InputParameter name=\"VHostName\" type=\"xs:string\" />\n" +
            "\t\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t</Operation>\n" +
            "\t\t\t</Interface>\n" +
            "\t\t</Interfaces>\n" +
            "\t</NodeType>\n" +
            "\t<ServiceTemplate id=\"Clearo\" name=\"\"\n" +
            "\t\tsubstitutableNodeType=\"QName\" targetNamespace=\"http://tempuri.org\">\n" +
            "\n" +
            "\t\t<TopologyTemplate>\n" +
            "\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t<NodeTemplate id=\"ClearoVM\" maxInstances=\"1\"\n" +
            "\t\t\t\tminInstances=\"1\" name=\"\" type=\"VMhost\">\n" +
            "\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<Properties>\n" +
            "\t\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t\t<co:VMhostProperties>\n" +
            "\t\t\t\t\t\t<co:SLAsProperties>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Big City\" id=\"BigCity\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>2</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>2</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>10000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>10</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Small City\" id=\"SmallCity\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>1</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>1</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>5000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>5</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Region\" id=\"Region\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>4</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>4</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>20000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>20</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:vmImage></co:vmImage>\n" +
            "\t\t\t\t\t\t</co:SLAsProperties>\n" +
            "\t\t\t\t\t</co:VMhostProperties>\n" +
            "\t\t\t\t</Properties>\n" +
            "\t\t\t</NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"DockerContainer\" id=\"ClearoApacheDC\"></NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"Apache\" id=\"ClearoApache\"></NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"ApacheVirtualHost\" id=\"ClearoApacheVH\">\n" +
            "\t\t\t\t<Properties>\n" +
            "\t\t\t\t\t<co:ApacheVirtualHostproperties>\n" +
            "\t\t\t\t\t\t<co:VHostName><?getInput VHostName?></co:VHostName>\n" +
            "\t\t\t\t\t</co:ApacheVirtualHostproperties>\n" +
            "\t\t\t\t</Properties>\n" +
            "\t\t\t</NodeTemplate>\n" +
            "\t\t</TopologyTemplate>\n" +
            "\t\t<Plans targetNamespace=\"http://tempuri.org\">\n" +
            "\t\t\t<Plan id=\"idvalue5\" name=\"\" planLanguage=\"http://tempuri.org\"\n" +
            "\t\t\t\tplanType=\"http://tempuri.org\">\n" +
            "\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<Precondition expressionLanguage=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t<InputParameter name=\"\" required=\"yes\" type=\"\" />\n" +
            "\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t<OutputParameters>\n" +
            "\t\t\t\t\t<OutputParameter name=\"\" required=\"yes\" type=\"\" />\n" +
            "\t\t\t\t</OutputParameters>\n" +
            "\t\t\t\t<PlanModel>\n" +
            "\t\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t</PlanModel>\n" +
            "\t\t\t</Plan>\n" +
            "\t\t</Plans>\n" +
            "\t</ServiceTemplate>\n" +
            "\t<NodeTypeImplementation nodeType=\"QName\" name=\"NCName\"></NodeTypeImplementation>\n" +
            "</Definitions>\n" +
            "},  \"customizationCreation\": \"\",  \"customizationActivation\": \"\",  \"customizationDecommission\": \"\",  \"username\": \"admin\",  \"applicationId\": \"Applications\",  \"statusId\": {   \"id\": 0,   \"status\": \"\",   \"customizations\": \"Customizations\",   \"applications\": \"Applications\"  } }, \"applicationMedia\": {  \"id\": 0,  \"mediaContent\": [  \"\"  ],  \"applicationId\": \"Applications\" }, \"statusId\": {  \"id\": 0,  \"status\": \"\",  \"customizations\": {   \"id\": 760843691,   \"customizationToscaFile\": {<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Definitions id=\"Clearo\" name=\"\" targetNamespace=\"http://tempuri.org\"\n" +
            "\txmlns=\"http://docs.oasis-open.org/tosca/ns/2011/12\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"\n" +
            "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txmlns:co=\"http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes\"\n" +
            "\txsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12 TOSCA-v1.0.xsd \n" +
            "\thttp://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes ./Types/CloudOptingTypes.xsd\">\n" +
            "\t<NodeType name=\"VMhost\">\n" +
            "\t\t<documentation>This is the VM description, we need to collect the SLA\n" +
            "\t\t\tinformation (that is the set of CPU+RAM+DISK) that the VM need to\n" +
            "\t\t\thave for the service (this information is just a label for the end\n" +
            "\t\t\tuser but translate to data for the system)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"co:VMhostProperties\"\n" +
            "\t\t\ttype=\"co:tVMhostProperties\" />\n" +
            "\t\t<Interfaces>\n" +
            "\t\t\t<Interface name=\"http://tempuri.org\">\n" +
            "\t\t\t\t<Operation name=\"Install\">\n" +
            "\t\t\t\t\t<documentation>The parameters to ask to the end user to execute the\n" +
            "\t\t\t\t\t\t\"install\" operation of this node\n" +
            "\t\t\t\t\t</documentation>\n" +
            "\t\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t\t<InputParameter name=\"co:SLA.co:Chosen\" type=\"co:SLA\" />\n" +
            "\t\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t</Operation>\n" +
            "\t\t\t</Interface>\n" +
            "\t\t</Interfaces>\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"DockerContainer\">\n" +
            "\t\t<documentation>This is the Docker Container (the Docker host is\n" +
            "\t\t\talready installed in the VM image)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"Apache\">\n" +
            "\t\t<documentation>This is the Apache server (we should not ask anything\n" +
            "\t\t\tto the end user about apache, but we need to set the properties)\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t</NodeType>\n" +
            "\t<NodeType name=\"ApacheVirtualHost\">\n" +
            "\t\t<documentation>This is the Apache Virtual Host and here we have things\n" +
            "\t\t\tto ask to the user\n" +
            "\t\t</documentation>\n" +
            "\t\t<PropertiesDefinition element=\"\" type=\"\" />\n" +
            "\t\t<Interfaces>\n" +
            "\t\t\t<Interface name=\"http://tempuri.org\">\n" +
            "\t\t\t\t<Operation name=\"Install\">\n" +
            "\t\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t\t<InputParameter name=\"VHostName\" type=\"xs:string\" />\n" +
            "\t\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t</Operation>\n" +
            "\t\t\t</Interface>\n" +
            "\t\t</Interfaces>\n" +
            "\t</NodeType>\n" +
            "\t<ServiceTemplate id=\"Clearo\" name=\"\"\n" +
            "\t\tsubstitutableNodeType=\"QName\" targetNamespace=\"http://tempuri.org\">\n" +
            "\n" +
            "\t\t<TopologyTemplate>\n" +
            "\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t<NodeTemplate id=\"ClearoVM\" maxInstances=\"1\"\n" +
            "\t\t\t\tminInstances=\"1\" name=\"\" type=\"VMhost\">\n" +
            "\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<Properties>\n" +
            "\t\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t\t<co:VMhostProperties>\n" +
            "\t\t\t\t\t\t<co:SLAsProperties>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Big City\" id=\"BigCity\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>2</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>2</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>10000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>10</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Small City\" id=\"SmallCity\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>1</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>1</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>5000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>5</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:SLA Name=\"Region\" id=\"Region\">\n" +
            "\t\t\t\t\t\t\t\t<co:NumCpus>4</co:NumCpus>\n" +
            "\t\t\t\t\t\t\t\t<co:Memory>4</co:Memory>\n" +
            "\t\t\t\t\t\t\t\t<co:Price>20000</co:Price>\n" +
            "\t\t\t\t\t\t\t\t<co:Disk>20</co:Disk>\n" +
            "\t\t\t\t\t\t\t\t<co:Chosen>false</co:Chosen>\n" +
            "\t\t\t\t\t\t\t</co:SLA>\n" +
            "\t\t\t\t\t\t\t<co:vmImage></co:vmImage>\n" +
            "\t\t\t\t\t\t</co:SLAsProperties>\n" +
            "\t\t\t\t\t</co:VMhostProperties>\n" +
            "\t\t\t\t</Properties>\n" +
            "\t\t\t</NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"DockerContainer\" id=\"ClearoApacheDC\"></NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"Apache\" id=\"ClearoApache\"></NodeTemplate>\n" +
            "\t\t\t<NodeTemplate type=\"ApacheVirtualHost\" id=\"ClearoApacheVH\">\n" +
            "\t\t\t\t<Properties>\n" +
            "\t\t\t\t\t<co:ApacheVirtualHostproperties>\n" +
            "\t\t\t\t\t\t<co:VHostName><?getInput VHostName?></co:VHostName>\n" +
            "\t\t\t\t\t</co:ApacheVirtualHostproperties>\n" +
            "\t\t\t\t</Properties>\n" +
            "\t\t\t</NodeTemplate>\n" +
            "\t\t</TopologyTemplate>\n" +
            "\t\t<Plans targetNamespace=\"http://tempuri.org\">\n" +
            "\t\t\t<Plan id=\"idvalue5\" name=\"\" planLanguage=\"http://tempuri.org\"\n" +
            "\t\t\t\tplanType=\"http://tempuri.org\">\n" +
            "\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<Precondition expressionLanguage=\"http://tempuri.org\" />\n" +
            "\t\t\t\t<InputParameters>\n" +
            "\t\t\t\t\t<InputParameter name=\"\" required=\"yes\" type=\"\" />\n" +
            "\t\t\t\t</InputParameters>\n" +
            "\t\t\t\t<OutputParameters>\n" +
            "\t\t\t\t\t<OutputParameter name=\"\" required=\"yes\" type=\"\" />\n" +
            "\t\t\t\t</OutputParameters>\n" +
            "\t\t\t\t<PlanModel>\n" +
            "\t\t\t\t\t<documentation xml:lang=\"\" source=\"http://tempuri.org\" />\n" +
            "\t\t\t\t</PlanModel>\n" +
            "\t\t\t</Plan>\n" +
            "\t\t</Plans>\n" +
            "\t</ServiceTemplate>\n" +
            "\t<NodeTypeImplementation nodeType=\"QName\" name=\"NCName\"></NodeTypeImplementation>\n" +
            "</Definitions>\n" +
            "},   \"customizationCreation\": \"\",   \"customizationActivation\": \"\",   \"customizationDecommission\": \"\",   \"username\": \"admin\",   \"applicationId\": \"Applications\",   \"statusId\": \"Status\"  },  \"applications\": \"Applications\" }, \"userId\": {  \"createdBy\": \"admin\",  \"createdDate\": \"1423958400000\",  \"lastModifiedBy\": \"admin\",  \"lastModifiedDate\": \"1423958400000\",  \"id\": 1,  \"login\": \"admin\",  \"firstName\": \"Admin\",  \"lastName\": \"Administrator\",  \"email\": \"admin@admin.com\",  \"activated\": true,  \"langKey\": \"en\",  \"activationKey\": \"55555\" }}";


    String simplerJson = "{\"applicationName\": \"Clearo\", \"applicationDescription\": \"\",   \"customizationCreation\": \"\",   \"customizationActivation\": \"\",   \"customizationDecommission\": \"\",   \"username\": \"admin\",   \"applicationId\": \"Applications\"},  \"applications\": \"Applications\" }, \"userId\": {  \"createdBy\": \"admin\",  \"createdDate\": \"1423958400000\",  \"lastModifiedBy\": \"admin\",  \"lastModifiedDate\": \"1423958400000\",  \"id\": 1,  \"login\": \"admin\",  \"firstName\": \"Admin\",  \"lastName\": \"Administrator\",  \"email\": \"admin@admin.com\",  \"activated\": true,  \"langKey\": \"en\",  \"activationKey\": \"55555\" }}";

    @Test
    public void testToscaJsonunmarshal(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Applications app = mapper.readValue(simplerJson,Applications.class);
            System.out.println(app);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
