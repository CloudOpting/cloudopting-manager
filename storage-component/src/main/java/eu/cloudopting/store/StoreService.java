package eu.cloudopting.store;

import java.io.InputStream;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
	private final Logger log = LoggerFactory.getLogger(StoreService.class);
	
	@Inject
    Repository repository;
    @Inject
    Session session;
 //   @Inject
 //   Mapper mapper;
    
  //  ObjectContentManager ocm;
    /*
    public StoreService(){
    	super();
    	log.debug("repository"+repository);
    	log.debug("session"+session);
    	log.debug("mapper"+mapper);
  //  	this.ocm = new ObjectContentManagerImpl(session,mapper);
    //	log.debug("ocm"+this.ocm);
    }
    */
    
        
    public InputStream getDocumentAsStream(String originPath){
    	InputStream retStream = null;
    	log.debug("in getDocumentAsStream");
		log.debug("originPath: "+originPath);
		try {
			Node storedFile = session.getRootNode().getNode(originPath);
			log.debug("file: "+storedFile.toString());
			PropertyIterator props = storedFile.getProperties();
			while(props.hasNext()){
				Property prop = (Property) props.next();
				log.debug("prop: "+prop.toString());
				log.debug("prop name: "+prop.getName());
//				log.debug("prop name: "+prop.get);
			}
//			final Binary in = storedFile.getProperty("jcr:data").getBinary();
			retStream = JcrUtils.readFile(storedFile);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	return retStream;
    	
    }
    /*
    public boolean getDocument(String originPath, String destinationPath){
    	log.debug("in getDocument");
		log.debug(originPath);
		log.debug("destinationPath: "+destinationPath);
		try {
			Node storedFile = session.getRootNode().getNode(originPath);
			log.debug("file: "+storedFile.toString());
			PropertyIterator props = storedFile.getProperties();
			while(props.hasNext()){
				Property prop = (Property) props.next();
				log.debug("prop: "+prop.toString());
				log.debug("prop name: "+prop.getName());
//				log.debug("prop name: "+prop.get);
			}
			
			InputStream contentStream = JcrUtils.readFile(storedFile);
			
			
			
			
			final Binary in = file.getProperty("jcr:data").getBinary();
			retStream = in.getStream();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File targetFile = new File("src/main/resources/targetFile.tmp");
 
    FileUtils.copyInputStreamToFile(initialStream, targetFile);
    	return true;
    }
    */

}
