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
 * Wraps all the docker related functions (building, clustering and composing).
 * 
 * Typically the process will be similar to this (pseudocode):
 * 
 * dockerService.builder.addImage(image1, new File("/service1/apache/Dockerfile"), new File ("/service1/apache/Puppetscript.pp"));
 * dockerService.builder.addImage(image2, new File(/service1/mysql/Dockerfile), new File ("/service1/mysql/Puppetscript.pp"));
 * dockerService.builder.start();
 * while(!dockerService.builder.isFinished()){}
 * if(!dockerService.builder.isFinishedSuccefully()){
 * 	exit();
 * }
 * 
 * 
 * 
 * dockeService.cluster.addMachine("192.168.1.10", "21", new File ("/service1/vms/vm1.key", "passphrase");
 * dockeService.cluster.addMachine("192.168.1.11", "21", new File ("/service1/vms/vm2.key", "passphrase");
 * dockeService.cluster.addMachine("192.168.1.12", "21", new File ("/service1/vms/vm3.key", "passphrase");
 * dockerService.cluster.createMaster();
 * while(!dockerService.cluster.isMasterRunning()){}
 * dockerService.cluster.joinNodes();
 * while(!dockerServcide.cluster.areNodesConnectedToMaster()){}
 * 
 * 
 * 
 * dockerService.composer.start(new File ("/service1/docker-compose.yml"));
 * while(!dockerService.composer.isFinished()){}
 *
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
