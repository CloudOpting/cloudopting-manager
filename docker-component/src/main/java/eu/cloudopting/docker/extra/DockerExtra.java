package eu.cloudopting.docker.extra;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import eu.cloudopting.docker.DockerError;
import eu.cloudopting.docker.cluster.Machine;

/**
 *
 * Wrap some extra functionality.
 *
 */
public class DockerExtra {
	
	private String endPoint;
	private HttpHeaders genericHeaders;
	private RestTemplate rest;
	
	public DockerExtra(String endPoint) {
		this.endPoint = endPoint;
		this.genericHeaders = new HttpHeaders();
		this.genericHeaders.add("Content-Type", "application/json");
		this.genericHeaders.add("Accept", "*/*");
		this.rest = new RestTemplate();
	}
	
	/**
	 * Check if crane is alive.
	 * @return API response
	 */
	public ResponseEntity<String> isCraneAlive(){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/extra/alive", HttpMethod.GET, requestEntity, String.class);
		return responseEntity;
	}

	/**
	 * Check if Crane docker engine is alive.
	 * @return API response
	 */
	public ResponseEntity<String> checkCrane(){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/extra/check", HttpMethod.GET, requestEntity, String.class);
		return responseEntity;
	}
	
	/**
	 * get information about endpoint doker daemon
	 * @param machine machine with docker engine
	 * @return API response
	 * @throws DockerError Throws this when the API returns a non successful response.
	 */
	public ResponseEntity<String> dockerInfo(Machine machine) throws DockerError {
		// Prepare files
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("endpoint", "http://"+machine.getHostname()+":"+machine.getDockerPort());
		
		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/extra/dockerInfo",
					HttpMethod.POST, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}
		
		return responseEntity;
	}
}
