package eu.cloudopting.tosca.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import javax.inject.Inject;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

import eu.cloudopting.config.jcr.JcrConfig;
import eu.cloudopting.store.StoreService;

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
    StoreService storeService;
    
	@Autowired
	private ToscaUtils toscaUtils;
	
	public void unzipToscaCsar(String originPath, String destinationPath){
		log.debug("CSARUtils.unzipToscaCsar starting.");
		log.debug("CSARUtils.unzipToscaCsar originPath: " + originPath);
//		storeService.storeFile("/cloudOptingData/", "Clearo.czar", "csisp", "Clearo.czar");
		log.debug("CSARUtils.unzipToscaCsar destinationPath: " + destinationPath);
		log.debug("CSARUtils.unzipToscaCsar repository: " + repository.toString());

		//Getting the stream to save.
		InputStream stream = storeService.getDocumentAsStream(originPath);
		File targetFile = new File("/tmp/targetFile.zip");
		 
	    try {
			FileUtils.copyInputStreamToFile(stream, targetFile);
		} catch (IOException e) {
			log.error("IOException in CSARUtils.unzipToscaCsar copying stream to file.");
			e.printStackTrace();
		}
		try {
			toscaUtils.unzip("/tmp/targetFile.zip", destinationPath);
		} catch (IOException e1) {
			log.error("IOException in CSARUtils.unzipToscaCsar unzipping a file.");
			e1.printStackTrace();
		}
		/*
		String content;
		try {
			content = IOUtils.toString(stream);
			log.debug(content);
//			JcrUtils.
	        File f = new File(destinationPath+"from.csar");
	        FileUtils.writeStringToFile(f, content, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			*/
	}

	public String getDefinitionFile(String originPath) {
		log.debug("CSARUtils.getDefinitionFile starting.");
		String toscaDefinitionLocation = null;
		String toscaDefinitionContent = null;

		try {
			InputStream streamMeta = new BufferedInputStream(new FileInputStream(originPath + "/TOSCA-Metadata/TOSCA.meta"));
//			InputStream streamMeta = storeService.getDocumentAsStream(originPath+"/jcr:content/TOSCA-Metadata/TOSCA.meta/jcr:content");
			Properties properties = new Properties();
			properties.load(streamMeta);

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				log.debug("CSARUtils.getDefinitionFile. key: " + key + ", value: " + value);
			}

			toscaDefinitionLocation = properties.getProperty("Entry-Definitions");
			log.debug("CSARUtils.getDefinitionFile toscaDefinitionLocation: " + toscaDefinitionLocation);
			// with the location of the META I can get the XML stream of the Definition
	//		InputStream streamDef = storeService.getDocumentAsStream(originPath+"/"+toscaDefinitionLocation);
			InputStream streamDef = new BufferedInputStream(new FileInputStream(originPath+"/"+toscaDefinitionLocation));
			toscaDefinitionContent = IOUtils.toString(streamDef);
			log.debug("CSARUtils.getDefinitionFile toscaDefinitionContent: " + toscaDefinitionContent);
		} catch (IOException e) {
			log.error("IOException in CSARUtils.getDefinitionFile.");
			e.printStackTrace();
		}
		return toscaDefinitionContent;
	}
	

}
