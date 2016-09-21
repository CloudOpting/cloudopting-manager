package eu.cloudopting.store;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.storagecomponent.StorageComponent;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreRequest;
import eu.cloudopting.store.jackrabbit.JackrabbitStoreResult;

@Service
public class StoreService {
	private final Logger log = LoggerFactory.getLogger(StoreService.class);
	
	@Inject
    Repository repository;
    @Inject
    Session session;
    
    /**
     * JackRabbit Repository URL
     */
    @Value("${spring.jcr.repo_http}")
	private String jrHttp;

	@Inject
	StorageComponent<JackrabbitStoreResult,JackrabbitStoreRequest> jackrabbitBinaryStore;
	
	/**
	 * Gets the path where to save Service Template files
	 * @param organizationKey The Organization Key
	 * @param applicationToscaName The Tosca Name of the Template (no spaces and fancy chars)
	 * @return The path to be passed as first parameter to the JackRabbitStoreRequest constructor.
	 */
	public String getTemplatePath (String organizationKey, String applicationToscaName){
		return this.getTemplatePath(organizationKey, applicationToscaName, false);
	}
	
	/**
	 * Gets the path where to save Service Template files
	 * @param organizationKey The Organization Key
	 * @param applicationToscaName The Tosca Name of the Template (no spaces and fancy chars)
	 * @param withAbsolutePath prefix the path with the http URL prefix
	 * @return The path to be passed as first parameter to the JackRabbitStoreRequest constructor.
	 */
	public String getTemplatePath (String organizationKey, String applicationToscaName, boolean withAbsolutePath){
		//TODO Uncomment once JackRabbit is able to create intermediate paths
		String prefix = withAbsolutePath?jrHttp:"";
		return prefix + organizationKey + "/" + applicationToscaName + "/template";
	}

	/**
	 * Stores the binary content into the repository
	 * @param request The request object
	 * @return The result of the operation.
	 */
	public JackrabbitStoreResult storeBinary(JackrabbitStoreRequest request){
		return jackrabbitBinaryStore.store(request);
	}

	/**
	 * Reads content from the repository based on the path
	 * @param path The path of the object to be read
	 * @return The InputStream of the object that was read.
	 */
	public JackrabbitStoreResult<InputStream> retrieve(String path) {
		JackrabbitStoreResult<InputStream> retrieve = jackrabbitBinaryStore.retrieve(path);
		return retrieve;
	}

	/**
	 * Deletes content from the repository based on the path
	 * @param path The path of the object to be deleted
	 * @return The result of the delete operation.
	 */
	public JackrabbitStoreResult<InputStream> remove(String path) {
		JackrabbitStoreResult remove = jackrabbitBinaryStore.remove(path);
		return remove;
	}

	public InputStream getDocumentAsStream(String originPath){
		log.debug("in getDocumentAsStream");
		InputStream retStream = null;
    	log.debug("Original Path:'"+originPath+"'");
    	originPath = originPath.replaceFirst(getJrHttp(), "");
    	log.debug("Relative Path:'"+originPath+"'");
    	try {
			Node storedFile = session.getRootNode().getNode(originPath);
			log.debug("file: "+storedFile.toString());
			PropertyIterator props = storedFile.getProperties();
			while(props.hasNext()){
				Property prop = (Property) props.next();
				log.debug("prop: "+prop.toString());
				log.debug("prop name: "+prop.getName());
			}
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
			log.debug("stream loc:"+filePath+theFile);
	        stream = new BufferedInputStream(new FileInputStream(filePath+theFile));
	        //String mimeType = MimeTypeUtils.mimeUtilDetectMimeType(stream);
	        String mimeType2 = URLConnection.guessContentTypeFromStream(stream);
	        if (mimeType2==null){
	        	log.error("Unable to detect the mime type for file "+theFile+", defaulting to 'application/octet-stream'");
	        	mimeType2 = "application/octet-stream";
	        }
	        log.debug(mimeType2);
	        //Add the file separator to the local path, if missing
	        filePath += filePath.endsWith(File.separator)?"":File.separator;
	        folder = this.createNodesForPath(storePath);
	        log.debug(folder.getPath());
	        log.debug("filePath:"+filePath);
	        log.debug("storePath:"+storePath);
	        JcrUtils.putFile(folder, storeFile, mimeType2, stream);
	        log.debug("after put");
	        session.save();
	        log.debug("after save");
	        stream.close();
	        log.debug("after stream close");
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
    }
    
    /**
     * Creates a hierarchy of Nodes by splitting the input String on occurrences of File.pathSeparator
     * @param jackRabbitRemotePath the path whose matching nodes have to be created
     * @return The last (deepest) created node for the provided path
     * @throws RepositoryException
     */
    public Node createNodesForPath(String jackRabbitRemotePath) throws RepositoryException{
    	String splitRegex = Pattern.quote("/");
    	if (jackRabbitRemotePath.startsWith(File.separator)){
    		jackRabbitRemotePath = jackRabbitRemotePath.replaceFirst(splitRegex, "");
    	}
		String[] splittedFileName = jackRabbitRemotePath.split(splitRegex);
		Node lastCreatedNode = session.getRootNode();
		log.debug("trovato nodo radice");;
		for (int i = 0; i < splittedFileName.length; i++) {
			if (!this.nodeAlreadyExist(splittedFileName[i], lastCreatedNode)) {
				lastCreatedNode = this.addChildToNode(lastCreatedNode, splittedFileName[i]);
			}
			else {
				lastCreatedNode = JcrUtils.getNodeIfExists(lastCreatedNode, splittedFileName[i]);
			}
		}
		return lastCreatedNode;
    }
    
    private boolean nodeAlreadyExist(String nodeToCreate, Node path) throws RepositoryException {
    	log.debug("Check if jackrabbit node exists");
    	log.debug("Path "+path.getPath());
    	log.debug("Node.to create: "+nodeToCreate);
    	Node nodeIfExists = JcrUtils.getNodeIfExists(path, nodeToCreate);
    	if (nodeIfExists == null) {
    		log.debug("Node does not exists");
    	}
    	else {
    		log.debug("Node exists");
    	}
		return !(nodeIfExists == null);
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
	    log.debug("aggiunto node");
		return childNode;
    }
    
	/**
     * Removes the JCR Path that is identified by the parameters
     * @param orgKey The Organization Key
     * @param toscaName The Service Tosca Name
     */
    public void deletePath(String orgKey, String toscaName){
        Node folder,
             folderParent;
        try {
			folder = session.getRootNode().getNode(this.getTemplatePath(orgKey, toscaName));
			log.debug("Folder->"+folder.getPath());
			folderParent = folder.getParent();
			log.debug("FolderParent->"+folderParent.getPath());
			folderParent.remove();
			session.save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			log.error("Repository Exception: "+e.getLocalizedMessage());
			//e.printStackTrace();
		}
    }  
    
    public void deleteFile(String filePath) {
    	log.debug("File to remove: " + filePath);
    	if (filePath.startsWith("/")) {
    		filePath = filePath.substring(1);
    	}
    	log.debug("Path: " + filePath);
    	Node nodeToRemove;
    try {
		nodeToRemove = session.getRootNode().getNode(filePath);
		nodeToRemove.remove();
		session.save();
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		log.error("Repository Exception: "+e.getLocalizedMessage());
		//e.printStackTrace();
	}
    }
    
    /**
     * Returns the set of file paths for each element below the provided
     * root path.
     * @param path The root node path for retrieval of files
     * @return A set of strings each with the JCR Path to a file
     */
	public Set<String> getFilesStartingFromPath(String path) {
		Set<String> resultSet = new HashSet<String>();
		try {
			QueryManager manager = session.getWorkspace().getQueryManager();
			String basePath = session.getRootNode().getPath();
			String queryStatement = "select * from nt:file where jcr:path LIKE '" + basePath + path + "/%'";
			Query query = manager.createQuery(queryStatement, Query.SQL);
			QueryResult result = query.execute();
			
			NodeIterator nodeIterator = result.getNodes();
			Node node = null;
			while (nodeIterator.hasNext()) {
				node = (Node) nodeIterator.next();
				resultSet.add(node.getPath());
			}
		} catch (InvalidQueryException e) {
			log.error("InvalidQueryException", e);
			e.printStackTrace();
		} catch (RepositoryException e) {
			log.error("RepositoryException", e);
			e.printStackTrace();
		}
		return resultSet;
	}

	public String getJrHttp() {
		return jrHttp;
	}

}
