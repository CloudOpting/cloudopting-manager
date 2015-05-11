package eu.cloudopting.tosca;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xalan.extensions.XPathFunctionResolverImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.apache.xpath.jaxp.XPathImpl;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;




@Service
public class ToscaService {

	private final Logger log = LoggerFactory.getLogger(ToscaService.class);
	
	private HashMap<String, DefaultDirectedGraph<String, DefaultEdge>> graphHash = new HashMap<String, DefaultDirectedGraph<String,DefaultEdge>>();
	
	private XPathImpl xpath;
	
	private DocumentBuilderImpl db; 
	
	private HashMap<String, DocumentImpl> xdocHash = new HashMap<String, DocumentImpl>();

	public ToscaService() {
		super();
		XPathFactoryImpl xpathFactory = (XPathFactoryImpl) XPathFactoryImpl
				.newInstance();
		this.xpath = (XPathImpl) xpathFactory.newXPath();
		this.xpath.setNamespaceContext(new eu.cloudopting.tosca.xml.coNamespaceContext());
		this.xpath.setXPathFunctionResolver(new XPathFunctionResolverImpl());
		DocumentBuilderFactoryImpl dbf = new DocumentBuilderFactoryImpl();
		dbf.setNamespaceAware(true);
		
		try {
			this.db = (DocumentBuilderImpl) dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	
	public void setToscaCustomization(String customizationId, String xml){
		// parse the string
		InputSource source = new InputSource(new StringReader(xml));

		try {
			DocumentImpl document = (DocumentImpl) this.db.parse(source);
			xdocHash.put(customizationId, document);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO add the graph part
		
	}
	
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
	
	public String getNodeType(String customizationId, String id) {
		log.debug("in getNodeType");
		log.info(xdocHash.get(customizationId).toString());
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
	
	public void getPuppetModules(String id){
		// here I get the puppet module list and use r10k to download them
		
	}
}
