package eu.cloudopting.provision.cloudstack;

import eu.cloudopting.provision.ProvisionRequest;
import eu.cloudopting.provision.azure.AzureProvision;

import java.util.Random;

/**
 * cloudstack request
 * @author Daniel P.
 */
public class CloudstackRequest implements ProvisionRequest {

    static int RAND = new Random().nextInt(999);

    /**
     * An unique cloud service name or an existing one
     */
    private String cloudService = String.format("%s%d-%s",
            System.getProperty("user.name"), RAND, AzureProvision.class.getSimpleName()).toLowerCase();

    /**
     * Where the virtual machine will be located, in witch geographical area
     */
    private String location = "West Europe";

    public String getCloudService() {
        return cloudService;
    }

    public void setCloudService(String cloudService) {
        this.cloudService = cloudService;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
