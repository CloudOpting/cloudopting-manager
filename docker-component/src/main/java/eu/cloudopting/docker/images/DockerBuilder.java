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
	
	private ArrayList<DockerImage> imageList;
	private CraneRestClient clientHandler;

	public DockerBuilder(CraneRestClient clientHandler) {
		this.clientHandler = clientHandler;
	}
	
	/**
	 * Adds a image (base from Dockerfile) to be built to the list.
	 * @param name Desired Name for the image.
	 * @param sourceDockerfile Base Dockerfile.
	 * @param puppetManifest Puppet recipe for the image.
	 */
	public void addImage(String name, File sourceDockerfile, File puppetManifest){
		imageList.add(new DockerImage(this.clientHandler, name, sourceDockerfile, puppetManifest));
	}
	
	/**
	 * Adds a image (base from image name) to be built to the list.
	 * @param name Desired Name for the image.
	 * @param sourceDockerBaseImage Name of the base docker image 
	 * @param puppetManifest Puppet recipe for the image.
	 */
	public void addImage(String name, String sourceDockerBaseImage, File puppetManifest){
		imageList.add(new DockerImage(this.clientHandler, name, sourceDockerBaseImage, puppetManifest));
	}


	/**
	 * Start the build process for the images in the list.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public void start() throws DockerError {
		Iterator<DockerImage> iterator = imageList.iterator();
		while (iterator.hasNext()) {
			iterator.next().launchCreationRequest();
		}
	}
	
	/**
	 * Checks if the build process has finished for all images.
	 * @return True if all the images are built or an an error occurred. False in other case.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public boolean isFinished() throws DockerError{
		Iterator<DockerImage> iterator = imageList.iterator();
		while (iterator.hasNext()) {
			if(!iterator.next().isFinished())
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if the build process has finished and the images has been created correctly.
	 * @return True if all the images are built correctly. False in other case.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public boolean isFinishedSuccessfully() throws DockerError{
		Iterator<DockerImage> iterator = imageList.iterator();
		while (iterator.hasNext()) {
			if(!iterator.next().isCreated())
				return false;
		}
		return true;
	}


	/**
	 * Ask Docker Crane to stop the build process of the images in the list and destroy them.
	 * @param token
	 * @throws DockerError
	 */
	public void stop()  throws DockerError {
		// TODO
	}

}
