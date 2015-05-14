package eu.cloudopting.docker.images;

import java.util.ArrayList;

import eu.cloudopting.docker.DockerError;
import eu.cloudopting.docker.restclient.CraneRestClient;

/**
 *
 * TODO: javadoc
 *
 */
public class DockerBuilder {

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
			result = "{\"statusCode\":\"1\" ,\"statusDescription\": \"building\" }";
		}else if(token.equals("52d6NR1U1X")){
			result = "{\"statusCode\":\"0\" ,\"statusDescription\": \"completed\" }";
		}else{
			result = "{\"statusCode\":\"2\" ,\"statusDescription\":\"build error\", \"additonalInfo\":\"INFO[0004] Error: image library/imagename:latest not found\"}";
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
