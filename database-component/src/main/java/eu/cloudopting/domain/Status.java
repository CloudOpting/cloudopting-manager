package eu.cloudopting.domain;

import eu.cloudopting.events.api.entity.BaseEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "public", name = "status")
@Configurable

public class Status implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "statusId")
    private Set<Applications> applicationss;

    @Column(name = "status", length = 20, unique = true)
    @NotNull
    private String status;

    public Set<Applications> getApplicationss() {
        return applicationss;
    }

    public void setApplicationss(Set<Applications> applicationss) {
        this.applicationss = applicationss;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("applicationss", "customizationss").toString();
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

    public static final EntityManager entityManager() {
        EntityManager em = new Status().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countStatuses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Status o", Long.class).getSingleResult();
    }

    public static List<Status> findAllStatuses() {
        return entityManager().createQuery("SELECT o FROM Status o", Status.class).getResultList();
    }

    public static List<Status> findAllStatuses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Status o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Status.class).getResultList();
    }

    public static Status findStatus(Long id) {
        if (id == null) return null;
        return entityManager().find(Status.class, id);
    }

    public static List<Status> findStatusEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Status o", Status.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<Status> findStatusEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Status o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Status.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Status attached = Status.findStatus(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public Status merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Status merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
