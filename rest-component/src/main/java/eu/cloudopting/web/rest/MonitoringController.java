package eu.cloudopting.web.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.CustomizationDeployInfo;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.monitoring.MonitoringService;
import eu.cloudopting.monitoring.elastic.MonitordataService;
import eu.cloudopting.monitoring.elastic.data.ElasticData;
import eu.cloudopting.monitoring.elastic.data.ElasticGraphData;
import eu.cloudopting.monitoring.elastic.data.Monitordata;
import eu.cloudopting.service.CustomizationDeployInfoService;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.web.rest.dto.Data;
import eu.cloudopting.web.rest.dto.ElasticGraphDTO;
import eu.cloudopting.web.rest.dto.GraphDTO;

/**
 * @author Xavier Cases Camats (xavier.cases@worldline.com)
 */
@RestController
@RequestMapping("/api")
public class MonitoringController {
	private final Logger log = LoggerFactory.getLogger(MonitoringController.class);

	@Autowired
	MonitoringService monitoringService;

	@Autowired
	MonitordataService monitordataService;


	/**
	 * The the list of monitored objects
	 * 
	 * @param instanceId
	 */
	@RequestMapping(value = "/monitoring/{instanceId}", method = RequestMethod.GET)
	@ResponseBody
	public String findAllObjectsByInstance(@PathVariable("instanceId") final String instanceId) {
		monitoringService.testZabbix();
		return "graphs: [" + "{data:{}, xkey: {}, ykey: {}}," + "{data:{}, xkey: {}, ykey: {}},"
				+ "{data:{}, xkey: {}, ykey: {}}," + "{data:{}, xkey: {}, ykey: {}}" + "]";
	}

	/**
	 * Get an object fom a specific instance.
	 * 
	 * @param instanceId
	 * @param objectId
	 */
	@RequestMapping(value = "/monitoring/{instanceId}/{objectId}", method = RequestMethod.GET)
	@ResponseBody
	public GraphDTO findObject(@PathVariable("instanceId") final String instanceId,
			@PathVariable("objectId") final String objectId) {
		// monitoringService.testZabbix();
		GraphDTO graph = new GraphDTO();
		graph.setData(new Data[] { new Data("2001", "20", "12", "15"), new Data("2002", "10", "5", "13"),
				new Data("2003", "5", "20", "19"), new Data("2004", "5", "3", "0"),
				new Data("2005", "20", "10", "5") });
		graph.setXkey("time");
		graph.setYkeys(new String[] { "disk", "cpu", "ram" });
		graph.setLabels(new String[] { "Disk", "CPU", "RAM" });
		graph.setLineColors(new String[] { "green", "blue", "orange" });

		return graph;
		/*
		 * return "graph: {" + "data: [" +
		 * " { time: '2001', disk: 20, cpu: 12, ram: 15 }," +
		 * " { time: '2002', disk: 10, cpu: 5, ram: 13 }," +
		 * " { time: '2003', disk: 5, cpu: 20, ram: 19 }," +
		 * " { time: '2004', disk: 5, cpu: 3, ram: 0 }," +
		 * " { time: '2005', disk: 20, cpu: 10, ram: 5 }" + "], " +
		 * "xkey: 'time'," + "ykeys: ['disk', 'cpu', 'ram']," +
		 * "labels: ['Disk', 'CPU', 'RAM']," +
		 * "lineColors: ['green', 'blue', 'orange']" + "}";
		 */
	}

	@RequestMapping(value = "/monitoring/elastic/{instanceId}", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<ElasticGraphData> findAllElasticDataById(@PathVariable("instanceId") final Long instanceId) {
		// I get the id of the customization
		log.debug(instanceId.toString());
		ArrayList<ElasticGraphData> ret = monitordataService.getAllMonitorData(instanceId);
		return ret;
	}

		
	
	@RequestMapping(value = "/monitoring/elastic_old/{instanceId}", method = RequestMethod.GET)
	@ResponseBody
	public GraphDTO findOneDataById(@PathVariable("instanceId") final String instanceId) {
		Monitordata one = monitordataService.findOne("AVI6Z24UAx6YebBYGh3x");
		log.debug(one.getHost());
		log.debug(one.getTimestamp().toGMTString());

		List<Monitordata> listret = monitordataService.findCustom("66.249.78.191");
		log.debug(listret.toString());

		GraphDTO gdto = new GraphDTO();
		Data[] dataarr = new Data[listret.size()];
		ArrayList<Data> darr = new ArrayList<Data>();

		// for(Monitordata temp : listret){
		for (int i = 0; i < listret.size(); i++) {
			log.debug(listret.get(i).getHost());
			Data el = new Data(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listret.get(i).getTimestamp()), "1",
					"0", "0");
			dataarr[i] = el;
			// darr.add(el);
		}
		gdto.setData(dataarr);
		gdto.setXkey("time");
		gdto.setYkeys(new String[] { "disk", "cpu", "ram" });
		gdto.setLabels(new String[] { "Disk", "CPU", "RAM" });
		gdto.setLineColors(new String[] { "green", "blue", "orange" });
		return gdto;
		/*
		 * String ret = null; return ret;
		 */
	}

	@RequestMapping(value = "/monitoring/elastic", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	@ResponseBody
	public ElasticGraphDTO getMonitoringData(@RequestParam(value = "container", required = false) String container,
			@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "fields", required = false) String fields,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "pagination", required = false) Long pagination) {

		List<Monitordata> listret = monitordataService.getMonitorData(container, condition, fields, type, pagination);
		log.debug(listret.toString());

		ElasticGraphDTO gdto = new ElasticGraphDTO();
		ElasticData[] dataarr = new ElasticData[listret.size()];

		for (int i = 0; i < listret.size(); i++) {
			log.debug(listret.get(i).getHost());
			ElasticData el = new ElasticData(
					new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listret.get(i).getTimestamp()), "1");
			dataarr[i] = el;
		}
		gdto.setData(dataarr);
		gdto.setXkey("time");
		gdto.setYkeys(new String[] { "occurrence" });
		gdto.setLabels(new String[] { "Occurrence" });
		gdto.setLineColors(new String[] { "green" });
		return gdto;
	}

	@RequestMapping(value = "/monitoring/hosts/{instanceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> findHosts(@PathVariable("instanceId") final Long instanceId) {
		// here I get the name of the host from the Db to get info from zabbix
		
		// whti the instance id I get the Db fqdn
		
		// need to login to zabbix
		monitoringService.loginZabbix();
		return new ResponseEntity<String>(monitoringService.getHostId(instanceId).toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/monitoring/items/{instanceId}/{hostId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> findItemss(@PathVariable("instanceId") final String instanceId,
			@PathVariable("hostId") final String hostId) {
		// here I get the name of the host from the Db to get info from zabbix
		
		// whti the instance id I get the Db fqdn
		String fqdn = "corbyportal.cs8cloud.internal";
		// need to login to zabbix
		monitoringService.loginZabbix();
		return new ResponseEntity<String>(monitoringService.getItems(hostId).toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/monitoring/history/{instanceId}/{hostId}/{itemId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> findItemss(@PathVariable("instanceId") final String instanceId,
			@PathVariable("hostId") final String hostId,@PathVariable("itemId") final String itemId) {
		// here I get the name of the host from the Db to get info from zabbix
		// whti the instance id I get the Db fqdn
		String fqdn = "corbyportal.cs8cloud.internal";
		// need to login to zabbix
		monitoringService.loginZabbix();
		return new ResponseEntity<String>(monitoringService.getDataHistory(itemId).toString(), HttpStatus.OK);
	}

}
