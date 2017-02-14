package eu.cloudopting.bpmn.tasks.deploy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;

import eu.cloudopting.service.MailService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeploySetup implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeploySetup.class);
	@Autowired
	ToscaService toscaService;
	
	@Autowired
	private Environment environment;
	
	@Value("${cloud.doDeploy}")
	private boolean doDeploy;
	private String publicKey;
	private String privateKey;
	private String privateKeyPath;
	private String publicKeyPath;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.info("in DeploySetup");
		String customizationId = (String) execution.getVariable("customizationId");
		String organizationName = (String) execution.getVariable("organizationName");

		String service = toscaService.getServiceName(customizationId);
		log.debug("service: "+service);
		String coRoot = new String("/cloudOptingData");
		String serviceHome = new String(coRoot+"/"+organizationName+"-"+service);
		log.debug("serviceHome: "+serviceHome);
		
		boolean success = false;
		File directory = new File(serviceHome);
		if (directory.exists()) {
			System.out.println("Directory already exists ...");
			FileUtils.cleanDirectory(directory);

		} else {
			System.out.println("Directory not exists, creating now");

			success = directory.mkdir();
			if (success) {
				System.out.printf("Successfully created new directory : %s%n",
						serviceHome);
			} else {
				System.out.printf("Failed to create new directory: %s%n", serviceHome);
			}
		}
		
		toscaService.generatePuppetfile(customizationId, serviceHome);
		
		ArrayList<String> dockerPortsList = toscaService.getHostPorts(customizationId);
		dockerPortsList.add("Port1");
		
		ArrayList<String> dockerNodesList = toscaService.getArrNodesByType(customizationId, "DockerContainer");
		ArrayList<String> dockerDataVolumeNodesList = toscaService.getArrNodesByType(customizationId, "DockerDataVolumeContainer");
		
		
		String RSApassphrase = "foo"; //TODO we should find a way to get this password from the user -  from the TOSCA file?
		createRSAKeys(RSApassphrase, service, organizationName);
		

		execution.setVariable("publickey", publicKey);
		execution.setVariable("privatekey", privateKey);
		execution.setVariable("privateKeyPath", privateKeyPath);
		execution.setVariable("publicKeyPath", publicKeyPath);
		execution.setVariable("passphrase", RSApassphrase);
		
		log.debug("dockerNodesList");
		log.debug(dockerNodesList.toString());
		log.debug("dockerPortsList");
		log.debug(dockerPortsList.toString());
		execution.setVariable("dockerNodesList", dockerNodesList);
		execution.setVariable("dockerDataVolumeNodesList", dockerDataVolumeNodesList);
		execution.setVariable("vmPortsList", dockerPortsList);
		
		log.debug("organizationName:"+organizationName);
		log.debug("service:"+service);
		log.debug("coRoot:"+coRoot);
		log.debug("serviceHome:"+serviceHome);
		
		String[] activeProfile = this.environment.getActiveProfiles();
		execution.setVariable("isDevelopment", false);
		if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
				   env -> (env.equalsIgnoreCase("dev")) )) 
				{		
			execution.setVariable("isDevelopment", true);
				}
		// setting the variables for the rest of the tasks
		execution.setVariable("customizationName", organizationName+"-"+service);
		execution.setVariable("coRoot", coRoot);
		execution.setVariable("service", service);
		execution.setVariable("serviceHome", serviceHome);

		
	}
	
	private void createRSAKeys(String passphrase, String servicename, String organizationName) throws JSchException, FileNotFoundException, IOException {
		// http://www.jcraft.com/jsch/examples/KeyGen.java.html
		JSch jsch = new JSch();

		KeyPair kpair;

		kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
		
		String privateKeyName = String.format("private-%s-%s.key", servicename, organizationName);
		String publicKeyName = String.format("public-%s-%s.key", servicename, organizationName);
		
		//private key file name should less generic - something like {servicename}-{userid}.key?
		privateKeyPath = "/cloudOptingData/" + privateKeyName;
		publicKeyPath = "/cloudOptingData/" + publicKeyName;
		kpair.writePrivateKey(privateKeyPath, passphrase.getBytes());
		kpair.writePublicKey(publicKeyPath, "");
		
		System.out.println("Finger print: " + kpair.getFingerPrint());
		// TODO strange that we dispose of the privatekey before writing it to the string
//		kpair.dispose();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		kpair.writePublicKey(out, "");
		publicKey = out.toString("UTF-8");
		log.debug("publicKey: "+publicKey);
		out = new ByteArrayOutputStream();
		kpair.writePrivateKey(out, passphrase.getBytes());
		privateKey = out.toString("UTF-8");
		log.debug("privateKey: "+privateKey);
		// moved to end to safety
		kpair.dispose();
	}

}
