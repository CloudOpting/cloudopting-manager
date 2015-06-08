package eu.cloudopting.cloudstack;

import br.com.abril.api.cloudstack.utils.CloudStackHelper;
import br.com.abril.api.cloudstack.utils.Utils;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.cloudstack.CloudstackProvision;
import eu.cloudopting.provision.cloudstack.CloudstackRequest;
import eu.cloudopting.provision.cloudstack.CloudstackResult;
import eu.cloudopting.provision.config.ProvisionConfig;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.View;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.cloudstack.CloudStackApi;
import org.jclouds.cloudstack.CloudStackApiMetadata;
import org.jclouds.cloudstack.CloudStackContext;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.*;

import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static org.jclouds.reflect.Reflection2.typeToken;

/**
 * Cloudstack provision tests
 */
@ContextConfiguration(classes = {ProvisionConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CloudstackProvisionTests {

    @Inject
    ProvisionComponent<CloudstackResult,CloudstackRequest> cloudStackProvision;

    CloudstackRequest request;
    private final Set<Module> modules = ImmutableSet.<Module>of(new ExecutorServiceModule(newDirectExecutorService()));
    String API_KEY = "ViYyhNulKV9XikY6NgD_bHicFYWBUjDKYwut2ei5C3JtLEz0QphurUsVqc_olCHxg4bkuW-";
    String SECRET_KEY = "RWRh3OO07QOaSKTi4IOlDvH6S6t0bJRTD6mqJSRgueGsaVIxVO7gDkavQ77oLNdIaFzrkNxic70Q";

    private View view;

    @Test
    public void testCloudStack(){
        cloudStackProvision.provision(new CloudstackRequest());
        System.out.println("ddd");
    }

    @Test
    public void testCloudStackDeploy(){
        ApiMetadata  metadata = new CloudStackApiMetadata();
    }

    private void createContext(){
        Properties prop = new Properties();
        prop.put(Constants.PROPERTY_ENDPOINT, "http://192.168.56.10:8096/client/api");
        ContextBuilder builder = ContextBuilder.newBuilder("cloudstack")
                .credentials(API_KEY, SECRET_KEY)
                .overrides(prop);
        this.view = builder.buildView(CloudStackContext.class);
    }

    private CloudStackApi api(){
      return this.view.unwrapApi(CloudStackApi.class);
    }



    private static String apiKey = "ViYyhNulKV9XikY6NgD_bHicFYWBUjDKYwut2ei5C3JtLEz0QphurUsVqc_olCHxg4bkuW";
    private static String secretKey = "RWRh3OO07QOaSKTi4IOlDvH6S6t0bJRTD6mqJSRgueGsaVIxVO7gDkavQ77oLNdIaFzrkNxic70Q";
    private static String host = "http://192.168.56.10:8096";
    private static String userId = "718da213-09f1-11e5-b2dc-080027f4dca6";



    /*private CloudStackApi api() {
        Properties properties = new Properties();
      *//*  ProviderMetadata metadata = new CloudStackApiMetadata();*//*
        ApiMetadata metadata = new CloudStackApiMetadata();

        return ContextBuilder.newBuilder(metadata).credentials(API_KEY, SECRET_KEY).endpoint("http://192.168.56.10:8080/client/api/")
                .modules(modules).overrides(properties).buildApi(CloudStackApi.class);
    }*/
}
