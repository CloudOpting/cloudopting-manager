package eu.cloudopting.docker.config;

import eu.cloudopting.docker.DockerService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Config for docker bean
 */
@Configuration
//@PropertySource("classpath:/eu/cloudopting/application_docker_crane.properties")
/*@ConfigurationProperties(prefix="azure")*/
public class DockerConfig {

    @Bean
    public DockerService dockerService(){
        return new DockerService();
    }

}
