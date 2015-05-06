package eu.cloudopting.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class DockerService {
	private final Logger log = LoggerFactory.getLogger(DockerService.class);
	
	public String buildDockerImage(String customer, String service, String dockerfile,String path){
		// TODO imagename is dummy API will return the imagename so that in sequent calls we can do check on asynch work
		String imagename=null;
		return imagename;
		
	}
	
	public boolean isImageBuilt(String imagename){
		return false;
	}
	
	public String runDockerCompose(String customer, String service, String path){
		// TODO we need to define what we return from docker-compose asynch execution
		// imagename is no more consistent
		String imagename=null;
		return imagename;

		
	}
	
	public boolean isDockerComposeRunning(String token){
		return false;
	}
}
