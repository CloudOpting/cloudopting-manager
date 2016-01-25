package eu.cloudopting.monitoring.elastic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import eu.cloudopting.monitoring.elastic.data.Monitordata;

public interface MonitordataRepository extends ElasticsearchRepository<Monitordata, String> {
	public Monitordata findOne(String id); 
	
//	@Query(value="{\"query\": {\"filtered\": {\"query\": {\"simple_query_string\" : {\"fields\" : [\"path\"],\"query\" : \"/html\"}},\"filter\": {\"and\": [{\"range\" : {\"@timestamp\" : {\"gte\": \"2015-01-13T10:55:28+01:00\",\"lte\": \"now\",\"time_zone\": \"+01:00\"}}},{\"term\": {\"host\": \"66.249.78.191\"}}]}}}}")
	@Query(value="{\"filtered\": {\"query\": {\"simple_query_string\" : {\"fields\" : [\"path\"],\"query\" : \"/html\"}},\"filter\": {\"and\": [{\"range\" : {\"@timestamp\" : {\"gte\": \"2015-01-13T10:55:28+01:00\",\"lte\": \"now\",\"time_zone\": \"+01:00\"}}},{\"term\": {\"host\": \"66.249.78.191\"}}]}}}")
	public List<Monitordata> findCustom();

}
