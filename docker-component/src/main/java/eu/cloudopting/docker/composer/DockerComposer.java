package eu.cloudopting.docker.composer;

import java.util.ArrayList;

import eu.cloudopting.docker.restclient.CraneRestClient;
import eu.cloudopting.docker.DockerError;

/**
 *
 * TODO: javadoc
 *
 */
public class DockerComposer {



	public DockerComposer(CraneRestClient restClient) {
		// TODO Auto-generated constructor stub
	}

	/* Methods */

	/**
	 * TODO: start the deploy of a composition of containers
	 */
	public String start(String pathToComposeYml) throws DockerError {
		// TODO
		String token = null;
		return token;
	}

	/**
	 * TODO: retrieve info about the deploy process status
	 */
	public String getInfo(String token)  throws DockerError {
		// TODO
		String result = null;
		return result;
	}

	/**
	 * TODO: destroy stop the deploy process
	 */
	public void stop(String token)  throws DockerError {
		// TODO
	}
}
