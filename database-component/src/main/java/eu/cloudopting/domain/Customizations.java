package eu.cloudopting.domain;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import eu.cloudopting.events.api.entity.BaseEntity;

@Configurable
@Entity
@Table(schema = "public",name = "customizations")
public class Customizations implements BaseEntity {

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

	@ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Applications applicationId;

	@ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private Status statusId;

	@Column(name = "customization_tosca_file")
    @NotNull
    private String customizationToscaFile;

	@Column(name = "customization_creation")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date customizationCreation;

	@Column(name = "customization_activation")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date customizationActivation;

	@Column(name = "customization_decommission")
    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date customizationDecommission;

	@Column(name = "username", length = 15, unique = true)
    @NotNull
    private String username;
	
	@Column(name = "process_id", length = 64, unique = true)
    @NotNull
    private String processId;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Applications getApplicationId() {
        return applicationId;
    }

	public void setApplicationId(Applications applicationId) {
        this.applicationId = applicationId;
    }

	public Status getStatusId() {
        return statusId;
    }

	public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

	public String getCustomizationToscaFile() {
        return customizationToscaFile;
    }

	public void setCustomizationToscaFile(String customizationToscaFile) {
        this.customizationToscaFile = customizationToscaFile;
    }

	public Date getCustomizationCreation() {
        return customizationCreation;
    }

	public void setCustomizationCreation(Date customizationCreation) {
        this.customizationCreation = customizationCreation;
    }

	public Date getCustomizationActivation() {
        return customizationActivation;
    }

	public void setCustomizationActivation(Date customizationActivation) {
        this.customizationActivation = customizationActivation;
    }

	public Date getCustomizationDecommission() {
        return customizationDecommission;
    }

	public void setCustomizationDecommission(Date customizationDecommission) {
        this.customizationDecommission = customizationDecommission;
    }

	public String getUsername() {
        return username;
    }

	public void setUsername(String username) {
        this.username = username;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("applicationId", "statusId").toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Customizations().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCustomizationses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Customizations o", Long.class).getSingleResult();
    }

	public static List<Customizations> findAllCustomizationses() {
        return entityManager().createQuery("SELECT o FROM Customizations o", Customizations.class).getResultList();
    }

	public static List<Customizations> findAllCustomizationses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Customizations o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Customizations.class).getResultList();
    }

	public static Customizations findCustomizations(Long id) {
        if (id == null) return null;
        return entityManager().find(Customizations.class, id);
    }

	public static List<Customizations> findCustomizationsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Customizations o", Customizations.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Customizations> findCustomizationsEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Customizations o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Customizations.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Customizations attached = Customizations.findCustomizations(this.id);
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
    public Customizations merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Customizations merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
