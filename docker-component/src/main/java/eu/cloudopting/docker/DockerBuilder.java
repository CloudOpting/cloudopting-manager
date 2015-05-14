package eu.cloudopting.docker;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import eu.cloudopting.docker.CraneRestClient;
import eu.cloudopting.docker.DockerError;

/**
 *
 * TODO: javadoc
 *
 */
public class DockerBuilder {
	private final Logger log = LoggerFactory.getLogger(DockerBuilder.class);

	public DockerBuilder(CraneRestClient restClient) {
		// TODO
	}

	/* Methods */
	/**
	 * TODO: start the building process for one docker image
	 */
	public String start(String pathToDockerfile, String pathToPuppetManifest, String imageName) throws DockerError {
		// TODO
		String token = "52d6NR1U1X";
		return token;
	}

	/**
	 * TODO: start the building process for several docker images
	 */
	public String start(ArrayList<String> pathsToDockerfiles, ArrayList<String> pathsToPuppetManifests, ArrayList<String> imageNames)  throws DockerError {
		// TODO
		String token = "vJQ22H4P8f";
		return token;
	}

	/**
	 * TODO: retrieve info about the building process
	 */
	public String getInfo(String token)  throws DockerError {
		// TODO
		String result;
		if(token.equals("vJQ22H4P8f")){
			result = "{"status": "building" }";
		}else if(token.equals("52d6NR1U1X")){
			result = "{"status": "completed" }";
		}else  if(token.equals("76c6yU8W1k")){
			result = "{"status":"build error", "additonalInfo":"INFO[0004] Error: image library/imagename:latest not found"}"
		}
		return result;
	}

	/**
	 * TODO: retrieve info about the building process
	 */
	public void stopBuild(String token)  throws DockerError {
		// TODO
	}

}
