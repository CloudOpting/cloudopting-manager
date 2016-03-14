package eu.cloudopting.tosca.nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;
import eu.cloudopting.tosca.nodes.CloudOptingNodeImpl;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service
public class ToscaOperationImpl {

	private final Logger log = LoggerFactory.getLogger(ToscaOperationImpl.class);

	@Autowired
	ToscaService tfm;
	
	@Autowired
	CloudOptingNodeImpl cloudOptingNodeImpl;
	
	@Value("${spring.jcr.user}")
	String co_jack_user;
	
	@Value("${spring.jcr.password}")
	String co_jack_password;
	
	public String compilePuppetTemplateHierarchy(HashMap<String, String> data){
		String id = data.get("id");
		String customizationId = data.get("customizationId");
		String toscaPath = data.get("toscaPath");
		log.debug("ToscaOperationImpl.compilePuppetTemplateHierarchy for id: " + id);

		// With my ID I ask to the TFM the array of my sons
		ArrayList<String> mychildren = tfm.getChildrenOfNode(customizationId, id);

		ArrayList<String> templateChunks = new ArrayList<String>();
		for (int i = 0; i < mychildren.size(); i++) {
//			CloudOptingNodeImpl childInstance = new CloudOptingNodeImpl();
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("id", mychildren.get(i));
			hm.put("customizationId", customizationId);
			hm.put("toscaPath", toscaPath);
			templateChunks.add(cloudOptingNodeImpl.execute(hm));
		}

		//Getting the puppetfile template name
		String myTemplate = tfm.getTemplateForNode(customizationId,id, "PuppetTemplate");
		log.debug("The template for " + id + " is: " + myTemplate);

		// I merge all the template chunks from sons and all my own data and get the final template and write it
		Map nodeData = tfm.getPropertiesForNode(customizationId,id);
		// nodeData.put("hostname", id+"."+customer+".local");
		nodeData.put("childtemplates", templateChunks);
		nodeData.put("co_jack_user", this.co_jack_user);
		nodeData.put("co_jack_password", this.co_jack_password);

		return compilePuppetTemplate(null, null , myTemplate, toscaPath, nodeData);
	}
	
	public String writePuppetDockerTemplateHierarchy(HashMap<String, String> data){
		String id = data.get("id");
		String customizationId = data.get("customizationId");
		String toscaPath = data.get("toscaPath");
		String creationPath = data.get("creationPath");
		String servicePath = data.get("servicePath");
		String imageName = data.get("imageName");
		String customer = data.get("customer");
		log.debug("ToscaOperationImpl.writePuppetDockerTemplateHierarchy for id: " + id);
		
		// With my ID I ask to the TFM the array of my sons
		ArrayList<String> mychildren = tfm.getChildrenOfNode(customizationId,id);

		// I cycle on my sons and instantiate dynamically a class of type son to manage this part
		// that method will return a string that represent the chunk of template I need to put in the puppet file

		ArrayList<String> templateChunks = new ArrayList<String>();
		for(int i = 0; i < mychildren.size(); i++){
//			CloudOptingNodeImpl childInstance = new CloudOptingNodeImpl(); 
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("id", mychildren.get(i));
			hm.put("customizationId", customizationId);
			hm.put("toscaPath", toscaPath);
			templateChunks.add(cloudOptingNodeImpl.execute(hm));
		}

		///////////////////
		/// PUPPETFILE PART
		///////////////////

		//Gettomg the puppetfile template name
		String myTemplate = tfm.getTemplateForNode(customizationId, id, "PuppetTemplate");
		log.debug("The Puppet template for this Docker container is: " + myTemplate);

		// I merge all the template chunks from sons and all my own data and get the final template and write it
		Map nodeData = new HashMap();
		nodeData.put("hostname", id + "." + customer + ".local");
		nodeData.put("childtemplates", templateChunks);
		
		String puppetFile = new String(id + ".pp");
		compilePuppetTemplate(puppetFile, servicePath , myTemplate, toscaPath, nodeData);
		log.info("The Puppetfile has been created.");

		///////////////////
		/// DOCKERFILE PART
		///////////////////

		//Getting the exposed ports
		ArrayList<String> exPorts = tfm.getExposedPortsOfChildren(customizationId, id);
		log.debug("The EXPOSED PORTS of the Docker container are: " + exPorts.toString());

		// I get the Dockerfile template name
		Map nodeDataDC = tfm.getPropertiesForNode(customizationId, id);
		nodeDataDC.put("puppetFile", puppetFile);
		nodeDataDC.put("imageName", imageName);
		nodeDataDC.put("exposedPorts", exPorts);
		String myDCTemplate = tfm.getTemplateForNode(customizationId, id,"DockerfileTemplate");
		log.debug("The Dockerfile template for this Docker container is: " + myDCTemplate);
		// I add the data and get the final docker template and write it

		String dockerFile = new String(id + ".dockerfile");
		compilePuppetTemplate(dockerFile, servicePath , myDCTemplate, toscaPath, nodeDataDC);
		log.info("The Dockerfile has been created.");
		return id;
		
	}
	
	public String compilePuppetTemplate(String destinationName,
			String destinationPath, String template, String templateLocation, Map data) {


		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(templateLocation));
		} catch (IOException e1) {
			log.error("IOException in ToscaOperationImpl.compilePuppetTemplate error setting the directory for the template.");
			e1.printStackTrace();
		}

		Template tpl = null;
		try {
			tpl = cfg.getTemplate(template);
		} catch (TemplateNotFoundException e) {
			log.error("TemplateNotFoundException in ToscaOperationImpl.compilePuppetTemplate error on getting the template.");
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			log.error("MalformedTemplateNameException in ToscaOperationImpl.compilePuppetTemplate error on getting the template.");
			e.printStackTrace();
		} catch (ParseException e) {
			log.error("ParseException in ToscaOperationImpl.compilePuppetTemplate error on getting the template.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException in ToscaOperationImpl.compilePuppetTemplate error on getting the template.");
			e.printStackTrace();
		}

		Writer writer = null;
		if (destinationName == null) {
			writer = new StringWriter();
		} else {
			try {
				writer = new PrintWriter(destinationPath + "/" + destinationName, "UTF-8");
			} catch (FileNotFoundException e) {
				log.error("FileNotFoundException in ToscaOperationImpl.compilePuppetTemplate error on creating the writer");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException in ToscaOperationImpl.compilePuppetTemplate error on creating the writer");
				e.printStackTrace();
			}
		}

		try {
			tpl.process(data, writer);
		} catch (TemplateException e) {
			log.error("TemplateException in ToscaOperationImpl.compilePuppetTemplate error on processing the template.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException in ToscaOperationImpl.compilePuppetTemplate error on processing the template.");
			e.printStackTrace();
		}

		if (destinationName == null) {
			return ((StringWriter) writer).getBuffer().toString();
		}
		return null;
	}


}
