package eu.cloudopting.bpmn.tasks.deploy;

import java.io.InputStream;
import java.util.Properties;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import eu.cloudopting.docker.DockerService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployDockerSwarm implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDockerSwarm.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	DockerService dockerService;
	
	@Value("${server.ip}")
	private String hostip;
	
	@Value("${docker.port}")
	String dockerPort = "2377";

	private static int SSH_PORT = 22;
	private static String ROOT_USER = "root";


	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String ip = (String) execution.getVariable("ip");
		//TODO al momento è vuoto perchè non lo leggiamo da nulla valutare se leggerlo dinamicamente o da file di configurazione
		String privateKeyPath = (String) execution.getVariable("privateKeyPath");
		String passphrase = (String) execution.getVariable("passphrase");
		
		//join the new machine to the swarm
		String remote_command = "/usr/bin/docker run -d swarm join --advertise="+ip+":"+dockerPort+" consul://"+hostip+":8500";
		
		Properties config = new Properties(); 
		config.put("StrictHostKeyChecking", "no"); 	//without this it cannot connect because the host key verification fails
													//the right way would be to somehow get the server key and add it to the trusted keys
		                                            //jsch.setKnownHosts()    https://epaul.github.io/jsch-documentation/javadoc/com/jcraft/jsch/JSch.html#setKnownHosts-java.lang.String-
													// see also http://stackoverflow.com/a/32858953/28582
		JSch jsch=new JSch(); 
		jsch.addIdentity(privateKeyPath, passphrase);
		
		Session session = jsch.getSession(ROOT_USER, ip, SSH_PORT);
		session.setConfig(config);
		session.connect();
		
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(remote_command);
		
		channel.setInputStream(null);
		channel.setErrStream(System.err);
		
		InputStream in = channel.getInputStream();
		
		channel.connect();
		
		byte[] tmp = new byte[1024];
		
		int exitStatus = -1;
		while (true){
			while (in.available() > 0){
				int i = in.read(tmp, 0, 1024);
				if (i < 0) {
					break;
				}
				log.debug(new String(tmp, 0, i));
	        }
	        if (channel.isClosed()) {
	        	if (in.available() > 0) {
	        		continue; 
	        	}
	        	exitStatus = channel.getExitStatus();
	        	log.debug("exit status: " + exitStatus);
	        	break;
	        }
	        try{
	        	Thread.sleep(1000);
	        } catch (Exception ee) {
	        	log.debug("Exception: " + ee.getMessage());
	        }
	      }
	      channel.disconnect();
	      session.disconnect();
	    
	    log.debug("Command: [" + remote_command + "]");
	    if (exitStatus != 0) {
	    	log.debug("FAILED - exit status: " + exitStatus);
	    }
	    else {
	    	log.debug("Executed successfully");
	    }
		
		log.debug("in DeployDockerSwarm");
//		toscaService.getNodeType("");
		//dockerService.addMachine(ip, 2376); //SSL
		dockerService.addMachine(ip, Integer.parseInt(dockerPort)); //no SSL
		
	}

}


