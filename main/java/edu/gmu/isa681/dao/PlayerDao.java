package edu.gmu.isa681.dao;

import java.util.List;

import edu.gmu.isa681.model.Player;

public interface PlayerDao {

	public void save(Player player);

	public Player findPlayerById(int id);

	public Player findBySSO(String sso);
	
	public Player findByEmail(String email);
	
	public List<Player>  findListSSO(String sso);
	
	public List<Player>  findListEmail(String email);
}