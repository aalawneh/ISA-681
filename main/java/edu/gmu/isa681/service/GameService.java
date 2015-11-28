package edu.gmu.isa681.service;

import java.util.List;

import edu.gmu.isa681.dto.GameDto;
import edu.gmu.isa681.dto.GameMoveDto;
import edu.gmu.isa681.dto.PlayerGamesDto;

public interface GameService {

	public PlayerGamesDto getPlayerGamesResults(int playerId);
	
	public GameDto joinAGame(int playerId);
		
	public String getGameStatusForPlayer(int playerId);
	
	public int play(int playerId, int gameId, String cardId);
	
	public void setCheaterMsg(int playerId, int gameId, String gameMsg);
	
	public List<GameMoveDto> getGameMoves(int playerId);
	
	public List<String> getPlayerCards(int playerId, int gameId);
}
