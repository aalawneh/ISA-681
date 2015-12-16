package edu.gmu.isa681.dao;

import java.util.List;

import edu.gmu.isa681.model.GameMove;

public interface GameMoveDao {

	public void save(GameMove gameMove);

	public List<String> getPlayerCards(int playerId, int gameId);
	
	public Integer whoHasTwoClubs(int gameId, int handId);
	
	public Integer getCurrHand(int gameId);
	
	public List<Object[]> getCurrRound(int gameId);

	public List<Object[]> getRoundById(int gameId, int roundId);
	
	public void updateCardStatus(int playerId, int gameId, int handId, String cardId, int roundId);

	public List<Object[]> getGameMoves(int gameId, String gameStatus);
	
	public void updatePlayerCard(int playerId, int gameId, int handId, String oldCardId, String newCardId);
}
