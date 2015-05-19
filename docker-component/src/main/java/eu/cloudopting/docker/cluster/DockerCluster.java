package eu.cloudopting.docker.cluster;

import java.io.File;
import java.util.ArrayList;

import eu.cloudopting.docker.DockerError;
import eu.cloudopting.docker.restclient.CraneRestClient;

/**
 *
 * Handles the swarm cluster creation
 *
 */
public class DockerCluster {

	private ArrayList<Machine> machineList;
	private ArrayList<SwarmNode> nodeList;
	private SwarmMaster master;
	private CraneRestClient craneHandler;

	public DockerCluster(CraneRestClient craneHandler) {
		this.craneHandler = craneHandler;
	}


	/**
	 * Adds a machine to the list.
	 * @param hostname	Machine hostname or IP
	 * @param port	Machine ssh port
	 * @param privateKey	File containing the private key for ssh access
	 * @param passphrase	Passphrase to decrypt the private key
	 */
	public void addMachine(String hostname, int port, File privateKey, String passphrase) {
		machineList.add(new Machine(this.craneHandler, hostname, port, privateKey, passphrase));
	}

	/**
	 * Creates a swarm master in the first machine provided.
	 * @throws DockerError
	 */
	public void createMaster() throws DockerError {
		this.master = new SwarmMaster(this.craneHandler, this.machineList.iterator().next());
		this.master.install();
	}

	/**
	 * Checks if the swarm master is running.
	 * @return true if yes, false if not.
	 * @throws DockerError if error
	 */
	public boolean isMasterRunning() throws DockerError {
		// TODO: check in is master running
		return true;
	}

	/**
	 * Ask the swarm agent in the machines to join the swarm cluster.
	 * @throws DockerError
	 */
	public void joinNodes() throws DockerError {
		// TODO
	}

	/**
	 * Checks if the swarm agents have been connected to the master.
	 * @return true if yes, false if not.
	 * @throws DockerError if error
	 */
	public boolean areNodesConnectedToMaster() throws DockerError {
		// TODO: check in is master running
		return true;
	}

	/**
	 * Stops the cluster and unlink the swarm agents
	 */
	public void stop(String token) throws DockerError {
		// TODO
	}

}
