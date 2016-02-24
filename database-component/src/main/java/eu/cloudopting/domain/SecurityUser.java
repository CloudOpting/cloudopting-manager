package eu.cloudopting.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User{

	private static final long serialVersionUID = 5678376828773744347L;
	
	private final Long organizationId;
	
	public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, Long organizationId) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.organizationId = organizationId;
	}

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
    		Long organizationId) {
    	super(username, password, true, true, true, true, authorities);
    	this.organizationId = organizationId;
    }

	public Long getOrganizationId() {
		return organizationId;
	}
}
