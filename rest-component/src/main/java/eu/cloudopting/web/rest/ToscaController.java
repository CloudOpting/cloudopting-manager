package eu.cloudopting.web.rest;

import javax.annotation.security.RolesAllowed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.security.AuthoritiesConstants;
import eu.cloudopting.tosca.ToscaService;

/**
 * REST controller for managing tosca
 */
@RestController
@RequestMapping("/api")
public class ToscaController {
	private final Logger log = LoggerFactory.getLogger(ToscaController.class);

	@Autowired
	private ToscaService ts;
	
    @RequestMapping(value = "/toscaGraph/{id}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE)
    @RolesAllowed(AuthoritiesConstants.ANONYMOUS)
    @ResponseBody byte[] getToscaGraph(@PathVariable String id) {
        log.info("REST request to get graph of tosca id : {}", id);
        return ts.getToscaGraph(id);
    }

    @RequestMapping(value = "/toscaEntity/{id}",
            method = RequestMethod.GET)
    @ResponseBody String getEntity(@PathVariable String id) {
        log.info("REST request to get entity for dyn form of tosca id : {}", id);
        JSONObject entity = new JSONObject();
        try {
			entity.put("name", "Sam");
	        entity.put("country", 2);
	        entity.put("sla", 0);
	        entity.put("licenceAgreement", true);
	        entity.put("description", "I like TOSCA");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return entity.toString();
    }

    @RequestMapping(value = "/toscaCustomization/{id}",
            method = RequestMethod.GET)
    @ResponseBody String getCustomization(@PathVariable String id) {
        log.info("REST request to get customization of dyn form of tosca id : {}", id);
        JSONArray customization = null;
        try {
        	customization = new JSONArray("[{\"name\": \"name\",	\"title\": \"Name\",	\"required\": true,	\"type\": {	\"view\": \"input\"	}},{\"name\": \"sla\",\"title\": \"SLA\",\"type\": {\"view\": \"select\",\"options\": [{\"id\": 0,\"name\": \"BigCity\"},{\"id\": 1,\"name\": \"SmallCity\"}]}},{\"name\": \"country\",\"title\": \"Country\",\"type\": {\"view\": \"select\",\"options\": [{\"id\": 0,\"name\": \"USA\"},{\"id\": 1,\"name\": \"German\"},{\"id\": 2,\"name\": \"Russia\"}]},{\"name\": \"licenceAgreement\",\"title\": \"LicenceAgreement\",\"type\": {\"view\": \"checkbox\"}},{\"name\": \"description\",\"title\": \"Description\",\"type\": {\"view\": \"textarea\"}}]");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return customization.toString();
    }
}
