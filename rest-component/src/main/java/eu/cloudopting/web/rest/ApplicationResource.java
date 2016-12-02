package eu.cloudopting.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import eu.cloudopting.bpmn.BpmnService;
import eu.cloudopting.domain.ApplicationMedia;
import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.ApplicationFile;
import eu.cloudopting.domain.Organizations;
import eu.cloudopting.domain.User;
import eu.cloudopting.dto.ActivitiDTO;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.dto.MultipleUploadDTO;
import eu.cloudopting.dto.UploadDTO;
import eu.cloudopting.events.api.constants.QueryConstants;
import eu.cloudopting.events.api.controller.AbstractController;
import eu.cloudopting.events.api.events.PaginatedResultsRetrievedEvent;
import eu.cloudopting.events.api.exceptions.ResourceNotFoundException;
import eu.cloudopting.events.api.service.BaseService;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.StatusService;
import eu.cloudopting.service.UserService;
import eu.cloudopting.store.StoreService;


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
    
    @Inject
    private UserService userService;
    
    @Inject
    private StoreService storeService;

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
    
    public UserService getUserService() {
		return userService;
	}
    
    public StoreService getStoreService() {
		return storeService;
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
    @RequestMapping(value = "/application", params = {QueryConstants.PAGE, QueryConstants.SIZE,
            QueryConstants.SORT_BY, QueryConstants.SORT_ORDER}, method = RequestMethod.GET)
    @ResponseBody
    public final Page<Applications> findAllPaginatedAndSorted(
            @RequestParam(QueryConstants.PAGE) final int page,
            @RequestParam(QueryConstants.SIZE) final int size,
            @RequestParam(value = QueryConstants.SORT_BY, defaultValue = "applicationName") final String sortBy,
            @RequestParam(value = QueryConstants.SORT_ORDER, defaultValue = "ASC") final String sortOrder,
            @RequestParam(value = QueryConstants.FILTER, required = false) final String filterObj,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response) {
        final Page<Applications> resultPage = ((ApplicationService)getService()).findForApiGetAll(page, size, sortBy, sortOrder, filterObj);
        if (resultPage == null || page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException();
        }
        try {
            getEventPublisher().publishEvent(new PaginatedResultsRetrievedEvent<>(Applications.class, uriBuilder, response, page,
                    resultPage.getTotalPages(), size));
        } catch (Exception e) {
            if (!e.getMessage().contains("null source")) {
                throw new RuntimeException(e);
            }
        }
        return resultPage;
    }

	/**
     * This method returns a list of Applications
     *
     * @param uriBuilder UriComponentsBuilder
     * @param response   HttpServletResponse
     * @return applications list
     */
    @RequestMapping(value = "/application/unpaginated", method = RequestMethod.GET)
    @ResponseBody
    public final Page<Applications> findAllPaginated(
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response, final HttpServletRequest request, final Pageable pageable) {
        return new PageImpl(findAllSorted("applicationName", "ASC", uriBuilder, response));
    }

    /**
     * This method creates a new Applications object
     *
     * @param application
     * @param uriBuilder
     * @param response
     */
    @RequestMapping(value = "/application", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO create(@RequestBody ApplicationDTO application, final UriComponentsBuilder uriBuilder,
                                    final HttpServletResponse response, final HttpServletRequest request) {
    	 User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
         Organizations org = user.getOrganizationId();
         return getBpmnService().startPublish(application, org);
    }

    /**
     * This method returns the set of files associated to the Application with the provided id
     * @param idApp			The id of the application whose file paths we want to retrieve
     * @param uriBuilder
     * @param response
     * @param request
     * @return The set of of associated files paths.
     */
    @RequestMapping(value = "/application/{idApp}/file", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final @ResponseBody Map<String, Set<ApplicationFile>> getApplicationFiles(@PathVariable("idApp") final Long idApp, final UriComponentsBuilder uriBuilder,
                                               final HttpServletResponse response, final HttpServletRequest request) {
        Applications app = getService().findOne(idApp);
        
        Set<ApplicationFile> allFiles = app.getAllFiles();
        
        Map<String, Set<ApplicationFile>> outObject = new HashMap<String, Set<ApplicationFile>>();
        outObject.put("files", allFiles);
        return outObject;

    }
    
    /**
     * This method returns a single Applications instance.
     *
     * @param id         the id to be returned
     * @param uriBuilder UriComponentsBuilder
     * @param response   HttpServletResponse
     * @return applications instance
     */
    @RequestMapping(value = "/application/{idApp}", method = RequestMethod.GET)
    @ResponseBody
    public final Applications findOne(@PathVariable("idApp") final Long idApp, final UriComponentsBuilder uriBuilder,
                                               final HttpServletResponse response) {
        return ((ApplicationService)getService()).findForApiGetOne(idApp);
    }

    @RequestMapping(value = "/application/{idApp}/{processId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO updateApplication(HttpServletRequest request, @PathVariable Long idApp,
                                               @PathVariable String processId,
                                               @RequestBody ApplicationDTO application) throws IOException {
        //TODO: If idApp and application.getId() are not equals should we throw an exception?
        //TODO: THe processId should be sended to the BPMN.
    	//TODO: Guido: distinguish between various types of updates
        return getBpmnService().updateApplication(application, processId);
    }

    @RequestMapping(value = "/application/{idApp}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO deleteApplication(@PathVariable Long idApp,
                                               HttpServletRequest request) throws IOException {
        ApplicationDTO application = new ApplicationDTO();
        application.setId(idApp);
        return getBpmnService().deleteApplication(application);
    }

    @RequestMapping(value = "/application/{idApp}/{processId}/file", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO upload( HttpServletRequest request, @PathVariable String idApp,
                                     @PathVariable String processId,
                                     @RequestParam("file") MultipartFile file) throws IOException {
    	String isZipFile = request.getParameter("isZipFile");  
    	User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
        Organizations org = user.getOrganizationId();
    	if (isZipFile == null || !isZipFile.equals("true")) {
	    	
			UploadDTO dto = new UploadDTO();
	        dto.setName(request.getParameter("name"));
	        dto.setType(request.getParameter("type"));
	        dto.setProcessId(processId);
	        dto.setIdApp(idApp);
	        dto.setFile(file.getInputStream());
	        dto.setOrg(org);
	        dto.setUser(user);
	        return getBpmnService().upload(dto);
    	}
    	else {
    		File tempFile = Files.createTempFile(null, ".zip").toFile();
    		File tempDir = Files.createTempDirectory(null).toFile();
    		OutputStream out  = new FileOutputStream(tempFile);
    		IOUtils.copy(file.getInputStream(), out);
    		out.close();
    		ZipFile zipFile = new ZipFile(tempFile);
    		List<File> imageList = new ArrayList<File>();
    		
    		 MultipleUploadDTO dto = new MultipleUploadDTO();
		      dto.setType(request.getParameter("type"));
		        dto.setProcessId(processId);
		        dto.setIdApp(idApp);
		        dto.setOrg(org);
		        dto.setUser(user);
    		try {
    			  Enumeration<? extends ZipEntry> entries = zipFile.entries();
    			  while (entries.hasMoreElements()) {
    			    ZipEntry entry = entries.nextElement();
    			    File entryDestination = new File(tempDir,  entry.getName());
    			    if (entry.isDirectory()) {
    			        entryDestination.mkdirs();
    			    } else {
    			        dto.addImage(entry.getName(), zipFile.getInputStream(entry));
    			    }
    			    imageList.add(entryDestination);
    			  }
    			  
    			  return getBpmnService().uploadMultiple(dto);
    			} finally {
    			  zipFile.close();
    			}
    	}
    }

    @RequestMapping(value="/application/{idApp}/{processId}/file/{idFile}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO updateFile( HttpServletRequest request, @PathVariable String idApp,
                                         @PathVariable String processId, @PathVariable String idFile,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        UploadDTO dto = new UploadDTO();
        dto.setName(request.getParameter("name"));
        dto.setFileId(idFile);
        dto.setIdApp(idApp);
        dto.setFile(file.getInputStream());
        dto.setProcessId(processId);
        dto.setIdApp(idApp);
        return getBpmnService().upload(dto);
    }

    @RequestMapping(value="/application/{idApp}/{processId}/file/{idFile}",method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ActivitiDTO deleteFile(HttpServletRequest request, @PathVariable String idApp,
                                        @PathVariable String processId, @PathVariable String idFile ) throws IOException {
        UploadDTO dto = new UploadDTO();
        dto.setFileId(idFile);
        dto.setIdApp(idApp);
        dto.setProcessId(processId);
        return getBpmnService().deleteFile(dto);
    }
    
    //update application logo
    @RequestMapping(value = "/application/{idApp}/updatelogo", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public final boolean updateApplicationLogo(@PathVariable Long idApp, 
    											HttpServletRequest request, 
    											@RequestParam("file") MultipartFile file) throws IOException {
        ApplicationDTO application = new ApplicationDTO();
        application.setId(idApp);
        //the path of the logo image must come from the frontend
        User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
        Organizations org = user.getOrganizationId();
        boolean result = getBpmnService().updateApplicationLogo(application, file, request.getParameter("name"), request.getParameter("type"), user, org);
        
        
        return result;
        //return getBpmnService().deleteApplication(application);
    }
    
  //update tosca file
    @RequestMapping(value = "/application/{idApp}/updatetoscafile", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public final boolean updateToscaFile(@PathVariable Long idApp, 
    											HttpServletRequest request, 
    											@RequestParam("file") MultipartFile file) throws IOException {
        ApplicationDTO application = new ApplicationDTO();
        application.setId(idApp);
        User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
        Organizations org = user.getOrganizationId();
        boolean result = getBpmnService().updateToscaFile(application, file, request.getParameter("name"), request.getParameter("type"), user, org);

        return result;
        //return getBpmnService().deleteApplication(application);
    }
    
    
    //update application metadata
    @RequestMapping(value = "/application/{idApp}/updatemetadata", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public final boolean updateApplicationMetadata(@RequestBody ApplicationDTO application, @PathVariable Long idApp, HttpServletRequest request) {
    	User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
        Organizations org = user.getOrganizationId();
        boolean result = getBpmnService().updateApplicationMetadata(application, user, org);
        return result;
    }
    
  //add media file
    @RequestMapping(value = "/application/{idApp}/mediafile", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public final boolean addMediaFile(@PathVariable Long idApp, 
    											HttpServletRequest request, 
    											@RequestParam("file") MultipartFile file) throws IOException {
        ApplicationDTO application = new ApplicationDTO();
        application.setId(idApp);
        User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
        Organizations org = user.getOrganizationId();
        boolean result = getBpmnService().addMediaFile(application, file, request.getParameter("name"), request.getParameter("type"), user, org);

        return result;
        //return getBpmnService().deleteApplication(application);
    }
    
    //delete media file
    @RequestMapping(value = "/application/{idApp}/mediafile/{idMedia}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public final boolean addMediaFile(@PathVariable Long idApp, @PathVariable Long idMedia, HttpServletRequest request){
    	ApplicationDTO application = new ApplicationDTO();
        application.setId(idApp);
        User user = getUserService().loadUserByLogin(request.getUserPrincipal().getName());
        Organizations org = user.getOrganizationId();
        boolean result = getBpmnService().deleteMediaFile(application, idMedia, user, org);

    	return result;
    }
}
