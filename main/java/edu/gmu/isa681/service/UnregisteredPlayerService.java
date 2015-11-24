package edu.gmu.isa681.service;

public interface UnregisteredPlayerService {

	public Integer isValidPlayerFirstName(String name);
	
	public Integer isValidPlayerLastName(String name);

	public Integer isValidPlayerSso(String sso);

	public Integer isValidPlayerPassword(String pwd1, String pwd2);
	
	public Integer isValidPlayerEmail(String email);
	
}
