package eu.cloudopting.events.api.controller;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import eu.cloudopting.events.api.constants.QueryConstants;
import eu.cloudopting.events.api.constants.WebConstants;
import eu.cloudopting.events.api.entity.BaseEntity;
import eu.cloudopting.events.api.events.MultipleResourcesRetrievedEvent;
import eu.cloudopting.events.api.events.PaginatedResultsRetrievedEvent;
import eu.cloudopting.events.api.preconditions.RestPreconditions;
import eu.cloudopting.events.api.exceptions.ResourceNotFoundException;
import eu.cloudopting.events.api.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Internal controller care va efectua operatiile care nu sunt expuse de catre api.
 *
 * @param <T> entitatea care va fi procesata
 */
abstract class AbstractInternalController<T extends BaseEntity> {

    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * The Clazz.
     */
    private Class<T> clazz;
    /**
     * The Event publisher.
     */
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * Instantiates a new Abstract internal controller.
     *
     * @param clazzToSet the clazz to set
     */
    public AbstractInternalController(final Class<T> clazzToSet) {
        super();

        Preconditions.checkNotNull(clazzToSet);
        clazz = clazzToSet;
    }

    // search

    /**
     * Search all internal.
     *
     * @param queryString the query string
     * @return the list
     */
    public List<T> searchAllInternal(@RequestParam(QueryConstants.Q_PARAM) final String queryString) {
        return getService().searchAll(queryString);
    }

    /**
     * Search all internal paginated.
     *
     * @param queryString the query string
     * @param page        the page
     * @param size        the size
     * @return the list
     */
    public List<T> searchAllInternalPaginated(@RequestParam(QueryConstants.Q_PARAM) final String queryString,
                                              final int page,
                                              final int size) {
        return getService().searchPaginated(queryString, page, size);
    }

    // find - one

    /**
     * Find one internal.
     *
     * @param id         the id
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the t
     */
    protected final T findOneInternal(final Long id,
                                      final UriComponentsBuilder uriBuilder,
                                      final HttpServletResponse response) {
        final T resource = findOneInternal(id);
        //  getEventPublisher().publishEvent(new SingleResourceRetrievedEvent<>(clazz, uriBuilder, response));
        return resource;
    }

    /**
     * Find one internal.
     *
     * @param id the id
     * @return the t
     */
    protected final T findOneInternal(final Long id) {
        return RestPreconditions.checkNotNull(getService().findOne(id));
    }


    private void raiseEvent(final UriComponentsBuilder uriBuilder,
                            final HttpServletResponse response) {
        try {
            getEventPublisher().publishEvent(new MultipleResourcesRetrievedEvent<>(clazz, uriBuilder, response));
        } catch (Exception e) {
            if (!e.getMessage().contains("null source")) {
                throw new RuntimeException(e);
            }
        }
    }

    // find - all

    /**
     * Find all internal.
     *
     * @param request    the request
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    protected final List<T> findAllInternal(final HttpServletRequest request,
                                            final UriComponentsBuilder uriBuilder,
                                            final HttpServletResponse response) {
        if (request.getParameterNames().hasMoreElements()) {
            throw new ResourceNotFoundException();
        }

        raiseEvent(uriBuilder, response);
        return getService().findAll();
    }

    protected final Page<T> findAllInternalPageable(final HttpServletRequest request,
                                                    final UriComponentsBuilder uriBuilder,
                                                    final HttpServletResponse response,
                                                    Pageable pageable) {
        if (request.getParameterNames().hasMoreElements()) {
            throw new ResourceNotFoundException();
        }
        try {
            getEventPublisher().publishEvent(new MultipleResourcesRetrievedEvent<>(clazz, uriBuilder, response));
        } catch (Exception e) {
            if (!e.getMessage().contains("null source")) {
                throw new RuntimeException(e);
            }
        }
        return getService().findAll(pageable);
    }

    /**
     * Find all redirect to pagination.
     *
     * @param uriBuilder the uri builder
     * @param response   the response
     */
    protected final void findAllRedirectToPagination(final UriComponentsBuilder uriBuilder,
                                                     final HttpServletResponse response) {
        final String resourceName = clazz.getSimpleName().toLowerCase();
        final String locationValue = uriBuilder.path(WebConstants.PATH_SEP + resourceName).build()
                .encode().toUriString() + QueryConstants.QUESTIONMARK + "page=0&size=10";

        response.setHeader(HttpHeaders.LOCATION, locationValue);
    }

    /**
     * Find paginated and sorted internal.
     *
     * @param page       the page
     * @param size       the size
     * @param sortBy     the sort by
     * @param sortOrder  the sort order
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    protected final List<T> findPaginatedAndSortedInternal(final int page,
                                                           final int size,
                                                           final String sortBy,
                                                           final String sortOrder,
                                                           final UriComponentsBuilder uriBuilder,
                                                           final HttpServletResponse response) {
        final Page<T> resultPage = getService().findAllPaginatedAndSortedRaw(page, size, sortBy, sortOrder);
        if (page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException();
        }
        getEventPublisher().publishEvent(new PaginatedResultsRetrievedEvent<>(clazz, uriBuilder, response, page,
                resultPage.getTotalPages(), size));

        return Lists.newArrayList(resultPage.getContent());
    }

    /**
     * Find paginated internal.
     *
     * @param page       the page
     * @param size       the size
     * @param sortBy     the sort by
     * @param sortOrder  the sort order
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    protected final List<T> findPaginatedInternal(final int page,
                                                  final int size,
                                                  final String sortBy,
                                                  final String sortOrder,
                                                  final UriComponentsBuilder uriBuilder,
                                                  final HttpServletResponse response) {
        final Page<T> resultPage = getService().findAllPaginatedAndSortedRaw(page, size, sortBy, sortOrder);
        if (page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException();
        }
        getEventPublisher().publishEvent(new PaginatedResultsRetrievedEvent<>(clazz, uriBuilder, response, page,
                resultPage.getTotalPages(), size));

        return Lists.newArrayList(resultPage.getContent());
    }

    /**
     * Find all sorted internal.
     *
     * @param sortBy    the sort by
     * @param sortOrder the sort order
     * @return the list
     */
    protected final List<T> findAllSortedInternal(final String sortBy, final String sortOrder) {
        return getService().findAllSorted(sortBy, sortOrder);
    }

    // count

    /**
     * Count internal.
     *
     * @return the long
     */
    protected final long countInternal() {
        // InvalidDataAccessApiUsageException dataEx - ResourceNotFoundException
        return getService().count();
    }

    /**
     * Getter pentru eventpublisher.
     *
     * @return event publisher
     */
    public ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    // generic REST operations

    // count

    /**
     * Counts all  resources in the system.
     *
     * @return long
     */
    @RequestMapping(method = RequestMethod.GET, value = "/count")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public long count() {
        return countInternal();
    }

    /**
     * Metoda trebuie implementata de fiecare controller in parte si trebuie sa returneze serviciul folosit in
     * cadrul controllerului.
     *
     * @return the service
     */
    protected abstract BaseService<T> getService();

    /**
     * @return clazz
     */
    final Class<T> getClazz() {
        return clazz;
    }

    /**
     * Find paginated and sorted internal.
     *
     * @param page       the page
     * @param size       the size
     * @param sortBy     the sort by
     * @param sortOrder  the sort order
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    protected final Page<T> findPaginatedAndSorted(final int page,
                                                   final int size,
                                                   final String sortBy,
                                                   final String sortOrder,
                                                   final UriComponentsBuilder uriBuilder,
                                                   final HttpServletResponse response) {
        final Page<T> resultPage = getService().findAllPaginatedAndSortedRaw(page, size, sortBy, sortOrder);
        if (page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException();
        }
        getEventPublisher().publishEvent(new PaginatedResultsRetrievedEvent<>(clazz, uriBuilder, response, page,
                resultPage.getTotalPages(), size));

        return resultPage;
    }

    /**
     * Find paginated and sorted internal.
     *
     * @param page         the page
     * @param size         the size
     * @param sortBy       the sort by
     * @param sortOrder    the sort order
     * @param filterObject the object to be filtered by
     * @param uriBuilder   the uri builder
     * @param response     the response
     * @return the list
     */
    protected final Page<T> findPaginatedAndSortedWithFilter(final int page,
                                                             final int size,
                                                             final String sortBy,
                                                             final String sortOrder,
                                                             final String filterObject,
                                                             final UriComponentsBuilder uriBuilder,
                                                             final HttpServletResponse response) {
        final Page<T> resultPage = getService().findAllPaginatedAndSortedRawWithFilter(page, size, sortBy, sortOrder, filterObject);
        if (resultPage == null || page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException();
        }
        try {
            getEventPublisher().publishEvent(new PaginatedResultsRetrievedEvent<>(clazz, uriBuilder, response, page,
                    resultPage.getTotalPages(), size));
        } catch (Exception e) {
            if (!e.getMessage().contains("null source")) {
                throw new RuntimeException(e);
            }
        }
        return resultPage;
    }
    /**
     * Find all sorted.
     *
     * @param sortBy     the sort by
     * @param sortOrder  the sort order
     * @param uriBuilder the uri builder
     * @param response   the response
     * @return the list
     */
    protected final List<T> findAllSorted(final String sortBy,
                                          final String sortOrder,
                                          final UriComponentsBuilder uriBuilder,
                                          final HttpServletResponse response) {
        final List<T> resultPage = getService().findAllSorted(sortBy, sortOrder);
        raiseEvent(uriBuilder,response);
        return resultPage;
    }

}
