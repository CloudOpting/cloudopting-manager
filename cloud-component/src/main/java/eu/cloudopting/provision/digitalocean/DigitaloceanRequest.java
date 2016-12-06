package eu.cloudopting.provision.digitalocean;

import eu.cloudopting.provision.ProvisionRequest;

public class DigitaloceanRequest implements ProvisionRequest {

    String identity;
    String credential;
    String endpoint;
    String virtualMachineId;
    String userData;

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
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getVirtualMachineId() {
		return virtualMachineId;
	}
	public void setVirtualMachineId(String virtualMachineId) {
		this.virtualMachineId = virtualMachineId;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
}
