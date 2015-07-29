package eu.cloudopting.tosca.utils;

import java.io.IOException;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

import eu.cloudopting.config.jcr.JcrConfig;
import eu.cloudopting.store.config.StorageConfig;
import eu.cloudopting.store.jackrabbit.JackrabbitStore;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;


@ContextConfiguration(classes = {StorageConfig.class, JcrConfig.class})
@Service
public class CSARUtils {
	private final Logger log = LoggerFactory.getLogger(CSARUtils.class);
	/*
	@Inject
	private JackrabbitStore jackrabbitOcmStore;

	public JackrabbitStore getJackrabbitStore() {
		return jackrabbitOcmStore;
	}
	*/
    @Inject
    Repository repository;
    @Inject
    Session session;

	@Autowired
	private ToscaUtils toscaUtils;
	
	public void getToscaTemplate(String originPath, String destinationPath){
		log.debug("in getToscaTemplate");
		log.debug(originPath);
		log.debug("destinationPath: "+destinationPath);
		log.debug("repository: "+repository.toString());
		try {
			Node file = session.getRootNode().getNode("1431431601575/jcr:content");
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*
		JackrabbitStoreResult<JackrabbitStoreRequest> retrieve = jackrabbitOcmStore.retrieve(originPath);
		try {
			toscaUtils.unzip(retrieve.getStoredContent().getContent(), destinationPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	*/	
	}

}
