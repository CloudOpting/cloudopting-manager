package eu.cloudopting.provision.azure;

import eu.cloudopting.provision.ProvisionResult;

/**
 * Azure result object.
 */
public class AzureResult implements ProvisionResult {
    /**
     * The name of the created instance
     */
    String deplymentName;
    /**
     * The id of the deplyment
     */
    String deplymentId;

    /**
     * The name of the virtual network
     */
    String virtualNetwokName;

    /**
     * The name of the cloud service
     */
    String cloudServiceName;

    /**
     * Storage name
     */
    String storageName;

    /**
     * The username to login into the vm
     */
    String vmUser;

    /**
     * The password used to login into the vm
     */
    String vmPassword;

    String requestId;

    public String getDeplymentName() {
        return deplymentName;
    }

    public void setDeplymentName(String deplymentName) {
        this.deplymentName = deplymentName;
    }

    public String getDeplymentId() {
        return deplymentId;
    }

    public void setDeplymentId(String deplymentId) {
        this.deplymentId = deplymentId;
    }

    public String getVirtualNetwokName() {
        return virtualNetwokName;
    }

    public void setVirtualNetwokName(String virtualNetwokName) {
        this.virtualNetwokName = virtualNetwokName;
    }

    public String getCloudServiceName() {
        return cloudServiceName;
    }

    public void setCloudServiceName(String cloudServiceName) {
        this.cloudServiceName = cloudServiceName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getVmUser() {
        return vmUser;
    }

    public void setVmUser(String vmUser) {
        this.vmUser = vmUser;
    }

    public String getVmPassword() {
        return vmPassword;
    }

    public void setVmPassword(String vmPassword) {
        this.vmPassword = vmPassword;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
