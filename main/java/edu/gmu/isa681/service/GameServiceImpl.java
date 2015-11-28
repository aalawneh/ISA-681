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
import edu.gmu.isa681.dto.GameMoveDto;
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

@Service("gameService")
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
	
	public String getGameStatusForPlayer(int playerId) {
		String gameStatus = "";

		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(playerId);

		if(playerOpenGame != null) {
			gameStatus = gameDao.findGameById(playerOpenGame.getGamePlayerKey().getGameId()).getStatus();
		}
		return gameStatus;
	}	
	
	public GameDto joinAGame(int playerId) {
		GameDto gameDto = null;
		
		// 1. Check if the player already in a game and the game is not over, then return the GameDto.
		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(playerId);
		
		// 2. Else check if there is an open game then have him join the game and return GameDto.
		GamePlayer gameToJoin = new GamePlayer();
		
		List<String> cardsInRound = null;
		
		int twoofclubs = 0;

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
				dto.setPlayerSso(p.getSsoId());
				dto.setFirstName(p.getFirstName());
				dto.setLastName(p.getLastName());
				dto.setPosition(g.getPosition());		
				dto.setScore(g.getScore());
				
				opponents.add(dto);				
			}
			
			// get player cards
			List<String> playerCards = gameMoveDao.getPlayerCards(gameDto.getPlayerId(), gameDto.getGameId());
			
			int whoseTurnId = -1;
			List<GamePlayer> playersInGame = gamePlayerDao.getPlayersInGame(gameToJoin.getGamePlayerKey().getGameId());
			
			if (playersInGame != null && playersInGame.size() == 4 && (playerCards == null || playerCards.isEmpty())) {

	        	String[] cards = CardShuffler.shuffle();

	        	// what is the current hand?
	        	Integer currHand = gameMoveDao.getCurrHand(gameToJoin.getGamePlayerKey().getGameId());
	        	if(currHand == null) 
	        		currHand = 1;
	        	else
	        		currHand += 1;

	        	int start = 0, end = 13;
	        	for(GamePlayer player : playersInGame) {
	        		
					System.out.println("+++++++++++++++++++++++++ cards for player id = " + player.getGamePlayerKey().getPlayerId());
					List<String> tempPlayerCards = new ArrayList<String>();
					for(int i = start; i < end; i++) {
	        			System.out.println("+++++++++++++++++++++++++ cards for card id = " + cards[i]);
			        	GameMoveKey gMoveKey = new GameMoveKey();
			        	gMoveKey.setPlayerId(player.getGamePlayerKey().getPlayerId());
			        	gMoveKey.setGameId(player.getGamePlayerKey().getGameId());
			        	
			        	gMoveKey.setHandId(currHand);  
			        	gMoveKey.setCardId(cards[i]);
			        	
			        	if(CardShuffler.TWO_CLUBS.equals(cards[i])) {
			        		whoseTurnId = player.getGamePlayerKey().getPlayerId();
			        		//whoseTurnName = player.getGamePlayerKey().getPlayerId()Id();
			        	}
			        	
			        	GameMove gMove = new GameMove();
			        	gMove.setGameMoveKey(gMoveKey); 
			        	gameMoveDao.save(gMove);
			        	
			        	tempPlayerCards.add(cards[i]);
	        		}
	        		
	        		start = end;
	        		end += 13;
	        		
	        		if(player.getGamePlayerKey().getPlayerId() == gameToJoin.getGamePlayerKey().getPlayerId()) {
	        			playerCards.addAll(tempPlayerCards);
	        		}
	        	}
	        	
			}
			
			if(playersInGame != null && playersInGame.size() == 4 && whoseTurnId < 0) {
				// We need to figure out whose playing next - based on max(roundId)
				// else check to see who has the 2 clubs, if no player played in current round

				playerCards = CardShuffler.sort(playerCards);
				
				cardsInRound = cardsInRound(gameToJoin.getGamePlayerKey().getGameId());

				Integer currRound = currRound(gameToJoin.getGamePlayerKey().getGameId());
				if(currRound == null) {
					// then who has 2 clubs start first.
					int currHand = gameMoveDao.getCurrHand(gameToJoin.getGamePlayerKey().getGameId());
					whoseTurnId = gameMoveDao.whoHasTwoClubs(gameToJoin.getGamePlayerKey().getGameId(), currHand);
					twoofclubs=1;
				}
				else {
					GamePlayer whoseNext = null;
					twoofclubs=0;
					List<Integer> playersInRound = playersInRound(gameToJoin.getGamePlayerKey().getGameId());
					if(playersInRound.size() == 4) {
						// who lost will be first						
						
						// therefore player at location i lost, then he get to start
						int index = CardShuffler.loser(cardsInRound);
						whoseTurnId = playersInRound.get(index);
						System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW loser is " + whoseTurnId);
					}
					else {
						int lastPrayerId = playersInRound.get(playersInRound.size()-1);
						GamePlayer aPlayer = gamePlayerDao.getPlayerInGame(gameToJoin.getGamePlayerKey().getGameId(), lastPrayerId);
						
						if(aPlayer.getPosition() == 3) {
							whoseNext = gamePlayerDao.getPlayerPositionInGame(gameToJoin.getGamePlayerKey().getGameId(), 0);
						}
						else {
							whoseNext = gamePlayerDao.getPlayerPositionInGame(gameToJoin.getGamePlayerKey().getGameId(), aPlayer.getPosition()+1);
						}
						whoseTurnId = whoseNext.getGamePlayerKey().getPlayerId();
					}
				}						
			}

			if (whoseTurnId >= 0 ){		
				gameDto.setPlayerCards(playerCards);
				gameDto.setWhoseTurnId(whoseTurnId);
				Player whoseTurn = playerDao.findPlayerById(whoseTurnId);			
				System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW whoseTurnId: " + whoseTurnId);
				gameDto.setWhoseTurnName(whoseTurn.getSsoId());
				//gameDto.setWhoseTurnName(whoseTurn.getFirstName() + " " + whoseTurn.getLastName());
				gameDto.setCardsInRound(cardsInRound);
				if (twoofclubs == 1) {
					gameDto.setGameMsg(whoseTurn.getSsoId() + " must start the game with the two of clubs.");
				} else {
					gameDto.setGameMsg("");
				}
			}
		}
		gameDto.setGameStatus(getGameStatusForPlayer(gameToJoin.getGamePlayerKey().getPlayerId()));
		
		return gameDto;
	}
	
	public Integer currRound(int gameId) {
		
		List<Object[]> result = gameMoveDao.getCurrRound(gameId);
		
		Integer currRound = null;
	
		for (Object[] tuple : result) {
		    currRound = ((BigInteger)tuple[0]).intValue();
		    System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ       currRound = " + currRound);
		}
		
		return currRound;
    }		

	public List<Integer> playersInRound(int gameId) {
		
		List<Object[]> result = gameMoveDao.getCurrRound(gameId);
		
		List<Integer> totalPlayersInRound = new ArrayList<Integer>();
		
		for (Object[] tuple : result) {
			totalPlayersInRound.add(((BigInteger)tuple[1]).intValue());
		}
		
		for(Integer playerId : totalPlayersInRound) {
		    System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ       playerId = " + playerId);
		}
		
		return totalPlayersInRound;
    }
	
	public List<String> cardsInRound(int gameId) {
		
		List<Object[]> result = gameMoveDao.getCurrRound(gameId);
		
		List<String> cardsInRound = new ArrayList<String>();
		
		for (Object[] tuple : result) {
			cardsInRound.add((String)tuple[2]);
		}
		
		for(String card_id : cardsInRound) {
		    System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ       cards_id = " + card_id);
		}
		
		return cardsInRound;
    }		

	public void play(int playerId, int gameId, String cardId) {
				
		// We need to change the status of the card 
		// if round_id has some value then this indicate that the card has already been played in that round
		
		Integer currHand = gameMoveDao.getCurrHand(gameId);
		Integer currRound = currRound(gameId);
		List<Integer> playersInRound = playersInRound(gameId);
	    int totalPlayersInRound = playersInRound.size();

		System.out.println("+++++++++++++++++ current roundId = " + currRound + " for handId = " + currHand + " and gameId = " + gameId);
		System.out.println("+++++++++++++++++ current card = " + cardId);
		
		//validate card is in player's hand
		//validate card has not yet been played
		
		//check card validity
		if(currRound == null) {
			if (!cardId.equals("2 clubs")) {  //first card must be two clubs
				return;
			}
		} else if (totalPlayersInRound == 0) {
			//cannot start with heart unless heart has been played
		} else {
			//must play same suit if player has it in their hand
		}
		
		//card is ok.  progress the round.
		if(currRound == null) {
			currRound = 1;
		}
		else if(totalPlayersInRound == 4) {
			// everytime the current player count who played a round reaches 4 then start a new round
			currRound += 1;
		}
		else if(totalPlayersInRound == 3) {
			// this means that this player is going to play his card, making a complete round
			// which means we are ready to calculate who lost.
			
			//TODO: calculate who lost in this block of code.
			// check if the game is over too!!! when some has a score > 100
			List<String> cardsInRound = cardsInRound(gameId);
			int index = CardShuffler.loser(cardsInRound);
			int loserId = playersInRound.get(index); // this is the loser's playerId 
			
			// now update the scores here 
			
			
			// next check if the game is over and update the game status
		}
		
		gameMoveDao.updateCardStatus(playerId, gameId, currHand, cardId, currRound);		
	}
	
	public List<GameMoveDto> getGameMoves(int playerId) {
		
		List<GameMoveDto> gameMoves = new ArrayList<GameMoveDto>();		
		
		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(playerId);
		int gameId = playerOpenGame.getGamePlayerKey().getGameId();
		
		List<Object[]> result = gameMoveDao.getGameMoves(gameId, GameStatus.STARTED.getStatus());
	
		for (Object[] tuple : result) {
			GameMoveDto gameMoveDto = new GameMoveDto();
			gameMoveDto.setPlayerName((String)tuple[0]);
			gameMoveDto.setHandId(((BigInteger)tuple[1]).intValue());   
			gameMoveDto.setCardId((String)tuple[2]);
			gameMoveDto.setRoundId(((BigInteger)tuple[3]).intValue());
			
			gameMoves.add(gameMoveDto);
		}
		
		return gameMoves;
		
	}
	
	public List<String> getPlayerCards(int playerId, int gameId) {
		return gameMoveDao.getPlayerCards(playerId, gameId);
	}
}