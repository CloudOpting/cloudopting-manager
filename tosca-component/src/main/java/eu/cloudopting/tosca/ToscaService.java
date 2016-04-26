package eu.cloudopting.tosca;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.text.AbstractDocument.Content;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xalan.extensions.XPathFunctionResolverImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;

import org.apache.xerces.xs.XSModel;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.apache.xpath.jaxp.XPathImpl;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.exceptions.DynamicException;
import org.eclipse.persistence.internal.oxm.Marshaller;
import org.eclipse.persistence.jaxb.JAXBMarshaller;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContext;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import eu.cloudopting.exception.ToscaException;
import eu.cloudopting.tosca.utils.CSARUtils;
import eu.cloudopting.tosca.utils.CustomizationUtils;
import eu.cloudopting.tosca.utils.R10kResultHandler;
import eu.cloudopting.tosca.utils.ToscaUtils;
import jlibs.xml.sax.XMLDocument;
import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;
import scala.annotation.meta.getter;

@Service
public class ToscaService {

	private final Logger log = LoggerFactory.getLogger(ToscaService.class);

	private HashMap<String, DefaultDirectedGraph<String, DefaultEdge>> graphHash = new HashMap<String, DefaultDirectedGraph<String, DefaultEdge>>();

	private XPathImpl xpath;

	private DocumentBuilderImpl db;

	private HashMap<String, DocumentImpl> xdocHash = new HashMap<String, DocumentImpl>();

	private ArrayList<String> nodeTypeList;
	private JSONObject nodeJsonList;
	private JSONObject nodeJsonTypeList;
	private ArrayList<String> edgeTypeList;
	private JSONObject edgeJsonList;
	private JSONObject edgeJsonTypeList;
	private DocumentImpl definitionTemplate;
	private DocumentImpl documentTypes;
	private JSONObject nodeTypePropList;

	@Autowired
	private ToscaUtils toscaUtils;

	@Autowired
	private CustomizationUtils customizationUtils;

	@Autowired
	private CSARUtils csarUtils;

	@Value("${orchestrator.logger_address}")
	private String logger_address;

	public ToscaService() {
		super();
		XPathFactoryImpl xpathFactory = (XPathFactoryImpl) XPathFactoryImpl.newInstance();
		this.xpath = (XPathImpl) xpathFactory.newXPath();
		this.xpath.setNamespaceContext(new eu.cloudopting.tosca.xml.coNamespaceContext());
		this.xpath.setXPathFunctionResolver(new XPathFunctionResolverImpl());
		DocumentBuilderFactoryImpl dbf = new DocumentBuilderFactoryImpl();
		dbf.setNamespaceAware(true);
		dbf.setXIncludeAware(true);
		dbf.setIgnoringElementContentWhitespace(true);

		try {
			this.db = (DocumentBuilderImpl) dbf.newDocumentBuilder();
			db.setErrorHandler(new ErrorHandlerImpl());
		} catch (ParserConfigurationException e2) {
			log.error("ParserConfigurationException in ToscaService.ToscaService");
			e2.printStackTrace();
		}
		prepareNodeTypes();
		readDefinitionTemplate();
	}

	private void readDefinitionTemplate() {

		InputStream in;
        ClassPathResource definitions = new ClassPathResource("definitionsTemplate.xml");
		try {
			in = definitions.getInputStream();
//			in = new FileInputStream("classpath:definitionsTemplate.xml");
			this.definitionTemplate = (DocumentImpl) this.db.parse(in);
		} catch (SAXException e) {
			log.error("SAXException in ToscaService.readDefinitionTemplate");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException in ToscaService.readDefinitionTemplate");
			e.printStackTrace();
		}

	}

	public String readXsd(String element) {
		// String PATH_TO_XSD = null;
		log.debug("ToscaService.readXsd starting.");
		ClassPathResource nodetypes = new ClassPathResource("toscaTemplate/Types/CloudOptingTypes.xsd");
		File file = null;
		try {
			file = nodetypes.getFile();
		} catch (IOException e1) {
			log.error("IOException in ToscaService.readXsd");
			e1.printStackTrace();
		}
//		File file = new File("types/nodeTypes.xsd");
		final XSModel xsModel = new XSParser().parse(file.getPath());
		log.debug("ToscaService.readXsd xsModel: " + xsModel.toString());
		final XSInstance xsInstance = new XSInstance();
		xsInstance.generateOptionalElements = Boolean.TRUE; // null means random
		// final QName rootElement = new QName(null, element);
		final QName rootElement = new QName("http://docs.oasis-open.org/tosca/ns/2011/12/CloudOptingTypes", element);
		log.debug("ToscaService.readXsd rootElement: " + rootElement.toString());
		XMLDocument sampleXml;
		try {
			log.debug("ToscaService.readXsd Providing a writer.");
			StringWriter writer = new StringWriter();
			sampleXml = new XMLDocument(new StreamResult(writer), true, 4, null);
			xsInstance.generate(xsModel, rootElement, sampleXml);
			log.debug("ToscaService.readXsd sampleXml: " + sampleXml.toString());
			log.debug("ToscaService.readXsd writer: " + writer.toString());
			return writer.toString();
		} catch (TransformerConfigurationException e) {
			log.error("TransformerConfigurationException in ToscaService.readXsd");
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException in ToscaService.readXsd");
			e.printStackTrace();
			return null;
		}

		/*
		 * InputStream in; DynamicJAXBContext jaxbContext = null; DynamicType
		 * rootType = null; DynamicEntity root = null; try { in = new
		 * FileInputStream("types/nodeTypes.xsd");
		 * 
		 * jaxbContext = DynamicJAXBContextFactory.createContextFromXSD(in,
		 * null, Thread.currentThread().getContextClassLoader(), null); //
		 * String YOUR_ROOT_TYPE = null; log.debug(element); rootType =
		 * jaxbContext.getDynamicType(element); log.debug(rootType.toString());
		 * root = rootType.newDynamicEntity(); traverseProps(jaxbContext, root,
		 * rootType, 0); log.debug(root.toString()); JAXBMarshaller marshaller
		 * =jaxbContext.createMarshaller();
		 * 
		 * } catch (FileNotFoundException | JAXBException | DynamicException |
		 * InstantiationException | IllegalAccessException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}

	private void traverseProps(DynamicJAXBContext c, DynamicEntity e, DynamicType t, int level)
			throws DynamicException, InstantiationException, IllegalAccessException {
		if (t != null) {
			log.info(level + "type [" + t.getName() + "] of class [" + t.getClassName() + "] has "
					+ t.getNumberOfProperties() + " props");
			for (String pName : t.getPropertiesNames()) {
				Class<?> clazz = t.getPropertyType(pName);
				log.info(level + "prop [" + pName + "] in type: " + clazz);
				// logger.info("prop [" + pName + "] in entity: " +
				// e.get(pName));

				if (clazz == null) {
					// need to create an instance of object
					String updatedClassName = pName.substring(0, 1).toUpperCase() + pName.substring(1);
					log.info(level + "Creating new type instance for " + pName + " using following class name: "
							+ updatedClassName);
					DynamicType child = c.getDynamicType("generated." + updatedClassName);
					DynamicEntity childEntity = child.newDynamicEntity();
					e.set(pName, childEntity);
					traverseProps(c, childEntity, child, level + 1);
				} else {
					// just set empty value
					e.set(pName, clazz.newInstance());
				}
			}
		} else {
			log.warn("type is null");
		}
	}

	public void prepareNodeTypes() {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		};

		ClassPathResource typefolder = new ClassPathResource("types");
		File folder = null;
		try {
			folder = typefolder.getFile();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
//		File folder = new File("types");
		File[] listOfFiles = folder.listFiles(filter);

		String fullXml = "<Nodes>";
if(listOfFiles!=null){
		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			String content = null;
			try {
				content = FileUtils.readFileToString(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// log.debug(content);
			fullXml = fullXml.concat(content);
			// do something with the file
		}
}
		fullXml = fullXml.concat("</Nodes>");
		log.debug("xml: " + fullXml);
		/*
		 * String xml = null; try { xml = new
		 * String(Files.readAllBytes(Paths.get("master.xml"))); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		InputSource source = new InputSource(new StringReader(fullXml));
		this.documentTypes = null;
		try {
			this.documentTypes = (DocumentImpl) this.db.parse(source);
			log.debug("xml: " + fullXml);
			log.debug(this.documentTypes.toString());
			// this.xdocHash.put(customizationId, document);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// MANAGE NODES
		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//Nodes/NodeType", this.documentTypes, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug(nodes.toString());
		this.nodeTypeList = new ArrayList<String>();
		this.nodeJsonList = new JSONObject();
		this.nodeJsonTypeList = new JSONObject();

		this.nodeTypePropList = new JSONObject();
		// NodeType cycle
		for (int i = 0; i < nodes.getLength(); ++i) {
			log.debug(nodes.item(i).getChildNodes().item(1).getNodeName());
			String nodeName = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
			log.debug(nodeName);

			// recover the name and place into an array
			this.nodeTypeList.add(nodeName);
			String color = nodes.item(i).getAttributes().getNamedItem("color").getNodeValue();
			String shape = nodes.item(i).getAttributes().getNamedItem("shape").getNodeValue();
			String image = nodes.item(i).getAttributes().getNamedItem("image").getNodeValue();
			// JSONObject jret = new JSONObject();
			JSONObject data = new JSONObject();
			JSONObject dataType = new JSONObject();
			JSONObject props = new JSONObject();
			JSONObject capProps = new JSONObject();
			String theProperty = null;
			for (int j = 0; j < nodes.item(i).getChildNodes().getLength(); ++j) {
				log.debug(nodes.item(i).getChildNodes().item(j).getNodeName());
				String childNode = nodes.item(i).getChildNodes().item(j).getNodeName();
				if (childNode.equals("PropertiesDefinition")) {
					log.debug("matched PropertiesDefinition");
					String element = nodes.item(i).getChildNodes().item(j).getAttributes().getNamedItem("element")
							.getNodeValue();
					String xsdType = nodes.item(i).getChildNodes().item(j).getAttributes().getNamedItem("type")
							.getNodeValue();
					log.debug(element);
					log.debug(xsdType);
					String xmlModel = readXsd(element);
					props = createFormObject(element, xmlModel, nodeName, "property");
					theProperty = element;
				} else if (childNode.equals("CapabilityDefinitions")) {
					log.debug("matched CapabilityDefinitions");
					String xpathCap = "//CapabilityType[@name=string(//Nodes/NodeType[@name='" + nodeName
							+ "']/CapabilityDefinitions/CapabilityDefinition/@capabilityType)]/PropertiesDefinition";
					log.debug(xpathCap);
					DTMNodeList nodeCaps = null;
					try {
						nodeCaps = (DTMNodeList) this.xpath.evaluate(xpathCap, this.documentTypes,
								XPathConstants.NODESET);
					} catch (XPathExpressionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String elCapProp = nodeCaps.item(0).getAttributes().getNamedItem("element").getNodeValue();
					log.debug(elCapProp);
					String xmlCapModel = readXsd(elCapProp);
					log.debug(xmlCapModel);
					capProps = createFormObject(elCapProp, xmlCapModel, nodeName, "capability");
					log.debug(capProps.toString());

				}

			}
			try {
				// need to merge all the properties
				JSONObject mergedJSON = new JSONObject();

				JSONObject[] objs = new JSONObject[] { capProps, props };
				for (JSONObject obj : objs) {
					Iterator it = obj.keys();
					while (it.hasNext()) {
						String key = (String) it.next();
						mergedJSON.put(key, obj.get(key));
					}
				}
				/*
				 * mergedJSON = new JSONObject(props,
				 * JSONObject.getNames(props)); for (String crunchifyKey :
				 * JSONObject.getNames(capProps)) { mergedJSON.put(crunchifyKey,
				 * capProps.get(crunchifyKey)); }
				 */

				JSONObject template = new JSONObject("{\"type\":\"object\",\"title\":\"Node properties\"}");
				template.put("properties", mergedJSON);

				data.put("shape", shape);
				data.put("image", image);
				data.put("color", color);
				data.put("props", template);
				dataType.put("props", template);
				dataType.put("propName", theProperty);
				// jret.put(nodeName, data);
				log.debug(data.toString());
				this.nodeJsonList.put(nodeName, data);
				this.nodeJsonTypeList.put(nodeName, dataType);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// recover the property and read the xsd than generate a xml and
			// with that generate the proper json
		}
		log.debug("the prop list -------------------");
		log.debug(this.nodeTypePropList.toString());
		// MANAGE EDGES
		DTMNodeList edges = null;
		try {
			edges = (DTMNodeList) this.xpath.evaluate("//Nodes/RelationshipType", this.documentTypes,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug(edges.toString());
		this.edgeTypeList = new ArrayList<String>();
		this.edgeJsonList = new JSONObject();
		this.edgeJsonTypeList = new JSONObject();
		// RelationshipType cycle
		for (int i = 0; i < edges.getLength(); ++i) {
			// log.debug(edges.item(i).getChildNodes().item(1).getNodeName());
			String edgeName = edges.item(i).getAttributes().getNamedItem("name").getNodeValue();
			log.debug(edgeName);
			// recover the name and place into an array
			this.edgeTypeList.add(edgeName);
			String color = edges.item(i).getAttributes().getNamedItem("color").getNodeValue();
			String style = edges.item(i).getAttributes().getNamedItem("style").getNodeValue();

			// managing properties for relationship
			JSONObject props = new JSONObject();
			String theProperty = null;
			for (int j = 0; j < edges.item(i).getChildNodes().getLength(); ++j) {
				log.debug(edges.item(i).getChildNodes().item(j).getNodeName());
				String childNode = edges.item(i).getChildNodes().item(j).getNodeName();
				if (childNode.equals("PropertiesDefinition")) {
					log.debug("matched PropertiesDefinition for edges");
					String element = edges.item(i).getChildNodes().item(j).getAttributes().getNamedItem("element")
							.getNodeValue();
					String xsdType = edges.item(i).getChildNodes().item(j).getAttributes().getNamedItem("type")
							.getNodeValue();
					log.debug(element);
					log.debug(xsdType);
					String xmlModel = readXsd(element);
					props = createFormObject(element, xmlModel, edgeName, "property");
					theProperty = element;
				}
			}

			JSONObject template = null;
			try {
				template = new JSONObject("{\"type\":\"object\",\"title\":\"Edge properties\"}");
				template.put("properties", props);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			JSONObject data = new JSONObject();
			JSONObject dataType = new JSONObject();
			try {
				data.put("style", style);
				data.put("color", color);
				data.put("props", template);
				dataType.put("props", template);
				dataType.put("propName", theProperty);
				log.debug(data.toString());
				this.edgeJsonList.put(edgeName, data);
				this.edgeJsonTypeList.put(edgeName, dataType);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.debug(this.edgeJsonList.toString());
		}

	}

	private JSONObject createFormObject(String element, String doc, String nodeName, String propertyType) {
		log.debug("in createFormObject con: " + element);
		JSONObject returnObj = new JSONObject();
		JSONObject props = null;
		try {
			// read the XML document
			InputSource source = new InputSource(new StringReader(doc));
			DocumentImpl document = (DocumentImpl) this.db.parse(source);
			// look if the parent node has some attributes for the form (this
			// used for arrays)
			Node parentFormTypeNode = document.getElementsByTagName(element).item(0).getAttributes()
					.getNamedItem("formtype");
			String parentFormType = null;
			// get the props of the element
			/*
			 * boolean setParent = true; if (parentFormTypeNode != null) {
			 * log.debug("the parent node has a formtype"); parentFormType =
			 * parentFormTypeNode.getNodeValue(); String parentFormTitle =
			 * document.getElementsByTagName(element).item(0).getAttributes()
			 * .getNamedItem("formtype").getNodeValue(); switch (parentFormType)
			 * { case "array": setParent = false; break;
			 * 
			 * default: break; } }
			 */
			props = processProperties(document, nodeName, propertyType, element);
			log.debug(props.toString());
			if (parentFormTypeNode != null) {
				log.debug("the parent node has a formtype");
				parentFormType = parentFormTypeNode.getNodeValue();
				String parentFormTitle = document.getElementsByTagName(element).item(0).getAttributes()
						.getNamedItem("formtype").getNodeValue();
				switch (parentFormType) {
				case "array":
					JSONObject arr = new JSONObject("{\"type\":\"array\"}");
					arr.put("items", new JSONObject().put("type", "object").put("properties", props));
					returnObj.put(element, arr);
					break;

				default:
					break;
				}
			} else {
				returnObj = props;
			}

		} catch (JSONException | SAXException | IOException | XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnObj;
	}

	private JSONObject processProperties(DocumentImpl document, String nodeName, String propertyType,
			String propertyTypeName) throws XPathExpressionException, JSONException {
		log.debug("in processProperties");
		DTMNodeList nodes = (DTMNodeList) this.xpath.evaluate("/*/*", document, XPathConstants.NODESET);
		JSONObject props = new JSONObject();
		JSONObject propsList = new JSONObject();
		for (int i = 0; i < nodes.getLength(); ++i) {
			String name = nodes.item(i).getNodeName();
			log.debug("Property:" + name);

			String defValue = null;
			String description = null;
			if (nodes.item(i).getAttributes().getNamedItem("default") != null) {
				defValue = nodes.item(i).getAttributes().getNamedItem("default").getNodeValue();
			}
			if (nodes.item(i).getAttributes().getNamedItem("description") != null) {
				description = nodes.item(i).getAttributes().getNamedItem("description").getNodeValue();
			}
			String title = nodes.item(i).getAttributes().getNamedItem("title").getNodeValue();
			log.debug("title:" + title);
			String formtype = nodes.item(i).getAttributes().getNamedItem("formtype").getNodeValue();
			log.debug("formtype:" + formtype);
			JSONObject form = new JSONObject();
			JSONObject xPropList = new JSONObject();
			switch (formtype) {
			case "array":
				// need to cycle in the node
				JSONObject childprops = new JSONObject();
				log.debug("/*/*/*");
				DTMNodeList childnodes = (DTMNodeList) this.xpath.evaluate("/*/*/*", document, XPathConstants.NODESET);
				log.debug(new Integer(childnodes.getLength()).toString());
				for (int n = 0; n < childnodes.getLength(); n++) {
					JSONObject childform = new JSONObject();
					String childName = childnodes.item(n).getNodeName();
					log.debug("Child Property:" + childName);
					String childtitle = childnodes.item(n).getAttributes().getNamedItem("title").getNodeValue();
					log.debug("title:" + childtitle);
					String childformtype = childnodes.item(n).getAttributes().getNamedItem("formtype").getNodeValue();
					log.debug("formtype:" + childformtype);
					childform.put("title", childtitle);
					childform.put("type", childformtype);
					childprops.put(childName, childform);
					xPropList.append(name, childName);
				}
				// childprops.put(name, form);
				JSONObject arr = new JSONObject("{\"type\":\"array\"}");
				arr.put("items", new JSONObject().put("type", "object").put("properties", childprops));
				props.put(name, arr);
				switch (propertyType) {
				case "capability":
					propsList.append(propertyTypeName, xPropList);
					break;
				default:
					propsList.append(propertyType, xPropList);
					break;
				}
				break;
			default:
				Node nodeEnum = nodes.item(i).getAttributes().getNamedItem("enum");
				if (nodeEnum != null) {
					form.put("enum", new JSONArray(nodeEnum.getNodeValue()));
				}
				form.put("title", title);
				form.put("type", formtype);
				if (description != null) {
					form.put("description", description);
				}
				if (defValue != null) {
					form.put("default", defValue);
				}
				props.put(name, form);
				switch (propertyType) {
				case "capability":
					propsList.append(propertyTypeName, name);
					break;
				default:
					propsList.append(propertyType, name);
					break;
				}
				break;
			}

		}
		switch (propertyType) {
		case "capability":
			JSONObject cap = new JSONObject();
			cap.append(propertyType, propsList);
			this.nodeTypePropList.append(nodeName, cap);
			break;

		default:
			this.nodeTypePropList.append(nodeName, propsList);
			break;
		}

		return props;
	}

	public ArrayList<String> getNodeTypeList() {
		return this.nodeTypeList;
	}

	public ArrayList<String> getEdgeTypeList() {
		return this.edgeTypeList;
	}

	public JSONObject getNodeTypeJsonList() {
		return this.nodeJsonList;
	}

	public JSONObject getEdgeTypeJsonList() {
		return this.edgeJsonList;
	}

	public void writeToscaDefinition(JSONObject data, String destDir) {
		// recover the definition template.
		try {
			String serviceName = data.getString("serviceName");
			log.debug(serviceName);
			this.definitionTemplate.getElementsByTagName("Definitions").item(0).getAttributes().getNamedItem("id")
					.setTextContent(serviceName);
			this.definitionTemplate.getElementsByTagName("ServiceTemplate").item(0).getAttributes().getNamedItem("id")
					.setTextContent(serviceName);
			this.definitionTemplate.getElementsByTagName("ServiceTemplate").item(0).getAttributes().getNamedItem("name")
					.setTextContent(serviceName);

			// COPYING NodeTypes, NodeTypeImplementation, ArtifactTemplate
			String xPathNodeType = "//Nodes/NodeType|//Nodes/NodeTypeImplementation|//Nodes/ArtifactTemplate|//Nodes/RelationshipType";
			DTMNodeList nodestypes = null;
			try {
				nodestypes = (DTMNodeList) this.xpath.evaluate(xPathNodeType, this.documentTypes,
						XPathConstants.NODESET);

				for (int nt = 0; nt < nodestypes.getLength(); nt++) {
					Node newNode = this.definitionTemplate.importNode(nodestypes.item(nt), true);

					// String strNode =
					// this.documentTypes.saveXML(nodestypes.item(nt));
					// strNode = strNode.replace("<?xml version=\"1.0\"
					// encoding=\"UTF-16\"?>", "");
					// log.debug(strNode);
					// Node newNode =
					// this.definitionTemplate.createElement(strNode);
					// newNode.getAttributes().removeNamedItem("xmlns");
					this.definitionTemplate.getElementsByTagName("Definitions").item(0).appendChild(newNode);
				}

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

			JSONArray nodeArr = data.getJSONArray("nodes");
			JSONArray edgeArr = data.getJSONArray("edges");
			log.debug("the original JSON string");
			log.debug(this.nodeJsonTypeList.toString());
			for (int i = 0; i < nodeArr.length(); i++) {
				JSONObject theNode = nodeArr.getJSONObject(i);
				String nodeType = theNode.getString("type");
				String nodeId = theNode.getString("name");
				log.debug("nodeType:" + nodeType);
				log.debug("nodeId:" + nodeId);
				Element nodeTemplate = this.definitionTemplate.createElement("NodeTemplate");
				Element properties = this.definitionTemplate.createElement("Properties");
				Element capabilities = this.definitionTemplate.createElement("Capabilities");
				String propertyName = this.nodeJsonTypeList.getJSONObject(theNode.getString("type"))
						.getString("propName");
				Element thePropBlock = this.definitionTemplate.createElement("co:" + propertyName);
				JSONObject model = theNode.getJSONObject("model");
				JSONObject jsonType = this.nodeJsonTypeList.getJSONObject(theNode.getString("type"));
				log.debug(jsonType.toString());

				log.debug(this.nodeTypePropList.getJSONArray(nodeType).toString());
				// log.debug(this.nodeTypePropList.getJSONArray(nodeType).gtoString());

				// here we need to go over all the effective data the the type
				// require and check if we have a value for it
				for (int n = 0; n < this.nodeTypePropList.getJSONArray(nodeType).length(); n++) {
					JSONObject proSet = this.nodeTypePropList.getJSONArray(nodeType).getJSONObject(n);
					log.debug(proSet.toString());
					String propKey = proSet.keys().next().toString();
					log.debug(propKey);
					switch (propKey) {
					case "capability":
						// capability has an array og jsonobject with the
						// properties os a cycle more
						for (int c = 0; c < proSet.getJSONArray(propKey).length(); c++) {
							JSONObject aCap = proSet.getJSONArray(propKey).getJSONObject(c);
							String capName = ((JSONObject) aCap).keys().next().toString();

							log.debug(capName);
							Element aNodeCap = this.definitionTemplate.createElement("co:" + capName);
							Element capability = this.definitionTemplate.createElement("Capability");
							Element capProperties = this.definitionTemplate.createElement("Properties");

							capability.setAttribute("type", capName);
							capability.setAttribute("id", "c" + c);
							for (int p = 0; p < aCap.getJSONArray(capName).length(); p++) {
								// String propName =
								// aCap.getJSONArray(capName).getString(p);
								Object propName = aCap.getJSONArray(capName).get(p);

								// String capName = new String();
								if (propName instanceof JSONObject) {
									log.debug("the capability is an object");
									String keyName = ((JSONObject) propName).keys().next().toString();
									log.debug(keyName);
									JSONArray capModel = model.getJSONArray(keyName);
									log.debug(capModel.toString());
									for (int cp = 0; cp < capModel.length(); cp++) {
										JSONObject capObj = capModel.getJSONObject(cp);
										Element capArr = this.definitionTemplate.createElement("co:" + keyName);
										Iterator capIter = capObj.keys();
										while (capIter.hasNext()) {
											String capModelPropName = capIter.next().toString();
											String capModelPropVal = capObj.getString(capModelPropName);
											log.debug("capModelPropName:" + capModelPropName);
											log.debug("capModelPropVal:" + capModelPropVal);
											Element capArrProp = this.definitionTemplate
													.createElement("co:" + capModelPropName);
											capArrProp.appendChild(
													this.definitionTemplate.createTextNode(capModelPropVal));
											capArr.appendChild(capArrProp);
										}
										capProperties.appendChild(capArr);
									}
								} else {
									log.debug("the capability is a simple string");
									log.debug((String) propName);

								}

							}
							capability.appendChild(capProperties);
							capabilities.appendChild(capability);

						}

						break;

					default:
						for (int p = 0; p < proSet.getJSONArray(propKey).length(); p++) {
							log.debug(proSet.getJSONArray(propKey).get(p).getClass().getName());
							Object childProp = proSet.getJSONArray(propKey).get(p);
							if (childProp instanceof String) {
								String theProp = proSet.getJSONArray(propKey).getString(p);
								log.debug(theProp);

								if (model.has(theProp)) {
									String propVal = model.getString(theProp);
									Element aNodeProp = this.definitionTemplate.createElement("co:" + theProp);
									JSONObject proInfo = jsonType.getJSONObject("props").getJSONObject("properties")
											.getJSONObject(theProp);
									log.debug("proInfo:" + proInfo.toString());
									aNodeProp.appendChild(
											generateInteralField(nodeId, propertyName, theProp, propVal, proInfo));
									thePropBlock.appendChild(aNodeProp);
								}

							} else {
								// we have a jsonw object sign that it could be
								// part of an array
								JSONObject thePropArr = proSet.getJSONArray(propKey).getJSONObject(p);
								String proArrKey = thePropArr.keys().next().toString();
								log.debug("thePropArr");
								log.debug(thePropArr.toString());
								log.debug("proArrKey:" + proArrKey);
								// check if in the model exists the array
								if (model.has(proArrKey)) {
									String arrName = thePropArr.keys().next().toString();
									log.debug("arrName:" + arrName);

									JSONArray theArr = thePropArr.getJSONArray(arrName);
									log.debug(theArr.toString());

									JSONArray modelArr = model.getJSONArray(proArrKey);
									log.debug(modelArr.toString());

									for (int m = 0; m < modelArr.length(); m++) {
										JSONObject intModel = modelArr.getJSONObject(m);
										Element aNodeProp = this.definitionTemplate.createElement("co:" + proArrKey);
										for (int a = 0; a < theArr.length(); a++) {
											String intProps = theArr.getString(a);
											log.debug("intProps:" + intProps);
											if (intModel.has(intProps)) {
												String intVal = intModel.getString(intProps);
												log.debug("intVal:" + intVal);
												Element aNodeIntProp = this.definitionTemplate.createElement("co:" + intProps);
												aNodeIntProp.appendChild(this.definitionTemplate.createTextNode(intVal));
												aNodeProp.appendChild(aNodeIntProp);
											}
										}
										thePropBlock.appendChild(aNodeProp);
									}

								}

							}
						}
						break;
					}

				}

				/*
				 * Iterator allProps = model.keys(); while (allProps.hasNext())
				 * { String aProp = (String) allProps.next(); log.debug(aProp);
				 * log.debug(jsonType.toString());
				 * 
				 * Element aNodeProp =
				 * this.definitionTemplate.createElement("co:" + aProp); String
				 * proVal = model.getString(aProp); if
				 * (proVal.equals("%%USERINPUT%%")) { JSONObject proDesc = new
				 * JSONObject(); JSONObject proInfo =
				 * jsonType.getJSONObject("props").getJSONObject("properties")
				 * .getJSONObject(aProp);
				 * log.debug("proInfo:"+proInfo.toString());
				 * 
				 * String xpath = "//ns:NodeTemplate[@id='" + nodeId +
				 * "']/ns:Properties/co:" + propertyName + "/co:" + aProp;
				 * proDesc.put(aProp, new JSONObject().put("form",
				 * proInfo).put("xpath", xpath)); log.debug(xpath);
				 * aNodeProp.appendChild(
				 * this.definitionTemplate.createProcessingInstruction(
				 * "userInput", proDesc.toString())); } else {
				 * aNodeProp.appendChild(this.definitionTemplate.createTextNode(
				 * proVal)); } thePropBlock.appendChild(aNodeProp); }
				 */
				nodeTemplate.setAttribute("id", nodeId);
				nodeTemplate.setAttribute("type", nodeType);
				properties.appendChild(thePropBlock);
				nodeTemplate.appendChild(properties);
				nodeTemplate.appendChild(capabilities);

				// Manage Artifacts
				DTMNodeList nodes = null;
				try {
					nodes = (DTMNodeList) this.xpath
							.evaluate(
									"//Nodes/NodeType[@name='" + nodeType
											+ "']/* | //Nodes/NodeTypeImplementation[@nodeType='" + nodeType + "']/*",
									this.documentTypes, XPathConstants.NODESET);

					for (int j = 0; j < nodes.getLength(); j++) {
						log.debug(nodes.item(j).getNodeName());
						if (nodes.item(j).getNodeName().equals("DeploymentArtifacts")) {

							Element depArts = this.definitionTemplate.createElement("DeploymentArtifacts");
							for (int k = 0; k < nodes.item(j).getChildNodes().getLength(); k++) {

								Node templNode = nodes.item(j).getChildNodes().item(k);
								log.debug(templNode.getNodeName());
								if (templNode.getNodeName().equals("DeploymentArtifact")) {
									Element depArt = this.definitionTemplate.createElement("DeploymentArtifact");
									log.debug(String.valueOf(templNode.getAttributes().getLength()));
									log.debug(templNode.toString());
									// depArt.setAttribute("name",
									// templNode.getAttributes().getNamedItem("name").getNodeValue());
									depArt.setAttribute("artifactType",
											templNode.getAttributes().getNamedItem("artifactType").getNodeValue());
									depArt.setAttribute("artifactRef",
											templNode.getAttributes().getNamedItem("artifactRef").getNodeValue());
									depArts.appendChild(depArt);
								}
							}
							nodeTemplate.appendChild(depArts);

						}

					}
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				this.definitionTemplate.getElementsByTagName("TopologyTemplate").item(0).appendChild(nodeTemplate);

			}
			// MANAGE Relationship
			for (int i = 0; i < edgeArr.length(); i++) {
				JSONObject theEdge = edgeArr.getJSONObject(i);
				String edgeType = theEdge.getString("type");
				String edgeId = theEdge.getString("id");
				String edgeSource = theEdge.getString("source");
				String edgeTarget = theEdge.getString("target");
				Element edgeTemplate = this.definitionTemplate.createElement("RelationshipTemplate");
				edgeTemplate.setAttribute("id", edgeId);
				edgeTemplate.setAttribute("type", edgeType);
				Element sourceElement = this.definitionTemplate.createElement("SourceElement");
				String refSource = null;
				for (int j = 0; j < nodeArr.length(); j++) {
					if (nodeArr.getJSONObject(j).getInt("id") == Integer.parseInt(edgeSource)) {
						refSource = nodeArr.getJSONObject(j).getString("name");
						break;
					}
				}
				sourceElement.setAttribute("ref", refSource);
				Element targetElement = this.definitionTemplate.createElement("TargetElement");
				String refTarget = null;
				for (int j = 0; j < nodeArr.length(); j++) {
					if (nodeArr.getJSONObject(j).getInt("id") == Integer.parseInt(edgeTarget)) {
						refTarget = nodeArr.getJSONObject(j).getString("name");
						break;
					}
				}
				targetElement.setAttribute("ref", refTarget);
				edgeTemplate.appendChild(sourceElement);
				edgeTemplate.appendChild(targetElement);
				this.definitionTemplate.getElementsByTagName("TopologyTemplate").item(0).appendChild(edgeTemplate);
			}
		} catch (

		JSONException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.definitionTemplate.setXmlEncoding("UTF-8");

		// log.debug(this.definitionTemplate.saveXML(null));
		String finalModel = this.definitionTemplate.saveXML(null);
		finalModel = finalModel.replace("xmlns=\"\"", "");
		// cycle in the nodes

		try

		{
			FileUtils.writeStringToFile(new File(destDir), finalModel);
		} catch (

		DOMException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (

		IOException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Node generateInteralField(String nodeId, String propertyName, String theProp, String propVal,
			JSONObject proInfo) throws JSONException {
		if (propVal.equals("%%USERINPUT%%")) {
			JSONObject proDesc = new JSONObject();

			String xpath = "//ns:NodeTemplate[@id='" + nodeId + "']/ns:Properties/co:" + propertyName + "/co:"
					+ theProp;
			proDesc.put(theProp, new JSONObject().put("form", proInfo).put("xpath", xpath));
			log.debug(xpath);
			return this.definitionTemplate.createProcessingInstruction("userInput", proDesc.toString());

		} else if (propVal.startsWith("%%SERVPATH")) {
			log.debug(propVal);
			String filename = propVal.substring(10, propVal.length() - 2);
			return this.definitionTemplate.createProcessingInstruction("servPath", filename);
		} else if (propVal.startsWith("%%SERVURL")) {
			log.debug(propVal);
			String filename = propVal.substring(10, propVal.length() - 2);
			return this.definitionTemplate.createProcessingInstruction("servUrl", filename);
		} else {
			return this.definitionTemplate.createTextNode(propVal);
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
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:NodeTemplate", document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the RelationshipTemplate
		DTMNodeList relations = null;
		try {
			relations = (DTMNodeList) this.xpath.evaluate("//ns:RelationshipTemplate[@type='hostedOn']", document,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			log.debug("PROBLEMA IN PATH");
			log.debug(e.getMessage());
			e.printStackTrace();
		}

		// Now we create the Graph structure so we know the correct traversal
		// ordering
		ArrayList<String> values = new ArrayList<String>();
		DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		for (int i = 0; i < nodes.getLength(); ++i) {
			// values.add(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getFirstChild().getNodeValue());
			// System.out.println(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
			g.addVertex(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
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
			String sourceElement = null;
			String targetElement = null;
			for (int r = 0; r < nl.getLength(); r++) {
				Node el = nl.item(r);
				switch (el.getNodeName()) {
				case "SourceElement":
					sourceElement = el.getAttributes().getNamedItem("ref").getNodeValue();
					break;
				case "TargetElement":
					targetElement = el.getAttributes().getNamedItem("ref").getNodeValue();
					break;
				default:
					break;
				}

			}
			log.debug(targetElement);
			log.debug(sourceElement);
			g.addEdge(sourceElement, targetElement);
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

	public void removeToscaCustomization(String customizationId) {
		this.graphHash.remove(customizationId);
		this.xdocHash.remove(customizationId);
		return;
	}

	public boolean validateToscaCsar(String csarPath) throws ToscaException {
		boolean isValid = true;
		if (csarPath.isEmpty()) {
			throw new ToscaException("File not good");
		}
		// Perform validation here and change the value of isValid accordingly
		return isValid;
	}

	public byte[] getToscaGraph(String customizationId) {
		log.debug("in getToscaGraph");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		return null;

	}

	public String getOperationForNode(String customizationId, String id, String interfaceType) {
		log.debug("in getOperationForNode");
		log.debug("customizationId:" + customizationId);
		log.debug("id:" + id);
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		// log.debug(theDoc.saveXML(null));
		DTMNodeList nodes = null;
		// System.out.println("//ns:NodeType[@name=string(//ns:NodeTemplate[@id='"
		// + id + "']/@type)]/ns:Interfaces/ns:Interface[@name='" +
		// interfaceType + "']/ns:Operation/@name");
		/*
		 * String xq = "//ns:NodeType[@name=string(//ns:NodeTemplate[@id='" + id
		 * + "']/@type)]/ns:Interfaces/ns:Interface[@name='" + interfaceType +
		 * "']/ns:Operation/@name";
		 */
		String xq = "//ns:NodeType[@name=string(//ns:NodeTemplate[@id='" + id
				+ "']/@type)]/ns:Interfaces/ns:Interface[@name='" + interfaceType + "']/ns:Operation/@name";
		log.debug("xq:" + xq);
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(xq, theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			log.debug("PROBLEMA IN PATH");
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		log.debug("nodes:" + new Integer(nodes.getLength()).toString());
		String template = nodes.item(0).getNodeValue();
		return template;
	}

	public DTMNodeList getNodesByType(String customizationId, String type) {
		log.debug("in getNodesByType");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		log.debug("type:" + type);
		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:NodeTemplate[@type='" + type + "']", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			log.debug("PROBLEMA IN PATH");
			log.debug(e.getMessage());
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
	public void manageToscaCsar(String customizationId, String service, String serviceHome, String provider,
			String toscaCsarPath) {
		log.debug("in manageToscaCsar");
		String fileName = service + ".czar";
		String path = "/cloudOptingData/";

		csarUtils.unzipToscaCsar(toscaCsarPath, serviceHome + "/tosca");
		/*
		 * try { toscaUtils.unzip(path + service + ".czar", serviceHome +
		 * "/tosca"); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
	}

	public HashMap<String, String> getCloudData(String customizationId) {
		HashMap<String, String> retData = new HashMap<String, String>();
		retData.put("cpu", "1");
		retData.put("mamory", "1");
		retData.put("disk", "1");

		return retData;

	}

	public ArrayList<String> getArrNodesByType(String customizationId, String type) {
		DTMNodeList nodes = getNodesByType(customizationId, type);
		ArrayList<String> retList = new ArrayList<String>();
		System.out.println("before cycle");
		for (int i = 0; i < nodes.getLength(); ++i) {
			retList.add(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue());
		}
		return retList;
	}

	public void getRootNode(String customizationId) {
		// getNodesByType("VMhost");
		return;
	}

	public String getTemplateForNode(String customizationId, String id, String templateType) {
		log.debug("in getTemplateForNode");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		log.debug("//ArtifactTemplate[@id=string(//NodeTemplate[@id='" + id
				+ "']/DeploymentArtifacts/DeploymentArtifact[@artifactType='" + templateType
				+ "']/@artifactRef)]/ArtifactReferences/ArtifactReference/@reference");
		try {

			nodes = (DTMNodeList) this.xpath.evaluate(
					"//ns:ArtifactTemplate[@id=string(//ns:NodeTemplate[@id='" + id
							+ "']/ns:DeploymentArtifacts/ns:DeploymentArtifact[@artifactType='" + templateType
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
			modules = (DTMNodeList) this.xpath.evaluate(
					"//ns:NodeTypeImplementation/ns:DeploymentArtifacts/ns:DeploymentArtifact[@artifactType='PuppetModule']/@artifactRef",
					theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			log.debug("PROBLEMA IN PATH");
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		ArrayList<String> modulesList = new ArrayList<String>();

		for (int i = 0; i < modules.getLength(); ++i) {
			String module = modules.item(i).getNodeValue();
			modulesList.add(module);
		}

		return modulesList;
	}

	public HashMap<String, String> getPuppetModulesProperties(String customizationId, String module) {
		log.info("in getPuppetModulesProperties");
		log.info("module" + module);
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		DTMNodeList nodes = null;
		// System.out.println("//ArtifactTemplate[@id='" + module +
		// "']/Properties/*");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:ArtifactTemplate[@id='" + module + "']/ns:Properties/*",
					theDoc, XPathConstants.NODESET);
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

	public HashMap getPropertiesForNode(String customizationId, String id) {
		log.debug("in getPropertiesForNode with id: " + id);
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		// System.out.println("//NodeTemplate[@id='" + id + "']/Properties/*");
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:NodeTemplate[@id='" + id + "']/ns:Properties/*", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap myHash = new HashMap();
		NodeList props = nodes.item(0).getChildNodes();
		log.debug(props.toString());

		for (int i = 0; i < props.getLength(); ++i) {
			String[] keys = props.item(i).getNodeName().split(":");
			String key = "";
			log.debug("value of keys -----------");
			log.debug(keys.toString());
			if (keys.length > 1) {
				key = keys[1];
			}
			log.debug("props.item(i):");
			log.debug(props.item(i).toString());
			log.debug("props.item(i).getFirstChild():");
			// log.debug(props.item(i).getFirstChild().toString());
			if (props.item(i).getFirstChild() != null) {
				if (props.item(i).getFirstChild().getNodeType() == Node.TEXT_NODE) {
					log.debug("HAS CHILD TEXT NODES ----------------------*****");
					myHash.put(key, props.item(i).getTextContent());
				} else {
					log.debug("HAS CHILD ELEMENT NODES *********++++++++++*****");
					ArrayList myArrChild = null;
					if (myHash.containsKey(key)) {
						myArrChild = (ArrayList) myHash.get(key);
					} else {
						myArrChild = new ArrayList();
					}
					HashMap myHashChild = new HashMap<>();
					for (int c = 0; c < props.item(i).getChildNodes().getLength(); c++) {
						String[] keysChild = props.item(i).getChildNodes().item(c).getNodeName().split(":");
						String keyChild = null;
						if (keysChild.length > 1) {
							keyChild = keysChild[1];
							log.debug("keyChild:" + keyChild);
						}
						myHashChild.put(keyChild, props.item(i).getChildNodes().item(c).getTextContent());
					}
					myArrChild.add(myHashChild);
					myHash.put(key, myArrChild);
				}
			}

		}
		log.debug(myHash.toString());
		return myHash;
	}

	public HashMap getPropertiesForNodeApplication(String customizationId, String id) {
		log.debug("in getPropertiesForNodeApplication");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:NodeTemplate[@id='" + id + "']/ns:Properties/*", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> myHash = new HashMap<String, String>();
		NodeList props = nodes.item(0).getChildNodes();
		for (int i = 0; i < props.getLength(); ++i) {
			String key = props.item(i).getAttributes().getNamedItem("name").getNodeValue();
			myHash.put(key, props.item(i).getTextContent());
		}
		return myHash;
	}

	public ArrayList<String> getChildrenOfNode(String customizationId, String node) {
		log.debug("in getChildrenOfNode");
		DefaultDirectedGraph<String, DefaultEdge> graph = this.graphHash.get(customizationId);
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

	public ArrayList<String> getAllChildrenOfNode(String customizationId, String node) {
		log.debug("in getAllChildrenOfNode");
		ArrayList<String> children = new ArrayList<String>();
		children = getChildrenOfNode(customizationId, node);
		Iterator<String> child = children.iterator();
		ArrayList<String> returnChildren = new ArrayList<String>();
		while (child.hasNext()) {
			String theChild = child.next();
			returnChildren.addAll(getAllChildrenOfNode(customizationId, theChild));
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
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:NodeTemplate[@id='" + id + "']", theDoc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		// We need to get the type
		String type = nodes.item(0).getAttributes().getNamedItem("type").getNodeValue();
		return type;
	}

	public String getServiceName(String customizationId) {
		log.info("in getServiceName");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;
		DTMNodeList nodes = null;

		try {
			nodes = (DTMNodeList) this.xpath.evaluate("//ns:ServiceTemplate/@id", theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			log.debug("PROBLEMA IN PATH");
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		// since there is a single ID we are sure that the array is with a
		// single element
		String serviceName = nodes.item(0).getNodeValue();
		log.debug("serviceName:" + serviceName);
		return serviceName;
	}

	public ArrayList<String> getExposedPortsOfChildren(String customizationId, String id) {
		log.debug("in getExposedPortsOfChildren");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		ArrayList<String> exPorts = new ArrayList<String>();
		ArrayList<String> allChildren = getAllChildrenOfNode(customizationId, id);
		Iterator<String> aChild = allChildren.iterator();
		log.debug("all children" + allChildren.toString());
		ArrayList<String> xPathExprList = new ArrayList<String>();
		while (aChild.hasNext()) {
			xPathExprList.add(
					"//ns:NodeTemplate[@id='" + aChild.next() + "']/ns:Capabilities/ns:Capability/ns:Properties/*");
		}
		String xPathExpr = StringUtils.join(xPathExprList, "|");
		log.debug("xpath :" + xPathExpr);
		// since child nodes could not have ports to expose xpath could be ampty
		if (!xPathExpr.isEmpty()) {
			DTMNodeList nodes = null;
			try {
				nodes = (DTMNodeList) this.xpath.evaluate(xPathExpr, theDoc, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < nodes.getLength(); ++i) {
				exPorts.add(nodes.item(i).getTextContent());
			}
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
			links = (DTMNodeList) this.xpath
					.evaluate("//ns:RelationshipTemplate[@type='containerLink']/ns:SourceElement[@ref='" + id
							+ "']/../ns:TargetElement", theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> linksList = new ArrayList<String>();

		for (int i = 0; i < links.getLength(); ++i) {
			String link = links.item(i).getAttributes().getNamedItem("ref").getNodeValue();
			linksList.add(link);
		}

		return linksList;
	}

	public ArrayList<String> getVolumesFrom(String customizationId, String id) {
		log.debug("in getVolumesFrom");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		DTMNodeList volumesFroms = null;
		try {
			volumesFroms = (DTMNodeList) this.xpath
					.evaluate("//ns:RelationshipTemplate[@type='volumeFrom']/ns:SourceElement[@ref='" + id
							+ "']/../ns:TargetElement", theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> volumesFromList = new ArrayList<String>();

		for (int i = 0; i < volumesFroms.getLength(); ++i) {
			String volumesFrom = volumesFroms.item(i).getAttributes().getNamedItem("ref").getNodeValue();
			volumesFromList.add(volumesFrom);
		}

		return volumesFromList;
	}

	public ArrayList<HashMap> getVolumes(String customizationId, String id) {
		log.debug("in getVolumes");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		HashMap props = getPropertiesForNode(customizationId, id);
		ArrayList<HashMap> volumesList = null;
		if (props.containsKey("volumes")) {
			return (ArrayList<HashMap>) props.get("volumes");
		}

		// ArrayList<HashMap> volumesList = new ArrayList<HashMap>();

		return null;
	}

	public String getLogType(String customizationId, String id) {
		log.debug("in getLogType");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		HashMap props = getPropertiesForNode(customizationId, id);
		String logType = null;
		if (props.containsKey("logtype")) {
			return (String) props.get("logtype");
		}

		// ArrayList<HashMap> volumesList = new ArrayList<HashMap>();

		return null;
	}

	public ArrayList<String> getContainerPorts(String customizationId, String id) {
		log.debug("in getContainerPorts");
		DocumentImpl theDoc = this.xdocHash.get(customizationId);
		if (theDoc == null)
			return null;

		ArrayList<String> ports = new ArrayList<String>();
		String xPathExpr = new String("//ns:NodeTemplate[@id='" + id
				+ "']/ns:Capabilities/ns:Capability[@type='DockerContainerPortsCaps']/ns:Properties/co:DockerContainerPorts");

		DTMNodeList nodes = null;
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(xPathExpr, theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("nodes :" + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); ++i) {
			if (nodes.item(i).getChildNodes().getLength() > 0) {
				String portInfo = nodes.item(i).getLastChild().getTextContent() + ":"
						+ nodes.item(i).getFirstChild().getTextContent();
				ports.add(portInfo);
				System.out.println("portInfo :" + portInfo);
			}
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
				"//ns:NodeTemplate[@type='DockerContainer']/ns:Capabilities/ns:Capability[@type='DockerContainerPortsCaps']/ns:Properties/co:DockerContainerPorts");
		// System.out.println("xpath :" + xPathExpr);

		DTMNodeList nodes = null;
		try {
			XPathExpression expr = this.xpath.compile(xPathExpr);

			nodes = (DTMNodeList) this.xpath.evaluate(xPathExpr, theDoc, XPathConstants.NODESET);
			nodes = (DTMNodeList) expr.evaluate(theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("nodes :" + nodes.getLength());
		for (int i = 0; i < nodes.getLength(); ++i) {
			// interested only in the host port that is the last
			// since the container could have no attributes we need before to
			// chack if there are children
			if (nodes.item(i).getChildNodes().getLength() > 0) {
				String portInfo = nodes.item(i).getLastChild().getTextContent();
				log.debug("portInfo:" + portInfo);
				log.debug("nodename:" + nodes.item(i).getLastChild().getNodeName());
				log.debug("nodetype:" + nodes.item(i).getLastChild().getNodeType());
				log.debug("textcontent:" + nodes.item(i).getLastChild().getTextContent());
				ports.add(portInfo);
				// System.out.println("portInfo :" + portInfo);

			}
		}
		return ports;
	}

	/*
	 * public void getPuppetModules(String customizationId, String id){ // here
	 * I get the puppet module list and use r10k to download them log.debug(
	 * "in getHostPorts"); DocumentImpl theDoc =
	 * this.xdocHash.get(customizationId); if (theDoc == null) return null;
	 * 
	 * }
	 */
	public void runR10k(String customizationId, String serviceHome, String coRoot) {
		log.debug("in getDefinitionFile");
		final long r10kJobTimeout = 95000;
		final boolean r10kInBackground = true;
		String puppetFile = serviceHome + "/Puppetfile";
		String puppetDir = coRoot + "/puppet/modules";
		log.debug("puppetFile:" + puppetFile);
		log.debug("puppetDir:" + puppetDir);

		R10kResultHandler r10kResult = toscaUtils.runR10k(puppetFile, puppetDir, r10kJobTimeout, r10kInBackground,
				serviceHome);

		try {
			r10kResult.waitFor();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;

	}

	public void generateDockerCompose(String customizationId, String organizationName, String serviceHome,
			ArrayList<String> dockerNodesList, ArrayList<String> dockerDataVolumeNodesList) {
		ArrayList<HashMap<String, Object>> modData = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> modVolData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> contImages = new HashMap<String, String>();
		for (String node : dockerNodesList) {
			HashMap<String, Object> containerData = new HashMap<String, Object>();
			String imageName = "cloudopting/" + organizationName + "_" + node.toLowerCase();
			contImages.put(node, imageName);
			containerData.put("container", node);
			containerData.put("image", imageName);
			log.debug("dockerNodesList element working on: " + node);
			String logType = getLogType(customizationId, node);
			containerData.put("logtype", logType);
			containerData.put("log_driver_address", logger_address);
			// modData.add(toscaFileManager.getPuppetModulesProperties(mod));
			// get the link information for the node

			ArrayList<String> links = getContainerLinks(customizationId, node);
			if (links != null && !links.isEmpty()) {
				containerData.put("links", "   - " + StringUtils.join(links, "\n   - "));
			}
			ArrayList<String> exPorts = getExposedPortsOfChildren(customizationId, node);
			if (exPorts != null && !exPorts.isEmpty()) {
				containerData.put("exPorts", "   - \"" + StringUtils.join(exPorts, "\"\n   - \"") + "\"");
			}
			ArrayList<String> ports = getContainerPorts(customizationId, node);
			if (ports != null && !ports.isEmpty()) {
				containerData.put("ports", "   - \"" + StringUtils.join(ports, "\"\n   - \"") + "\"");
			}
			ArrayList<String> volumesFrom = getVolumesFrom(customizationId, node);
			if (volumesFrom != null && !volumesFrom.isEmpty()) {
				containerData.put("volumesFrom", "   - " + StringUtils.join(volumesFrom, "\n   - "));
			}
			ArrayList<HashMap> volumes = getVolumes(customizationId, node);
			if (volumes != null && !volumes.isEmpty()) {
				containerData.put("volumes", volumes);
			}

			System.out.println(node);
			modData.add(containerData);
		}
		System.out.println(modData.toString());
		for (String node : dockerDataVolumeNodesList) {
//			HashMap<String, Object> containerDataVolumeData = new HashMap<String, Object>();
			HashMap containerDataVolumeData = this.getPropertiesForNode(customizationId, node);
			containerDataVolumeData.put("container", node);
			String image = (String) containerDataVolumeData.get("image");
			if (image.startsWith("%%CONTAINERIMAGE")) {
				log.debug(image);
				String contImage = image.substring(16, image.length() - 2);
				contImage = contImage.trim();
				containerDataVolumeData.put("image", contImages.get(contImage));
			}
			
			modVolData.add(containerDataVolumeData);
		}

		HashMap<String, Object> templData = new HashMap<String, Object>();
		templData.put("dockerContainers", modData);
		templData.put("dockerDataVolumeContainers", modVolData);
		// write the "Puppetfile" file
		toscaUtils.generateDockerCompose(templData, serviceHome);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUBSCRIBER') or @toscaAuthorization.hasReadCustomizationPermission(#idApp)")
	public JSONObject getCustomizationFormData(Long idApp, String csarPath) {

		return customizationUtils.getCustomizationFormData(idApp, csarPath);

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUBSCRIBER') or @toscaAuthorization.hasWriteCustomizationPermission(#idApp)")
	public String generateCustomizedTosca(Long idApp, String csarPath, JSONObject data, String organizationkey, String serviceName) {
		return customizationUtils.generateCustomizedTosca(idApp, csarPath, data, organizationkey, serviceName);

	}
	
	static class ErrorHandlerImpl implements ErrorHandler {

		/**
		 *
		 * @param sAXParseException
		 * @throws SAXException
		 */
		public void error(SAXParseException sAXParseException) throws SAXException {
			System.out.println(sAXParseException);
		}

		/**
		 *
		 * @param sAXParseException
		 * @throws SAXException
		 */
		public void fatalError(SAXParseException sAXParseException) throws SAXException {
			System.out.println(sAXParseException);
		}

		/**
		 *
		 * @param sAXParseException
		 * @throws SAXException
		 */
		public void warning(org.xml.sax.SAXParseException sAXParseException) throws org.xml.sax.SAXException {
			System.out.println(sAXParseException);
		}

	}
}
