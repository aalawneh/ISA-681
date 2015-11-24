package edu.gmu.isa681.dao;

import java.util.List;

import edu.gmu.isa681.model.GameMove;

public interface GameMoveDao {

	public void save(GameMove gameMove);

	public List<String> getPlayerCards(int playerId, int gameId);
	
	public Integer getHandId(int playerId, int gameId);
	
	public void updateCardStatus(int playerId, int gameId, int handId, String cardId);
}
