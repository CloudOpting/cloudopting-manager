package eu.cloudopting.events.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;
import eu.cloudopting.events.api.entity.BaseEntity;
import eu.cloudopting.events.api.events.AfterResourceCreatedEvent;
import eu.cloudopting.events.api.exceptions.*;
import eu.cloudopting.events.api.exceptions.messages.ServerErrorInfo;
import eu.cloudopting.events.api.preconditions.RestPreconditions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Abstract controller care va fi subcalsata de toate controllerele.
 * Extinde <code>AbstractInternalController</code> care se ocupa de tote operatiile interne.
 *
 * @param <T> entitatea pentru care se vor efectua operatiile.
 */
public abstract class AbstractController<T extends BaseEntity> extends AbstractInternalController<T> {
    /**
     * Default contructor.
     *
     * @param clazzToSet class
     */
    @Autowired
    public AbstractController(final Class<T> clazzToSet) {
        super(clazzToSet);
    }

    /**
     * metoda interna folosita in API pentru a crea o entitate.
     *
     * @param resource   entitatea
     * @param uriBuilder uriBuilder dat ca parametru in <code>publishEvent</code>
     * @param response   HttpServletResponse
     */
    protected final void createInternal(final T resource,
                                        final UriComponentsBuilder uriBuilder,
                                        final HttpServletResponse response) {
        RestPreconditions.checkRequestElementNotNull(resource);
        RestPreconditions.checkRequestState(resource.getId() == null);
        final T existingResource = getService().create(resource);

        // note: mind the autoboxing and potential NPE when the resource has null id at this point
        // (likely when working with DTOs)
        try {
            getEventPublisher().publishEvent(new AfterResourceCreatedEvent<>(getClazz(), uriBuilder, response,
                    existingResource.getId().toString()));
        }catch (Exception e){
            if(!e.getMessage().contains("null source")){
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * metoda interna folosita in API pentru a crea o lista de entitati
     *
     * @param resources  entitatile care se creaza
     * @param uriBuilder
     * @param response
     */
    protected final void createMultipleInternal(final List<T> resources,
                                                final UriComponentsBuilder uriBuilder,
                                                final HttpServletResponse response) {
        RestPreconditions.checkRequestElementNotNull(resources);
        RestPreconditions.checkRequestState(resources.size() > 0);
        for (T resource : resources) {
            createInternal(resource, uriBuilder, response);
        }
    }

    /**
     * metoda interna folosita in API pentru a crea o entitate.
     *
     * @param resource   entitatea
     * @param uriBuilder uriBuilder dat ca parametru in <code>publishEvent</code>
     * @param response   HttpServletResponse
     */
    protected final T createInternalAndReturnResource(final T resource,
                                                      final UriComponentsBuilder uriBuilder,
                                                      final HttpServletResponse response) {
        RestPreconditions.checkRequestElementNotNull(resource);
        RestPreconditions.checkRequestState(resource.getId() == null);
        final T existingResource = getService().create(resource);

        // note: mind the autoboxing and potential NPE when the resource has null id at this point
        // (likely when working with DTOs)
        getEventPublisher().publishEvent(new AfterResourceCreatedEvent<>(getClazz(), uriBuilder, response,
                existingResource.getId().toString()));
        return existingResource;
    }

    // update

    /**
     * Metoda de update entitate.
     * note: the operation is IDEMPOTENT <br/>
     *
     * @param id       id-ul entitatii
     * @param resource - entitatea
     */
    protected final void updateInternal(final long id, final T resource) {
        RestPreconditions.checkRequestElementNotNull(resource);
        RestPreconditions.checkRequestElementNotNull(resource.getId());
        RestPreconditions.checkRequestState(resource.getId() == id);
        RestPreconditions.checkNotNull(getService().findOne(resource.getId()));

        getService().update(resource);
    }

    /**
     * Metoda interna pentru stergerea unei entitati din DB.
     *
     * @param id id-ul entitatii
     */
    protected final void deleteByIdInternal(final long id) {
        // InvalidDataAccessApiUsageException - ResourceNotFoundException
        // IllegalStateException - ResourceNotFoundException
        // DataAccessException - ignored
        getService().delete(id);
    }

    /**
     * Tratam exceptia {@link BadRequestException}.
     * Se preia mesajul din exceptie si se impacheteaza in raspunsul transmis catre client.
     *
     * @param ex  exceptia
     * @param req request-ul
     * @return mesaj pentru client
     * @throws java.io.IOException
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public
    @ResponseBody
    ServerErrorInfo handleBadRequestException(BadRequestException ex,
                                              HttpServletRequest req) throws IOException {
        return new ServerErrorInfo(ex.getMessage(), req.getRequestURL().toString());
    }

    /**
     * Tratam exceptia {@link ConflictException}.
     * Se preia mesajul din exceptie si se impacheteaza in raspunsul transmis catre client.
     *
     * @param ex  exceptia
     * @param req request-ul
     * @return mesaj pentru client
     * @throws java.io.IOException
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public
    @ResponseBody
    ServerErrorInfo handleConflictException(ConflictException ex,
                                            HttpServletRequest req) throws IOException {
        return new ServerErrorInfo(ex.getMessage(), req.getRequestURL().toString());
    }

    /**
     * Tratam exceptia {@link ForbiddenException}.
     * Se preia mesajul din exceptie si se impacheteaza in raspunsul transmis catre client.
     *
     * @param ex  exceptia
     * @param req request-ul
     * @return mesaj pentru client
     * @throws java.io.IOException
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public
    @ResponseBody
    ServerErrorInfo handleForbiddenException(ForbiddenException ex,
                                             HttpServletRequest req) throws IOException {
        return new ServerErrorInfo(ex.getMessage(), req.getRequestURL().toString());
    }

    /**
     * Tratam exceptia {@link EntityNotFoundException}.
     * Se preia mesajul din exceptie si se impacheteaza in raspunsul transmis catre client.
     *
     * @param ex  exceptia
     * @param req request-ul
     * @return mesaj pentru client
     * @throws java.io.IOException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public
    @ResponseBody
    ServerErrorInfo handleEntityNotFoundException(EntityNotFoundException ex,
                                                  HttpServletRequest req) throws IOException {
        return new ServerErrorInfo(ex.getMessage(), req.getRequestURL().toString());
    }

    /**
     * Tratam exceptia {@link ResourceNotFoundException}.
     * Se preia mesajul din exceptie si se impacheteaza in raspunsul transmis catre client.
     *
     * @param ex  exceptia
     * @param req request-ul
     * @return mesaj pentru client
     * @throws java.io.IOException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public
    @ResponseBody
    ServerErrorInfo handleResourceNotFoundException(ResourceNotFoundException ex,
                                                    HttpServletRequest req) throws IOException {
        return new ServerErrorInfo(ex.getMessage(), req.getRequestURL().toString());
    }

}
