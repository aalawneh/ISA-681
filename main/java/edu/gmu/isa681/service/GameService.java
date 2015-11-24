package edu.gmu.isa681.service;

import edu.gmu.isa681.dto.GameDto;
import edu.gmu.isa681.dto.PlayerGamesDto;

public interface GameService {

	public PlayerGamesDto getPlayerGamesResults(int playerId);
	
	public GameDto joinAGame(int playerId);
	
	public void play(int playerId, int gameId, int handId, String cardId);
}
