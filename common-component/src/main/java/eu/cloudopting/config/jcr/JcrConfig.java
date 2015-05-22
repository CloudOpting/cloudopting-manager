package eu.cloudopting.config.jcr;

import eu.cloudopting.exception.CommonException;
import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.api.JackrabbitRepositoryFactory;
import org.apache.jackrabbit.api.management.RepositoryManager;
import org.apache.jackrabbit.commons.JcrUtils;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.UrlResource;



import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Java Content repository configuration.
 */
@Configuration
public class JcrConfig {


    @Bean
    @Lazy
    public Repository repository() {
        Repository repository;
        try {
            repository = JcrUtils.getRepository("http://cloudopting1.cloudapp.net:8082/server");
        } catch (RepositoryException e) {
            throw new CommonException(e);
        }

        return repository;
    }

    @Bean
    public Session session(Repository repository){
        try {
            return repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        } catch (RepositoryException e) {
            throw new CommonException(e);
        }
    }




}
