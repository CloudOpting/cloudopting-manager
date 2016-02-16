package eu.cloudopting.web.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.store.StoreService;

@RestController
@RequestMapping("/api")
public class JcrImageResource {

	private final Logger log = LoggerFactory.getLogger(JcrImageResource.class);

	@Inject
	private StoreService storeService;
	
	@Inject
	private Session session;
    
    /**
     * JackRabbit Repository URL
     */
    @Value("${spring.jcr.repo_http}")
	private String jrHttp;
    
    private String getRelativePathForNode(String jcrPath){
    	String result = null;
    	String jrRepositoryBase = "http://lab1.cloudopting.org:8083/repository/default/";
		StoreService ss = this.getStoreService();
		if (ss!=null){
			jrRepositoryBase = ss.getJrHttp();
		}else{
			String s = "Store Service NOT injected!";
			if (log!=null){
				log.error(s);
			}else{
				System.out.println(s);
			}
		}
		result = StringUtils.removeStart(jcrPath, jrRepositoryBase); 
		if (!result.startsWith("/")){
			result = "/"+result;
		}
		return result;
    }
	
	@RequestMapping(value = "/jr/img", method = RequestMethod.GET)
	public final ResponseEntity<InputStreamResource> getJcrImage(@RequestParam("jcrPath") String jcrPath) {
		ResponseEntity<InputStreamResource> result = null;
		String relativePath = this.getRelativePathForNode(jcrPath);
		InputStream is = null;
		try {
			Session s = this.getSession();
			Node n = JcrUtils.getNodeIfExists(relativePath, s);
			is = JcrUtils.readFile(n);
			Node jcrContent = n.getNode("jcr:content");
			long size = jcrContent.getProperty("jcr:data").getBinary().getSize();
			HttpHeaders respHeaders = new HttpHeaders();
			String mType = jcrContent.getProperty("jcr:mimeType").getString();
			respHeaders.setContentType(MediaType.parseMediaType(mType));
			respHeaders.setContentLength(size);
			InputStreamResource isr = new InputStreamResource(is);
			result = new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error("Repository Exception", e);
			result = new ResponseEntity<InputStreamResource>(new InputStreamResource(is), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (is!=null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.warn("Repository Exception", e);
			}
		}
	    return result;
	}
	
	@RequestMapping(value = "/jr/img", method = RequestMethod.DELETE)
	public final Boolean deleteJcrImage(@RequestParam("jcrPath") String jcrPath) {
		Boolean result = false;
		String relativePath = this.getRelativePathForNode(jcrPath);
		try {
			Session s = this.getSession();
			s.removeItem(relativePath);
			s.save();
			result = true;
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error("Repository Exception", e);
			result = false;
		} 
	    return result;
	}
	
	public Logger getLog() {
		return log;
	}

	public StoreService getStoreService() {
		return storeService;
	}
	
	public Session getSession() {
		return session;
	}
}