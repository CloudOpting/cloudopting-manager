package eu.cloudopting.cloud;

import java.util.HashMap;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Providers;
import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.cloudstack.CloudstackRequest;
import eu.cloudopting.provision.cloudstack.CloudstackResult;

@Service
public class CloudService {
	private final Logger log = LoggerFactory.getLogger(CloudService.class);

	private HashMap<Long, HashMap<String, String>> accounts = new HashMap<Long, HashMap<String, String>>();

	@Inject
	ProvisionComponent<CloudstackResult, CloudstackRequest> cloudStackProvision;

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
	public String createVM(Long cloudAccountId, String cpu, String memory, String disk) {
		log.debug("in createVM");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		String cloudTaskId = null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			log.debug("before creating the cloudstack VM");
			CloudstackRequest myRequest = new CloudstackRequest();
			myRequest.setEndpoint(theAccount.get("endpoint"));
			myRequest.setIdentity(theAccount.get("apikey"));
			myRequest.setCredential(theAccount.get("secretKey"));
			myRequest.setDefaultTemplate("88fcdf8f-891a-4d11-b02f-448861216b02");
			log.debug("the request:" + myRequest.toString());
			// CloudstackResult result =
			// cloudStackProvision.provision(myRequest);
			cloudTaskId = cloudStackProvision.provisionVM(myRequest);
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
			log.debug("before creating the cloudstack VM");
			CloudstackRequest myRequest = new CloudstackRequest();
			myRequest.setEndpoint(theAccount.get("endpoint"));
			myRequest.setIdentity(theAccount.get("apikey"));
			myRequest.setCredential(theAccount.get("secretKey"));
			myRequest.setDefaultTemplate("88fcdf8f-891a-4d11-b02f-448861216b02");
			log.debug("the request:" + myRequest.toString());
			theCheck = cloudStackProvision.checkVMdeployed(myRequest, taskId);
			break;
		case "azure":

			break;

		default:
			break;
		}
		return theCheck;
	}

	public JSONObject getVMinfo(Long cloudAccountId, String taskId) {
		log.debug("in getVMinfo");
		// TODO this will have to be set to false in production
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		JSONObject vmData = null;
		if (theAccount == null)
			return null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			CloudstackRequest myRequest = new CloudstackRequest();
			myRequest.setEndpoint(theAccount.get("endpoint"));
			myRequest.setIdentity(theAccount.get("apikey"));
			myRequest.setCredential(theAccount.get("secretKey"));
			myRequest.setDefaultTemplate("88fcdf8f-891a-4d11-b02f-448861216b02");
			log.debug("the request:" + myRequest.toString());
			
			vmData = cloudStackProvision.getVMinfo(myRequest, taskId);
			log.debug(vmData.toString());
			break;
		case "azure":

			break;

		default:
			break;
		}
		return vmData;
	}

}
