package eu.cloudopting.tosca;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xml.dtm.ref.DTMNodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class ToscaService {
	private final Logger log = LoggerFactory.getLogger(ToscaService.class);
	
	public byte[] getToscaGraph( String id){
		log.info("in the swervice");
		return null;
		
	}
	
	public String getOperationForNode(String id,String interfaceType) {
		return null;
		
	}
	
	public DTMNodeList getNodesByType(String type) {
		return null;
		
	}
	
	public void getRootNode() {
		getNodesByType("VMhost");
		return;
	}
	
	public String getTemplateForNode(String id,String templateType) {
		return templateType;
		
	}
	
	public ArrayList<String> getPuppetModules() {
		return null;
		
	}
	
	public HashMap<String, String> getPuppetModulesProperties(String module) {
		return null;
		
	}
	
	public ArrayList<String> getOrderedContainers() {
		return null;
		
	}
	
	public HashMap<String, String> getPropertiesForNode(String id) {
		return null;
		
	}
	
	public HashMap getPropertiesForNodeApplication(String id) {
		return null;
		
	}
	
	public ArrayList<String> getChildrenOfNode(String node) {
		return null;
		
	}
	
	public ArrayList<String> getAllChildrenOfNode(String node){
		return null;
		
	}
	
	public String getNodeType(String id) {
		return null;
		
	}
	
	public String getServiceName(){
		return null;
		
	}
	
	public ArrayList<String> getExposedPortsOfChildren(String id){
		return null;
		
	}
	
	public ArrayList<String> getContainerLinks(String id) {
		return null;
		
	}
	
	public ArrayList<String> getContainerPorts(String id){
		return null;
		
	}
	
	public ArrayList<String> getHostPorts(){
		return null;
		
	}
	
	public void getDefinitionFile(String path){
		
	}
}
