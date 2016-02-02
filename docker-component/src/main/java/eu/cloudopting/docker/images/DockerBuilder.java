package eu.cloudopting.docker.images;

import java.lang.reflect.InvocationTargetException;

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
	 * 
	 * @return API response
	 */
	public ResponseEntity<String> getListOfContexts() {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/contexts",
					HttpMethod.GET, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	/**
	 * Requests the creation of new context.
	 * 
	 * @param name
	 *            Name of the context
	 * @param pathToPuppetfile
	 *            Path to the puppetfile where the needed modules are listed
	 * @return API response
	 */
	public ResponseEntity<String> newContext(
			String pathToPuppetfile) {
		// Prepare files
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		/*
		if (name != "") {
			map.add("group", new String(name));
		}
		*/
		/// again no chenges without having had a call on these
		if (pathToPuppetfile != "") {
			map.add("puppetfile", new FileSystemResource(pathToPuppetfile));
		}

		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/contexts",
					HttpMethod.POST, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	/**
	 * Request to remove a context and the related data.
	 * 
	 * @param token
	 *            Token that identifies the context
	 * @return API response
	 */
	public ResponseEntity<String> removeContext(String token) {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/contexts/"
					+ token, HttpMethod.DELETE, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	/**
	 * Requests information about a context.
	 * 
	 * @param token
	 *            Token that identifies the context
	 * @return API response
	 */
	public ResponseEntity<String> getContextInfo(String token) {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/contexts/"
					+ token, HttpMethod.GET, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

		/**
	 * Requests information about a context.
	 * 
	 * @param token
	 *            Token that identifies the context
	 * @return API response
	 */
	public ResponseEntity<String> getContextDetail(String token) {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/contexts/"
					+ token + "/detail", HttpMethod.GET, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	/**
	 * Requests the creation of a new image base.
	 * 
	 * @param baseName
	 *            Desired name
	 * @param pathToDockerfile
	 *            Path where the base dockerfile is located
	 * @return API response
	 */
	public ResponseEntity<String> newBase(String baseName, String pathToDockerfile) {
		// Prepare files
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("dockerfile", new FileSystemResource(pathToDockerfile));
		map.add("imageName", new String(baseName));

		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/images/bases",
					HttpMethod.POST, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}


	/**
	 * Requests information about an image base.
	 * 
	 * @param baseName
	 *            Name that identifies the image base
	 * @return API response
	 */
	public ResponseEntity<String> getBaseInfo(String baseName) {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);

		ResponseEntity<String> responseEntity = null;
		try {
			System.out.println("base name:" + baseName);
			responseEntity = rest.exchange(endPoint + "/builder/images/bases/"
					+ baseName, HttpMethod.GET, requestEntity, String.class);
			System.out.println("responseEntity:" + responseEntity.toString());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
			System.out.println("error:" + e.getResponseBodyAsString());
		}

		return responseEntity;
	}

/*	/**
	 * Requests a list with the images under creation or created.
	 * 
	 * @return API response
	 
	public ResponseEntity<String> getListOfImages() {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/images/",
					HttpMethod.GET, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}
*/

	/**
	 * Requests the creation of a new image.
	 * 
	 * @param name
	 *            Desired name
	 * @param pathToDockerfile
	 *            Path where the base dockerfile is located
	 * @param pathToPuppetManifest
	 *            Path where the puppet manifest that defines the service in the
	 *            container are located
	 * @param contextToken
	 *            Token that identifies the context where the image will be
	 *            built.
	 * @return API response
	 */
	public ResponseEntity<String> newImage(String name, String pathToDockerfile, String pathToPuppetManifest, String contextToken) {
		
		// Prepare files
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		if (name != ""){
			map.add("imageName", new String(name));
		}
/// What this base represent??????????
		// you cannot alter the method signature without talking with the team
		/*
		if (baseName != ""){
			map.add("base", new String(baseName));
		}
*/
		if (pathToDockerfile != ""){
			map.add("dockerfile", new FileSystemResource(pathToDockerfile));
		}

		if (pathToPuppetManifest != ""){
			map.add("puppetmanifest", new FileSystemResource(pathToPuppetManifest));
		}

		if (contextToken != ""){
			map.add("contextReference", new String(contextToken));
		}

		// Request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/images",
					HttpMethod.POST, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	/**
	 * Requests to remove an image and the related data.
	 * 
	 * @param token
	 *            Token that identifies the image
	 * @return API response
	 */
	public ResponseEntity<String> removeImage(String token) {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = rest.exchange(endPoint + "/builder/images/"
					+ token, HttpMethod.DELETE, requestEntity, String.class);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	/**
	 * Requests information about an image.
	 * 
	 * @param token
	 *            Token that identifies the image
	 * @return API response
	 */
	public ResponseEntity<String> getImageInfo(String token) {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("",
				genericHeaders);

		ResponseEntity<String> responseEntity = null;
		try {
			System.out.println("token:" + token);
			responseEntity = rest.exchange(endPoint + "/builder/images/"
					+ token, HttpMethod.GET, requestEntity, String.class);
			System.out.println("responseEntity:" + responseEntity.toString());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
			System.out.println("error:" + e.getResponseBodyAsString());
		}

		return responseEntity;
	}

	/**
	 * Requests information about an image.
	 * 
	 * @param token
	 *            Token that identifies the image
	 * @return API response
	 */
	public ResponseEntity<String> getImageDetail(String token) {
		// Request
		HttpEntity<String> requestEntity = new HttpEntity<String>("", genericHeaders);

		ResponseEntity<String> responseEntity = null;
		try {
			System.out.println("token:" + token);
			responseEntity = rest.exchange(endPoint + "/builder/images/"
					+ token + "/detail", HttpMethod.GET, requestEntity, String.class);
			System.out.println("responseEntity:" + responseEntity.toString());
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				responseEntity = new ResponseEntity<String>(
						e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
			System.out.println("error:" + e.getResponseBodyAsString());
		}

		return responseEntity;
	}

}
