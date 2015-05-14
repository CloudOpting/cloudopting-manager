package eu.cloudopting.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.cloudopting.docker.restclient.CraneRestClient;
import eu.cloudopting.docker.images.DockerBuilder;
import eu.cloudopting.docker.cluster.DockerCluster;
import eu.cloudopting.docker.composer.DockerComposer;

/**
 *
 * TODO: javadoc
 *
 */
@Service
public class DockerService {
	private final Logger log = LoggerFactory.getLogger(DockerService.class);

	public CraneRestClient restClient;
	public DockerBuilder builder;
	public DockerCluster cluster;
	public DockerComposer composer;

	public DockerService (){
		this("http://localhost:8888");	// By default the cloudopting-crane api end-point will be port 8888 on localhost.
	}

	public DockerService(String uri){
		this.restClient = new CraneRestClient(uri);
		this.builder = new DockerBuilder(restClient);
		this.cluster = new DockerCluster(restClient);
		this.composer = new DockerComposer(restClient);
	}

}
