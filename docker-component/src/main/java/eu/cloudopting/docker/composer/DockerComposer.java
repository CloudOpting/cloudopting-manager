package eu.cloudopting.docker.composer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import eu.cloudopting.docker.images.DockerImage;
import eu.cloudopting.docker.restclient.CraneRestClient;
import eu.cloudopting.docker.DockerError;

/**
 *
 * Handles the composing and deploy process.
 *
 */
public class DockerComposer {
	
	/**
	 * 
	 * DockerComposer possible statuses.
	 *
	 */
	public enum Status {
	    NOT_STARTED, STARTED, FINISHED, FAILURE
	}

	private CraneRestClient craneHandler;
	private File sourceDockerComposeYml;
	private String deployOperationToken;
	private Status status;
	
	public DockerComposer(CraneRestClient craneHandler) {
		this.craneHandler = craneHandler;
		this.status = this.status.NOT_STARTED;
	}


	/**
	 * Starts the deploy process.
	 * @param sourceDockerComposeYml docker-compose.yml to deploy
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public void start(File sourceDockerComposeYml) throws DockerError {
		this.sourceDockerComposeYml = sourceDockerComposeYml;
		// TODO: call the API to start the process
		this.deployOperationToken = craneHandler.newComposition(this.sourceDockerComposeYml);
	}
	
	/**
	 * Checks if the deploy process has finished
	 * @return True if the process has finished (doesn't care the result) or false.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public boolean isFinished() throws DockerError{
		// TODO: retrieve the status, parse response.
		this.status=this.status.FINISHED;
		return true;
	}
	
	/**
	 * Checks if the deploy process has finished
	 * @return True if the process has finished properly or false if not.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public boolean isFinishedSuccessfully() throws DockerError{
		// TODO: retrieve the status, parse response.
		this.status=this.status.FINISHED;
		return true;
	}


	/**
	 * Ask Docker Crane to stop the deploy process and destroy the containers.
	 * @param token
	 * @throws DockerError
	 */
	public void stop()  throws DockerError {
		// TODO
	}

}
