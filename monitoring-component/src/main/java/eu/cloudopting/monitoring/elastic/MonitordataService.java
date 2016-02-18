package eu.cloudopting.monitoring.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.monitoring.elastic.data.Monitordata;
import eu.cloudopting.service.CustomizationService;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
@Service
public class MonitordataService {
	
	@Autowired
	CustomizationService customizationService;

	
	@Autowired
	private MonitordataRepository monitordataRepository;
	private static final Logger logger = LoggerFactory.getLogger(MonitordataService.class);

		public Monitordata findOne(String id) {
			logger.debug("in findone: "+id);
			return monitordataRepository.findOne(id);
		}

		public Iterable<Monitordata> findAll() {
			// TODO Auto-generated method stub
			return monitordataRepository.findAll();
		}
		
		public List<Monitordata> findCustom(String container){
			return monitordataRepository.findCustom(container, new PageRequest(0,20,new Sort(new Sort.Order(Sort.Direction.ASC,"@timestamp"))));
		}

		public List<Monitordata> getMonitorData(String container, String condition, String fields, String type, String pagination){
			return monitordataRepository.findCustom(container, new PageRequest(0,20,new Sort(new Sort.Order(Sort.Direction.ASC,"@timestamp"))));
		}
		
		public void getAllMonitorData(Long customizationId){
			// recover the customization
			Customizations cust = customizationService.findOne(customizationId);
			// get info on elastic form Db
//			cust.
			
			// with info I get data to pass to the get monitor data
			
			// will return a list of list
			
		}

}
