package eu.cloudopting.web.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Repository;
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

import eu.cloudopting.security.AuthoritiesConstants;
import eu.cloudopting.store.StoreService;

//http://lab1.cloudopting.org:8083/repository/default/organization_key/XVS_NEWT/template/go.png
//http%3A%2F%2Flab1.cloudopting.org%3A8083%2Frepository%2Fdefault%2Forganization_key%2FXVS_NEWT%2Ftemplate%2Fgo.png
//http://localhost:8080/jr/img?jcrPath=http%3A%2F%2Flab1.cloudopting.org%3A8083%2Frepository%2Fdefault%2Forganization_key%2FXVS_NEWT%2Ftemplate%2Fgo.png


@RestController
@RequestMapping("/jr")
public class JcrImageResource {

	private final Logger log = LoggerFactory.getLogger(JcrImageResource.class);

	@Inject
	private StoreService storeService;
	
	@Inject 
	private Repository repo;
		
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
	
	@RequestMapping(value = "/img", method = RequestMethod.GET)
	@RolesAllowed(AuthoritiesConstants.ANONYMOUS)
	public final ResponseEntity<InputStreamResource> getJcrImage(@RequestParam("jcrPath") String jcrPath) {
		ResponseEntity<InputStreamResource> result = null;
		String relativePath = this.getRelativePathForNode(jcrPath);
		InputStream is = null;
		try {
			Session s = this.getSession();
			Node n = JcrUtils.getNodeIfExists(relativePath, s);
			is = JcrUtils.readFile(n);
			Node jcrContent = n.getNode("jcr:content");
			String fileName = n.getName();
			long size = jcrContent.getProperty("jcr:data").getBinary().getSize();
			HttpHeaders respHeaders = new HttpHeaders();
			String mType = jcrContent.getProperty("jcr:mimeType").getString();
			respHeaders.setContentType(MediaType.parseMediaType(mType));
			respHeaders.setContentLength(size);
			respHeaders.setContentDispositionFormData("attachment", fileName);
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