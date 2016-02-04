package eu.cloudopting.config.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import eu.cloudopting.exception.CommonException;

/**
 * Java Content repository configuration.
 */
@Configuration
public class JcrConfig implements EnvironmentAware{

	private RelaxedPropertyResolver propertyResolver;

    private Environment env;

    private static final String repoUrlProperty = "repo_url";
    private static final String repoUserProperty = "user";
    private static final String repoPasswordProperty = "password";

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.jcr.");
    }
    
    @Bean
    public Repository repository() {
        Repository repository;
        try {
            repository = JcrUtils.getRepository(propertyResolver.getProperty(JcrConfig.repoUrlProperty));
        } catch (RepositoryException e) {
            throw new CommonException(e);
        }

        return repository;
    }

    @Bean
    public Session session(Repository repository){
        try {
            return repository.login(new SimpleCredentials(propertyResolver.getProperty(JcrConfig.repoUserProperty), propertyResolver.getProperty(JcrConfig.repoPasswordProperty).toCharArray()));
        } catch (RepositoryException e) {
            throw new CommonException(e);
        }
    }




}
