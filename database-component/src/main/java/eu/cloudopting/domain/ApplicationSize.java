package eu.cloudopting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import eu.cloudopting.events.api.entity.BaseEntity;

@Entity
@Table(name = "APPLICATION_SIZE")
@SequenceGenerator(name = "applicationSizeGen", sequenceName = "application_size_id_seq")
public class ApplicationSize implements BaseEntity {

	private static final long serialVersionUID = 6110539543035890023L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicationSizeGen")
    private Long id;
    
    @Column(name = "size", length = 100, unique = true)
    @NotNull
    private String size;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
