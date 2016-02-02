package eu.cloudopting.docker;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import java.lang.UnsupportedOperationException;

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

	/**
	 * Check if Crane docker engine is alive.
	 * @return true if yes, false if no.
	 */
	public boolean checkCraneEngine(){
		ResponseEntity<String> response = extra.checkCrane();
		if(!response.getStatusCode().is2xxSuccessful())
			return false;
		return true;
	}
	/**
	 * get information about endpoint doker daemon
	 * @param machine machine with docker engine
	 * @return API body response
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String dockerInfo(String hostname, int dockerPort) throws DockerError{
		log.debug("in dockerInfo with hostname:port'"+hostname+":"+dockerPort+"' and calling the API");
		ResponseEntity<String> response = extra.dockerInfo(new Machine(hostname, dockerPort));
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		return response.getBody();
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
			throw new DockerError(map.get("description").toString());
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
	 * Retrieves information about the context creation.
	 * @param token Token that identifies the context
	 * @return Response from API in JSON format.
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String contextDetail(String token) throws DockerError{
		ResponseEntity<String> response = builder.getContextDetail(token);
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
	 * Starts build process for an image base.
	 * @param baseName base name
	 * @param dockerFilePath Dockerfile path
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String buildBase(String baseName, String dockerFilePath) throws DockerError{
		log.debug("in buildBase and calling the API");
		log.debug("executing: docker build -t "+ baseName + " -f " + dockerFilePath );
		ResponseEntity<String> response = builder.newBase(baseName, dockerFilePath);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		return response.getBody();
	}

	/**
	 * Checks if the build process for an image is finished.
	 * @param baseName Name that identifies the image base
	 * @return true if build finished, false if not.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public boolean isBuiltBase(String baseName) throws DockerError{
		log.debug("in isBuiltBase and calling the API");
		
		ResponseEntity<String> response = builder.getBaseInfo(baseName);
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
	 * Retrieves detailed information about the build process of an image base.
	 * @param baseName Name that identifies the image base
	 * @return Response from API in JSON format.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public String getBuildBaseInfo(String baseName) throws DockerError{
		log.debug("in getBuildInfo and calling the API");
		
		ResponseEntity<String> response = builder.getBaseInfo(baseName);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		return response.getBody();
	}
	
	/**
	 * Starts build process for an image.
	 * @param image Image name
	 * @param base Base name
	 * @param dockerFile Dockerfile path
	 * @param puppetManifestPath Puppet manifests path
	 * @param contextReference ContextToken
	 * @throws DockerError If API returns error when starting the process.
	 */
	public String buildDockerImage(String image, String base, String dockerFilePath, String puppetManifestPath, String contextReference) throws DockerError{
		log.debug("in buildDockerImage and calling the API");
	//	log.debug("executing: docker build -t "+ image + " -f " + dockerFilePath );
		ResponseEntity<String> response = builder.newImage(image, base, dockerFilePath, puppetManifestPath, contextReference);
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
	 * Retrieves detailed information about the build process of an image.
	 * @param token Operation token
	 * @return Response from API in JSON format.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public String imageDetail(String token) throws DockerError{
		log.debug("in getBuildInfo and calling the API");
		
		ResponseEntity<String> response = builder.getImageDetail(token);
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
		throw new UnsupportedOperationException("Invalid operation. Commiting is now done in the same step that building.");
	}
	
	public boolean isCommitted(String token){
		throw new UnsupportedOperationException("Invalid operation. Commiting is now done in the same step that building.");
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
		throw new UnsupportedOperationException("Cluster creation from SSH credentials is not supported for the moment. Use 'addMachine'.");
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
		throw new UnsupportedOperationException("Cluster creation from SSH credentials is not supported for the moment. Use 'addMachine'.");
	}
	
	/**
	 * Create a cluster with a list of machines.
	 * @param machines List of machines that will compose the cluster.
	 * @return Operation token that also identifies the cluster in the Docker Crane.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String createCluster(ArrayList<Machine> machines) throws DockerError{
		throw new UnsupportedOperationException("Cluster creation from list of machines is not supported for the moment. Use 'addMachine'.");
	}

	
	/**
	 * Start cluster with machine.
	 * @return Cluster token.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String addMachine(String hostname, int dockerPort) throws DockerError{
		log.debug("in addMachine with hostname:port'"+hostname+":"+dockerPort+"' and calling the API");
		
		
		ResponseEntity<String> response = cluster.initCluster(new Machine(hostname, dockerPort));
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		String aux = map.get("token").toString(); 
		
		return aux;
	}
	
	
	/**
	 * Add Machine to Cluster.
	 * @return Cluster token.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void addMachine(String hostname, int dockerPort, String clusterToken) throws DockerError{
		log.debug("in addMachine with hostname:port'"+hostname+":"+dockerPort+"' and clusterToken '"+clusterToken+"' and calling the API");
		//cluster.addToCluster(new Machine(hostname, dockerPort), clusterToken);
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	
	
	/**
	 * Checks if the cluster is ready to deploy containers and has all his nodes joined.
	 * @return true if it is ready, false if not.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public boolean isClusterReady(String clusterToken) throws DockerError{
		log.debug("in isClusterReady and calling the API");
		
		ResponseEntity<String> response = cluster.getInfo(clusterToken);
		Map<String, Object> map = parser.parseMap(response.getBody());
		String status = map.get("status").toString();
		if(!response.getStatusCode().is2xxSuccessful() || status.equals("error"))
			throw new DockerError(map.get("description").toString());
		
		if(status.equals("ready"))
			return true;
		else
			return false;
	}
	
	/**
	 * Retrieves detailed information about docker cluster.
	 * @param token Operation token
	 * @return Status information about the cluster in a human-readable format.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String getClusterInfo(String clusterToken) throws DockerError{
		log.debug("in getClusterInfo and calling the API");
		
		ResponseEntity<String> response = cluster.getInfo(clusterToken);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		return response.getBody();
	}
	/**
	 * Retrieves detailed information about docker cluster.
	 * @param token Operation token
	 * @return Status information about the cluster in a human-readable format.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String clusterDetail(String clusterToken) throws DockerError{
		log.debug("in getClusterInfo and calling the API");
		
		ResponseEntity<String> response = cluster.getClusterDetail(clusterToken);
		Map<String, Object> map = parser.parseMap(response.getBody());
		if(!response.getStatusCode().is2xxSuccessful())
			throw new DockerError(map.get("description").toString());
		
		return response.getBody();
	}
	/**
	 * Tries to stop the cluster (destroy containers, and unlink machines from master)
	 * @param token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void stopCluster(String token) throws DockerError{
		throw new UnsupportedOperationException("Operation not supported for the moment.");
	}
	
	// Composing
	
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
	
	/**
	 * Checks if the composition has been deployed successfully.
	 * @return true if it is ready, false if not.
	 * @throws DockerError Throws this when the API returns a non successful response. If error in occur in the deployment this will be raised.
	 */
	public boolean isCompositionDeployed(String token) throws DockerError{
	//	log.debug("in isCompositionDeployed and calling the API");
	//	return this.composer.isDeployed(token);
		throw new UnsupportedOperationException("Operation not supported for the moment.");
	}
	
	/**
	 * Retrieves detailed information about deployment.
	 * @param token Operation token
	 * @return Status information about the deployment in a human-readable format.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public String getDeploymentInfo(String token) throws DockerError{
	//	log.debug("in getDeploymentInfo and calling the API");
	//	return this.composer.getInfo(token);
		throw new UnsupportedOperationException("Operation not supported for the moment.");
	}
	
	/**
	 * Tries to stop the deployment and remove the containers.
	 * @param token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void stopComposition(String token) throws DockerError{
	//	log.debug("in stopComposition and calling the API");
	//	this.composer.stopComposition(token);
		throw new UnsupportedOperationException("Operation not supported for the moment.");
	}
	
}
