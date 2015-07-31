package eu.cloudopting.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.service.common.MonitoringServiceInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.User;
import eu.cloudopting.monitoring.MonitoringService;
import eu.cloudopting.service.UserService;
import eu.cloudopting.tosca.ToscaService;
import eu.cloudopting.web.rest.dto.Data;
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
	
	/**
	 * The the list of monitored objects
	 * @param instanceId
	 */
	@RequestMapping(value = "/monitoring/{instanceId}", method = RequestMethod.GET)
	@ResponseBody
	public String findAllObjectsByInstance(@PathVariable("instanceId") final String instanceId){
		monitoringService.testZabbix();
		return "graphs: [" +
				"{data:{}, xkey: {}, ykey: {}}," +
				"{data:{}, xkey: {}, ykey: {}}," +
				"{data:{}, xkey: {}, ykey: {}}," +
				"{data:{}, xkey: {}, ykey: {}}" +
				"]";
	}

	/**
	 * Get an object fom a specific instance.
	 * @param instanceId
	 * @param objectId
	 */
	@RequestMapping(value = "/monitoring/{instanceId}/{objectId}", method = RequestMethod.GET)
	@ResponseBody
	public GraphDTO findObject(@PathVariable("instanceId") final String instanceId, @PathVariable("objectId") final String objectId) {
		monitoringService.testZabbix();
        GraphDTO graph = new GraphDTO();
        graph.setData(
                new Data[] {
                        new Data("2001", "20", "12", "15"),
                        new Data("2002", "10", "5", "13"),
                        new Data("2003", "5", "20", "19"),
                        new Data("2004", "5", "3", "0"),
                        new Data("2005", "20", "10", "5")
                });
        graph.setXkey("time");
        graph.setYkeys(new String[] {"disk", "cpu", "ram"});
        graph.setLabels(new String[] {"Disk", "CPU", "RAM"});
        graph.setLineColors(new String[] {"green", "blue", "orange"});

        return graph;
/*
		return "graph: {" +
				"data: [" +
				" { time: '2001', disk: 20, cpu: 12, ram: 15 }," +
				" { time: '2002', disk: 10, cpu: 5, ram: 13 }," +
				" { time: '2003', disk: 5, cpu: 20, ram: 19 }," +
				" { time: '2004', disk: 5, cpu: 3, ram: 0 }," +
				" { time: '2005', disk: 20, cpu: 10, ram: 5 }" +
				"], " +
				"xkey: 'time'," +
				"ykeys: ['disk', 'cpu', 'ram']," +
				"labels: ['Disk', 'CPU', 'RAM']," +
				"lineColors: ['green', 'blue', 'orange']" +
				"}";
				*/
    }

}
