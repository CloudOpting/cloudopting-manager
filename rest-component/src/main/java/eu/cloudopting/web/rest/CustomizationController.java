package eu.cloudopting.web.rest;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.internal.util.privilegedactions.NewJaxbContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.User;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.service.UserService;
import eu.cloudopting.tosca.ToscaService;

@RestController
@RequestMapping("/api")
public class CustomizationController {
	private final Logger log = LoggerFactory.getLogger(CustomizationController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ToscaService toscaService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	CustomizationService customizationService;
	
	@RequestMapping(value = "/application/{idApp}/getSizings",
            method = RequestMethod.GET)
	public void getSizing(@PathVariable("appId") final Long id){
		
	}
	
	@RequestMapping(value = "/application/{idApp}/getCustomizationForm",
            method = RequestMethod.GET)
	@ResponseBody
	public String getCustomizationForm(@PathVariable("idApp") final Long idApp){
		log.debug("in getCustomizationForm");
		log.debug(idApp.toString());
//		JSONObject jret = new JSONObject("{\"type\": \"object\",\"title\": \"Compute\",\"properties\": {\"node_id\":  {\"title\": \"Node ID\",\"type\": \"string\"},\"node_label\":  {\"title\": \"Node Label\",\"type\": \"string\",\"description\": \"Email will be used for evil.\"},\"memory\":  {\"title\": \"Memory\",\"type\": \"string\",\"enum\": [\"512\",\"1024\",\"2048\"]},\"cpu\": {\"title\": \"CPU\",\"type\": \"integer\",\"maxLength\": 20,\"validationMessage\": \"Dont be greedy!\"}},\"required\": [\"node_id\",\"node_label\",\"memory\", \"cpu\"]}");
		Applications application = applicationService.findOne(idApp);
		String csarPath = application.getApplicationToscaTemplate();
		JSONObject jret = toscaService.getCustomizationFormData(idApp, csarPath);
		return jret.toString();
  		
	}

	@RequestMapping(value = "/application/{idApp}/sendCustomizationForm",
            method = RequestMethod.POST)
	public String postCustomizationForm(@PathVariable("idApp") final Long idApp, @RequestParam(value = "formData") String formData,HttpServletRequest request){
		log.debug("in postCustomizationForm");
		log.debug(idApp.toString());
		log.debug(formData);
		JSONObject jsonData = null;;
		try {
			jsonData = new JSONObject(formData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User user = userService.loadUserByLogin(request.getUserPrincipal().getName());
//		Long orgId = user.getOrganizationId().getId()
		Applications application = applicationService.findOne(idApp);
		String csarPath = application.getApplicationToscaTemplate();
		String theTosca = toscaService.generateCustomizedTosca(idApp, csarPath, jsonData);
		
		Customizations newC = new Customizations();
		newC.setApplicationId(idApp);
		newC.setCustomizationToscaFile(theTosca);
		newC.setCustomerOrganizationId(user.getOrganizationId());
		newC.setCustomizationActivation(new Date());
		newC.setCustomizationCreation(new Date());
		newC.setCustomizationDecommission(new Date());
		newC.setStatusId(new Long(100));
		//TODO Check this is correct
		newC.setCustomizationFormValue(formData);
		
		customizationService.create(newC);
		
		return "Customization successfully saved";
	}
}
