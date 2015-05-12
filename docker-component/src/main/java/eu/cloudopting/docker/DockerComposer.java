package eu.cloudopting.docker;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * TODO: javadoc
 *
 */
@Service
public class DockerComposer {
	private final Logger log = LoggerFactory.getLogger(DockerComposer.class);

	

	public DockerComposer(CraneRestClient restClient) {
		// TODO Auto-generated constructor stub
	}

	/* Methods */
		
	/**
	 * TODO: start the deploy of a composition of containers
	 */
	public String start(String pathToComposeYml) throws DockerError {
		// TODO
		String token = null;
		return token;
	}

	/**
	 * TODO: retrieve info about the deploy process status
	 */
	public String getInfo(String token)  throws DockerError {
		// TODO
		String result = null;
		return result;
	}
	
	/**
	 * TODO: destroy stop the deploy process
	 */
	public void stop(String token)  throws DockerError {
		// TODO
	}
}



