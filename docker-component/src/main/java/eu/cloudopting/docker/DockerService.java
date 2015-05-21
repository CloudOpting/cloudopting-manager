package eu.cloudopting.docker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.cloudopting.docker.restclient.CraneRestClient;
import eu.cloudopting.docker.images.DockerBuilder;
import eu.cloudopting.docker.cluster.DockerCluster;
import eu.cloudopting.docker.cluster.Machine;
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
	
	
	// Building

	/**
	 * Starts build process for an image.
	 * @param image Image name
	 * @param dockerFile Dockerfile path
	 * @param executionPath Puppet manifests path
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String buildDockerImage(String image, String dockerFile, String executionPath) throws DockerError{
		log.debug("in buildDockerImage and calling the API");
		log.debug("executing: docker build -t "+ image + " -f " + dockerFile + " " + executionPath);
		return this.builder.startBuild(image, dockerFile, executionPath);
	}
	
	/**
	 * Checks if the building is finished.
	 * @return true if build finished, false if not.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public boolean isBuilt(String token) throws DockerError{
		log.debug("in isBuilt and calling the API");
		return this.builder.isFinished(token);
	}
	
	/**
	 * Retrieves detailed information about the build process.
	 * @param token Operation token
	 * @return Build log
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public String getBuildInfo(String token) throws DockerError{
		log.debug("in getBuildInfo and calling the API");
		return this.builder.getInfo(token);
	}
	
	/**
	 * Tries to stop the build process and destroy the related data (images, temporal containers, etc)
	 * @param token Operation token
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public void stopBuild(String token) throws DockerError{
		log.debug("in stop and calling the API");
		this.builder.stop(token);
	}
	
	public String commitImage(String image){
		log.debug("Committing the image to the registry");
		return "1234";
	}
	
	public boolean isCommitted(String tocken){
		log.debug("check to see if the commit went ok");
		return true;
	}
	
	
	
	// Clustering
	
	/**
	 * Create a cluster with a unique machine and private key + passphrase credentials.
	 * @param hostname Machine hostname or IP address
	 * @param port SSH port
	 * @param privateKey Private key with SSH privileges 
	 * @param passphrase Passphrase to decrypt the privateKey. Null if passphrase not needed.
	 * @return Operation token that also identifies the cluster in the Docker Crane.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String createCluster(String hostname, int port, File privateKey, String passphrase) throws DockerError{
		log.debug("in createCluster (private key + passphrase credentials) and calling the API");
		return this.cluster.initCluster(new Machine(hostname, port, privateKey, passphrase));
	}
	
	/**
	 * Create a cluster with a unique machine and user + password credentials.
	 * @param hostname Machine hostname or IP address
	 * @param port SSH port
	 * @param user User
	 * @param password Password for user
	 * @return Operation token that also identifies the cluster in the Docker Crane.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String createCluster(String hostname, int port, String user, String password) throws DockerError{
		log.debug("in createCluster (user + pass credentials) and calling the API");
		return this.cluster.initCluster(new Machine(hostname, port, user, password));
	}
	
	/**
	 * Create a cluster with a list of machines.
	 * @param machines List of machines that will compose the cluster.
	 * @return Operation token that also identifies the cluster in the Docker Crane.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String createCluster(ArrayList<Machine> machines) throws DockerError{
		log.debug("in createCluster (from list of machines) and calling the API");
		return this.cluster.initCluster(machines);
	}
	
	/**
	 * Checks if the cluster is ready to deploy containers.
	 * @return true if it is ready, false if not.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public boolean isClusterReady(String token) throws DockerError{
		log.debug("in isClusterReady and calling the API");
		return this.cluster.isReady(token);
	}
	
	/**
	 * Retrieves detailed information about docker cluster.
	 * @param token Operation token
	 * @return Status information about the cluster in a human-readable format.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String getClusterInfo(String token) throws DockerError{
		log.debug("in getClusterInfo and calling the API");
		return this.cluster.getInfo(token);
	}
	
	/**
	 * Tries to stop the cluster (destroy containers, and unlink machines from master)
	 * @param token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void stopCluster(String token) throws DockerError{
		log.debug("in stopCluster and calling the API");
		this.cluster.stop(token);
	}
	
	
	// Composing
	
	/**
	 * Start the deployment of a docker composition
	 * @param machines List of machines that will compose the cluster.
	 * @return Operation token that also identifies the cluster in the Docker Crane.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	
	/**
	 * Starts the deployment of a docker composition.
	 * @param composerFilePath Path to the docker-compose.yml file
	 * @param clusterToken Token that identifies the cluster. It is given by the createCluster operation.
	 * @return OperationToken Token that identifies the deployment.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String deployComposition(String composerFilePath, String clusterToken) throws DockerError{
		log.debug("in deployComposition and calling the API");
		// checkpoint-----------
		// at the moment we need to do the following call:
//		"cd "+path+"/"+customer+"-"+service+" && docker-compose up --no-build -d"

		// need to have a description of what the closterTocken is and how is used that information 
		return this.composer.startDeployment(composerFilePath, clusterToken);
	}
	
	/**
	 * Checks if the composition has been deployed successfully.
	 * @return true if it is ready, false if not.
	 * @throws DockerError Throws this when the API returns a non successful response. If error in occur in the deployment this will be raised.
	 */
	public boolean isCompositionDeployed(String token) throws DockerError{
		log.debug("in isCompositionDeployed and calling the API");
		return this.composer.isDeployed(token);
	}
	
	/**
	 * Retrieves detailed information about deployment.
	 * @param token Operation token
	 * @return Status information about the deployment in a human-readable format.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String getDeploymentInfo(String token) throws DockerError{
		log.debug("in getDeploymentInfo and calling the API");
		return this.composer.getInfo(token);
	}
	
	/**
	 * Tries to stop the deployment and remove the containers.
	 * @param token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void stopComposition(String token) throws DockerError{
		log.debug("in stopComposition and calling the API");
		this.composer.stopComposition(token);
	}
	
}
