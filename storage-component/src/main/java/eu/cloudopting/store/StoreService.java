package eu.cloudopting.store;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.FileUtils;
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
		//TODO Uncomment once JackRabbit is able to create intermediate paths
		return organizationKey + "/" + applicationToscaName + "/template";
		
	}

	public JackrabbitStoreResult storeBinary(JackrabbitStoreRequest request){
		return jackrabbitBinaryStore.store(request);
	}

	public JackrabbitStoreResult<InputStream> retrieve(String s) {
		JackrabbitStoreResult<InputStream> retrieve = jackrabbitBinaryStore.retrieve(s);
		return retrieve;
	}

	public JackrabbitStoreResult<InputStream> remove(String s) {
		JackrabbitStoreResult<InputStream> remove = jackrabbitBinaryStore.remove(s);
		return remove;
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
			log.debug("[Local]\tFile Path:"+filePath+" - File name:"+theFile);
			log.debug("[Remote]\tFile Path:"+storePath+" - File name:"+storeFile);
	        stream = new BufferedInputStream(new FileInputStream(filePath+theFile));
	        String mimeType = MimeTypeUtils.mimeUtilDetectMimeType(stream);
//			folder = session.getRootNode().getNode(storePath);
//			Node file = folder.addNode(theFile, "nt:file");
//	        Node content = file.addNode("jcr:content", "nt:resource");
//			folder = JcrUtils.getOrAddFolder(session.getRootNode(), storePath);
	        //Add the file separator to the local path, if missing
	        filePath += filePath.endsWith(File.separator)?"":File.separator;
	        folder = this.createNodesForPath(storePath);
	        JcrUtils.putFile(folder, storeFile, mimeType, stream);
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
    
    /**
     * Creates a hierarchy of Nodes by splitting the input String on occurrences of File.pathSeparator
     * @param localFileAbsolutePath the path whose matching nodes have to be created
     * @return The last (deepest) created node for the provided path
     * @throws RepositoryException
     */
    public Node createNodesForPath(String localFileAbsolutePath) throws RepositoryException{
    	String splitRegex = Pattern.quote(File.separator);
    	if (localFileAbsolutePath.startsWith(File.separator)){
    		localFileAbsolutePath = localFileAbsolutePath.replaceFirst(splitRegex, "");
    	}
		String[] splittedFileName = localFileAbsolutePath.split(splitRegex);
		Node lastCreatedNode = session.getRootNode();
		for (int i = 0; i < splittedFileName.length; i++) {
			lastCreatedNode = this.addChildToNode(lastCreatedNode, splittedFileName[i]);
		}
		return lastCreatedNode;
    }
    
    /**
     * Creates a JCR Child Node for the provided parent node
     * @param parent The containing node
     * @param childFolder The name of the child node
     * @return The new child node, or null if there were issues.
     */
    public Node addChildToNode(Node parent, String childFolder) throws RepositoryException{
    	Node childNode = null;
    	childNode = JcrUtils.getOrAddFolder(parent, childFolder);
	    session.save();
		return childNode;
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
