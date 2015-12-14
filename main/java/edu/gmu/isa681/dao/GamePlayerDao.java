package edu.gmu.isa681.dao;

import java.util.List;

import edu.gmu.isa681.model.GamePlayer;

public interface GamePlayerDao {

	void save(GamePlayer game);

	public List<GamePlayer> getPlayerGamesResults(int playerId);

	public GamePlayer getPlayerInGame(int gameId, int playerId);

	public GamePlayer getPlayerPositionInGame(int gameId, int position);

	public GamePlayer getPlayerOldGame(int gameId, int playerId);
	
	public GamePlayer getPlayerOpenGame(int playerId);

	public List<Object[]> getOpenGame(int playerId);
	
	public List<GamePlayer> getOpponentsInGame(int gameId, int playerId);
	
	public List<GamePlayer> getPlayersInGame(int gameId);
	
	public void updatePlayerScore(int playerId, int gameId, int score);
	
	public String getGameMessage(int playerId, int gameId);

	public void updateGameMessage(int playerId, int gameId, String message);
}