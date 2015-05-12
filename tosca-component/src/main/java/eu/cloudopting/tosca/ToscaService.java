package eu.cloudopting.tosca;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.cloudopting.tosca.utils.ToscaUtils;




@Service
public class ToscaService {

	private final Logger log = LoggerFactory.getLogger(ToscaService.class);
	
	private HashMap<String, DefaultDirectedGraph<String, DefaultEdge>> graphHash = new HashMap<String, DefaultDirectedGraph<String,DefaultEdge>>();
	
	private XPathImpl xpath;
	
	private DocumentBuilderImpl db; 
	
	private HashMap<String, DocumentImpl> xdocHash = new HashMap<String, DocumentImpl>();
	
	@Autowired
	private ToscaUtils toscaUtils;

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

	
	/**
	 * This method set the XML and creates the structures (DOM and graph) to be used in the next calls to the service 
	 * 
	 * @param customizationId the customizationId used to make the tosca service operate on the correct XML in a multi user environment
	 * @param xml             the XML of the TOSCA customization taken from the DB
	 */
	public void setToscaCustomization(String customizationId, String xml){
		// parse the string
		InputSource source = new InputSource(new StringReader(xml));

		try {
			DocumentImpl document = (DocumentImpl) this.db.parse(source);
			this.xdocHash.put(customizationId, document);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO add the graph part
		log.info(this.xdocHash.toString());
	}
	
	public byte[] getToscaGraph(String customizationId){
		log.info("in the service");
		return null;
		
	}
	
	public String getOperationForNode(String customizationId, String id,String interfaceType) {
		return null;
		
	}
	
	public DTMNodeList getNodesByType(String customizationId, String type) {
		return null;
		
	}
	
	/**
	 * This method retrieve the tosca csar from the storage component and unzip it in the proper folder
	 * 
	 * @param customizationId
	 * @param service
	 * @param serviceHome
	 * @param provider
	 */
	public void manageToscaCsar(String customizationId, String service, String serviceHome, String provider){
/*		
		try {
			toscaUtils.unzip(service+".czar", serviceHome+"/tosca");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/	
	}
	
	public HashMap<String, String> getCloudData(String customizationId){
		HashMap<String, String> retData = new HashMap<String, String>();
		retData.put("cpu", "1");
		retData.put("mamory", "1");
		retData.put("disk", "1");
		
		return retData;
		
	}
	
	public ArrayList<String> getArrNodesByType(String customizationId, String type) {
		DTMNodeList nodes = getNodesByType(customizationId,type);
		ArrayList<String> retList = new ArrayList<String>();
		System.out.println("before cycle");
		for (int i = 0; i < nodes.getLength(); ++i) {
			retList.add(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
		}
		return retList;		
	}

	public void getRootNode(String customizationId ) {
//		getNodesByType("VMhost");
		return;
	}
	
	public String getTemplateForNode(String customizationId, String id,String templateType) {
		return templateType;
		
	}
	
	public ArrayList<String> getPuppetModules(String customizationId ) {
		return null;
		
	}
	
	public HashMap<String, String> getPuppetModulesProperties(String customizationId, String module) {
		return null;
		
	}
	
	public ArrayList<String> getOrderedContainers() {
		return null;
		
	}
	
	public HashMap<String, String> getPropertiesForNode(String customizationId, String id) {
		return null;
		
	}
	
	public HashMap getPropertiesForNodeApplication(String customizationId, String id) {
		return null;
		
	}
	
	public ArrayList<String> getChildrenOfNode(String customizationId, String node) {
		return null;
		
	}
	
	public ArrayList<String> getAllChildrenOfNode(String customizationId, String node){
		return null;
		
	}
	
	public String getNodeType(String customizationId, String id) {
		log.debug("in getNodeType");
		log.info(this.xdocHash.get(customizationId).toString());
		return null;
		
	}
	
	public String getServiceName(String customizationId){
		log.info("in getServiceName");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		DTMNodeList nodes = null;

		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:ServiceTemplate/@id", theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		String serviceName = nodes.item(0).getNodeValue();
		return serviceName;
	}
	
	public ArrayList<String> getExposedPortsOfChildren(String customizationId, String id){
		return null;
		
	}
	
	public ArrayList<String> getContainerLinks(String customizationId, String id) {
		return null;
		
	}
	
	public ArrayList<String> getContainerPorts(String customizationId, String id){
		return null;
		
	}
	
	public ArrayList<String> getHostPorts(String customizationId){
		return null;
		
	}
	
	public void getDefinitionFile(String customizationId, String path){
		
	}
	
	public void getPuppetModules(String customizationId, String id){
		// here I get the puppet module list and use r10k to download them
		
	}
}
