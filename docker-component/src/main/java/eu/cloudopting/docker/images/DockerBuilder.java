package eu.cloudopting.docker.images;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import eu.cloudopting.docker.DockerError;
import eu.cloudopting.docker.restclient.CraneRestClient;

/**
 *
 * Handles the creation of images.
 *
 */
public class DockerBuilder {
	
	private CraneRestClient craneHandler;

	public DockerBuilder(CraneRestClient craneHandler) {
		this.craneHandler = craneHandler;
	}
	
	/**
	 * Asks the API to start a building process.
	 * @param name Desired Name for the image.
	 * @param sourceDockerfile Base Dockerfile path.
	 * @param executionPath Path where the puppet stuff is.
	 * @return Operation token.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public String startBuild(String name, String sourceDockerfile, String executionPath){
		return "token";
	}
	
	/**
	 * Checks if the building process has finished.
	 * @return True if the image has been built. False in other case.
	 * @throws DockerError Throws this when the builder returns any non successful response (also when the building process finished but with errors).
	 */
	public boolean isFinished(String token) throws DockerError{
		return true;
	}
	
	/**
	 * Asks the API for information about the building process
	 * @param token Operation token
	 * @return Build log
	 * @throws DockerError Throws this when the API returns any non successful response (i.e. can not get log, or something like this).
	 */
	public String getInfo(String token) throws DockerError{
		return "This is a log with information about the building process.";
	}
	

	/**
	 * Asks the API to stop the building process and destroy the related temporal data.
	 * @param token Operation token
	 * @throws DockerError Throws this when the API returns any non successful response.
	 */
	public void stop(String token) throws DockerError {
	}

}
