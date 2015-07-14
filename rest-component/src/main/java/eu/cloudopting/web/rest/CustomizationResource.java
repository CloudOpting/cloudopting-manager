package eu.cloudopting.web.rest;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.dto.CustomizationDTO;
import eu.cloudopting.service.CustomizationService;
import eu.cloudopting.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by danielpo on 19/03/2015.
 */
@RestController
@RequestMapping("/api")
public class CustomizationResource  extends AbstractController<Customizations> {
   @Inject
   CustomizationService customizationService;
    /**
     * Default contructor.
     *
     */
    public CustomizationResource() {
        super(Customizations.class);
    }

    @Inject
    private BpmnService bpmnService;


    public BpmnService getBpmnService() {
        return bpmnService;
    }


    @Override
    protected BaseService<Customizations> getService() {
        return customizationService;
    }

    @RequestMapping(value = "/customization/{id}", method = RequestMethod.GET)
    @ResponseBody
    public final Customizations findOne(@PathVariable("id") final Long id, final UriComponentsBuilder uriBuilder,
                                      final HttpServletResponse response) {
        return findOneInternal(id);
    }

    @RequestMapping(value = "/customization", method = RequestMethod.GET)
    @ResponseBody
    public final List<Customizations> findAll(final UriComponentsBuilder uriBuilder,
                                        final HttpServletResponse response, final HttpServletRequest request) {
        return findAllInternal(request, uriBuilder, response);
    }

    @RequestMapping(value="/customization",method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final void create(@RequestBody CustomizationDTO customizationDTO, final UriComponentsBuilder uriBuilder,
                             final HttpServletResponse response, final HttpServletRequest request) {
        getBpmnService().createCustomization(customizationDTO);
    }

    @RequestMapping(value="/customization/{customizationId}",method = RequestMethod.DELETE,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final void delete(@PathVariable String customizationId, final UriComponentsBuilder uriBuilder,
                             final HttpServletResponse response, final HttpServletRequest request) {
        getBpmnService().deleteCustomization(customizationId);
    }


    @RequestMapping(value="/customization/{customizationId}",method = RequestMethod.PUT,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final void update(@RequestBody CustomizationDTO customizationDTO,@PathVariable String customizationId, final UriComponentsBuilder uriBuilder,
                             final HttpServletResponse response, final HttpServletRequest request) {
        getBpmnService().updateCustomization(customizationDTO);
    }
}
