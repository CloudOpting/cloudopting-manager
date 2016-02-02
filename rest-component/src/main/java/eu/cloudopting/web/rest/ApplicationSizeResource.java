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

import eu.cloudopting.domain.ApplicationSize;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.ApplicationSizeService;

@RestController
@RequestMapping("/api")
public class ApplicationSizeResource extends AbstractController<ApplicationSize> {

	@Inject
	private ApplicationSizeService applicationSizeService;
	
	public ApplicationSizeResource() {
		super(ApplicationSize.class);
	}

	@RequestMapping(value = "/applicationSize", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<ApplicationSize>> findAll() {
		List<ApplicationSize> applicationSizeList = getService().findAll();
		return new ResponseEntity<>(applicationSizeList, HttpStatus.OK);
	}
	
	@Override
	protected BaseService<ApplicationSize> getService() {
		return applicationSizeService;
	}
}
