package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.Player;

public interface PlayerDao {

	public void save(Player player);

	public Player findPlayerById(int id);

	public Player findBySSO(String sso);
	
	public Player findByEmail(String email);
	
	public Integer isAvailPlayerSSO(String sso);
	
	public Integer isAvailPlayerEmail(String email);
}