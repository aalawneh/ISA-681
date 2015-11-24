package edu.gmu.isa681.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gmu.isa681.dao.GameDao;
import edu.gmu.isa681.dao.GameMoveDao;
import edu.gmu.isa681.dao.GamePlayerDao;
import edu.gmu.isa681.dao.PlayerDao;
import edu.gmu.isa681.dto.GameDto;
import edu.gmu.isa681.dto.OpponentsDto;
import edu.gmu.isa681.dto.PlayerGamesDto;
import edu.gmu.isa681.model.Game;
import edu.gmu.isa681.model.GameMove;
import edu.gmu.isa681.model.GameMoveKey;
import edu.gmu.isa681.model.GamePlayer;
import edu.gmu.isa681.model.GamePlayerKey;
import edu.gmu.isa681.model.GameStatus;
import edu.gmu.isa681.model.Player;
import edu.gmu.isa681.util.CardShuffler;

@Service("GameService")
@Transactional
public class GameServiceImpl implements GameService {

	@Autowired
	private PlayerDao playerDao;

	@Autowired
	private GameDao gameDao;

	@Autowired
	private GamePlayerDao gamePlayerDao;

	@Autowired
	private GameMoveDao gameMoveDao;
	
	public PlayerGamesDto getPlayerGamesResults(int playerId) {
		List<GamePlayer> games = gamePlayerDao.getPlayerGamesResults(playerId);
		
		if (games == null || games.isEmpty()) {
			return null;
		}
		
		List<GameDto> playerGames = new ArrayList<GameDto>();
		int tempGameId = games.get(0).getGamePlayerKey().getGameId();
		GameDto gameDto = new GameDto();	
		StringBuffer strBuff = new StringBuffer();
		int winnerId = -1;
		int winnerScore = Integer.MAX_VALUE;
		int totalWins = 0;
		
		for(GamePlayer game: games) {
			 
			if(tempGameId != game.getGamePlayerKey().getGameId()) {

				gameDto.setGameId(tempGameId);
				gameDto.setPlayersScores(strBuff);
				if(winnerId == playerId) {
					gameDto.setPlayerWon("Y");
					totalWins += 1;
				}
				playerGames.add(gameDto);

				// Reset everything now
				winnerId = -1;
				winnerScore = Integer.MAX_VALUE;
				tempGameId = game.getGamePlayerKey().getGameId();				
				gameDto = new GameDto(); 
				strBuff = new StringBuffer();
			} 
			
			String opponentName = (playerDao.findPlayerById(game.getGamePlayerKey().getPlayerId())).getLastName();
			strBuff.append(opponentName).append(" [").append(game.getScore()).append("] ");
			if(game.getScore() < winnerScore) {
				winnerScore = game.getScore();
				winnerId = game.getGamePlayerKey().getPlayerId();
			}
		}
		
		/*
		 * This is for a special case, for the very last record "row" in the result set.
		 */
		gameDto.setGameId(tempGameId);
		gameDto.setPlayersScores(strBuff);
		if(winnerId == playerId) {
			gameDto.setPlayerWon("Y");
			totalWins += 1;
		}
		playerGames.add(gameDto);
		/*  End of special case */
		
		PlayerGamesDto playerGamesDto = new PlayerGamesDto();
		playerGamesDto.setGames(playerGames);
		int losses = playerGames.size() - totalWins;
		playerGamesDto.setTotalLosses(losses);
		
		playerGamesDto.setTotalWins(totalWins);
		
		return playerGamesDto;
	}
	
	public GameDto joinAGame(int playerId) {
		GameDto gameDto = null;
		
		// 1. Check if the player already in a game and the game is not over, then return the GameDto.
		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(playerId);
		
		// 2. Else check if there is an open game then have him join the game and return GameDto.
		GamePlayer gameToJoin = new GamePlayer();

		if(playerOpenGame == null) {
			List<Object[]> openGame = gamePlayerDao.getOpenGame(playerId);
			
			if(openGame == null || openGame.isEmpty()) {
				//start a new game. all others are full
				
				Game newGame = new Game();
				int newGameId = gameDao.getGamesCount().intValue();
				newGame.setGameId(newGameId);
				newGame.setStatus(GameStatus.WAITING.getStatus()); // status is waiting for quorum to reach 4
				
				GamePlayerKey gamePlayerKey = new GamePlayerKey();
				gamePlayerKey.setGameId(newGame.getGameId());
				gamePlayerKey.setPlayerId(playerId);
				
				gameToJoin.setGamePlayerKey(gamePlayerKey);
				gameToJoin.setPosition(0);	// first one to join then he sits at position 0
				gameToJoin.setScore(0);
				gameDao.save(newGame);
				gamePlayerDao.save(gameToJoin);
			}
			else {
				//join a game that has players waiting ...
				System.out.println("+++++++++++++++++++++++++ join a game that has players waiting ...");
				Integer currGameId = null;
				Long opponents = null;
				for (Object[] result : openGame) {
					currGameId = (Integer) result[0];
					opponents = (Long) result[1];
					System.out.println("Total opponents in game with id " + currGameId + " is " + opponents);
			    }
				
				GamePlayerKey gamePlayerKey = new GamePlayerKey();
				gamePlayerKey.setGameId(currGameId.intValue());
				gamePlayerKey.setPlayerId(playerId);
				
				gameToJoin.setGamePlayerKey(gamePlayerKey);
				gameToJoin.setPosition(opponents.intValue());
				gameToJoin.setScore(0);
				gamePlayerDao.save(gameToJoin);

				// is quorum == 4? yes - numOfOponents + current player = 4
		        if(opponents.intValue() == 3) {

		        	//update the game status now to S
		        	Game currGame = gameDao.findGameById(currGameId.intValue());
		        	currGame.setStatus(GameStatus.STARTED.getStatus());
		        	gameDao.save(currGame);
		        	
		        	// if quorum = 4 - then we need to shuffle the card between the players for the first time
		        	String[] cards = CardShuffler.shuffle();
		        	
		        	List<GamePlayer> playersInGame = gamePlayerDao.getPlayersInGame(currGameId.intValue());
		        	
		        	int start = 0, end = 13;
		        	for(GamePlayer player : playersInGame) {
		        		
						System.out.println("+++++++++++++++++++++++++ cards for player id = " + player.getGamePlayerKey().getPlayerId());
		        		for(int i = start; i < end; i++) {
		        			System.out.println("+++++++++++++++++++++++++ cards for card id = " + cards[i]);
				        	GameMoveKey gMoveKey = new GameMoveKey();
				        	gMoveKey.setPlayerId(player.getGamePlayerKey().getPlayerId());
				        	gMoveKey.setGameId(player.getGamePlayerKey().getGameId());
				        	gMoveKey.setHandId(1); // it is the first hand
				        	gMoveKey.setCardId(cards[i]);
				        	GameMove gMove = new GameMove();
				        	gMove.setGameMoveKey(gMoveKey); 
				        	gameMoveDao.save(gMove);
		        		}
		        		
		        		start = end;
		        		end += 13;
		        	}
		        }
			}
		}
		else {
			// return him to the game he is already in - he can be on only one open game at a time			
			gameToJoin = playerOpenGame;
		}
		
		if(gameToJoin != null) {
			gameDto = new GameDto();
			gameDto.setGameId(gameToJoin.getGamePlayerKey().getGameId());
			gameDto.setGameStatus(gameDao.findGameById(gameDto.getGameId()).getStatus());
			gameDto.setPlayerId(gameToJoin.getGamePlayerKey().getPlayerId());
			gameDto.setPlayerPosition(gameToJoin.getPosition());
			gameDto.setPlayerScore(gameToJoin.getScore());
			List<OpponentsDto> opponents = new ArrayList<OpponentsDto>();
			gameDto.setOpponents(opponents);
			
			// get the list of opponents
			List<GamePlayer> oppnentsIngame = gamePlayerDao.getOpponentsInGame(gameToJoin.getGamePlayerKey().getGameId(), gameToJoin.getGamePlayerKey().getPlayerId());
			for(GamePlayer g : oppnentsIngame) {
				Player p = playerDao.findPlayerById(g.getGamePlayerKey().getPlayerId());
				System.out.println("++++++++++++++++++++++++++++++++++++++++++" + p.getLastName());
				OpponentsDto dto = new OpponentsDto();
				dto.setPlayerId(p.getPlayerId());
				dto.setFirstName(p.getFirstName());
				dto.setLastName(p.getLastName());
				dto.setPosition(g.getPosition());		
				dto.setScore(g.getScore());
				
				opponents.add(dto);				
			}
			
			// get player cards
			List<String> playerCards = gameMoveDao.getPlayerCards(gameDto.getPlayerId(), gameDto.getGameId());
			
			//if playerCards.size() == 0
			
			gameDto.setPlayerCards(playerCards);
		}
		
		return gameDto;
	}

	public void play(int playerId, int gameId, String cardId) {
		// We need to change the status of the card 
		// if round_id has some value then this indicate that the card has already been played in that round
		
		Integer currHand = gameMoveDao.getCurrHand(playerId, gameId);
		List<Object[]> result = gameMoveDao.getCurrRound(gameId);
		
		Integer currRound = null;
		Integer playerCounter = null; // how many player played in this round so far not including the current player

		for (Object[] item : result) {
			currRound = ((BigInteger) item[0]).intValue();
			playerCounter = ((BigInteger) item[1]).intValue();
	    }		
		System.out.println("+++++++++++++++++ current roundId = " + currRound + " for handId = " + currHand + " and gameId = " + gameId);
		System.out.println("+++++++++++++++++ playerCounter = " + playerCounter);
		
		if(currRound == null) {
			currRound = 1;
		}
		else if(playerCounter == 4) {
			// everytime the current player count who played a round reaches 4 then start a new round
			currRound += 1;
		}
		else if(playerCounter == 3) {
			// this means that this player is going to play his card making a complete round
			// which means we are ready to calculate who lost.
			
			//TODO: calculate who lost in this block of code.
			// check if the game is over too!!! when some has a score > 100
		}
		
		gameMoveDao.updateCardStatus(playerId, gameId, currHand, cardId, currRound);		
	}
}