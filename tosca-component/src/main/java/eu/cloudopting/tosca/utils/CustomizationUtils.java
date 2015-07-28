package eu.cloudopting.tosca.utils;

import java.util.HashMap;

import org.apache.xerces.dom.DocumentImpl;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CustomizationUtils {

	private HashMap<String, DocumentImpl> xToscaHash = new HashMap<String, DocumentImpl>();

	
	public JSONObject getCustomizationFormData(Long idApp){
		JSONObject jret = null;
		// need to read the Application and get the TOSCA file
		// maybe keeping a hash for it so sequent calls can go faster (there will be the instance generation)
		return jret;
	
	}

}
