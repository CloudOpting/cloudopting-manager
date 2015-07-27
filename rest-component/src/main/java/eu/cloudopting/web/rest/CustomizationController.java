package eu.cloudopting.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CustomizationController {
	private final Logger log = LoggerFactory.getLogger(CustomizationController.class);
	
	@RequestMapping(value = "/application/{idApp}/getSizings",
            method = RequestMethod.GET)
	public void getSizing(@PathVariable("appId") final Long id){
		
	}
	
	@RequestMapping(value = "/application/{idApp}/getCustomizationForm",
            method = RequestMethod.GET)
	public void getCustomizationForm(@PathVariable("appId") final Long id){
		/*
		{
    "type": "object",
    "title": "Compute",
    "properties": {
      "node_id":  {
        "title": "Node ID",
        "type": "string"
      },
      "node_label":  {
        "title": "Node Label",
        "type": "string",
        "pattern": "^\\S+@\\S+$",
        "description": "Email will be used for evil."
      },
      "memory":  {
        "title": "Memory",
        "type": "string",
        "enum": ["512","1024","2048"]
      },
      "cpu": {
        "title": "CPU",
        "type": "integer",
        "maxLength": 20,
        "validationMessage": "Don't be greedy!"
      }
    },
    "required": ["node_id","node_label","memory", "cpu"]
  }
  */
	}

}
