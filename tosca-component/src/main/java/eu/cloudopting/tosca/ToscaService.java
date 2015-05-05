package eu.cloudopting.tosca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class ToscaService {
	private final Logger log = LoggerFactory.getLogger(ToscaService.class);
	
	public byte[] getToscaGraph( String id){
		log.info("in the swervice");
		return null;
		
	}
}
