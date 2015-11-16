package eu.cloudopting;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.cloudopting.repository.ApplicationsRepository;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.impl.ApplicationServiceImpl;
import eu.cloudopting.web.rest.ApplicationResource;

/**
 * @author Daniel P.
 *         This test class will contain tests for the tosca database save scenarios.
 */
public class ToscaSaveTest {

    String createNewApplicationJson = "{\n" +
            "\t\"statusId\": {\n" +
            "\t\t\"id\": 1\n" +
            "\t},\n" +
            "\t\"userId\": {\n" +
            "\t\t\"id\": 3\n" +
            "\t},\n" +
            "\t\"applicationName\": \"test app xml 3333333\",\n" +
            "\t\"applicationDescription\": \"test description xml\",\n" +
            "\t\"applicationToscaTemplate\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\r\\n<Definitions id=\\\"Clearo\\\" name=\\\"\\\"targetNamespace=\\\"http://tempuri.org\\\"\\r\\n\\txmlns=\\\"http://docs.oasis-open.org/tosca/ns/2011/12\\\" xmlns:xml=\\\"http://www.w3.org/XML/1998/namespace\\\"\\r\\n\\txmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\"\\r\\n\\txmlns:co=\\\"http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes\\\"\\r\\n\\txsi:schemaLocation=\\\"http://docs.oasis-open.org/tosca/ns/2011/12 TOSCA-v1.0.xsd \\r\\n\\thttp://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes ./Types/CloudOptingTypes.xsd\\\">\\r\\n\\t<NodeType name=\\\"VMhost\\\">\\r\\n\\t\\t<documentation>This is the VM description, we need to collect the SLA\\r\\n\\t\\t\\tinformation (that is the set of CPU+RAM+DISK) that the VM need to\\r\\n\\t\\t\\thave for the service (this information is just a label for the end\\r\\n\\t\\t\\tuser but translate to data for the system)\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"co:VMhostProperties\\\"\\r\\n\\t\\t\\ttype=\\\"co:tVMhostProperties\\\" />\\r\\n\\t\\t<Interfaces>\\r\\n\\t\\t\\t<Interface name=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t\\t<Operation name=\\\"Install\\\">\\r\\n\\t\\t\\t\\t\\t<documentation>The parameters to ask to the end user to execute the\\r\\n\\t\\t\\t\\t\\t\\t\\\"install\\\" operation of this node\\r\\n\\t\\t\\t\\t\\t</documentation>\\r\\n\\t\\t\\t\\t\\t<InputParameters>\\r\\n\\t\\t\\t\\t\\t\\t<InputParameter name=\\\"co:SLA.co:Chosen\\\" type=\\\"co:SLA\\\" />\\r\\n\\t\\t\\t\\t\\t</InputParameters>\\r\\n\\t\\t\\t\\t</Operation>\\r\\n\\t\\t\\t</Interface>\\r\\n\\t\\t</Interfaces>\\r\\n\\t</NodeType>\\r\\n\\t<NodeType name=\\\"DockerContainer\\\">\\r\\n\\t\\t<documentation>This is the Docker Container (the Docker host is\\r\\n\\t\\t\\talready installed in the VM image)\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"\\\"type=\\\"\\\" />\\r\\n\\t</NodeType>\\r\\n\\t<NodeType name=\\\"Apache\\\">\\r\\n\\t\\t<documentation>This is the Apache server (we should not ask anything\\r\\n\\t\\t\\tto the end user about apache, but we need to set the properties)\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"\\\"type=\\\"\\\" />\\r\\n\\t</NodeType>\\r\\n\\t<NodeType name=\\\"ApacheVirtualHost\\\">\\r\\n\\t\\t<documentation>This is the Apache Virtual Host and here we have things\\r\\n\\t\\t\\tto ask to the user\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"\\\"type=\\\"\\\" />\\r\\n\\t\\t<Interfaces>\\r\\n\\t\\t\\t<Interface name=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t\\t<Operation name=\\\"Install\\\">\\r\\n\\t\\t\\t\\t\\t<InputParameters>\\r\\n\\t\\t\\t\\t\\t\\t<InputParameter name=\\\"VHostName\\\" type=\\\"xs:string\\\" />\\r\\n\\t\\t\\t\\t\\t</InputParameters>\\r\\n\\t\\t\\t\\t</Operation>\\r\\n\\t\\t\\t</Interface>\\r\\n\\t\\t</Interfaces>\\r\\n\\t</NodeType>\\r\\n\\t<ServiceTemplate id=\\\"Clearo\\\" name=\\\"\\\"\\r\\n\\t\\tsubstitutableNodeType=\\\"QName\\\" targetNamespace=\\\"http://tempuri.org\\\">\\r\\n\\r\\n\\t\\t<TopologyTemplate>\\r\\n\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t<NodeTemplate id=\\\"ClearoVM\\\" maxInstances=\\\"1\\\"\\r\\n\\t\\t\\t\\tminInstances=\\\"1\\\" name=\\\"\\\"type=\\\"VMhost\\\">\\r\\n\\t\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t<Properties>\\r\\n\\t\\t\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t\\t<co:VMhostProperties>\\r\\n\\t\\t\\t\\t\\t\\t<co:SLAsProperties>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:SLA Name=\\\"Big City\\\" id=\\\"BigCity\\\">\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:NumCpus>2</co:NumCpus>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Memory>2</co:Memory>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Price>10000</co:Price>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Disk>10</co:Disk>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Chosen>false</co:Chosen>\\r\\n\\t\\t\\t\\t\\t\\t\\t</co:SLA>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:SLA Name=\\\"Small City\\\" id=\\\"SmallCity\\\">\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:NumCpus>1</co:NumCpus>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Memory>1</co:Memory>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Price>5000</co:Price>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Disk>5</co:Disk>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Chosen>false</co:Chosen>\\r\\n\\t\\t\\t\\t\\t\\t\\t</co:SLA>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:SLA Name=\\\"Region\\\" id=\\\"Region\\\">\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:NumCpus>4</co:NumCpus>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Memory>4</co:Memory>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Price>20000</co:Price>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Disk>20</co:Disk>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Chosen>false</co:Chosen>\\r\\n\\t\\t\\t\\t\\t\\t\\t</co:SLA>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:vmImage></co:vmImage>\\r\\n\\t\\t\\t\\t\\t\\t</co:SLAsProperties>\\r\\n\\t\\t\\t\\t\\t</co:VMhostProperties>\\r\\n\\t\\t\\t\\t</Properties>\\r\\n\\t\\t\\t</NodeTemplate>\\r\\n\\t\\t\\t<NodeTemplate type=\\\"DockerContainer\\\" id=\\\"ClearoApacheDC\\\"></NodeTemplate>\\r\\n\\t\\t\\t<NodeTemplate type=\\\"Apache\\\" id=\\\"ClearoApache\\\"></NodeTemplate>\\r\\n\\t\\t\\t<NodeTemplate type=\\\"ApacheVirtualHost\\\" id=\\\"ClearoApacheVH\\\">\\r\\n\\t\\t\\t\\t<Properties>\\r\\n\\t\\t\\t\\t\\t<co:ApacheVirtualHostproperties>\\r\\n\\t\\t\\t\\t\\t\\t<co:VHostName><?getInput VHostName?></co:VHostName>\\r\\n\\t\\t\\t\\t\\t</co:ApacheVirtualHostproperties>\\r\\n\\t\\t\\t\\t</Properties>\\r\\n\\t\\t\\t</NodeTemplate>\\r\\n\\t\\t</TopologyTemplate>\\r\\n\\t\\t<Plans targetNamespace=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t<Plan id=\\\"idvalue5\\\" name=\\\"\\\"planLanguage=\\\"http://tempuri.org\\\"\\r\\n\\t\\t\\t\\tplanType=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t<Precondition expressionLanguage=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t<InputParameters>\\r\\n\\t\\t\\t\\t\\t<InputParameter name=\\\"\\\"required=\\\"yes\\\" type=\\\"\\\"/>\\r\\n\\t\\t\\t\\t</InputParameters>\\r\\n\\t\\t\\t\\t<OutputParameters>\\r\\n\\t\\t\\t\\t\\t<OutputParametername=\\\"\\\" required=\\\"yes\\\" type=\\\"\\\"/>\\r\\n\\t\\t\\t\\t</OutputParameters>\\r\\n\\t\\t\\t\\t<PlanModel>\\r\\n\\t\\t\\t\\t\\t<documentationxml: lang=\\\"\\\" source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t</PlanModel>\\r\\n\\t\\t\\t</Plan>\\r\\n\\t\\t</Plans>\\r\\n\\t</ServiceTemplate>\\r\\n\\t<NodeTypeImplementation nodeType=\\\"QName\\\" name=\\\"NCName\\\"></NodeTypeImplementation>\\r\\n</Definitions>\",\n" +
            "\t\"applicationVersion\": \"1\"\n" +
            "}";

    String createNewCustomizationJson = "{\n" +
            "\t\"statusId\": {\n" +
            "\t\t\"id\": 1\n" +
            "\t},\n" +
            "\t\"applicationId\":{\n" +
            "\t\t\"id\": 181\n" +
            "\t},\n" +
            "\t\"customizationToscaFile\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\r\\n<Definitions id=\\\"Clearo\\\" name=\\\"\\\"targetNamespace=\\\"http://tempuri.org\\\"\\r\\n\\txmlns=\\\"http://docs.oasis-open.org/tosca/ns/2011/12\\\" xmlns:xml=\\\"http://www.w3.org/XML/1998/namespace\\\"\\r\\n\\txmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\"\\r\\n\\txmlns:co=\\\"http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes\\\"\\r\\n\\txsi:schemaLocation=\\\"http://docs.oasis-open.org/tosca/ns/2011/12 TOSCA-v1.0.xsd \\r\\n\\thttp://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes ./Types/CloudOptingTypes.xsd\\\">\\r\\n\\t<NodeType name=\\\"VMhost\\\">\\r\\n\\t\\t<documentation>This is the VM description, we need to collect the SLA\\r\\n\\t\\t\\tinformation (that is the set of CPU+RAM+DISK) that the VM need to\\r\\n\\t\\t\\thave for the service (this information is just a label for the end\\r\\n\\t\\t\\tuser but translate to data for the system)\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"co:VMhostProperties\\\"\\r\\n\\t\\t\\ttype=\\\"co:tVMhostProperties\\\" />\\r\\n\\t\\t<Interfaces>\\r\\n\\t\\t\\t<Interface name=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t\\t<Operation name=\\\"Install\\\">\\r\\n\\t\\t\\t\\t\\t<documentation>The parameters to ask to the end user to execute the\\r\\n\\t\\t\\t\\t\\t\\t\\\"install\\\" operation of this node\\r\\n\\t\\t\\t\\t\\t</documentation>\\r\\n\\t\\t\\t\\t\\t<InputParameters>\\r\\n\\t\\t\\t\\t\\t\\t<InputParameter name=\\\"co:SLA.co:Chosen\\\" type=\\\"co:SLA\\\" />\\r\\n\\t\\t\\t\\t\\t</InputParameters>\\r\\n\\t\\t\\t\\t</Operation>\\r\\n\\t\\t\\t</Interface>\\r\\n\\t\\t</Interfaces>\\r\\n\\t</NodeType>\\r\\n\\t<NodeType name=\\\"DockerContainer\\\">\\r\\n\\t\\t<documentation>This is the Docker Container (the Docker host is\\r\\n\\t\\t\\talready installed in the VM image)\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"\\\"type=\\\"\\\" />\\r\\n\\t</NodeType>\\r\\n\\t<NodeType name=\\\"Apache\\\">\\r\\n\\t\\t<documentation>This is the Apache server (we should not ask anything\\r\\n\\t\\t\\tto the end user about apache, but we need to set the properties)\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"\\\"type=\\\"\\\" />\\r\\n\\t</NodeType>\\r\\n\\t<NodeType name=\\\"ApacheVirtualHost\\\">\\r\\n\\t\\t<documentation>This is the Apache Virtual Host and here we have things\\r\\n\\t\\t\\tto ask to the user\\r\\n\\t\\t</documentation>\\r\\n\\t\\t<PropertiesDefinition element=\\\"\\\"type=\\\"\\\" />\\r\\n\\t\\t<Interfaces>\\r\\n\\t\\t\\t<Interface name=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t\\t<Operation name=\\\"Install\\\">\\r\\n\\t\\t\\t\\t\\t<InputParameters>\\r\\n\\t\\t\\t\\t\\t\\t<InputParameter name=\\\"VHostName\\\" type=\\\"xs:string\\\" />\\r\\n\\t\\t\\t\\t\\t</InputParameters>\\r\\n\\t\\t\\t\\t</Operation>\\r\\n\\t\\t\\t</Interface>\\r\\n\\t\\t</Interfaces>\\r\\n\\t</NodeType>\\r\\n\\t<ServiceTemplate id=\\\"Clearo\\\" name=\\\"\\\"\\r\\n\\t\\tsubstitutableNodeType=\\\"QName\\\" targetNamespace=\\\"http://tempuri.org\\\">\\r\\n\\r\\n\\t\\t<TopologyTemplate>\\r\\n\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t<NodeTemplate id=\\\"ClearoVM\\\" maxInstances=\\\"1\\\"\\r\\n\\t\\t\\t\\tminInstances=\\\"1\\\" name=\\\"\\\"type=\\\"VMhost\\\">\\r\\n\\t\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t<Properties>\\r\\n\\t\\t\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t\\t<co:VMhostProperties>\\r\\n\\t\\t\\t\\t\\t\\t<co:SLAsProperties>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:SLA Name=\\\"Big City\\\" id=\\\"BigCity\\\">\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:NumCpus>2</co:NumCpus>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Memory>2</co:Memory>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Price>10000</co:Price>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Disk>10</co:Disk>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Chosen>false</co:Chosen>\\r\\n\\t\\t\\t\\t\\t\\t\\t</co:SLA>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:SLA Name=\\\"Small City\\\" id=\\\"SmallCity\\\">\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:NumCpus>1</co:NumCpus>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Memory>1</co:Memory>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Price>5000</co:Price>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Disk>5</co:Disk>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Chosen>false</co:Chosen>\\r\\n\\t\\t\\t\\t\\t\\t\\t</co:SLA>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:SLA Name=\\\"Region\\\" id=\\\"Region\\\">\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:NumCpus>4</co:NumCpus>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Memory>4</co:Memory>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Price>20000</co:Price>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Disk>20</co:Disk>\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t<co:Chosen>false</co:Chosen>\\r\\n\\t\\t\\t\\t\\t\\t\\t</co:SLA>\\r\\n\\t\\t\\t\\t\\t\\t\\t<co:vmImage></co:vmImage>\\r\\n\\t\\t\\t\\t\\t\\t</co:SLAsProperties>\\r\\n\\t\\t\\t\\t\\t</co:VMhostProperties>\\r\\n\\t\\t\\t\\t</Properties>\\r\\n\\t\\t\\t</NodeTemplate>\\r\\n\\t\\t\\t<NodeTemplate type=\\\"DockerContainer\\\" id=\\\"ClearoApacheDC\\\"></NodeTemplate>\\r\\n\\t\\t\\t<NodeTemplate type=\\\"Apache\\\" id=\\\"ClearoApache\\\"></NodeTemplate>\\r\\n\\t\\t\\t<NodeTemplate type=\\\"ApacheVirtualHost\\\" id=\\\"ClearoApacheVH\\\">\\r\\n\\t\\t\\t\\t<Properties>\\r\\n\\t\\t\\t\\t\\t<co:ApacheVirtualHostproperties>\\r\\n\\t\\t\\t\\t\\t\\t<co:VHostName><?getInput VHostName?></co:VHostName>\\r\\n\\t\\t\\t\\t\\t</co:ApacheVirtualHostproperties>\\r\\n\\t\\t\\t\\t</Properties>\\r\\n\\t\\t\\t</NodeTemplate>\\r\\n\\t\\t</TopologyTemplate>\\r\\n\\t\\t<Plans targetNamespace=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t<Plan id=\\\"idvalue5\\\" name=\\\"\\\"planLanguage=\\\"http://tempuri.org\\\"\\r\\n\\t\\t\\t\\tplanType=\\\"http://tempuri.org\\\">\\r\\n\\t\\t\\t\\t<documentation xml:lang=\\\"\\\"source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t<Precondition expressionLanguage=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t<InputParameters>\\r\\n\\t\\t\\t\\t\\t<InputParameter name=\\\"\\\"required=\\\"yes\\\" type=\\\"\\\"/>\\r\\n\\t\\t\\t\\t</InputParameters>\\r\\n\\t\\t\\t\\t<OutputParameters>\\r\\n\\t\\t\\t\\t\\t<OutputParametername=\\\"\\\" required=\\\"yes\\\" type=\\\"\\\"/>\\r\\n\\t\\t\\t\\t</OutputParameters>\\r\\n\\t\\t\\t\\t<PlanModel>\\r\\n\\t\\t\\t\\t\\t<documentationxml: lang=\\\"\\\" source=\\\"http://tempuri.org\\\" />\\r\\n\\t\\t\\t\\t</PlanModel>\\r\\n\\t\\t\\t</Plan>\\r\\n\\t\\t</Plans>\\r\\n\\t</ServiceTemplate>\\r\\n\\t<NodeTypeImplementation nodeType=\\\"QName\\\" name=\\\"NCName\\\"></NodeTypeImplementation>\\r\\n</Definitions>\",\n" +
            "\t\"customizationCreation\": \"2015-02-15\",\n" +
            "\t\"customizationActivation\": \"2015-02-15\",\n" +
            "\t\"customizationDecommission\": \"2015-02-15\",\n" +
            "\t\"username\": \"1\"\n" +
            "}";
    private MockMvc mockMvc;

    @Mock
    ApplicationService applicationService;

    @Mock
    ApplicationsRepository applicationsRepository;

  /*  @Autowired
    ApplicationEventPublisher eventPublisher;
*/
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ApplicationResource instance = new ApplicationResource();
        applicationService = new ApplicationServiceImpl();

        ApplicationEventPublisher appPublisher = mock(ApplicationEventPublisher.class);

        ReflectionTestUtils.setField(instance,"eventPublisher",appPublisher);
        ReflectionTestUtils.setField(applicationService, "applicationsRepository", applicationsRepository);
        ReflectionTestUtils.setField(applicationService, "eventPublisher",appPublisher);
        ReflectionTestUtils.setField(instance, "applicationService", applicationService);
        mockMvc = MockMvcBuilders.standaloneSetup(instance).build();
    }

    @Test
    public void testToscaSave() throws Exception {
//        HttpServletRequest request = anyObject();
//        HttpServletResponse response = anyObject();
        MockHttpServletRequestBuilder getRequest = post("/api/application/create").accept(MediaType.APPLICATION_JSON_VALUE).content("").contentType(MediaType.APPLICATION_JSON_VALUE);
        getRequest.requestAttr("xmlTosca", "");
        ResultActions results = mockMvc.perform(getRequest);
        results.andExpect(status().isCreated());
    }

    @Test
    public void testListAllToscaFile() throws Exception {
        HttpServletRequest request = anyObject();
        HttpServletResponse response = anyObject();
        MockHttpServletRequestBuilder getRequest = get("/api/application/list").accept(MediaType.ALL);
        ResultActions results = mockMvc.perform(getRequest);
        results.andExpect(status().isOk());
    }

    @Test
    public void listToscFileById() throws Exception {
        MockHttpServletRequestBuilder getRequest = get("/api/application/1").accept(MediaType.ALL);
        ResultActions results = mockMvc.perform(getRequest);
        results.andExpect(status().isOk());
    }

    @Test
    public void testHttpGetClientCall() throws IOException {


        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
        provider.setCredentials(AuthScope.ANY, credentials);
//            HttpClient client =
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
//        HttpResponse response = httpclient.execute(new HttpGet("http://localhost:8080"));
//        int statusCode = response.getStatusLine().getStatusCode();
//            assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        HttpGet httpget = new HttpGet("http://localhost:8080/api/application/1");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
/*        nvps.add(new BasicNameValuePair("username", "admin"));
        nvps.add(new BasicNameValuePair("password", "admin"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));*/
        StringEntity se = new StringEntity("");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//        httpPost.setEntity(se);
        httpget.setHeader("Content-type", "application/json");
        CloseableHttpResponse response2 = httpclient.execute(httpget);
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }
        System.out.println("test");
    }

    @Test
    public void testeCreateNewApplication() throws IOException {
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
            provider.setCredentials(AuthScope.ANY, credentials);
//            HttpClient client =
            CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/application/create");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            StringEntity se = new StringEntity(createNewApplicationJson);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            try {
                    System.out.println(response2.getStatusLine());
                    HttpEntity entity2 = response2.getEntity();
                    // do something useful with the response body
                    // and ensure it is fully consumed
                    EntityUtils.consume(entity2);
            } finally {
                    response2.close();
            }
            System.out.println("testeCreateNewApplication");
    }

    @Test
    public void testeCreateNewCustomization() throws IOException {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
        provider.setCredentials(AuthScope.ANY, credentials);
//            HttpClient client =
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        HttpPost httpPost = new HttpPost("http://localhost:8080/api/customization/create");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        StringEntity se = new StringEntity(createNewCustomizationJson);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPost.setEntity(se);
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }
        System.out.println("testeCreateNewApplication");
    }

    @Test
    public void testHttpClientCall() throws IOException {


        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
        provider.setCredentials(AuthScope.ANY, credentials);
//            HttpClient client =
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
//        HttpResponse response = httpclient.execute(new HttpGet("http://localhost:8080"));
//        int statusCode = response.getStatusLine().getStatusCode();
//            assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        HttpPost httpPost = new HttpPost("http://localhost:8080/api/application/create");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
/*        nvps.add(new BasicNameValuePair("username", "admin"));
        nvps.add(new BasicNameValuePair("password", "admin"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));*/
        StringEntity se = new StringEntity("");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPost.setEntity(se);
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }
        System.out.println("test");
    }
}
