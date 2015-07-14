package eu.cloudopting.web.rest;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.dto.ActivitiDTO;
import eu.cloudopting.dto.UploadDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.StatusService;
import eu.cloudopting.dto.ApplicationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import eu.cloudopting.events.api.constants.QueryConstants;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.service.BaseService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author Daniel P.
 */
@RestController
@Component
@RequestMapping("/api")
public class ApplicationResource extends AbstractController<Applications> {

    private final Logger log = LoggerFactory.getLogger(ApplicationResource.class);

    @Inject
    private ApplicationService applicationService;

    @Inject
    private StatusService statusService;

    @Inject
    private BpmnService bpmnService;



    /**
     * Default contructor.
     *
     */
    public ApplicationResource() {
        super(Applications.class);
    }



    public StatusService getStatusService(){
        return statusService;
    }


    public BpmnService getBpmnService() {
        return bpmnService;
    }

    @Override
    protected BaseService<Applications> getService() {
        return applicationService;
    }

    @RequestMapping("/log")
    @ResponseBody
    public final String testLog(){
        final Logger log = LoggerFactory.getLogger(ApplicationResource.class);
        log.debug("ApplicationResourceLog");
        return "testlog";
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
     * @param application
     * @param uriBuilder
     * @param response
     */
    @RequestMapping(value="/application",method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO create(@RequestBody ApplicationDTO application, final UriComponentsBuilder uriBuilder,
                             final HttpServletResponse response, final HttpServletRequest request) {
       /* String xmlTosca = (String) request.getAttribute("xmlTosca");
        if(xmlTosca!=null && !xmlTosca.equals("")){
            applications.setApplicationToscaTemplate(xmlTosca);
        }*/


//        createInternal(application, uriBuilder, response);

        return getBpmnService().startPublish(application);
    }

    @RequestMapping(value="/application/{appId}/{processId}/file",method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO upload( HttpServletRequest request) throws IOException {
        UploadDTO dto = new UploadDTO();
        dto.setName(request.getParameter("name"));
        dto.setType(request.getParameter("type"));
        dto.setFile(request.getInputStream());
        return getBpmnService().upload(dto);
    }

    @RequestMapping(value="/application/{idApp}/{processId}/file/{idFile}",method = RequestMethod.PUT,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO updateFile( HttpServletRequest request,@PathVariable String idApp,@PathVariable String idFile) throws IOException {
        UploadDTO dto = new UploadDTO();
        dto.setName(request.getParameter("name"));
        dto.setFileId(idFile);
        dto.setIdApp(idApp);
        return getBpmnService().upload(dto);
    }

    @RequestMapping(value="/application/{idApp}/{processId}/file/{idFile}",method = RequestMethod.DELETE,  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO deleteFile(@PathVariable String idApp,@PathVariable String idFile, HttpServletRequest request) throws IOException {
        UploadDTO dto = new UploadDTO();
        dto.setFileId(idFile);
        dto.setIdApp(idApp);
        return getBpmnService().deleteFile(dto);
    }

}
