package edu.gmu.isa681.service;

import edu.gmu.isa681.model.Player;

public interface PlayerService {

	public void save(Player player);

//	public Player findPlayerById(int id);

	public Player findBySso(String sso);
}
