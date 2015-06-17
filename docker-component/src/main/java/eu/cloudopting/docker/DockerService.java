package eu.cloudopting.docker;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import eu.cloudopting.docker.images.DockerBuilder;
import eu.cloudopting.docker.cluster.DockerCluster;
import eu.cloudopting.docker.cluster.Machine;
import eu.cloudopting.docker.composer.DockerComposer;
import eu.cloudopting.docker.extra.DockerExtra;

/**
 *
 * Wraps all the docker related functions (building, clustering and composing).
 *
 */
@Service
public class DockerService {
	private final Logger log = LoggerFactory.getLogger(DockerService.class);

	private DockerExtra extra;
	private DockerBuilder builder;
	private DockerCluster cluster;
	private DockerComposer composer;
	
	private BasicJsonParser parser;

	public DockerService (){
		// @TODO this has to be configured from properties and not hardcoded
		this("http://localhost:8888");	// By default the cloudopting-crane api end-point will be port 8888 on localhost.
	}

	public DockerService(String endpoint){
		this.extra = new DockerExtra(endpoint);
		this.builder = new DockerBuilder(endpoint);
		this.cluster = new DockerCluster(endpoint);
		this.composer = new DockerComposer(endpoint);
	
		this.parser = new BasicJsonParser();
	}
	
	// Extra
	
	/**
	 * Check if Crane is up and answering the API.
	 * @return true if yes, false if no.
	 */
	public boolean isCraneAlive(){
		ResponseEntity<String> response = extra.isCraneAlive();
		if(!response.getStatusCode().is2xxSuccessful())
			return false;
		return true;
	}

	
	// Building


	/**
	 * Create a new context
	 * @param name Desired name to the context (it helps in debug, but it does not have a function)
	 * @param pathToPuppetfile Path to the source puppetfile where the needed modules are listed
	 * @return Token that references the context
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String newContext(String name, String pathToPuppetfile) throws DockerError{
		ResponseEntity<String> response = builder.newContext(name, pathToPuppetfile);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		return map.get("token").toString();
	}
	

	/**
	 * Create a new context (with no name)
	 * @param pathToPuppetfile Path to the source puppetfile where the needed modules are listed
	 * @return Token that references the context
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String newContext(String pathToPuppetfile) throws DockerError{
		return newContext("", pathToPuppetfile);
	}
	
	
	/**
	 * Checks if a context is ready to start to build images.
	 * @param token Token that identifies the context
	 * @return true if yes, false if no
	 * @throws DockerError If API returns error or if errors while building the context.
	 */
	public boolean isContextReady(String token)  throws DockerError{
		ResponseEntity<String> response = builder.getContextInfo(token);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		String aux = map.get("status").toString();
		if(aux.equals("finished"))
			return true;
		else if(aux.equals("error"))
			throw new DockerError(map.get("description").toString() + "\n" + map.get("log").toString());
		else
			return false;
	}
	
	/**
	 * Retrieves information about the context creation.
	 * @param token Token that identifies the context
	 * @return Response from API in JSON format.
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String getContextInfo(String token) throws DockerError{
		ResponseEntity<String> response = builder.getContextInfo(token);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		return response.getBody();
	}
	
	
	/**
	 * Sends the order to remove a context and the related data.
	 * @param token Token that identifies the context
	 * @throws DockerError If API returns error when starting the process.
	 */
	public void removeContext(String token) throws DockerError {
		ResponseEntity<String> response = builder.removeContext(token);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
	}
	
	
	/**
	 * Starts build process for an image.
	 * @param image Image name
	 * @param dockerFile Dockerfile path
	 * @param executionPath Puppet manifests path
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String buildDockerImage(String image, String dockerFilePath, String puppetManifestPath, String contextReference) throws DockerError{
		log.debug("in buildDockerImage and calling the API");
		log.debug("executing: docker build -t "+ image + " -f " + dockerFilePath );
		ResponseEntity<String> response = builder.newImage(image, dockerFilePath, puppetManifestPath, contextReference);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		String aux = map.get("token").toString(); 
		return aux;
	}
	
	/**
	 * Checks if the build process for an image is finished.
	 * @return true if build finished, false if not.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public boolean isBuilt(String token) throws DockerError{
		log.debug("in isBuilt and calling the API");
		
		ResponseEntity<String> response = builder.getImageInfo(token);
		Map<String, Object> map = parser.parseMap(response.getBody());
		String status = map.get("status").toString();
		if(!response.getStatusCode().is2xxSuccessful() || status.equals("error"))
			throw new DockerError(map.get("description").toString());
		
		if(status.equals("finished"))
			return true;
		else
			return false;
	}
	
	/**
	 * Retrieves detailed information about the build process of an image.
	 * @param token Operation token
	 * @return Response from API in JSON format.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public String getBuildInfo(String token) throws DockerError{
		log.debug("in getBuildInfo and calling the API");
		
		ResponseEntity<String> response = builder.getImageInfo(token);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		return response.getBody();
	}
	
	/**
	 * Tries to stop the build process and destroy the related data (images, temporal containers, etc)
	 * @param token Operation token
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public void stopBuild(String token) throws DockerError{
		log.debug("in stop and calling the API");
		ResponseEntity<String> response = builder.removeImage(token);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
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
		
		ResponseEntity<String> response = composer.startDeployment(composerFilePath, clusterToken);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		return map.get("token").toString();
	}
	
	public String deployComposition(String composerFilePath) throws DockerError{
		return deployComposition(composerFilePath, null);
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
