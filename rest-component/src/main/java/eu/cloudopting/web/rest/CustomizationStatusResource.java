package eu.cloudopting.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.CustomizationStatus;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.CustomizationStatusService;

@RestController
@RequestMapping("/api")
public class CustomizationStatusResource extends AbstractController<CustomizationStatus> {

	@Inject
	private CustomizationStatusService customizationStatusService;
	
	public CustomizationStatusResource() {
		super(CustomizationStatus.class);
	}
	
	@RequestMapping(value = "/customizationStatus", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<CustomizationStatus>> findAll() {
		List<CustomizationStatus> customizationStatusList = getService().findAll();
		return new ResponseEntity<>(customizationStatusList, HttpStatus.OK);
	}
	
	@Override
	protected BaseService<CustomizationStatus> getService() {
		return customizationStatusService;
	}
}
