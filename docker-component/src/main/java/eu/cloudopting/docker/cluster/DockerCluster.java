package eu.cloudopting.docker.cluster;

import java.io.File;
import java.util.ArrayList;

import eu.cloudopting.docker.DockerError;

/**
 *
 * Handles the swarm cluster creation
 *
 */
public class DockerCluster {
	private String endPoint;
	public DockerCluster(String endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * Asks the API to initialize a cluster (or simple a docker host) in a machine.
	 * @param machine Machine in which the docker cluster will be initialized
	 * @return token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String initCluster(Machine machine) throws DockerError {
		// TODO
		return "token";
	}

	/**
	 * Asks the API to initialize a cluster with several machines.
	 * @param machines Machine in which the docker cluster will be initialized
	 * @return token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String initCluster(ArrayList<Machine> machines) throws DockerError {
		// TODO
		return "token";
	}

	/**
	 * Asks the API if the cluster is ready to deploy containers.
	 * @param token Operation token
	 * @return true if it is ready, false if not.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public boolean isReady(String token) throws DockerError{
		// TODO
		return true;
	}

	/**
	 * Asks the API for detailed information about cluster.
	 * @param token Operation token
	 * @return Info about the cluster.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String getInfo(String token) throws DockerError {
		// TODO
		return "This is info about the cluster.";
	}

	/**
	 * Stops the cluster and unlink the swarm agents
	 * @param token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void stop(String token) throws DockerError {
		// TODO
	}
}
