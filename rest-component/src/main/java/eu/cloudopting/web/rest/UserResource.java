package eu.cloudopting.web.rest;


import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.cloudopting.domain.User;
import eu.cloudopting.repository.UserRepository;
import eu.cloudopting.service.UserService;
import eu.cloudopting.web.rest.dto.UserDTO;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserService userService;

	@RequestMapping(value = "/users/{idUser}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<User> findOne(@PathVariable("idUser") final Long idUser) {
		User user = getUserRepository().findOne(idUser);
		if(user == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		//Don't send password to the client
		user.setPassword(null);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public final ResponseEntity<List<User>> findAll() {
		List<User> users = getUserRepository().findAll();
		//Don't send password to the client
		for(User user : users){
			user.setPassword(null);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{idUser}", method = RequestMethod.DELETE)
	public final void delete(@PathVariable Long idUser, HttpServletResponse response){
		User user = getUserRepository().findOne(idUser);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		getUserRepository().delete(idUser);
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> create(@Valid @RequestBody final UserDTO userDTO, HttpServletRequest request) {
		return getUserRepository().findOneByLogin(userDTO.getLogin())
				.map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
				.orElseGet(() -> getUserRepository().findOneByEmail(userDTO.getEmail())
						.map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
						.orElseGet(() -> {
							User user = getUserService().createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
									userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
									userDTO.getLangKey());
							user = getUserService().setUserActivatedFlag(user.getId(), userDTO.isActivated());
							return new ResponseEntity<>(HttpStatus.CREATED);
						})
						);
	}

	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public final void update(@RequestBody UserDTO userDTO, HttpServletResponse response) {
		User user = getUserRepository().findOne(userDTO.getId());
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		} 
		getUserService().updateUserInformation(user.getId(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
		getUserService().setUserActivatedFlag(user.getId(), userDTO.isActivated());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	protected UserRepository getUserRepository() {
		return userRepository;
	}
	
	protected UserService getUserService() {
		return userService;
	}
	
	//    /**
	//     * GET  /users/:login -> get the "login" user.
	//     */
	//    @RequestMapping(value = "/users/{login}",
	//            method = RequestMethod.GET,
	//            produces = MediaType.APPLICATION_JSON_VALUE)
	//    @RolesAllowed(AuthoritiesConstants.ADMIN)
	//    ResponseEntity<User> getUser(@PathVariable String login) {
	//        log.debug("REST request to get User : {}", login);
	//        return userRepository.findOneByLogin(login)
	//                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
	//                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	//    }
}
