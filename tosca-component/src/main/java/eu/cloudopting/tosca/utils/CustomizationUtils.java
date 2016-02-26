package eu.cloudopting.tosca.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.xalan.extensions.XPathFunctionResolverImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.apache.xpath.jaxp.XPathImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class CustomizationUtils {

	private final Logger log = LoggerFactory.getLogger(CustomizationUtils.class);

	private DocumentBuilderImpl db;
	private XPathImpl xpath;
	private HashMap<Long, DocumentImpl> xToscaHash = new HashMap<Long, DocumentImpl>();

	@Autowired
	private CSARUtils csarUtils;
	
	@Value("${spring.jcr.repo_http}")
	private String jackHttp;

	public CustomizationUtils() {
		super();
		XPathFactoryImpl xpathFactory = (XPathFactoryImpl) XPathFactoryImpl.newInstance();
		this.xpath = (XPathImpl) xpathFactory.newXPath();
		this.xpath.setNamespaceContext(new eu.cloudopting.tosca.xml.coNamespaceContext());

		this.xpath.setXPathFunctionResolver(new XPathFunctionResolverImpl());
		DocumentBuilderFactoryImpl dbf = new DocumentBuilderFactoryImpl();
		dbf.setNamespaceAware(true);

		try {
			this.db = (DocumentBuilderImpl) dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			log.error("ParserConfigurationException in CustomizationUtils.CustomizationUtils");
			e2.printStackTrace();
		}
	}

	public String generateCustomizedTosca(Long idApp, String csarPath, JSONObject data, String organizationkey, String serviceName) {
		log.debug("CustomizationUtils.generateCustomizedTosca starting.");

		DocumentImpl theDoc = new DocumentImpl();
		// DocumentImpl theDoc = null;
		try {
			// theDoc = (DocumentImpl)getToscaTemplateDesc(idApp,
			// csarPath).clone();
			// getToscaTemplateDesc(idApp, csarPath).cloneNode(true);
			theDoc = (DocumentImpl) this.db.newDocument();
			Node an = theDoc.importNode(getToscaTemplateDesc(idApp, csarPath).getDocumentElement(), true);
			theDoc.appendChild(an);
			// theDoc.loadXML(getToscaTemplateDesc(idApp,
			// csarPath).saveXML(null));
			// theDoc = (DocumentImpl) this.db.parse(new InputSource(new
			// ByteArrayInputStream(getToscaTemplateDesc(idApp,
			// csarPath).saveXML(null).getBytes())));
		} catch (DOMException e1) {
			log.error("DOMException in CustomizationUtils.generateCustomizedTosca importing nodes.");
			e1.printStackTrace();
		}
		// log.debug(getToscaTemplateDesc(idApp, csarPath).saveXML(null));
		// theDoc.load(getToscaTemplateDesc(idApp, csarPath).saveXML(null));

		JSONObject properties = getUserInputs(theDoc, "xpath");
		log.debug("CustomizationUtils.generateCustomizedTosca xpath: " + properties.toString());

		Iterator dataNames = data.keys();
		while (dataNames.hasNext()) {
			String dataKey = dataNames.next().toString();
			log.debug("CustomizationUtils.generateCustomizedTosca dataKey: " + dataKey);
			DTMNodeList nodes = null;
			switch (dataKey) {
			case "co_is_trial":
			case "co_buy_platform":
				
				break;
			default:
				try {
					String xPathProcInt = properties.getString(dataKey);
					// xPathProcInt =
					// "//ns:NodeTemplate[@id='ClearoPostgreSQLDB']/ns:Properties/co:PostgreSQLDatabaseProperties/co:password";
					log.debug("CustomizationUtils.generateCustomizedTosca xPathProcInt: " + xPathProcInt);
					nodes = (DTMNodeList) this.xpath.evaluate(xPathProcInt, theDoc, XPathConstants.NODESET);
					log.debug("CustomizationUtils.generateCustomizedTosca nodes length: " + new Integer(nodes.getLength()).toString());
					log.debug("CustomizationUtils.generateCustomizedTosca dataKey: " + data.getString(dataKey));
					nodes.item(0).removeChild(nodes.item(0).getFirstChild());
					nodes.item(0).setTextContent(data.getString(dataKey));
				} catch (XPathExpressionException e) {
					log.error("XPathExpressionException in CustomizationUtils.generateCustomizedTosca.");
					e.printStackTrace();
				} catch (JSONException e) {
					log.error("JSONException in CustomizationUtils.generateCustomizedTosca.");
					e.printStackTrace();
				}
				log.debug("CustomizationUtils.generateCustomizedTosca nodes: " + nodes.toString());
			
				break;
			}
			
			
		}
		theDoc = getServUrl(theDoc, organizationkey, serviceName);
		log.debug("CustomizationUtils.generateCustomizedTosca theDoc: " + theDoc.saveXML(null));
		log.debug("CustomizationUtils.generateCustomizedTosca --------ORIGINAL--------: \n" + getToscaTemplateDesc(idApp, csarPath).saveXML(null));
		return theDoc.saveXML(null);
	}

	public JSONObject getCustomizationFormData(Long idApp, String csarPath) {
		log.debug("CustomizationUtils.getCustomizationFormData starting.");
		JSONObject jret = null;
		try {
			jret = new JSONObject(
					"{\"type\": \"object\",\"title\": \"Compute\",\"properties\": {\"idApp\":  {\"title\": \"Application ID\",\"type\": \"string\"}}}");
		} catch (JSONException e) {
			log.error("JSONException in CustomizationUtils.getCustomizationFormData creating a JSONObject.");
			e.printStackTrace();
		}
		DocumentImpl theDoc = getToscaTemplateDesc(idApp, csarPath);

		JSONObject properties = getUserInputs(theDoc, "form");
		log.debug("CustomizationUtils.getCustomizationFormData properties: " + properties.toString());

		// maybe keeping a hash for it so sequent calls can go faster (there
		// will be the instance generation)

		// csarUtils.getToscaTemplate("csisp/Clearo.czar", "/cloudOptingData/");
		// TODO dummy data return for now
		try {
			/*
			 * jret = new JSONObject(
			 * "{\"type\": \"object\",\"title\": \"Compute\",\"properties\": {\"node_id\":  {\"title\": \"Node ID\",\"type\": \"string\"},\"node_label\":  {\"title\": \"Node Label\",\"type\": \"string\",\"description\": \"Email will be used for evil.\"},\"memory\":  {\"title\": \"Memory\",\"type\": \"string\",\"enum\": [\"512\",\"1024\",\"2048\"]},\"cpu\": {\"title\": \"CPU\",\"type\": \"integer\",\"maxLength\": 20,\"validationMessage\": \"Dont be greedy!\"}},\"required\": [\"node_id\",\"node_label\",\"memory\", \"cpu\"]}"
			 * );
			 */
			jret = new JSONObject("{\"type\": \"object\",\"title\": \"Customize the service\"}");
			log.debug("CustomizationUtils.getCustomizationFormData jret: " + jret.toString());
			log.debug("CustomizationUtils.getCustomizationFormData properties: " + properties.toString());

			properties.put("co_is_trial", new JSONObject("{\"title\":\"Is trial\",\"type\":\"boolean\"}"));
			properties.put("co_buy_platform",
					new JSONObject("{\"title\":\"Buy also the cloud platform?\",\"type\":\"boolean\"}"));

			jret.put("properties", properties);

		} catch (JSONException e) {
			log.error("JSONException in CustomizationUtils.getCustomizationFormData creating a JSONObject.");
			e.printStackTrace();
		}
		return jret;

	}

	private JSONObject getUserInputs(DocumentImpl theDoc, String what) {
		log.debug("CustomizationUtils.getUserInputs starting.");
		log.debug("CustomizationUtils.getUserInputs theDoc: " + theDoc.saveXML(null));

		JSONObject properties = new JSONObject();
		DTMNodeList nodes = null;
		String xPathProcInt = "//processing-instruction('userInput')";
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(xPathProcInt, theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.error("XPathExpressionException in CustomizationUtils.getUserInputs evaluating xpath.");
			log.debug(e.getMessage());
			e.printStackTrace();
		}

		log.debug("CustomizationUtils.getUserInputs nodes with PI: " + new Integer(nodes.getLength()).toString());
		for (int i = 0; i < nodes.getLength(); ++i) {
			log.debug(nodes.item(i).getNodeValue());
			try {
				JSONObject field = new JSONObject(nodes.item(i).getNodeValue());
				String fieldName = field.keys().next().toString();
				log.debug("CustomizationUtils.getUserInputs fieldName: " + fieldName);
				switch (what) {
				case "form":
					properties.put(fieldName, field.getJSONObject(fieldName).getJSONObject(what));
					break;
				default:
					properties.put(fieldName, field.getJSONObject(fieldName).getString(what));
					break;
				}

			} catch (DOMException | JSONException e) {
				log.error("DOMException | JSONException in CustomizationUtils.getUserInputs.");
				e.printStackTrace();
			}

		}
		return properties;
	}

	private DocumentImpl getServUrl(DocumentImpl theDoc, String organizationkey, String serviceName) {
		log.debug("CustomizationUtils.getServUrl starting.");
		log.debug("CustomizationUtils.getServUrl theDoc: " + theDoc.saveXML(null));

		JSONObject properties = new JSONObject();
		DTMNodeList nodes = null;
		String xPathProcInt = "//processing-instruction('servUrl')";
		try {
			nodes = (DTMNodeList) this.xpath.evaluate(xPathProcInt, theDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.error("XPathExpressionException in CustomizationUtils.getServUrl evaluating xpath.");
			log.debug(e.getMessage());
			e.printStackTrace();
		}

		log.debug("CustomizationUtils.getServUrl nodes with PI: " + new Integer(nodes.getLength()).toString());
		for (int i = 0; i < nodes.getLength(); ++i) {
			log.debug("CustomizationUtils.getServUrl Item NodeValue: " + nodes.item(i).getNodeValue() + ", NodeName: " + nodes.item(i).getNodeName());
			Node father = nodes.item(i).getParentNode();
			log.debug("CustomizationUtils.getServUrl Item father NodeName: " + father.getNodeName());
			try {
				String fileName = new String(nodes.item(i).getNodeValue());
				log.debug("CustomizationUtils.getServUrl fileName: " + fileName);
				father.removeChild(nodes.item(i));
				father.setTextContent(jackHttp+organizationkey + "/" + serviceName + "/template/" + fileName);
			} catch (DOMException e) {
				log.error("DOMException in CustomizationUtils.getServUrl.");
				e.printStackTrace();
			}

		}
		return theDoc;
	}

	private DocumentImpl getToscaTemplateDesc(Long idApp, String csarPath) {
		log.debug("CustomizationUtils.getToscaTemplateDesc starting.");

		DocumentImpl theDoc = this.xToscaHash.get(idApp);
		if (theDoc == null) {
			// need to read the Application and get the TOSCA file
			// Applications application = applicationService.findOne(idApp);
			// String csarPath = application.getApplicationToscaTemplate();
			log.debug("CustomizationUtils.getToscaTemplateDesc csarPath: " + csarPath);

			// unzip the csar
			String destinationPath = "/cloudOptingData/" + idApp;
			csarUtils.unzipToscaCsar(csarPath, destinationPath);

			// read the definition file
			String xmlDefinitionContent = csarUtils.getDefinitionFile(destinationPath);
			InputSource source = new InputSource(new StringReader(xmlDefinitionContent));
			DocumentImpl document = null;
			try {
				document = (DocumentImpl) this.db.parse(source);
				this.xToscaHash.put(idApp, document);
				theDoc = document;
				FileUtils.forceDelete(new File(destinationPath));
			} catch (SAXException e1) {
				log.error("SAXException in CustomizationUtils.getToscaTemplateDesc.");
				e1.printStackTrace();
			} catch (IOException e1) {
				log.error("IOException in CustomizationUtils.getToscaTemplateDesc.");
				e1.printStackTrace();
			}

		}
		log.debug("CustomizationUtils.getToscaTemplateDesc theDoc: " + theDoc.saveXML(null));
		return theDoc;
	}

}
