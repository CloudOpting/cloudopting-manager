package eu.cloudopting.cloudstack;

import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.cloudstack.CloudstackRequest;
import eu.cloudopting.provision.cloudstack.CloudstackResult;
import eu.cloudopting.provision.config.ProvisionConfig;
import org.jclouds.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * Cloudstack provision tests
 */
@ContextConfiguration(classes = {ProvisionConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CloudstackProvisionTests {

    @Inject
    ProvisionComponent<CloudstackResult, CloudstackRequest> cloudStackProvision;

    private View view;

    @Test
    public void testCloudStack() {
        CloudstackResult result = cloudStackProvision.provision(new CloudstackRequest());
        System.out.println("Provision complete");

    }


}
