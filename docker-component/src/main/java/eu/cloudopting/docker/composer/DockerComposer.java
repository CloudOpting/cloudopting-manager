package eu.cloudopting.docker.composer;

import eu.cloudopting.docker.DockerError;

/**
 *
 * Handles the composing and deploy process.
 *
 */
public class DockerComposer {
	
	private String endPoint;
	
	public DockerComposer(String endPoint) {
		this.endPoint = endPoint;
	}


	/**
	 * Asks the API to starts the deploy process.
	 * @param sourceDockerComposeYml Path to docker-compose.yml to deploy
	 * @param clusterToken Token that identifies the cluster. It is given by the createCluster operation.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public String startDeployment(String sourceDockerComposeYml, String clusterToken) throws DockerError {
		// TODO: call the API to start the process
		return "token";
	}
	
	/**
	 * Asks the API if the deploy process has finished
	 * @return True if the process has finished or false.
	 * @throws DockerError Throws this when the builder returns any non successful response or if there is any error in the deployment.
	 */
	public boolean isDeployed(String token) throws DockerError{
		// TODO: retrieve the status, parse response.
		return true;
	}
	
	/**
	 * Retrieves detailed information about the deployment.
	 * @param token Operation token
	 * @return Detailed information about the deployment.
	 * @throws DockerError Throws this when the builder returns any non successful response or if there is any error in the deployment.
	 */
	public String getInfo(String token) throws DockerError{
		// TODO 
		return "Detailed information about the deployment.";
	}

	/**
	 * Ask Docker Crane to stop the deploy process and destroy the containers.
	 * @param token Operation token.
	 * @throws DockerError
	 */
	public void stopComposition(String token)  throws DockerError {
		// TODO
	}


}
