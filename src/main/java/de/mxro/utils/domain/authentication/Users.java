package de.mxro.utils.domain.authentication;

public interface Users {
	public boolean addUser(User user);
	public boolean deleteUser(User user);
	public User getUser(String email);
	
}
