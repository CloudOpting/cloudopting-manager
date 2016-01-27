package eu.cloudopting.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.MonitoringInfoElastic;
import eu.cloudopting.dto.MonitoringInfoElasticDTO;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.MonitoringInfoElasticService;

@RestController
@RequestMapping("/api")
public class MonitoringInfoElasticResource extends AbstractController<MonitoringInfoElastic> {

	@Inject
	private MonitoringInfoElasticService monitoringInfoElasticService;
	
	public MonitoringInfoElasticResource() {
		super(MonitoringInfoElastic.class);
	}
	
	@Override
	protected BaseService<MonitoringInfoElastic> getService() {
		return monitoringInfoElasticService;
	}
	
	@RequestMapping(value = "/monitoring/elastic/info/{id}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<MonitoringInfoElastic> findOne(@PathVariable("id") final Long id) {
		MonitoringInfoElastic monitorInfo = getService().findOne(id);
		if(monitorInfo == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(monitorInfo, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/monitoring/elastic/info", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<MonitoringInfoElastic>> findAll() {
		List<MonitoringInfoElastic> monitorInfo = getService().findAll();
		return new ResponseEntity<>(monitorInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/monitoring/elastic/info/list/{customizationId}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<MonitoringInfoElastic>> findByCustomizationId(
			@PathVariable("customizationId") final Long customizationId) {
		List<MonitoringInfoElastic> monitorInfo = ((MonitoringInfoElasticService)getService()).findByCustomizationId(customizationId);
		return new ResponseEntity<>(monitorInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/monitoring/elastic/info", method = RequestMethod.POST)
	public final ResponseEntity<MonitoringInfoElastic> create(@RequestBody MonitoringInfoElasticDTO monitoringInfoElasticDTO) {
		MonitoringInfoElastic monitoringInfoElastic = 
				((MonitoringInfoElasticService)getService()).create(monitoringInfoElasticDTO);
		return new ResponseEntity<>(monitoringInfoElastic, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/monitoring/elastic/info", method = RequestMethod.PUT)
	public final void update(@RequestBody MonitoringInfoElasticDTO monitoringInfoElasticDTO, HttpServletResponse response) {
		MonitoringInfoElastic monitoringInfoElastic = getService().findOne(monitoringInfoElasticDTO.getId());
		if (monitoringInfoElastic == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		} 
		response.setStatus(HttpServletResponse.SC_OK);
		((MonitoringInfoElasticService)getService()).update(monitoringInfoElasticDTO);
	}

	@RequestMapping(value = "/monitoring/elastic/info/{id}", method = RequestMethod.DELETE)
	public final void delete(@PathVariable Long id, HttpServletResponse response){
		MonitoringInfoElastic monitorInfo = getService().findOne(id);
		if (monitorInfo == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		getService().delete(id);
	}
}
