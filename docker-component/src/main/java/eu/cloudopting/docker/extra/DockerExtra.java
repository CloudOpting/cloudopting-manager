package eu.cloudopting.docker.extra;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
	 * Requests the list of contexts.
	 * @return API response
	 */
	public ResponseEntity<String> isCraneAlive(){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/extra/alive", HttpMethod.GET, requestEntity, String.class);
		return responseEntity;
	}
	
}
