package eu.cloudopting.bpmn.tasks.deploy;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

@Service
public class DeployZip implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployZip.class);
	@Autowired
	ToscaService toscaService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		log.debug("in DeployZip");
		String serviceHome = (String) execution.getVariable("serviceHome");
		String coRoot = (String) execution.getVariable("coRoot");
//		toscaService.getNodeType(customizationId,"");
		// Remove the tosca customization
		//toscaService.removeToscaCustomization(customizationId);
		// delete the folder
		
		// remove the caches in dockerservice
		ZipFile zipFile = new ZipFile(coRoot+"/test.zip");
		
		ZipParameters parameters = new ZipParameters();
		
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		
		zipFile.addFolder(serviceHome, parameters);
		
	}

}
