package eu.cloudopting.cloud;

import org.springframework.stereotype.Service;

@Service
public class CloudService {

	public String createVM(String cloudId, String cpu, String memory,String disk){
		String id_of_cloud_task = "wrwerwerwrwre";
		// look in table the match of cpu+memory to get the name of the offering than get the disk info
		// get keys from db with cloudId
		// build the VM
		return id_of_cloud_task;
	}
	
	public boolean checkVM(String cloudId, String taskId){
		
		return true;
	}
}
