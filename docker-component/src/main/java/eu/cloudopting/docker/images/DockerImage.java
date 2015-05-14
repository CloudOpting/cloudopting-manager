package eu.cloudopting.docker.images;

import java.io.File;

import eu.cloudopting.docker.DockerError;



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
	
	private String name;
	private String buildOperationToken;
	private File sourceDockerfile;
	private String sourceDockerBaseimage;
	private File puppetManifest;
	private Status status;
	
	public DockerImage(String name, File sourceDockerfile, File puppetManifest) throws DockerError {
		this.name = name;
		this.sourceDockerfile = sourceDockerfile;
		this.puppetManifest = puppetManifest;
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
	public String getSourceDockerBaseimage() {
		return sourceDockerBaseimage;
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
	public Status getStatus() {
		return status;
	}
	
	

}
