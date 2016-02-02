package eu.cloudopting.web.rest;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import eu.cloudopting.service.CustomizationStatusService;
import eu.cloudopting.service.MailService;
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

	@Autowired
	CustomizationStatusService customizationStatusService;

	@Autowired
	MailService mailService;

	@RequestMapping(value = "/application/{idApp}/getSizings", method = RequestMethod.GET)
	public void getSizing(@PathVariable("appId") final Long id) {

	}

	@RequestMapping(value = "/application/{idApp}/getCustomizationForm", method = RequestMethod.GET)
	@ResponseBody
	public String getCustomizationForm(@PathVariable("idApp") final Long idApp) {
		log.debug("in getCustomizationForm");
		log.debug(idApp.toString());
		// JSONObject jret = new JSONObject("{\"type\": \"object\",\"title\":
		// \"Compute\",\"properties\": {\"node_id\": {\"title\": \"Node
		// ID\",\"type\": \"string\"},\"node_label\": {\"title\": \"Node
		// Label\",\"type\": \"string\",\"description\": \"Email will be used
		// for evil.\"},\"memory\": {\"title\": \"Memory\",\"type\":
		// \"string\",\"enum\": [\"512\",\"1024\",\"2048\"]},\"cpu\":
		// {\"title\": \"CPU\",\"type\": \"integer\",\"maxLength\":
		// 20,\"validationMessage\": \"Dont be greedy!\"}},\"required\":
		// [\"node_id\",\"node_label\",\"memory\", \"cpu\"]}");
		Applications application = applicationService.findOne(idApp);
		String csarPath = application.getApplicationToscaTemplate();
		JSONObject jret = toscaService.getCustomizationFormData(idApp, csarPath);
		log.debug("the final json");
		log.debug(jret.toString());
		return jret.toString();

	}

	@RequestMapping(value = "/application/{idApp}/sendCustomizationForm", method = RequestMethod.POST)
	public Customizations postCustomizationForm(@PathVariable("idApp") final Long idApp,
			@RequestParam(value = "formData") String formData, HttpServletRequest request,
			HttpServletResponse response) {
		log.debug("in postCustomizationForm");
		log.debug(idApp.toString());
		log.debug(formData);
		JSONObject jsonData = null;
		
		try {
			jsonData = new JSONObject(formData);
			log.debug("co_buy_platform: "+jsonData.getString("co_buy_platform"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User user = userService.loadUserByLogin(request.getUserPrincipal().getName());
		// Long orgId = user.getOrganizationId().getId()
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
		newC.setStatusId(customizationStatusService.findOne(100));
		// TODO Check this is correct
		log.debug(formData);
		log.debug(jsonData.toString());
		newC.setCustomizationFormValue(formData);

		customizationService.create(newC);

		// send mail only if user is not of the organization
		if (user.getOrganizationId().getId() != application.getOrganizationId().getId()) {
			// here we need to create the mail and send it to the SP and sub.
			// Here I need mail of the sub
			String mailSub = user.getOrganizationId().getEmail();
			// than the mail of the organization
			String mailSp = application.getOrganizationId().getEmail();

			HashMap<String, Object> mailData = new HashMap<String, Object>();
			mailData.put("serviceName", application.getApplicationName());
			mailData.put("serviceOrganization", application.getOrganizationId().getOrganizationName());
			mailData.put("serviceProviderMail", mailSp);
			mailData.put("serviceOrganizationContact", application.getOrganizationId().getContactRepresentative());
			mailData.put("serviceOrganizationContactPhone", application.getOrganizationId().getContactPhone());
			mailData.put("subscriberFirstName", user.getFirstName());
			mailData.put("subscriberLastName", user.getLastName());
			mailData.put("subscriberMail", mailSub);
			mailData.put("subscriberOrganization", user.getOrganizationId().getOrganizationName());

			String subjectSub = "Thanks for subscribing to "+application.getApplicationName();
			String templateSub = application.getApplicationSpMail();
			String subjectProv = "You have a new subscription for "+application.getApplicationName();
			String templateProv = application.getApplicationSubscriberMail();
			// than getting the template

			// compile the template

			// send the template as mail to subscriber
			mailService.sendTemplatedMail(mailSub, subjectSub, templateSub, mailData, false, true);
			// send the template as mail to service provider
			mailService.sendTemplatedMail(mailSp, subjectProv, templateProv, mailData, false, true);
		}

		response.setStatus(HttpServletResponse.SC_OK);
		return newC;
	}
}
