package eu.cloudopting.domain;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import eu.cloudopting.events.api.entity.BaseEntity;

@Configurable
@Entity
@Table(schema = "public",name = "application_media")
public class ApplicationMedia implements BaseEntity {

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new ApplicationMedia().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countApplicationMedias() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ApplicationMedia o", Long.class).getSingleResult();
    }

	public static List<ApplicationMedia> findAllApplicationMedias() {
        return entityManager().createQuery("SELECT o FROM ApplicationMedia o", ApplicationMedia.class).getResultList();
    }

	public static List<ApplicationMedia> findAllApplicationMedias(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ApplicationMedia o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ApplicationMedia.class).getResultList();
    }

	public static ApplicationMedia findApplicationMedia(Long id) {
        if (id == null) return null;
        return entityManager().find(ApplicationMedia.class, id);
    }

	public static List<ApplicationMedia> findApplicationMediaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ApplicationMedia o", ApplicationMedia.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ApplicationMedia> findApplicationMediaEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ApplicationMedia o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ApplicationMedia.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ApplicationMedia attached = ApplicationMedia.findApplicationMedia(this.id);
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
    public ApplicationMedia merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ApplicationMedia merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("applicationId").toString();
    }

	@ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    @JsonBackReference
    private Applications applicationId;

	@Column(name = "media_content")
    private byte[] mediaContent;

	public Applications getApplicationId() {
        return applicationId;
    }

	public void setApplicationId(Applications applicationId) {
        this.applicationId = applicationId;
    }

	public byte[] getMediaContent() {
        return mediaContent;
    }

	public void setMediaContent(byte[] mediaContent) {
        this.mediaContent = mediaContent;
    }

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
}
