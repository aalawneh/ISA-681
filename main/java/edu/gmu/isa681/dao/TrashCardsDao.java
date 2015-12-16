package edu.gmu.isa681.dao;

import java.util.List;

import edu.gmu.isa681.model.TrashCards;

public interface TrashCardsDao {
	
	public List<TrashCards> getAllPlayersTrashCards(int gameId, int handId);
	
	public List<TrashCards> getPlayerTrashCards(int gameId, int handId, int playerId);
	
	public void trashCards(int gameId, int handId, int srcPlayerId, int destPlayerId, String cards);
}
