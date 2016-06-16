package eu.cloudopting.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.events.api.entity.BaseEntity;

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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "applicationId", cascade = CascadeType.ALL)
    private Set<ApplicationMedia> applicationMedias = new HashSet<ApplicationMedia>();

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
	
	/**
	 * The path in JackRabbit where the Application Logo is saved
	 */
	@Column(name = "application_logo_ref")
    private String applicationLogoReference;

	@Column(name = "application_tosca_template")
    private String applicationToscaTemplate;

	@Column(name = "application_version", length = 10)
    private String applicationVersion;

	@Column(name = "short_description")
    private String shortDescription;
	
    @Column(name = "application_subscriber_mail")
    private String applicationSubscriberMail;
	
    @Column(name = "application_sp_mail")
    private String applicationSpMail;
    
    @Column(name = "application_is_tryable")
    private Boolean applicationIsTryable;
    
	@Column(name = "terms")
    private String terms;
	
	@Column(name = "service_price")
    private String servicePrice;
	
	@Column(name = "platform_price")
    private String platformPrice;
	
	@Column(name = "image_ref")
    private String imageRef;
	
	@ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "id")
	private ApplicationSize size;
	
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

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getApplicationSubscriberMail() {
		return applicationSubscriberMail;
	}

	public void setApplicationSubscriberMail(String applicationSubscriberMail) {
		this.applicationSubscriberMail = applicationSubscriberMail;
	}

	public String getApplicationSpMail() {
		return applicationSpMail;
	}

	public void setApplicationSpMail(String applicationSpMail) {
		this.applicationSpMail = applicationSpMail;
	}

	public Boolean getApplicationIsTryable() {
		return applicationIsTryable;
	}

	public void setApplicationIsTryable(Boolean applicationIsTryable) {
		this.applicationIsTryable = applicationIsTryable;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(String servicePrice) {
		this.servicePrice = servicePrice;
	}

	public String getPlatformPrice() {
		return platformPrice;
	}

	public void setPlatformPrice(String platformPrice) {
		this.platformPrice = platformPrice;
	}

	public ApplicationSize getSize() {
		return size;
	}

	public void setSize(ApplicationSize size) {
		this.size = size;
	}

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public String getApplicationLogoReference() {
		return applicationLogoReference;
	}

	public void setApplicationLogoReference(String applicationLogoReference) {
		this.applicationLogoReference = applicationLogoReference;
	}
	
	public Set<ApplicationFile> getAllFiles() {
		String applicationToscaTemplate = this.getApplicationToscaTemplate();
        String applicationLogoReference = this.getApplicationLogoReference();
        Set<ApplicationMedia> applicationMedias = this.getApplicationMedias();
        
        Set<ApplicationFile> out = new HashSet<ApplicationFile>();
        if (applicationToscaTemplate != null) {
        	out.add(new ApplicationFile(applicationToscaTemplate, ApplicationFile.TYPE_TOSCA));
        }
        if (applicationLogoReference != null) {
        	out.add(new ApplicationFile(applicationLogoReference, ApplicationFile.TYPE_PROMO));
        }
        
        for(ApplicationMedia media : applicationMedias) {
        	out.add(new ApplicationFile(media.getMediaContent(), ApplicationFile.TYPE_CONTENT));
        }
        
        return out;
	}
}
