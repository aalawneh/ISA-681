package edu.gmu.isa681.dao;

public interface UnregisteredPlayerDao {
	
	public Integer isMatchPlayerSso(String sso);

	public Integer isMatchPlayerEmail(String email);
	
}