package eu.cloudopting.web.rest;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import eu.cloudopting.events.api.constants.QueryConstants;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Daniel P.
 */
@RestController
@RequestMapping("/api")
public class ApplicationResource extends AbstractController<Applications> {

    @Autowired
    ApplicationService applicationService;

    /**
     * Default contructor.
     *
     */
    public ApplicationResource() {
        super(Applications.class);
    }


    @Override
    protected BaseService<Applications> getService() {
        return applicationService;
    }

    /**
     * This method returns a list of Applications
     *
     * @param page       Page number
     * @param size       Page size
     * @param sortBy     Sort by field
     * @param sortOrder  Sort order
     * @param filterObj  Search object
     * @param uriBuilder UriComponentsBuilder
     * @param response   HttpServletResponse
     * @return applications list
     */
    @RequestMapping(value = "/application/list", params = {QueryConstants.PAGE, QueryConstants.SIZE,
            QueryConstants.SORT_BY, QueryConstants.SORT_ORDER},
            method = RequestMethod.GET)
    @ResponseBody
    public final Page<Applications> findAllPaginatedAndSorted(
            @RequestParam(QueryConstants.PAGE) final int page,
            @RequestParam(QueryConstants.SIZE) final int size,
            @RequestParam(QueryConstants.SORT_BY) final String sortBy,
            @RequestParam(QueryConstants.SORT_ORDER) final String sortOrder,
            @RequestParam(QueryConstants.FILTER) final String filterObj,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response) {
        return findPaginatedAndSortedWithFilter(page, size, sortBy, sortOrder, filterObj, uriBuilder, response);
    }

    /**
     * This method returns a list of Applications
     *
     * @param uriBuilder UriComponentsBuilder
     * @param response   HttpServletResponse
     * @return applications list
     */
    @RequestMapping(value = "/application/listunpaginated",
            method = RequestMethod.GET)
    @ResponseBody
    public final Page<Applications> findAllPaginated(
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response, final HttpServletRequest request, final Pageable pageable) {
        return findAllInternalPageable(request, uriBuilder, response,pageable);
    }

    /**
     * This method returns a single Applications instance.
     *
     * @param id         the id to be returned
     * @param uriBuilder UriComponentsBuilder
     * @param response   HttpServletResponse
     * @return applications instance
     */
    @RequestMapping(value = "/application/{id}", method = RequestMethod.GET)
    @ResponseBody
    public final Applications findOne(@PathVariable("id") final Long id, final UriComponentsBuilder uriBuilder,
                                               final HttpServletResponse response) {
        return getService().findOne(id);
    }


    /**
     * This method creates a new Applications object
     *
     * @param applications
     * @param uriBuilder
     * @param response
     */
    @RequestMapping(value="/application/create",method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final void create(@RequestBody Applications applications, final UriComponentsBuilder uriBuilder,
                             final HttpServletResponse response, final HttpServletRequest request) {
        String xmlTosca = (String) request.getAttribute("xmlTosca");
        if(xmlTosca!=null && !xmlTosca.equals("")){
            applications.setApplicationToscaTemplate(xmlTosca);
        }
        createInternal(applications, uriBuilder, response);
    }

}
