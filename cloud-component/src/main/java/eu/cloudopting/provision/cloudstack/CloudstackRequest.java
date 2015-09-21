package eu.cloudopting.provision.cloudstack;

import eu.cloudopting.provision.ProvisionRequest;
import eu.cloudopting.provision.azure.AzureProvision;

import org.jclouds.cloudstack.domain.PortForwardingRule.Protocol;
import org.jclouds.compute.domain.OsFamily;

import java.util.Random;

/**
 * cloudstack request
 * @author Daniel P.
 */
public class CloudstackRequest implements ProvisionRequest {

    String defaultTemplate = "7135ff27-09f1-11e5-b2dc-080027f4dca6";
    String identity="ViYyhNulKV9XikY6NgD_bHicFYWBUjDKYwut2ei5C3JtLEz0QphurUsVqc_olCHxg4bkuW-_BRBN5IftgiIXoA";
    String credential = "RWRh3OO07QOaSKTi4IOlDvH6S6t0bJRTD6mqJSRgueGsaVIxVO7gDkavQ77oLNdIaFzrkNxic70Q-6CSvzNncg";
    String endpoint="http://192.168.56.10:8096/client/api";
    OsFamily osFamily = OsFamily.CENTOS;
    Integer minRam=128;
    String ipaddressId;
	String virtualMachineId;
    int publicPort;
    int privatePort;
    Protocol protocol = Protocol.TCP;
    String cpu;
    long diskSize;
    String userData;
    
    public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public long getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(long diskSize) {
		this.diskSize = diskSize;
	}

	
    
    public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	
    public String getIpaddressId() {
		return ipaddressId;
	}

	public void setIpaddressId(String ipaddressId) {
		this.ipaddressId = ipaddressId;
	}

	public String getVirtualMachineId() {
		return virtualMachineId;
	}

	public void setVirtualMachineId(String virtualMachineId) {
		this.virtualMachineId = virtualMachineId;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

	public int getPrivatePort() {
		return privatePort;
	}

	public void setPrivatePort(int privatePort) {
		this.privatePort = privatePort;
	}

    public Integer getMinRam() {
        return minRam;
    }

    public void setMinRam(Integer minRam) {
        this.minRam = minRam;
    }

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

    public String getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public OsFamily getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(OsFamily osFamily) {
        this.osFamily = osFamily;
    }
}
