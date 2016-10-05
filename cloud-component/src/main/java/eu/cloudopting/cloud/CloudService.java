package eu.cloudopting.cloud;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.cloudstack.CloudstackRequest;
import eu.cloudopting.provision.cloudstack.CloudstackResult;
import eu.cloudopting.provision.digitalocean.DigitaloceanRequest;
import eu.cloudopting.provision.digitalocean.DigitaloceanResult;

@Service
public class CloudService {
	private final Logger log = LoggerFactory.getLogger(CloudService.class);
	
	@Value("${cloud.ip}")
	String myIP = "127.0.0.1";

	@Value("${cloud.templateId}")
	String templateId = "88fcdf8f-891a-4d11-b02f-448861216b02";

	@Value("${cloud.diskId}")
	String diskId = "88fcdf8f-891a-4d11-b02f-448861216b02";

	private HashMap<Long, HashMap<String, String>> accounts = new HashMap<Long, HashMap<String, String>>();

	@Inject
	ProvisionComponent<CloudstackResult, CloudstackRequest> cloudStackProvision;

	@Inject
	ProvisionComponent<DigitaloceanResult, DigitaloceanRequest> digitaloceanProvision;
	
	public boolean setUpCloud(String apikey, String secretKey, String endpoint, String provider, Long id) {
		HashMap<String, String> theAccount = this.accounts.get(id);
		if (theAccount == null) {
			HashMap<String, String> myAccount = new HashMap<String, String>();
			myAccount.put("apikey", apikey);
			myAccount.put("secretKey", secretKey);
			myAccount.put("provider", provider);
			myAccount.put("endpoint", endpoint);
			log.debug(myAccount.toString());
			this.accounts.put(id, myAccount);
		}
		return true;
	}

	private CloudstackRequest createCloudStackRequest(HashMap<String, String> theAccount) {
		CloudstackRequest myRequest = new CloudstackRequest();
		myRequest.setEndpoint(theAccount.get("endpoint"));
		myRequest.setIdentity(theAccount.get("apikey"));
		myRequest.setCredential(theAccount.get("secretKey"));
		//TODO this will have to end up in the cloud account
		myRequest.setDefaultTemplate(this.templateId);
		log.debug("the request:" + myRequest.toString());
		return myRequest;
	}

	private DigitaloceanRequest createDigitaloceanRequest(HashMap<String, String> theAccount) {
		DigitaloceanRequest request = new DigitaloceanRequest();
		request.setEndpoint(theAccount.get("endpoint"));
		request.setIdentity(theAccount.get("apikey"));
		request.setCredential(theAccount.get("secretKey"));
		return request;
	}
	
	/**
	 * This method is the one that create the VM and return a String with the
	 * JobId so that we can than do the check of the async creation operation If
	 * there is a problem in the VM creation an error must be raised.
	 * 
	 * @param cloudAccountId
	 * @param cpu
	 * @param memory
	 * @param disk
	 * @return
	 */
	public String createVM(Long cloudAccountId, String cpu, String memory, String disk, String processInstanceId) {
		log.debug("in createVM");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		String cloudTaskId = null;
		String unencodedData = null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before creating the cloudstack VM");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			unencodedData = "#cloud-config\n"
					+"runcmd:\n"
					+"  - touch /root/cloudinitexecuted.txt\n"
					+"phone_home:\n"
					+"  url: http://"+myIP+"/api/bpmn/configuredVM/"+processInstanceId+"\n"
					+"  post: all";
			myRequest.setUserData(unencodedData);
			myRequest.setDiskId(this.diskId);

			// cloudStackProvision.provision(myRequest);
			cloudTaskId = cloudStackProvision.provisionVM(myRequest);
			log.debug("after creation" + cloudTaskId.toString());
			break;
		case "digitalocean":
			log.debug("before creating the digitalocean VM");
			DigitaloceanRequest doRequest = createDigitaloceanRequest(theAccount);
			unencodedData = "#cloud-config\n" +
					"yum_repos:\n" +
					"    docker:\n" +
					"        baseurl: https://yum.dockerproject.org/repo/main/centos/7\n" +
					"        enabled: 1\n" +
					"        gpgcheck: 1\n" +
					"        gpgkey: https://yum.dockerproject.org/gpg\n" +
					"        name: Docker Repository\n" +
					"packages:\n" +
					"- docker-engine\n" +
					"write_files:\n" +
					"- path: /etc/systemd/system/docker.service.d/docker.conf\n" +
					"  content: |\n" +
					"    [Service]\n" +
					"    ExecStart=\n" +
					"    ExecStart=/usr/bin/docker daemon -H fd:// -H tcp://0.0.0.0:2375\n" +
					"runcmd:\n" +
					"- 'systemctl daemon-reload'\n" +
					"- 'systemctl enable docker'\n" +
					"- 'systemctl start docker'";
			doRequest.setUserData(unencodedData);
			cloudTaskId = digitaloceanProvision.provisionVM(doRequest);
			log.debug("after creation" + cloudTaskId.toString());
			break;
		case "azure":

			break;

		default:
			break;
		}
		
		// look in table the match of cpu+memory to get the name of the offering
		// than get the disk info
		// get keys from db with cloudId
		// build the VM
		return cloudTaskId;
	}

	public boolean checkVM(Long cloudAccountId, String taskId) {
		log.debug("in checkVM");
		// TODO this will have to be set to false in production
		boolean theCheck = true;
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return false;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before checking the cloudstack VM");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			theCheck = cloudStackProvision.checkVMdeployed(myRequest, taskId);
			break;
		case "digitalocean":
			log.debug("before checking the digitalocean VM");
			DigitaloceanRequest doRequest = createDigitaloceanRequest(theAccount);
			theCheck = digitaloceanProvision.checkVMdeployed(doRequest, taskId);
			break;
		case "azure":

			break;

		default:
			break;
		}
		return theCheck;
	}

	public JSONObject getVMinfo(Long cloudAccountId, String taskId) {
		log.debug("CloudService in getVMinfo");
		// TODO this will have to be set to false in production
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		JSONObject vmData = null;
		if (theAccount == null)
			return null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			
			vmData = cloudStackProvision.getVMinfo(myRequest, taskId);
			log.debug(vmData.toString());
			break;
		case "digitalocean":
			DigitaloceanRequest doRequest = createDigitaloceanRequest(theAccount);
			vmData = digitaloceanProvision.getVMinfo(doRequest, taskId);
			log.debug(vmData.toString());
			break;
		case "azure":

			break;

		default:
			break;
		}
		return vmData;
	}

	public String acquireIp(Long cloudAccountId) {
		log.debug("in acquireIp");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		String acquireTaskId = null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before acquiring the IP");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			// CloudstackResult result =
			// cloudStackProvision.provision(myRequest);
			acquireTaskId = cloudStackProvision.acquireIp(myRequest);
			log.debug("after creation" + acquireTaskId.toString());
			break;
		case "digitalocean":
			// Nothing to do here
			break;
		case "azure":

			break;

		default:
			break;
		}
		
		return acquireTaskId;
	}

	public boolean checkAssociateIp(Map<String, Object> cloudParams) {
		log.debug("in checkAssociateIp");
		// TODO this will have to be set to false in production
		boolean theCheck = true;
		Long cloudAccountId = (Long)cloudParams.get("cloudAccountId");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return false;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before checking the associated IP");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			String taskId = (String)cloudParams.get("acquireJobId");
			theCheck = cloudStackProvision.checkIpAcquired(myRequest, taskId);
			break;
		case "digitalocean":
			// Nothing to do here
			break;
		case "azure":

			break;

		default:
			break;
		}
		return theCheck;
	}

	public JSONObject getAssociatedIpinfo(Map<String, Object> cloudParams) {
		log.debug("CloudService in getAssociatedIpinfo");
		// TODO this will have to be set to false in production
		Long cloudAccountId = (Long)cloudParams.get("cloudAccountId");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		JSONObject ipData = null;
		if (theAccount == null)
			return null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			String taskId = (String)cloudParams.get("acquireJobId");
			ipData = cloudStackProvision.getAcquiredIpinfo(myRequest, taskId);
			log.debug(ipData.toString());
			break;
		case "digitalocean":
			DigitaloceanRequest doRequest = createDigitaloceanRequest(theAccount);
			String vmId = (String)cloudParams.get("vmId");
			ipData = digitaloceanProvision.getAcquiredIpinfo(doRequest, vmId);
			log.debug(ipData.toString());
			break;
		case "azure":

			break;
		default:
			break;
		}
		return ipData;
	}

	public String createPortForward(Long cloudAccountId, String ipId, String vmId, int privatePort, int publicPort) {
		log.debug("in createPortForward");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		String portForwardTaskId = null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before creating the port forward rule");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			myRequest.setPrivatePort(privatePort);
			myRequest.setPublicPort(publicPort);
			myRequest.setIpaddressId(ipId);
			myRequest.setVirtualMachineId(vmId);
			portForwardTaskId = cloudStackProvision.portForward(myRequest);
			log.debug("after creation" + portForwardTaskId.toString());
			break;
		case "azure":

			break;

		default:
			break;
		}
		
		return portForwardTaskId;
	}

	public boolean checkPortForward(Long cloudAccountId, String taskId) {
		log.debug("in checkPortForward");
		// TODO this will have to be set to false in production
		boolean theCheck = true;
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return false;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before checking the PortForward");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			theCheck = cloudStackProvision.checkPortForward(myRequest, taskId);
			break;
		case "azure":

			break;

		default:
			break;
		}
		return theCheck;
	}
	
	public String createFirewall(Long cloudAccountId) {
		log.debug("in createFirewall");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		String firewallTaskId = null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before creating firewall rule");
			// On CloudStack the firewall is opened by default after doing port forward
			firewallTaskId = "dummy";
			log.debug("after creation" + firewallTaskId.toString());
			break;
		case "azure":

			break;

		default:
			break;
		}
		
		return firewallTaskId;
	}

	public boolean checkFirewall(Long cloudAccountId, String taskId) {
		log.debug("in checkFirewall");
		// TODO this will have to be set to false in production
		boolean theCheck = true;
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return false;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before checking the PortForward");
			// since the firewall rule is defaulted we always return true
			theCheck = true;
			break;
		case "azure":

			break;

		default:
			break;
		}
		return theCheck;
	}

	public boolean isVMrunning(Long cloudAccountId, String vmId) {
		log.debug("in isVMrunning");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return false;
		String cloudTaskId = null;
		JSONObject vmData = null;
		boolean isRunning = true;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before checking if Vm is down");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			myRequest.setVirtualMachineId(vmId);
			// cloudStackProvision.provision(myRequest);
			vmData = cloudStackProvision.getVMinfoById(myRequest);
			
			try {
				String state = vmData.getString("state");
				log.debug("The state is:"+state);
				if(state.equals("Stopped")){
					isRunning = false;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.debug(vmData.toString());
			break;
		case "digitalocean":
			log.debug("before checking if Vm is down");
			DigitaloceanRequest request = createDigitaloceanRequest(theAccount);
			request.setVirtualMachineId(vmId);
			vmData = digitaloceanProvision.getVMinfoById(request);
			
			try {
				String state = vmData.getString("state");
				log.debug("The state is:"+state);
				if(!state.equals("ACTIVE")){
					isRunning = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			log.debug(vmData.toString());
			break;
		case "azure":

			break;

		default:
			break;
		}
		
		// look in table the match of cpu+memory to get the name of the offering
		// than get the disk info
		// get keys from db with cloudId
		// build the VM
		return isRunning;
	}

	public String removeISO(Long cloudAccountId, String vmId){
		log.debug("in removeISO");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		String isoTaskId = null;
		boolean isRunning = true;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before removing the ISO");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			myRequest.setVirtualMachineId(vmId);
			// cloudStackProvision.provision(myRequest);
			isoTaskId = cloudStackProvision.removeISO(myRequest);
			log.debug(isoTaskId);
			break;
		case "digitalocean":
			// Nothing to do here
			break;
		case "azure":

			break;

		default:
			break;
		}
		
		return isoTaskId;
	}
	
	public boolean checkIso(Long cloudAccountId, String taskId) {
		log.debug("in checkIso");
		// TODO this will have to be set to false in production
		boolean theCheck = true;
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return false;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before checking the ISO removal");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			theCheck = cloudStackProvision.checkIso(myRequest, taskId);
			break;
		case "digitalocean":
			// Nothing to do here
			break;
		case "azure":

			break;

		default:
			break;
		}
		return theCheck;
	}

	public String startVM(Long cloudAccountId, String vmId){
		log.debug("in startVM");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		String startTaskId = null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before restarting the cloudstack VM");
			CloudstackRequest myRequest = createCloudStackRequest(theAccount);
			myRequest.setVirtualMachineId(vmId);
			// cloudStackProvision.provision(myRequest);
			startTaskId = cloudStackProvision.startVM(myRequest);
			log.debug(startTaskId);
			break;
		case "digitalocean":
			// Nothing to do here, the machine is already started
			break;
		case "azure":

			break;

		default:
			break;
		}
		
		return startTaskId;
	}

}
