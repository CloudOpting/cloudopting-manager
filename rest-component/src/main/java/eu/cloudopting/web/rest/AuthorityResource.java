package eu.cloudopting.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.Authority;
import eu.cloudopting.service.AuthorityService;

@RestController
@RequestMapping("/api")
public class AuthorityResource {

	@Inject
	private AuthorityService authorityService;
	
	@RequestMapping(value = "/roles", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<Authority>> findAll() {
		List<Authority> roles = getAuthorityService().findAll();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}
	
	protected AuthorityService getAuthorityService(){
		return authorityService;
	}
}