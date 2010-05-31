package de.mxro.utils.domain.authentication;

import java.util.Vector;

public interface User {
	public String getEmail();
	public String getPassword();
	
	public Vector<Identity> getIdentities();
	
}
