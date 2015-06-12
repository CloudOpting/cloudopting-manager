package eu.cloudopting.bla;

import eu.cloudopting.ProvisionBaseTest;
import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.azure.AzureRequest;
import eu.cloudopting.provision.azure.AzureResult;
import eu.cloudopting.provision.config.ProvisionConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * Tests for azure provision
 */
@ContextConfiguration(classes = {ProvisionConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DockerTests extends ProvisionBaseTest {

    @Inject
    ProvisionComponent<AzureResult, AzureRequest> azureProvision;

    @Test
    public void testAzureProvision() {
        AzureRequest request = createAzureRequest();
        azureProvision.provision(request);
    }


}
