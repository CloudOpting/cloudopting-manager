package eu.cloudopting.provision.azure;

import eu.cloudopting.provision.ProvisionRequest;
import org.jclouds.azurecompute.domain.OSImage;
import org.jclouds.azurecompute.domain.RoleSize;
import org.jclouds.azurecompute.domain.StorageService;

import java.util.Random;

/**
 * Azure request object.
 */
public class AzureRequest implements ProvisionRequest{

    static int RAND = new Random().nextInt(999);
    /**
     * The identity to be used to login to the azure/cloudstack platform. This can be the location of a keystore
     */
    private String identity;
    /**
     * The pasword used to login in the cloud provider or the password for keystore.
     */
    private String credential;
    /**
     * Endpoint to connect to the management service of the cloud provide
     */
    private String url="https://management.core.windows.net/";
    /**
     * The subcription id for the azure subscription plan
     */
    private String subscriptionId;
    /**
     * Where the virtual machine will be located, in witch geographical area
     */
    private String location = "West Europe";
    /**
     * The name of the virtual network that will be created
     */
    private String virtualNetworkName = "jclouds-virtual-network";
    /**
     * An unique service name or an existing service name
     */
    private String storageServiceName = String.format("%3.24s", System.getProperty("user.name") + RAND + this.getClass().getSimpleName()).toLowerCase();

    /**
     * An unique cloud service name or an existing one
     */
    private String cloudService = String.format("%s%d-%s",
            System.getProperty("user.name"), RAND, AzureProvision.class.getSimpleName()).toLowerCase();

    /**
     * The name of the deployment
     */
    private String deployment = String.format("%s%d-%s",
            System.getProperty("user.name"), RAND, AzureProvision.class.getSimpleName()).toLowerCase();

    /**
     * Allocated ip ranges for the deplyments
     */
    private  String addressSpace = "10.0.0.0/20";
    /**
     * The subnet name
     */
    private  String subnetName = "jclouds-1";
    /**
     * The subnet ip range
     */
    private  String subnetAddressSpace = "10.0.0.0/23";

    /**
     * The name of the image to be deployed
     */
    private String imageName
            = "b39f27a8b8c64d52b05eac6a62ebad85__Ubuntu-14_04_1-LTS-amd64-server-20150123-en-us-30GB";

    /**
     * Username for the deployed machine
     */
    private String username= "cloudopting";
    /**
     * Password for the deployed machine
     */
    private String password = "supersecurePassword1";

    /**
     * The size of the machine as defined by the cloud provider
     */
    private RoleSize.Type roleSizeType;

    /**
     * The image type of the machine
     */
    private OSImage.Type osImageType;

    /**
     * The storage account type from the cloud provider.
     */
    StorageService.AccountType storageAccountType;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }


    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVirtualNetworkName() {
        return virtualNetworkName;
    }

    public void setVirtualNetworkName(String virtualNetworkName) {
        this.virtualNetworkName = virtualNetworkName;
    }

    public String getStorageServiceName() {
        return storageServiceName;
    }

    public void setStorageServiceName(String storageServiceName) {
        this.storageServiceName = storageServiceName;
    }

    public String getCloudService() {
        return cloudService;
    }

    public void setCloudService(String cloudService) {
        this.cloudService = cloudService;
    }

    public String getDeployment() {
        return deployment;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }

    public String getAddressSpace() {
        return addressSpace;
    }

    public String getSubnetName() {
        return subnetName;
    }

    public String getSubnetAddressSpace() {
        return subnetAddressSpace;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setAddressSpace(String addressSpace) {
        this.addressSpace = addressSpace;
    }

    public void setSubnetName(String subnetName) {
        this.subnetName = subnetName;
    }

    public void setSubnetAddressSpace(String subnetAddressSpace) {
        this.subnetAddressSpace = subnetAddressSpace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleSize.Type getRoleSizeType() {
        if(roleSizeType==null) return RoleSize.Type.BASIC_A2; //default value
        return roleSizeType;
    }

    public void setRoleSizeType(RoleSize.Type type) {
        this.roleSizeType = type;
    }

    public OSImage.Type getOsImageType() {
        if(osImageType==null) return OSImage.Type.LINUX;
        return osImageType;
    }

    public void setOsImageType(OSImage.Type osImageType) {
        this.osImageType = osImageType;
    }

    public StorageService.AccountType getStorageAccountType() {
        if(storageAccountType ==null) return StorageService.AccountType.Standard_LRS;
        return storageAccountType;
    }

    public void setStorageAccountType(StorageService.AccountType storageAccountType) {
        this.storageAccountType = storageAccountType;
    }
}
