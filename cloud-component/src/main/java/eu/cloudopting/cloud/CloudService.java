package eu.cloudopting.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class CloudService {
	private final Logger log = LoggerFactory.getLogger(CloudService.class);

	public String createVM(String cloudId, String cpu, String memory,String disk){
		log.debug("in createVM");
		String id_of_cloud_task = "wrwerwerwrwre";
		// look in table the match of cpu+memory to get the name of the offering than get the disk info
		// get keys from db with cloudId
		// build the VM
		return id_of_cloud_task;
	}
	
	public boolean checkVM(String cloudId, String taskId){
		log.debug("in checkVM");
		return true;
	}
}
