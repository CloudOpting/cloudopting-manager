package eu.cloudopting.web.rest;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
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
	
	@RequestMapping(value = "/img", method = RequestMethod.GET)
	@RolesAllowed(AuthoritiesConstants.ANONYMOUS)
	public final String getJcrImage(@RequestParam("jcrPath") String jcrPath) {
		String jrRepositoryBase = "http://lab1.cloudopting.org:8083/repository/default/";
		StoreService ss = this.getStoreService();
		if (storeService!=null){
			jrRepositoryBase = jrHttp;
		}else{
			String s = "Store Service NOT injected!";
			if (log!=null){
				log.error(s);
			}else{
				System.out.println(s);
			}
		}
		String relativePath = StringUtils.removeStart(jcrPath, jrRepositoryBase); 
		return relativePath;
	}
	
	public Logger getLog() {
		return log;
	}

	public StoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}
	
}