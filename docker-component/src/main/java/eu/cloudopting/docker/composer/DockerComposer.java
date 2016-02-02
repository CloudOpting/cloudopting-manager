package eu.cloudopting.docker.composer;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import eu.cloudopting.docker.DockerError;

/**
 *
 * Handles the composing and deploy process.
 *
 */
public class DockerComposer {
	
	private String endPoint;
	private HttpHeaders genericHeaders;
	private RestTemplate rest;
	
	public DockerComposer(String endPoint) {
		this.endPoint = endPoint;
		this.genericHeaders = new HttpHeaders();
		this.genericHeaders.add("Content-Type", "application/json");
		this.genericHeaders.add("Accept", "*/*");
		this.rest = new RestTemplate();
	}


	/**
	 * Asks the API to starts the deploy process.
	 * @param sourceDockerComposeYml Path to docker-compose.yml to deploy
	 * @param clusterToken Token that identifies the cluster. It is given by the createCluster operation.
	 * @throws DockerError Throws this when the builder returns any non successful response.
	 */
	public ResponseEntity<String> startDeployment(String sourceDockerComposeYml, String clusterToken) throws DockerError {
		// Prepare files
		
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("composefile", new FileSystemResource(sourceDockerComposeYml));
		map.add("clusterToken", new String(clusterToken));
		
		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
		
		ResponseEntity<String> responseEntity = null;
		try{
			responseEntity = rest.exchange(endPoint + "/composer", HttpMethod.POST, requestEntity, String.class);
		}catch(HttpClientErrorException e){
			if(e.getStatusCode()==HttpStatus.NOT_FOUND)
			responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}
		
		return responseEntity;
	}
	
	/**
	 * Asks the API if the deploy process has finished
	 * @return True if the process has finished or false.
	 * @throws DockerError Throws this when the builder returns any non successful response or if there is any error in the deployment.
	 */
	public boolean isDeployed(String token) throws DockerError{
		// TODO: retrieve the status, parse response.
	//	return true;
		throw new UnsupportedOperationException("Operation not supported for the moment.");
	}
	
	/**
	 * Retrieves detailed information about the deployment.
	 * @param token Operation token
	 * @return Detailed information about the deployment.
	 * @throws DockerError Throws this when the builder returns any non successful response or if there is any error in the deployment.
	 */
	public String getInfo(String token) throws DockerError{
		// TODO 
	//	return "Detailed information about the deployment.";
		throw new UnsupportedOperationException("Operation not supported for the moment.");
	}

	/**
	 * Ask Docker Crane to stop the deploy process and destroy the containers.
	 * @param token Operation token.
	 * @throws DockerError
	 */
	public void stopComposition(String token)  throws DockerError {
		// TODO
		throw new UnsupportedOperationException("Operation not supported for the moment.");
	}


}
