package eu.cloudopting.provision.config;

import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.azure.AzureProvision;
import eu.cloudopting.provision.azure.AzureRequest;
import eu.cloudopting.provision.azure.AzureResult;
import eu.cloudopting.provision.cloudstack.CloudstackProvision;
import eu.cloudopting.provision.cloudstack.CloudstackRequest;
import eu.cloudopting.provision.cloudstack.CloudstackResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Config for provision bean
 */
@Configuration
/*@PropertySource("classpath:application_cloud.properties")
@ConfigurationProperties(prefix="azure")*/
public class ProvisionConfig {

    //@Value("${test.prop}")


    @Bean
    @Scope(value = "prototype")
    public ProvisionComponent<AzureResult, AzureRequest> azureProvision() {
        return new AzureProvision();
    }

    @Bean
    @Scope(value = "prototype")
    public ProvisionComponent<CloudstackResult, CloudstackRequest> cloudStackProvision() {
        return new CloudstackProvision();
    }


}
