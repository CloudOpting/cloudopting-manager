package eu.cloudopting.docker.cluster;

import java.io.File;
import java.util.ArrayList;

import java.lang.UnsupportedOperationException;

import eu.cloudopting.docker.DockerError;

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

/**
 *
 * Handles the swarm cluster creation
 *
 */
public class DockerCluster {
	private String endPoint;
	private HttpHeaders genericHeaders;
	private RestTemplate rest;
	public DockerCluster(String endPoint) {
		this.endPoint = endPoint;
		this.genericHeaders = new HttpHeaders();
		this.genericHeaders.add("Content-Type", "application/json");
		this.genericHeaders.add("Accept", "*/*");
		this.rest = new RestTemplate();
	}

	/**
	 * Asks the API to initialize a cluster (or simple a docker host) in a machine.
	 * @param machine Machine in which the docker cluster will be initialized
	 * @return API response
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public ResponseEntity<String> initCluster(Machine machine) throws DockerError {
		// Prepare files
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("endpoint", "tcp://"+machine.getHostname()+":"+machine.getDockerPort());
		
		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/cluster/provisionedSingleMachine",
					HttpMethod.POST, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}
		
		return responseEntity;
	}

	/**
	 * Adds machine to a cluster that already exist.
	 * @param machine Machine to be added
	 * @param clusterToken Token that identifies the cluster where to add the machine
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void addToCluster(Machine machine, String clusterToken) throws DockerError {
		throw new UnsupportedOperationException("Invalid operation. Not implemented yet.");
	}

	/**
	 * Asks the API for detailed information about cluster.
	 * @param clusterToken Cluster token
	 * @return Info about the cluster.
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public ResponseEntity<String> getInfo(String clusterToken) throws DockerError{
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/cluster/"
					+ clusterToken, HttpMethod.GET, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	/**
	 * Stops the cluster and unlink the swarm agents
	 * @param token Operation token
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public void stop(String token) throws DockerError {
		// TODO
	}
}
