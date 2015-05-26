package eu.cloudopting.docker.images;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * Handles the creation of images.
 *
 */
public class DockerBuilder {
	
	private String endPoint;
	private HttpHeaders genericHeaders;
	private RestTemplate rest;
	
	public DockerBuilder(String endPoint) {
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
	public ResponseEntity<String> getListOfContexts(){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/contexts", HttpMethod.GET, requestEntity, String.class);
		return responseEntity;
	}
	
	/**
	 * Requests the creation of new context.
	 * @param name Name of the context
	 * @param pathToPuppetfile Path to the puppetfile where the needed modules are listed
	 * @return API response
	 */
	public ResponseEntity<String> newContext(String name, String pathToPuppetfile){
		// Prepare files
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("puppetfile", new ClassPathResource(pathToPuppetfile));
		map.add("contextName", new String(name));
		
		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/contexts", HttpMethod.POST, requestEntity, String.class);
		return responseEntity;
	}
	
	/**
	 * Request to remove a context and the related data.
	 * @param token Token that identifies the context
	 * @return API response
	 */
	public ResponseEntity<String> removeContext(String token){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/contexts/" + token, HttpMethod.DELETE, requestEntity, String.class);
		return responseEntity;
	}
	
	/**
	 * Requests information about a context.
	 * @param token Token that identifies the context
	 * @return API response
	 */
	public ResponseEntity<String> getContextInfo(String token){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/contexts/" + token, HttpMethod.GET, requestEntity, String.class);
		return responseEntity;
	}
	
	/**
	 * Requests a list with the images under creation or created.
	 * @return API response
	 */
	public ResponseEntity<String> getListOfImages(){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/images/", HttpMethod.GET, requestEntity, String.class);
		return responseEntity;
	}
	
	
	/**
	 * Requests the creation of a new image.
	 * @param name Desired name
	 * @param pathToDockerfile Path where the base dockerfile is located
	 * @param pathToPuppetManifest Path where the puppet manifest that defines the service in the container are located
	 * @param contextToken Token that identifies the context where the image will be built.
	 * @return API response
	 */
	public ResponseEntity<String> newImage(String name, String pathToDockerfile, String pathToPuppetManifest, String contextToken){
		// Prepare files
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("puppetmanifest", new ClassPathResource(pathToPuppetManifest));
		map.add("dockerfile", new ClassPathResource(pathToDockerfile));
		map.add("imageName", new String(name));
		map.add("contextReference", new String(name));
		
		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/images", HttpMethod.POST, requestEntity, String.class);
		return responseEntity;
	}
	
	
	/**
	 * Requests to remove an image and the related data.
	 * @param token Token that identifies the image
	 * @return API response
	 */
	public ResponseEntity<String> removeImage(String token){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/images/" + token, HttpMethod.DELETE, requestEntity, String.class);
		return responseEntity;
	}
	
	/**
	 * Requests information about an image.
	 * @param token Token that identifies the image
	 * @return API response
	 */
	public ResponseEntity<String> getImageInfo(String token){
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);
		ResponseEntity<String> responseEntity = rest.exchange(endPoint + "/builder/images/" + token, HttpMethod.GET, requestEntity, String.class);
		return responseEntity;
	}

}
