package eu.cloudopting.docker.images;

import java.io.File;

import eu.cloudopting.docker.DockerError;
import eu.cloudopting.docker.restclient.CraneRestClient;



/**
 * 
 * Represents a Docker image in a build process. 
 *
 */
public class DockerImage {

	/**
	 * 
	 * DockerImage possible statuses.
	 *
	 */
	public enum Status {
	    NOT_STARTED, UNDER_CREATION, CREATED, FAILURE
	}
	
	private CraneRestClient craneHandler;
	private String name;
	private String buildOperationToken;
	private File sourceDockerfile;
	private String sourceDockerBaseImage;
	private File puppetManifest;
	private Status status;
	private String errorLog;
	
	/**
	 * New image from Dockerfile + puppet manifest.
	 * @param craneHandler Rest client handler.
	 * @param name Desired Name for the image.
	 * @param sourceDockerfile Base Dockerfile.
	 * @param puppetManifest Puppet recipe for the image
	 */
	public DockerImage(CraneRestClient craneHandler, String name, File sourceDockerfile, File puppetManifest) {
		this.craneHandler = craneHandler;
		this.name = name;
		this.sourceDockerfile = sourceDockerfile;
		this.puppetManifest = puppetManifest;
		this.status = this.status.NOT_STARTED;
	}
	
	/**
	 * New image from docker base image + puppet manifest.
	 * @param craneHandler Rest client handler.
	 * @param name Desired Name for the image.
	 * @param sourceDockerBaseImage Name of the base docker image 
	 * @param puppetManifest Puppet recipe for the image
	 */
	public DockerImage(CraneRestClient craneHandler, String name, String sourceDockerBaseImage, File puppetManifest) {
		this.craneHandler = craneHandler;
		this.name = name;
		this.sourceDockerBaseImage = sourceDockerBaseImage;
		this.puppetManifest = puppetManifest;
		this.status = this.status.NOT_STARTED;
		this.buildOperationToken = craneHandler.newBuildFromBaseImage(this.name, this.sourceDockerBaseImage, puppetManifest);
	}

	
	/**
	 * Launches the build request
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public void launchCreationRequest()  throws DockerError {
		this.buildOperationToken = craneHandler.newBuildFromDockerfile(this.name, this.sourceDockerfile, this.puppetManifest);
	}
	
	/**
	 * The name of the aim image.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The token that identifies the build operation in the Docker Builder in CloudOpting Crane. 
	 * @return the buildOperationToken
	 */
	public String getBuildOperationToken() {
		return buildOperationToken;
	}

	/**
	 * The source Dockerfile of the image.
	 * @return the sourceDockerfile
	 */
	public File getSourceDockerfile() {
		return sourceDockerfile;
	}

	/**
	 * The source docker image name
	 * @return the sourceDockerBaseimage
	 */
	public String getSourceDockerBaseImage() {
		return sourceDockerBaseImage;
	}

	/**
	 * The source puppet manifest to build the aim image.
	 * @return the puppetManifest
	 */
	public File getPuppetManifest() {
		return puppetManifest;
	}

	/**
	 * The status of the build process.
	 * @return the status
	 */
	public Status getStatus() throws DockerError{
		this.updateStatus();
		return status;
	}
	
	public boolean isFinished() throws DockerError{
		Status aux = this.getStatus();
		if(aux == this.status.CREATED || aux == this.status.FAILURE)
			return true;
		return false;
	}
	
	public boolean isCreated() throws DockerError{
		Status aux = this.getStatus();
		if(aux == this.status.CREATED)
			return true;
		return false;
	}

	/**
	 * 
	 * @return Error log if image is in FAILURE status and null in other case.
	 * @throws DockerError Throws this when the builder returns any non successful response. 
	 */
	public String getErrorLog() throws DockerError{
		this.updateStatus();
		if(this.status == this.status.FAILURE)
			return errorLog;
		
		return null;
	}
	
	/**
	 * Retrieve the build operation status from the Docker Builder in CloudOpting Crane.
	 * If error in the build procress, the errorLog will be filled. 
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	private void updateStatus() throws DockerError{
		//TODO
	}
	

	
	
	

}
