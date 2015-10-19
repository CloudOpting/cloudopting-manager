package eu.cloudopting.tosca.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.xalan.extensions.XPathFunctionResolverImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.apache.xpath.jaxp.XPathImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


@Service
public class CustomizationUtils {
	private final Logger log = LoggerFactory.getLogger(CustomizationUtils.class);
	
	private DocumentBuilderImpl db;
	private XPathImpl xpath;

	

	
	@Autowired
	private CSARUtils csarUtils;

	private HashMap<Long, DocumentImpl> xToscaHash = new HashMap<Long, DocumentImpl>();

	
	public CustomizationUtils() {
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
	
	public JSONObject getCustomizationFormData(Long idApp, String csarPath){
		JSONObject jret = null;
		try {
			jret = new JSONObject("{\"type\": \"object\",\"title\": \"Compute\",\"properties\": {\"idApp\":  {\"title\": \"Application ID\",\"type\": \"string\"}}}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DocumentImpl theDoc = this.xToscaHash.get(idApp);
		if (theDoc == null){
			// need to read the Application and get the TOSCA file
//			Applications application = applicationService.findOne(idApp);
//			String csarPath = application.getApplicationToscaTemplate();
			log.debug("path to csar:"+csarPath);
			// unzip the csar
			String destinationPath = "/cloudOptingData/"+idApp;
			csarUtils.unzipToscaCsar(csarPath, destinationPath);
			// read the definition file
			String xmlDefinitionContent = csarUtils.getDefinitionFile(destinationPath);
			InputSource source = new InputSource(new StringReader(xmlDefinitionContent));
			DocumentImpl document = null;
			try {
				document = (DocumentImpl) this.db.parse(source);
				this.xToscaHash.put(idApp, document);
				FileUtils.forceDelete(new File(destinationPath));
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else{
		
		}
		
		// maybe keeping a hash for it so sequent calls can go faster (there will be the instance generation)
		
//		csarUtils.getToscaTemplate("csisp/Clearo.czar", "/cloudOptingData/");
		// TODO dummy data return for now
		try {
			jret = new JSONObject("{\"type\": \"object\",\"title\": \"Compute\",\"properties\": {\"node_id\":  {\"title\": \"Node ID\",\"type\": \"string\"},\"node_label\":  {\"title\": \"Node Label\",\"type\": \"string\",\"description\": \"Email will be used for evil.\"},\"memory\":  {\"title\": \"Memory\",\"type\": \"string\",\"enum\": [\"512\",\"1024\",\"2048\"]},\"cpu\": {\"title\": \"CPU\",\"type\": \"integer\",\"maxLength\": 20,\"validationMessage\": \"Dont be greedy!\"}},\"required\": [\"node_id\",\"node_label\",\"memory\", \"cpu\"]}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jret;
	
	}

}
