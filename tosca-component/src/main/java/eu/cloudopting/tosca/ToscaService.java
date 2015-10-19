package eu.cloudopting.tosca;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.xalan.extensions.XPathFunctionResolverImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.apache.xpath.jaxp.XPathImpl;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.cloudopting.exception.ToscaException;
import eu.cloudopting.tosca.utils.CustomizationUtils;
import eu.cloudopting.tosca.utils.R10kResultHandler;
import eu.cloudopting.tosca.utils.ToscaUtils;

@Service
public class ToscaService {

	private final Logger log = LoggerFactory.getLogger(ToscaService.class);

	private HashMap<String, DefaultDirectedGraph<String, DefaultEdge>> graphHash = new HashMap<String, DefaultDirectedGraph<String, DefaultEdge>>();

	private XPathImpl xpath;

	private DocumentBuilderImpl db;

	private HashMap<String, DocumentImpl> xdocHash = new HashMap<String, DocumentImpl>();

	@Autowired
	private ToscaUtils toscaUtils;
	
	@Autowired
	private CustomizationUtils customizationUtils;

	public ToscaService() {
		super();
		XPathFactoryImpl xpathFactory = (XPathFactoryImpl) XPathFactoryImpl
				.newInstance();
		this.xpath = (XPathImpl) xpathFactory.newXPath();
		this.xpath
				.setNamespaceContext(new eu.cloudopting.tosca.xml.coNamespaceContext());
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
	 * This method set the XML and creates the structures (DOM and graph) to be
	 * used in the next calls to the service
	 * 
	 * @param customizationId
	 *            the customizationId used to make the tosca service operate on
	 *            the correct XML in a multi user environment
	 * @param xml
	 *            the XML of the TOSCA customization taken from the DB
	 */
	public void setToscaCustomization(String customizationId, String xml) {
		// parse the string
		InputSource source = new InputSource(new StringReader(xml));
		DocumentImpl document = null;
		try {
			document = (DocumentImpl) this.db.parse(source);
			this.xdocHash.put(customizationId, document);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO add the graph part
		// log.info(this.xdocHash.toString());
		// Get the NodeTemplates
		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:NodeTemplate",
					document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the RelationshipTemplate
		DTMNodeList relations = null;
		try {
			relations = (DTMNodeList) this.xpath.evaluate(
					"//ns:RelationshipTemplate[@type='hostedOn']", document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now we create the Graph structure so we know the correct traversal
		// ordering
		ArrayList<String> values = new ArrayList<String>();
		DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);
		for (int i = 0; i < nodes.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
			g.addVertex(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
		}

		for (int i = 0; i < relations.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
			NodeList nl = relations.item(i).getChildNodes();
			// System.out.println(nl.item(0).getNodeValue());
			// System.out.println(nl.item(1).getNodeValue());

			// System.out.println("relation s:"+
			// nl.item(1).getAttributes().getNamedItem("ref").getNodeValue());
			// System.out.println("relation t:"+
			// nl.item(3).getAttributes().getNamedItem("ref").getNodeValue());
			// System.out.println(relations.item(i).getFirstChild()
			// .getAttributes().getNamedItem("ref").getNodeValue());
			// System.out.println(relations.item(i).getAttributes()
			// .getNamedItem("id").getNodeValue());
			// this.g.addVertex(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
			g.addEdge(nl.item(1).getAttributes().getNamedItem("ref")
					.getNodeValue(),
					nl.item(3).getAttributes().getNamedItem("ref")
							.getNodeValue());
		}
		this.graphHash.put(customizationId, g);
		String v;
		TopologicalOrderIterator<String, DefaultEdge> orderIterator;

		orderIterator = new TopologicalOrderIterator<String, DefaultEdge>(g);
		// System.out.println("\nOrdering:");
		while (orderIterator.hasNext()) {
			v = orderIterator.next();
			// System.out.println(v);
		}

	}
	
	public void removeToscaCustomization(String customizationId){
		this.graphHash.remove(customizationId);
		this.xdocHash.remove(customizationId);
		return;
	}
	
	public boolean validateToscaCsar(String csarPath) throws ToscaException{
		boolean isValid = true;
		if(csarPath.isEmpty()){
			throw new ToscaException("File not good");
		}
		//Perform validation here and change the value of isValid accordingly
		return isValid;
	}

	public byte[] getToscaGraph(String customizationId) {
		log.debug("in getToscaGraph");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		return null;

	}

	public String getOperationForNode(String customizationId, String id,
			String interfaceType) {
		log.debug("in getOperationForNode");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		// System.out.println("//ns:NodeType[@name=string(//ns:NodeTemplate[@id='"
		// + id + "']/@type)]/ns:Interfaces/ns:Interface[@name='" +
		// interfaceType + "']/ns:Operation/@name");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(
					"//ns:NodeType[@name=string(//ns:NodeTemplate[@id='" + id
							+ "']/@type)]/ns:Interfaces/ns:Interface[@name='"
							+ interfaceType + "']/ns:Operation/@name", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		String template = nodes.item(0).getNodeValue();
		return template;
	}

	public DTMNodeList getNodesByType(String customizationId, String type) {
		log.debug("in getNodesByType");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(
					"//ns:NodeTemplate[@type='" + type + "']", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodes;
	}

	public void generatePuppetfile(String customizationId, String serviceHome) {
		ArrayList<String> modules = getPuppetModules(customizationId);
		log.debug(modules.toString());
		ArrayList<HashMap<String, String>> modData = new ArrayList<HashMap<String, String>>();
		for (String mod : modules) {
			modData.add(getPuppetModulesProperties(customizationId, mod));
			log.debug(mod);
		}
		log.debug(modData.toString());

		HashMap<String, Object> templData = new HashMap<String, Object>();
		templData.put("modData", modData);
		toscaUtils.generatePuppetfile(templData, serviceHome);
	}

	/**
	 * This method retrieve the tosca csar from the storage component and unzip
	 * it in the proper folder
	 * 
	 * @param customizationId
	 * @param service
	 * @param serviceHome
	 * @param provider
	 */
	public void manageToscaCsar(String customizationId, String service,
			String serviceHome, String provider) {
		log.debug("in manageToscaCsar");
		String fileName = service + ".czar";
		String path = "/cloudOptingData/";

		try {
			toscaUtils.unzip(path + service + ".czar", serviceHome + "/tosca");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public HashMap<String, String> getCloudData(String customizationId) {
		HashMap<String, String> retData = new HashMap<String, String>();
		retData.put("cpu", "1");
		retData.put("mamory", "1");
		retData.put("disk", "1");

		return retData;

	}

	public ArrayList<String> getArrNodesByType(String customizationId,
			String type) {
		DTMNodeList nodes = getNodesByType(customizationId, type);
		ArrayList<String> retList = new ArrayList<String>();
		System.out.println("before cycle");
		for (int i = 0; i < nodes.getLength(); ++i) {
			retList.add(nodes.item(i).getAttributes().getNamedItem("id")
					.getNodeValue());
		}
		return retList;
	}

	public void getRootNode(String customizationId) {
		// getNodesByType("VMhost");
		return;
	}

	public String getTemplateForNode(String customizationId, String id,
			String templateType) {
		log.debug("in getTemplateForNode");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		log.debug("//ArtifactTemplate[@id=string(//NodeTemplate[@id='"
				+ id
				+ "']/DeploymentArtifacts/DeploymentArtifact[@artifactType='"
				+ templateType
				+ "']/@artifactRef)]/ArtifactReferences/ArtifactReference/@reference");
		try {

			nodes = (DTMNodeList) this.xpath
					.evaluate(
							"//ns:ArtifactTemplate[@id=string(//ns:NodeTemplate[@id='"
									+ id
									+ "']/ns:DeploymentArtifacts/ns:DeploymentArtifact[@artifactType='"
									+ templateType
									+ "']/@artifactRef)]/ns:ArtifactReferences/ns:ArtifactReference/@reference",
							theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		String template = nodes.item(0).getNodeValue();
		return template;
	}

	public ArrayList<String> getPuppetModules(String customizationId) {
		log.info("in getPuppetModules");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		DTMNodeList modules = null;

		try {
			modules = (DTMNodeList) this.xpath
					.evaluate(
							"//ns:NodeTypeImplementation/ns:ImplementationArtifacts/ns:ImplementationArtifact[@artifactType='PuppetModule']/@artifactRef",
							theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> modulesList = new ArrayList<String>();

		for (int i = 0; i < modules.getLength(); ++i) {
			String module = modules.item(i).getNodeValue();
			modulesList.add(module);
		}

		return modulesList;
	}

	public HashMap<String, String> getPuppetModulesProperties(
			String customizationId, String module) {
		log.info("in getPuppetModulesProperties");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		DTMNodeList nodes = null;
		// System.out.println("//ArtifactTemplate[@id='" + module +
		// "']/Properties/*");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(
					"//ns:ArtifactTemplate[@id='" + module
							+ "']/ns:Properties/*", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> propHash = new HashMap<String, String>();
		NodeList props = nodes.item(0).getChildNodes();
		for (int i = 0; i < props.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());

			// System.out.println("property val:" +
			// props.item(i).getTextContent());
			String[] keys = props.item(i).getNodeName().split(":");
			if (keys.length > 1) {
				String key = keys[1];
				// System.out.println("property:" + key);
				propHash.put(key, props.item(i).getTextContent());
			}
		}
		return propHash;
	}

	public ArrayList<String> getOrderedContainers(String customizationId) {
		log.debug("in getOrderedContainers");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		ArrayList<String> dockerNodesList = new ArrayList<String>();
		dockerNodesList.add("ClearoApacheDC");
		dockerNodesList.add("ClearoMySQLDC");
		return dockerNodesList;
	}

	public HashMap<String, String> getPropertiesForNode(String customizationId,
			String id) {
		log.debug("in getPropertiesForNode");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		// System.out.println("//NodeTemplate[@id='" + id + "']/Properties/*");
		try {
			nodes = (DTMNodeList) this.xpath
					.evaluate("//ns:NodeTemplate[@id='" + id
							+ "']/ns:Properties/*", theDoc,
							XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> myHash = new HashMap<String, String>();
		NodeList props = nodes.item(0).getChildNodes();
		for (int i = 0; i < props.getLength(); ++i) {
			String[] keys = props.item(i).getNodeName().split(":");
			if (keys.length > 1) {
				String key = keys[1];
				myHash.put(key, props.item(i).getTextContent());
			}
		}
		return myHash;
	}

	public HashMap getPropertiesForNodeApplication(String customizationId,
			String id) {
		log.debug("in getPropertiesForNodeApplication");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath
					.evaluate("//ns:NodeTemplate[@id='" + id
							+ "']/ns:Properties/*", theDoc,
							XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> myHash = new HashMap<String, String>();
		NodeList props = nodes.item(0).getChildNodes();
		for (int i = 0; i < props.getLength(); ++i) {
			String key = props.item(i).getAttributes().getNamedItem("name")
					.getNodeValue();
			myHash.put(key, props.item(i).getTextContent());
		}
		return myHash;
	}

	public ArrayList<String> getChildrenOfNode(String customizationId,
			String node) {
		log.debug("in getChildrenOfNode");
		DefaultDirectedGraph<String, DefaultEdge> graph = this.graphHash
				.get(customizationId);
		if (graph == null)
			return null;

		Set edges = graph.outgoingEdgesOf(node);
		log.debug("Children of:" + node + " are:" + edges.toString());
		Iterator<DefaultEdge> iterator = edges.iterator();
		ArrayList<String> children = new ArrayList<String>();
		while (iterator.hasNext()) {
			String target = graph.getEdgeTarget(iterator.next());
			children.add(target);
		}
		return children;
	}

	public ArrayList<String> getAllChildrenOfNode(String customizationId,
			String node) {
		log.debug("in getAllChildrenOfNode");
		ArrayList<String> children = new ArrayList<String>();
		children = getChildrenOfNode(customizationId, node);
		Iterator<String> child = children.iterator();
		ArrayList<String> returnChildren = new ArrayList<String>();
		while (child.hasNext()) {
			String theChild = child.next();
			returnChildren.addAll(getAllChildrenOfNode(customizationId,
					theChild));
			returnChildren.add(theChild);
		}

		return returnChildren;
	}

	public String getNodeType(String customizationId, String id) {
		log.debug("in getNodeType");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;

		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:NodeTemplate[@id='"
					+ id + "']", theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		// We need to get the type
		String type = nodes.item(0).getAttributes().getNamedItem("type")
				.getNodeValue();
		return type;
	}

	public String getServiceName(String customizationId) {
		log.info("in getServiceName");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		DTMNodeList nodes = null;

		try {
			nodes = (DTMNodeList) this.xpath.evaluate(
					"//ns:ServiceTemplate/@id", theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		String serviceName = nodes.item(0).getNodeValue();
		return serviceName;
	}

	public ArrayList<String> getExposedPortsOfChildren(String customizationId,
			String id) {
		log.debug("in getExposedPortsOfChildren");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		ArrayList<String> exPorts = new ArrayList<String>();
		ArrayList<String> allChildren = getAllChildrenOfNode(customizationId,
				id);
		Iterator<String> aChild = allChildren.iterator();
		log.debug("all children" + allChildren.toString());
		ArrayList<String> xPathExprList = new ArrayList<String>();
		while (aChild.hasNext()) {
			xPathExprList.add("//ns:NodeTemplate[@id='" + aChild.next()
					+ "']/ns:Capabilities/ns:Capability/ns:Properties/*");
		}
		String xPathExpr = StringUtils.join(xPathExprList, "|");
		log.debug("xpath :" + xPathExpr);
		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(xPathExpr, theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < nodes.getLength(); ++i) {
			exPorts.add(nodes.item(i).getTextContent());
		}
		return exPorts;
	}

	public ArrayList<String> getContainerLinks(String customizationId, String id) {
		log.debug("in getContainerLinks");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList links = null;
		try {
			links = (DTMNodeList) this.xpath.evaluate(
					"//ns:RelationshipTemplate[@type='containerLink']/ns:SourceElement[@ref='"
							+ id + "']/../ns:TargetElement", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> linksList = new ArrayList<String>();

		for (int i = 0; i < links.getLength(); ++i) {
			String link = links.item(i).getAttributes().getNamedItem("ref")
					.getNodeValue();
			linksList.add(link);
		}

		return linksList;
	}

	public ArrayList<String> getContainerPorts(String customizationId, String id) {
		log.debug("in getContainerPorts");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		ArrayList<String> ports = new ArrayList<String>();
		String xPathExpr = new String("//ns:NodeTemplate[@id='" + id
				+ "']/ns:Capabilities/ns:Capability/ns:Properties/co:ports");

		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(xPathExpr, theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("nodes :" + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); ++i) {
			String portInfo = nodes.item(i).getAttributes()
					.getNamedItem("host").getNodeValue()
					+ ":"
					+ nodes.item(i).getAttributes().getNamedItem("container")
							.getNodeValue();
			ports.add(portInfo);
			System.out.println("portInfo :" + portInfo);
		}
		return ports;
	}

	public ArrayList<String> getHostPorts(String customizationId) {
		log.debug("in getHostPorts");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		ArrayList<String> ports = new ArrayList<String>();

		String xPathExpr = new String(
				"//ns:NodeTemplate[@type='DockerContainer']/ns:Capabilities/ns:Capability/ns:Properties/co:ports");
		// System.out.println("xpath :" + xPathExpr);

		DTMNodeList nodes = null;
		try {
			XPathExpression expr = this.xpath.compile(xPathExpr);

			nodes = (DTMNodeList) this.xpath.evaluate(xPathExpr, theDoc,
					XPathConstants.NODESET);
			nodes = (DTMNodeList) expr.evaluate(theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("nodes :" + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); ++i) {
			String portInfo = nodes.item(i).getAttributes()
					.getNamedItem("host").getNodeValue();
			ports.add(portInfo);
			// System.out.println("portInfo :" + portInfo);
		}
		return ports;
	}

	
	/*
	 * public void getPuppetModules(String customizationId, String id){ // here
	 * I get the puppet module list and use r10k to download them
	 * log.debug("in getHostPorts"); DocumentImpl theDoc =
	 * this.xdocHash.get(customizationId); if (theDoc == null) return null;
	 * 
	 * }
	 */
	public void runR10k(String customizationId, String serviceHome,
			String coRoot) {
		log.debug("in getDefinitionFile");
		final long r10kJobTimeout = 95000;
		final boolean r10kInBackground = true;
		String puppetFile = serviceHome + "/Puppetfile";
		String puppetDir = coRoot + "/puppet/modules";
		log.debug("puppetFile:" + puppetFile);
		log.debug("puppetDir:" + puppetDir);

		R10kResultHandler r10kResult = toscaUtils.runR10k(puppetFile,
				puppetDir, r10kJobTimeout, r10kInBackground, serviceHome);

		try {
			r10kResult.waitFor();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;

	}

	public void generateDockerCompose(String customizationId, String organizationName, String serviceHome, ArrayList<String> dockerNodesList) {
		ArrayList<HashMap<String, String>> modData = new ArrayList<HashMap<String, String>>();
		for (String node : dockerNodesList) {
			HashMap<String, String> containerData = new HashMap<String, String>();
			String imageName = "cloudopting/"+organizationName+"_"+node.toLowerCase();
			containerData.put("container", node);
			containerData.put("image", imageName);
			// modData.add(toscaFileManager.getPuppetModulesProperties(mod));
			// get the link information for the node

			ArrayList<String> links = getContainerLinks(customizationId, node);
			if (links != null && !links.isEmpty()) {
				containerData.put("links",
						"   - " + StringUtils.join(links, "\n   - "));
			}
			ArrayList<String> exPorts = getExposedPortsOfChildren(customizationId, node);
			if (exPorts != null && !exPorts.isEmpty()) {
				containerData.put("exPorts",
						"   - \"" + StringUtils.join(exPorts, "\"\n   - \"")
								+ "\"");
			}
			ArrayList<String> ports = getContainerPorts(customizationId, node);
			if (ports != null && !ports.isEmpty()) {
				containerData.put("ports",
						"   - \"" + StringUtils.join(ports, "\"\n   - \"")
								+ "\"");
			}

			System.out.println(node);
			modData.add(containerData);
		}
		System.out.println(modData.toString());

		HashMap<String, Object> templData = new HashMap<String, Object>();
		templData.put("dockerContainers", modData);
		// write the "Puppetfile" file
		toscaUtils.generateDockerCompose(templData, serviceHome);
	}
	
	public JSONObject getCustomizationFormData(Long idApp, String csarPath){
		
		return customizationUtils.getCustomizationFormData(idApp, csarPath);
	
	}
}
