package eu.cloudopting.monitoring.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.monitoring.elastic.data.Monitordata;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class MonitordataService {
	
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
			return monitordataRepository.findCustom(container);
		}
		
}
