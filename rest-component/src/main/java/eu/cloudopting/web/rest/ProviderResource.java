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

import eu.cloudopting.domain.Providers;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.ProviderService;

@RestController
@RequestMapping("/api")
public class ProviderResource extends AbstractController<Providers> {

	@Inject
	private ProviderService providerService;
	
	public ProviderResource() {
		super(Providers.class);
	}

	@RequestMapping(value = "/providers", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<Providers>> findAll() {
		List<Providers> providers = getService().findAll();
		return new ResponseEntity<>(providers, HttpStatus.OK);
	}
	
	@Override
	protected BaseService<Providers> getService() {
		return providerService;
	}
}
