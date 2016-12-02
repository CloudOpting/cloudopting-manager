package eu.cloudopting.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * REST controller for managing tosca
 */
@Controller
@RequestMapping("/")
public class RewriteURLController {
	private final Logger log = LoggerFactory.getLogger(RewriteURLController.class);

    @RequestMapping(value = "/{[path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}
