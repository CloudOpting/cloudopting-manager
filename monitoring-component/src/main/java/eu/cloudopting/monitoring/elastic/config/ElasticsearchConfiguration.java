package eu.cloudopting.monitoring.elastic.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration

@EnableElasticsearchRepositories(basePackages = "org/springframework/data/elasticsearch/repositories")
public class ElasticsearchConfiguration {
	@Resource
	private Environment environment;

	@Bean
	public Client client() {
//		TransportClient client = new TransportClient(ImmutableSettings.settingsBuilder().put("cluster.name", environment.getProperty("elasticsearch.cluster.name")).put("client.transport.sniff", true).put("client.transport.ping_timeout", 20, TimeUnit.SECONDS).build());
		TransportClient client = new TransportClient();
		TransportAddress address = new InetSocketTransportAddress(environment.getProperty("elasticsearch.host"),
				Integer.parseInt(environment.getProperty("elasticsearch.port")));
		client.addTransportAddress(address);

		return client;
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(client());
	}
}
