package eu.cloudopting.docker;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.cloudopting.docker.restclient.CraneRestClient;
import eu.cloudopting.docker.images.DockerBuilder;
import eu.cloudopting.docker.cluster.DockerCluster;
import eu.cloudopting.docker.composer.DockerComposer;

/**
 *
 * Wraps all the docker related functions (building, clustering and composing).
 *
 * Typically the process will be similar to this (pseudocode):
 *
 * dockerService.builder.addImage(image1, new File("/service1/apache/Dockerfile"), new File ("/service1/apache/Puppetscript.pp"));
 * dockerService.builder.addImage(image2, new File(/service1/mysql/Dockerfile), new File ("/service1/mysql/Puppetscript.pp"));
 * dockerService.builder.start();
 * while(!dockerService.builder.isFinished()){}
 * if(!dockerService.builder.isFinishedSuccefully()){
 * 	exit();
 * }
 *
 *
 *
 * dockeService.cluster.addMachine("192.168.1.10", "21", new File ("/service1/vms/vm1.key", "passphrase");
 * dockeService.cluster.addMachine("192.168.1.11", "21", new File ("/service1/vms/vm2.key", "passphrase");
 * dockeService.cluster.addMachine("192.168.1.12", "21", new File ("/service1/vms/vm3.key", "passphrase");
 * dockerService.cluster.createMaster();
 * while(!dockerService.cluster.isMasterRunning()){}
 * dockerService.cluster.joinNodes();
 * while(!dockerServcide.cluster.areNodesConnectedToMaster()){}
 *
 *
 *
 * dockerService.composer.start(new File ("/service1/docker-compose.yml"));
 * while(!dockerService.composer.isFinished()){}
 *
 *
 */
@Service
public class DockerService {
	private final Logger log = LoggerFactory.getLogger(DockerService.class);

	private CraneRestClient restClient;
	private DockerBuilder builder;
	private DockerCluster cluster;
	private DockerComposer composer;

	public DockerService (){
		// @TODO this has to be configured from properties and not hardcoded
		this("http://localhost:8888");	// By default the cloudopting-crane api end-point will be port 8888 on localhost.
	}

	public DockerService(String uri){
		this.restClient = new CraneRestClient(uri);
		this.builder = new DockerBuilder(restClient);
		this.cluster = new DockerCluster(restClient);
		this.composer = new DockerComposer(restClient);
	}

	/**
	 * Starts build process for an image.
	 * @param image Image name
	 * @param dockerFile Dockerfile path
	 * @param executionPath Puppet manifests path
	 * @throws DockerError if API returns error when starting the process.
	 */
	public String buildDockerImage(String image, String dockerFile, String executionPath) throws DockerError{
		String retToken = "1234";
		// THIS IS WRONG you do not get the file the file is already in the file system you get the correct path to it
		this.builder.addImage(image, new File (dockerFile), new File(executionPath));
		this.builder.start();
		log.debug("in buildDockerImage and calling the API");
		log.debug("executing: docker build -t "+ image + " -f " + dockerFile + " " + executionPath);
		return retToken;
	}
	
	/**
	 * Check if the build is finished.
	 * @return true if build finished, false if not.
	 * @throws DockerError if build error
	 */
	public boolean isBuilt() throws DockerError{
		return this.builder.isFinished();
	}
	
	// TODO: add methods for all the operations (cluster and composer)
	
	
}
