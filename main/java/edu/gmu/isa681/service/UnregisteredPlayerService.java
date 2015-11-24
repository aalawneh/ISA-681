package edu.gmu.isa681.service;

import edu.gmu.isa681.model.UnregisteredPlayer;

public interface UnregisteredPlayerService {

	public String checkPlayer(UnregisteredPlayer uPlayer);

	public Integer isValidPlayerFirstName(String name);
	
	public Integer isValidPlayerLastName(String name);

	public Integer isValidPlayerSso(String sso);
	
	public Integer isValidPlayerPassword(String password);
	
	public Integer isValidPlayerEmail(String email);

	public Integer isMatchPlayerSso(String sso);

	public Integer isMatchPlayerEmail(String email);
	
}
