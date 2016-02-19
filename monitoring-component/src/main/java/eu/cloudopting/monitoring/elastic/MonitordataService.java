package eu.cloudopting.monitoring.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.MonitoringInfoElastic;
import eu.cloudopting.monitoring.elastic.data.ElasticData;
import eu.cloudopting.monitoring.elastic.data.ElasticGraphData;
import eu.cloudopting.monitoring.elastic.data.Monitordata;
import eu.cloudopting.service.CustomizationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
@Service
public class MonitordataService {
	private final Logger log = LoggerFactory.getLogger(MonitordataService.class);
	@Autowired
	CustomizationService customizationService;

	@Autowired
	private MonitordataRepository monitordataRepository;
	private static final Logger logger = LoggerFactory.getLogger(MonitordataService.class);

	public Monitordata findOne(String id) {
		logger.debug("in findone: " + id);
		return monitordataRepository.findOne(id);
	}

	public Iterable<Monitordata> findAll() {
		// TODO Auto-generated method stub
		return monitordataRepository.findAll();
	}

	public List<Monitordata> findCustom(String container) {
		return monitordataRepository.findCustom(container,
				new PageRequest(0, 20, new Sort(new Sort.Order(Sort.Direction.ASC, "@timestamp"))));
	}

	public List<Monitordata> getMonitorData(String container, String condition, String fields, String type,
			Long pagination) {
		log.debug("pagination:"+pagination.toString());
		log.debug("condit"+condition);
		return monitordataRepository.findMonitorData(container, condition, fields,
				new PageRequest(0, pagination.intValue(), new Sort(new Sort.Order(Sort.Direction.ASC, "@timestamp"))));
	}

	public ArrayList<ElasticGraphData> getAllMonitorData(Long customizationId) {
		// recover the customization
		Customizations cust = customizationService.findOne(customizationId);
		// get info on elastic form Db
		Set<MonitoringInfoElastic> elastinfo = cust.getElasticInfos();
		log.debug(new Integer(elastinfo.size()).toString());

		ArrayList<ElasticGraphData> retData = new ArrayList<ElasticGraphData>();
		// with info I get data to pass to the get monitor data
		for (MonitoringInfoElastic ei : elastinfo) {
			log.debug(ei.getContainer());
			List<Monitordata> eData = this.getMonitorData(ei.getContainer(), ei.getCondition(), ei.getFields(),
					ei.getType(), ei.getPagination());

			ArrayList<ElasticData> graphData = new ArrayList<ElasticData>();
			for (Monitordata md : eData) {
				ElasticData ed = new ElasticData(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(md.getTimestamp()),
						"1");
				log.debug(ed.toString());
				graphData.add(ed);
			}
			ElasticGraphData egd = new ElasticGraphData();
			log.debug(new Integer(graphData.size()).toString());
			if (!graphData.isEmpty()) {
				egd.setData((ElasticData[]) graphData.toArray());
			}
			egd.setTitle(ei.getTitle());
			egd.setDescription(ei.getDescription());
			egd.setType(ei.getType());
			egd.setXkey("time");
			egd.setYkeys(new String[] { "value" });
			egd.setLabels(new String[] { "Value" });
			egd.setLineColors(new String[] { "blue" });

			retData.add(egd);
		}
		// will return a list of list
		return retData;
	}

}
