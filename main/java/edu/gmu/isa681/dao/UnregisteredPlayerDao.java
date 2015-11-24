package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.UnregisteredPlayer;

public interface UnregisteredPlayerDao {
	
	public Integer isMatchPlayerSso(String sso);

	public Integer isMatchPlayerEmail(String email);
	
}