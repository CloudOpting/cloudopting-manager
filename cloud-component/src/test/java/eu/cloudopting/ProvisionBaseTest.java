package eu.cloudopting;

import eu.cloudopting.provision.azure.AzureRequest;
import org.jclouds.azurecompute.domain.RoleSize;

/**
 * Base test for provision component
 */
public class ProvisionBaseTest {


    public AzureRequest createAzureRequest() {
        AzureRequest azureRequest = new AzureRequest();
        azureRequest.setSubscriptionId("CHANGEME");
       // azureRequest.setIdentity(this.getClass().getResource("/certificate.pfx").getFile());
        azureRequest.setIdentity("CHANGEME");
        azureRequest.setCredential("CHANGEME");
        return azureRequest;
    }

}
