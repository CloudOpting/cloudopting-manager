package eu.cloudopting;

import eu.cloudopting.provision.azure.AzureRequest;
import org.jclouds.azurecompute.domain.RoleSize;

/**
 * Base test for provision component
 */
public class ProvisionBaseTest {


    public AzureRequest createAzureRequest() {
        AzureRequest azureRequest = new AzureRequest();
        azureRequest.setSubscriptionId("d7aa0da8-ad36-440c-a363-f2e7d23f105b");
       // azureRequest.setIdentity(this.getClass().getResource("c:/tmp/mycert.pfx").getFile());
        azureRequest.setIdentity("c:/tmp/mycert.pfx");
       // azureRequest.setIdentity("CHANGEME");
        azureRequest.setCredential("1qaz2wsx");
        return azureRequest;
    }

}
