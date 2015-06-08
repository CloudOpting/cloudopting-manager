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
import org.hibernate.annotations.SourceType;
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

    private View view;

    @Test
    public void testCloudStack(){
        CloudstackResult result = cloudStackProvision.provision(new CloudstackRequest());
        System.out.println("Provision complete");;
    }


}
