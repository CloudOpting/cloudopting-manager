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

}
