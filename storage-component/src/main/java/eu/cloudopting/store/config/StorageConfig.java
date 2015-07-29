package eu.cloudopting.store.config;

import eu.cloudopting.store.jackrabbit.JackrabbitBinaryStoreImpl;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStore;
import eu.cloudopting.store.jackrabbit.JackrabbitOcmStoreImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage java config file
 */
@Configuration
public class StorageConfig {
    @Bean
    public Mapper mapper() {
        List<Class> classes = new ArrayList<>();
        classes.add(JackrabbitStoreRequest.class);
        return new AnnotationMapperImpl(classes);
    }
/*

    @Bean(name = "jackrabbitOcmStore")
    public JackrabbitStore jackrabbitOcmStore(){
        return new JackrabbitOcmStoreImpl();
    }

    @Bean(name = "jackrabbitBinaryStore")
    public JackrabbitStore jackrabbitBinaryStore(){
        return new JackrabbitBinaryStoreImpl();
    }

*/
}
