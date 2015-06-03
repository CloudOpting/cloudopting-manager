package eu.cloudopting.provision.config;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.azure.AzureProvision;
import eu.cloudopting.provision.azure.AzureRequest;
import eu.cloudopting.provision.azure.AzureResult;
import eu.cloudopting.provision.cloudstack.CloudstackProvision;
import eu.cloudopting.provision.cloudstack.CloudstackRequest;
import eu.cloudopting.provision.cloudstack.CloudstackResult;
import org.jclouds.ContextBuilder;
import org.jclouds.azurecompute.AzureComputeApi;
import org.jclouds.azurecompute.AzureComputeProviderMetadata;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.providers.ProviderMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import javax.inject.Singleton;
import java.util.Properties;
import java.util.Set;

import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;

/**
 * Config for provision bean
 */
@Configuration
@PropertySource("classpath:application_cloud.properties")
@ConfigurationProperties(prefix="azure")
public class ProvisionConfig {

    //@Value("${test.prop}")



    @Bean
    @Scope(value = "prototype")
    public ProvisionComponent<AzureResult,AzureRequest> azureProvision(){
        return new AzureProvision();
    }

    @Bean
    @Scope(value = "prototype")
    public ProvisionComponent<CloudstackResult,CloudstackRequest> cloudStackProvision(){
        return new CloudstackProvision();
    }



}
