package eu.cloudopting.store;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import eu.cloudopting.storagecomponent.StorageComponent;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;
import eu.cloudopting.util.MimeTypeUtils;

@Service
public class StoreService {
	private final Logger log = LoggerFactory.getLogger(StoreService.class);
	
	@Inject
    Repository repository;
    @Inject
    Session session;

	@Inject
	StorageComponent<JackrabbitStoreResult,JackrabbitStoreRequest> jackrabbitBinaryStore;



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
	
	/**
	 * Gets the path where to save Service Template files
	 * @param organizationKey The Organization Key
	 * @param applicationToscaName The Tosca Name of the Template (no spaces and fancy chars)
	 * @return The path to be passed as first parameter to the JackRabbitStoreRequest constructor.
	 */
	public String getTemplatePath (String organizationKey, String applicationToscaName){
		return organizationKey + "/" + applicationToscaName + "/template";
		
	}

	public JackrabbitStoreResult storeBinary(JackrabbitStoreRequest request){
		return jackrabbitBinaryStore.store(request);
	}

	public JackrabbitStoreResult<InputStream> retrieve(String s) {
		JackrabbitStoreResult<InputStream> retrieve = jackrabbitBinaryStore.retrieve(s);
		return retrieve;
	}

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
    
    public void storeFile(String filePath, String theFile, String storePath, String storeFile){
        Node folder;
        InputStream stream;
		try {
	        stream = new BufferedInputStream(new FileInputStream(filePath+theFile));
	        String mimeType = MimeTypeUtils.mimeUtilDetectMimeType(stream);
			folder = session.getRootNode().getNode(storePath);
//			Node file = folder.addNode(theFile, "nt:file");
//	        Node content = file.addNode("jcr:content", "nt:resource");
	        JcrUtils.putFile(folder, theFile, mimeType, stream);
//	        Binary binary = session.getValueFactory().createBinary(stream);
//	        content.setProperty("jcr:data", binary);
//	        content.setProperty("jcr:mimeType", mimeType);
	        session.save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

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
