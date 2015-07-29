package eu.cloudopting.tosca.utils;

import java.util.HashMap;

import org.apache.xerces.dom.DocumentImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.service.ApplicationService;

@Service
public class CustomizationUtils {
	private final Logger log = LoggerFactory.getLogger(CustomizationUtils.class);
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private CSARUtils csarUtils;

	private HashMap<String, DocumentImpl> xToscaHash = new HashMap<String, DocumentImpl>();

	
	public JSONObject getCustomizationFormData(Long idApp){
		JSONObject jret = null;
		try {
			jret = new JSONObject("{\"type\": \"object\",\"title\": \"Compute\",\"properties\": {\"idApp\":  {\"title\": \"Application ID\",\"type\": \"string\"}}}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// need to read the Application and get the TOSCA file
		Applications application = applicationService.findOne(idApp);

		String csarPath = application.getApplicationToscaTemplate();
		log.debug("path to csar:"+csarPath);
		// maybe keeping a hash for it so sequent calls can go faster (there will be the instance generation)
		
		csarUtils.getToscaTemplate(csarPath, "/");
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
