package eu.cloudopting.docker;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * TODO: javadoc
 *
 */
@Service
public class DockerCluster {
	private final Logger log = LoggerFactory.getLogger(DockerCluster.class);

	

	public DockerCluster(CraneRestClient restClient) {
		// TODO Auto-generated constructor stub
	}


	/* Methods */
	
	/**
	 * TODO: create cluster with one VM
	 */
	public String create(String hostname, int port, String privateKeyFilePath, String passphrase) throws DockerError {
		// TODO
		String token = null;
		return token;
	}
	
	/**
	 * TODO: start the building process for several docker images
	 */
	public String startBuild(ArrayList<String> pathsToDockerfiles, ArrayList<String> pathsToPuppetManifests, ArrayList<String> imageNames)  throws DockerError {
		// TODO
		String token = null;
		return token;
	}

	/**
	 * TODO: retrieve info about the general status of a cluster
	 */
	public String getInfo(String token)  throws DockerError {
		// TODO
		String result = null;
		return result;
	}
	
	/**
	 * TODO: destroy a cluster
	 */
	public void stop(String token)  throws DockerError {
		// TODO
	}
	
}



