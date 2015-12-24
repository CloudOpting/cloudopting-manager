package eu.cloudopting.domain;

import eu.cloudopting.events.api.entity.BaseEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Configurable
@Entity
@Table(schema = "public",name = "applications")
@NamedEntityGraph(name = "graph.api.application.GET",
	attributeNodes = @NamedAttributeNode(value = "organizationId", subgraph = "cloudAccountss"),
	subgraphs = @NamedSubgraph(name = "cloudAccountss", attributeNodes = @NamedAttributeNode("cloudAccountss")))
public class Applications implements BaseEntity {

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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static final EntityManager entityManager() {
        EntityManager em = new Applications().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countApplicationses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Applications o", Long.class).getSingleResult();
    }

	public static List<Applications> findAllApplicationses() {
        return entityManager().createQuery("SELECT o FROM Applications o", Applications.class).getResultList();
    }

	public static List<Applications> findAllApplicationses(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Applications o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Applications.class).getResultList();
    }

	public static Applications findApplications(Long id) {
        if (id == null) return null;
        return entityManager().find(Applications.class, id);
    }

	public static List<Applications> findApplicationsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Applications o", Applications.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Applications> findApplicationsEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Applications o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Applications.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Applications attached = Applications.findApplications(this.id);
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
    public Applications merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Applications merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@OneToMany(mappedBy = "applicationId")
    private Set<ApplicationMedia> applicationMedias;

	@ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organizations organizationId;

	@ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status statusId;

	@Column(name = "application_name", length = 50)
    private String applicationName;
	
	@Column(name = "application_tosca_name", length = 10)
    private String applicationToscaName;

	@Column(name = "application_description")
    private String applicationDescription;

	@Column(name = "application_tosca_template")
    private String applicationToscaTemplate;

	@Column(name = "application_version", length = 10)
    private String applicationVersion;

    @OneToMany(mappedBy = "applicationId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Customizations> customizationss;

    public Set<Customizations> getCustomizationss() {
        return customizationss;
    }

    public void setCustomizationss(Set<Customizations> customizationss) {
        this.customizationss = customizationss;
    }


    public Set<ApplicationMedia> getApplicationMedias() {
        return applicationMedias;
    }

	public void setApplicationMedias(Set<ApplicationMedia> applicationMedias) {
        this.applicationMedias = applicationMedias;
    }

	public Organizations getOrganizationId() {
        return organizationId;
    }

	public void setOrganizationId(Organizations organizationId) {
        this.organizationId = organizationId;
    }

	public Status getStatusId() {
        return statusId;
    }

	public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

	public String getApplicationName() {
        return applicationName;
    }

	public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

	public String getApplicationDescription() {
        return applicationDescription;
    }

	public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

	public String getApplicationToscaTemplate() {
        return applicationToscaTemplate;
    }

	public void setApplicationToscaTemplate(String applicationToscaTemplate) {
        this.applicationToscaTemplate = applicationToscaTemplate;
    }

	public String getApplicationVersion() {
        return applicationVersion;
    }

	public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

	public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("applicationMedias", "customizationss", "statusId", "userId", "organizationId").toString();
    }

	public String getApplicationToscaName() {
		return applicationToscaName;
	}

	public void setApplicationToscaName(String applicationToscaName) {
		this.applicationToscaName = applicationToscaName;
	}
}
