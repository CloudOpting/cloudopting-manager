package eu.cloudopting.cloud;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Providers;
import eu.cloudopting.provision.cloudstack.CloudstackRequest;

@Service
public class CloudService {
	private final Logger log = LoggerFactory.getLogger(CloudService.class);

	private HashMap<Long, HashMap<String, String>> accounts = new HashMap<Long, HashMap<String, String>>();

	public boolean setUpCloud(String apikey, String secretKey, String endpoint, String provider, Long id) {
		HashMap<String, String> theAccount = this.accounts.get(id);
		if (theAccount == null) {
			HashMap<String, String> myAccount = new HashMap<String, String>();
			myAccount.put("apikey", apikey);
			myAccount.put("secretKey", secretKey);
			myAccount.put("provider", provider);
			myAccount.put("endpoint", endpoint);
			this.accounts.put(id, myAccount);
		}
		return true;
	}

	public String createVM(Long cloudAccountId, String cpu, String memory, String disk) {
		log.debug("in createVM");
		HashMap<String, String> theAccount = this.accounts.get(cloudAccountId);
		if (theAccount == null)
			return null;
		switch (theAccount.get("provider")) {
		case "cloudstack":
			CloudstackRequest myRequest = new CloudstackRequest();
			myRequest.setEndpoint(theAccount.get("endpoint"));
			myRequest.setIdentity(theAccount.get("apikey"));
			myRequest.setCredential(theAccount.get("secretkey"));
			myRequest.setDefaultTemplate(theAccount.get("secretkey"));
			break;
		case "azure":
			
			break;

		default:
			break;
		}
		String id_of_cloud_task = "wrwerwerwrwre";
		// look in table the match of cpu+memory to get the name of the offering
		// than get the disk info
		// get keys from db with cloudId
		// build the VM
		return id_of_cloud_task;
	}

	public boolean checkVM(String cloudId, String taskId) {
		log.debug("in checkVM");
		return true;
	}
}
