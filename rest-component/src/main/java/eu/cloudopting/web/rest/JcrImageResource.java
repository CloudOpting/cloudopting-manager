package eu.cloudopting.web.rest;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.security.AuthoritiesConstants;
import eu.cloudopting.store.StoreService;

//http://lab1.cloudopting.org:8083/repository/default/organization_key/XVS_NEWT/template/go.png
//http%3A%2F%2Flab1.cloudopting.org%3A8083%2Frepository%2Fdefault%2Forganization_key%2FXVS_NEWT%2Ftemplate%2Fgo.png
//http://localhost:8080/api/jcrimage?jcrPath=http%3A%2F%2Flab1.cloudopting.org%3A8083%2Frepository%2Fdefault%2Forganization_key%2FXVS_NEWT%2Ftemplate%2Fgo.png


@RestController
@Component
@RequestMapping("/jr")
public class JcrImageResource {
	private final Logger log = LoggerFactory.getLogger(JcrImageResource.class);

	@Autowired(required=true)
	private StoreService storeService;
	
	@RequestMapping(value = "/img", method = RequestMethod.GET)
	@RolesAllowed(AuthoritiesConstants.ANONYMOUS)
	public final String getJcrImage(@RequestParam("jcrPath") String jcrPath) {
		String jrRepositoryBase = "http://lab1.cloudopting.org:8083/repository/default/";
		if (storeService!=null){
			jrRepositoryBase = storeService.getJrHttp();
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
	
}