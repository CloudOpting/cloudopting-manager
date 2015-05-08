package eu.cloudopting.events.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @param <T>  tipul entitatii
 * @param <ID> identificatorul entitatii
 * @author Daniel P.
 */
public interface GenericRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    //
}
