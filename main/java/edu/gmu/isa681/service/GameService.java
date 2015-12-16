package edu.gmu.isa681.service;

import java.util.List;

import edu.gmu.isa681.dto.GameDto;
import edu.gmu.isa681.dto.GameMoveDto;
import edu.gmu.isa681.dto.PlayerGamesDto;

public interface GameService {

	public PlayerGamesDto getPlayerGamesResults(int playerId);

	public GameDto getGame(int playerId);
	
	public GameDto joinAGame(int playerId);
	
	public GameDto getGameHistory(int gameId, int playerId);
		
	public String getGameStatusForPlayer(int playerId);
	
	public int play(int playerId, int gameId, String cardId);
	
	public void setCheaterMsg(int playerId, int gameId, String gameMsg);
	
	public List<GameMoveDto> getGameMoves(int playerId);
	
	public List<GameMoveDto> getGameOldMoves(int gameId);
	
	public List<String> getPlayerCards(int playerId, int gameId);
	
	public void updateScores(int gameId);
	
	public boolean didAllPlayersTrashCards(int gameId);

	public boolean didPlayerTrashCards(int gameId, int playerId);
	
	public List<String> getPlayerTrashCards(int gameId, int playerId);
	
	public void trashCards(int gameId, int srcPlayerId, String[] trashCards);
}
