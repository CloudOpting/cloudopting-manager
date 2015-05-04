package eu.cloudopting.events.api.controller;

import org.springframework.web.util.UriComponentsBuilder;
import eu.cloudopting.events.api.entity.BaseEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * Interfata specializata pentru operatiile Create, Update, Delete.
 *
 * @param <T> entitatea pentru care se vor efectua operatiil
 */
public interface CrudController<T extends BaseEntity> {
    /**
     * Update entity.
     *
     * @param id the id
     * @param resource the resource
     */
    void update(final Long id, final T resource);

    /**
     * Delete entity.
     *
     * @param id the id
     */
    void delete(final Long id);

    /**
     * Create entity.
     *
     * @param resource the resource
     * @param uriBuilder the uri builder
     * @param response the response
     */
    void create(final T resource, final UriComponentsBuilder uriBuilder, final HttpServletResponse response);

    /**
     * Find one.
     *
     * @param id the id
     * @param uriBuilder the uri builder
     * @param response the response
     * @return the t
     */
    T findOne(final Long id, final UriComponentsBuilder uriBuilder, final HttpServletResponse response);
}
