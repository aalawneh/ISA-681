package edu.gmu.isa681.dao;

public interface UnregisteredPlayerDao {
	
	public Integer isAvailPlayerSso(String sso);

	public Integer isAvailPlayerEmail(String email);
	
}