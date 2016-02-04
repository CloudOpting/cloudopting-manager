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
		System.out.println("I'm in the compilePuppetTemplateHierarchy for :" + id);

		// With my ID I ask to the TFM the array of my sons
		ArrayList<String> mychildren = tfm.getChildrenOfNode(customizationId, id);

		int i;
		ArrayList<String> templateChunks = new ArrayList<String>();
		for (i = 0; i < mychildren.size(); i++) {
//			CloudOptingNodeImpl childInstance = new CloudOptingNodeImpl();
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("id", mychildren.get(i));
			hm.put("customizationId", customizationId);
			hm.put("toscaPath", toscaPath);
			templateChunks.add(cloudOptingNodeImpl.execute(hm));
		}
		// I get the puppetFile template name
		String myTemplate = tfm.getTemplateForNode(customizationId,id, "PuppetTemplate");
		System.out.println("The template for "+id+" is :" + myTemplate);
		// I merge all the template chunks from sons and all my own data and get
		// the final template and write it

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
		System.out.println("I'm in the writePuppetDockerTemplateHierarchy for :" + id);
		
		// With my ID I ask to the TFM the array of my sons
		ArrayList<String> mychildren = tfm.getChildrenOfNode(customizationId,id);

		// I cycle on my sons and instantiate dynamically a class of type son to manage this part
		// that method will return a string that represent the chunk of template I need to put in the puppet file

		int i;
		ArrayList<String> templateChunks = new ArrayList<String>(); 
		for(i=0;i<mychildren.size();i++){
//			CloudOptingNodeImpl childInstance = new CloudOptingNodeImpl(); 
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("id", mychildren.get(i));
			hm.put("customizationId", customizationId);
			hm.put("toscaPath", toscaPath);
			templateChunks.add(cloudOptingNodeImpl.execute(hm));
		}
		
		
		
		// I get the puppetFile template name
		String myTemplate = tfm.getTemplateForNode(customizationId, id,"PuppetTemplate");
		System.out.println("The Puppet template for this DockerContainer is :"+myTemplate);
		// I merge all the template chunks from sons and all my own data and get the final template and write it
		
		Map nodeData = new HashMap();
		nodeData.put("hostname", id+"."+customer+".local");
		nodeData.put("childtemplates",templateChunks);
		
		String puppetFile = new String(id+".pp");
		compilePuppetTemplate(puppetFile, servicePath , myTemplate, toscaPath, nodeData);
		System.out.println("Puppet file created");
		/// DOCKERFILE PART
		
		// get the exposed ports
		ArrayList<String> exPorts = tfm.getExposedPortsOfChildren(customizationId, id);
		System.out.println("The EXPOSED PORTS DockerContainer are :"+exPorts.toString());
		// I get the Dockerfile template name
		Map nodeDataDC = tfm.getPropertiesForNode(customizationId, id);
		nodeDataDC.put("puppetFile",puppetFile);
		nodeDataDC.put("imageName",imageName);
		nodeDataDC.put("exposedPorts",exPorts);
		String myDCTemplate = tfm.getTemplateForNode(customizationId, id,"DockerfileTemplate");
		System.out.println("The Dockerfile template for this DockerContainer is :"+myDCTemplate);
		// I add the data and get the final docker template and write it

		String dockerFile = new String(id+".dockerfile");
		compilePuppetTemplate(dockerFile, servicePath , myDCTemplate, toscaPath, nodeDataDC);
		System.out.println("Dockerfile created");
		return id;
		
	}
	
	public String compilePuppetTemplate(String destinationName,
			String destinationPath, String template, String templateLocation, Map data) {


		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(templateLocation));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Template tpl = null;
		try {
			tpl = cfg.getTemplate(template);
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Writer writer = null;
		if (destinationName == null) {
			writer = new StringWriter();
		} else {
			try {
				writer = new PrintWriter(destinationPath + "/"
						+ destinationName, "UTF-8");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			tpl.process(data, writer);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (destinationName == null) {
			return ((StringWriter) writer).getBuffer().toString();
		}
		return null;
	}


}
