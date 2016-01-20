package eu.cloudopting.monitoring.elastic;

//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import eu.cloudopting.monitoring.elastic.data.Monitordata;

public interface MonitordataRepository {//extends ElasticsearchRepository<Monitordata, String> {
	public Monitordata findOne(String id); 

}
