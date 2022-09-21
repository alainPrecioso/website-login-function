package com.aprec.tristan.user.oauth2;

import java.util.Map;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.aprec.tristan.user.User;

@Entity
public class GitHubUser extends User implements OAuth2User {

	@Transient
	private OAuth2User oauth2User;
	
	private int identifier;
	
    public GitHubUser() {
		super();
	}

	public GitHubUser(OAuth2User oauth2User) {
    	super();
        this.oauth2User = oauth2User;
        this.identifier = oauth2User.getAttribute("id");
        super.username = oauth2User.getAttribute("login");
    }
 
	@Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }
 
    @Override
    public String getName() {
        return oauth2User.getAttribute("login");
    }

	public String getEmailAttribute() {
		return oauth2User.getAttribute("email");
	}

	public void setOauth2User(OAuth2User oauth2User) {
		this.oauth2User = oauth2User;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(identifier, oauth2User);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GitHubUser other = (GitHubUser) obj;
		return identifier == other.identifier && Objects.equals(oauth2User, other.oauth2User);
	}

	
	
}
