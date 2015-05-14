package eu.cloudopting.docker.cluster;

import java.util.ArrayList;

import eu.cloudopting.docker.DockerError;
import eu.cloudopting.docker.restclient.CraneRestClient;

/**
 *
 * TODO: javadoc
 *
 */
public class DockerCluster {

	public DockerCluster(CraneRestClient restClient) {
		// TODO Auto-generated constructor stub
	}


	/* Methods */

	/**
	 * TODO: create cluster with one VM
	 */
	public String create(String hostname, int port, String privateKeyFilePath, String passphrase) throws DockerError {
		// TODO
		String token = "gDxaX29P8v";
		return token;
	}


	/**
	 * TODO: retrieve info about the general status of a cluster
	 */
	public String getInfo(String token)  throws DockerError {
		// TODO
		String result;
		if(token.equals("gDxaX29P8v")){
			result = "{\"statusCode\":\"1\",\"statusDescription\": \"Connecting to machines\", \"detailedStatus\":{} }";
		}else if(token.equals("45R1NG9E5X")){
			result = "{\"statusCode\":\"2\" ,\"statusDescription\": \"Connected to machines. Creating swarm master.\" }";
		}else if(token.equals("45R1NG9E5X")){
			result = "{\"statusCode\":\"3\" ,\"statusDescription\": \"Creating swarm master\" }";
		}else{
			result = "{\"statusCode\":\"2\" ,\"statusDescription\":\"build error\", \"additonalInfo\":\"INFO[0004] Error: image library/imagename:latest not found\"}";
		}
		return result;
	}


	/**
	 * TODO: destroy a cluster
	 */
	public void stop(String token)  throws DockerError {
		// TODO
	}

}
