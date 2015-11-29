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
import edu.gmu.isa681.util.PlayingCardDealer;

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
			List<GamePlayer> playersInGame = gamePlayerDao.getPlayersInGame(gameDto.getGameId());
			
            // pre check to determine that all other players have no cards left.           
            boolean shuffle = true;
            for(int i = 0; i < playersInGame.size(); i++) {
                GamePlayer aPlayer = (GamePlayer) playersInGame.get(i);
                int aPlayerId = aPlayer.getGamePlayerKey().getPlayerId();
                if(aPlayerId != gameDto.getPlayerId()) {
                    List<String> aPlayerCards = gameMoveDao.getPlayerCards(aPlayerId, gameDto.getGameId());
                    if(aPlayerCards != null && !aPlayerCards.isEmpty()) {
                        shuffle = false;
                    }
                }               
            }
           
            if (playersInGame != null && playersInGame.size() == 4 && (playerCards == null || playerCards.isEmpty()) && shuffle 
            		&& !getGameStatusForPlayer(gameToJoin.getGamePlayerKey().getPlayerId()).equals("O")
            		&& !getGameStatusForPlayer(gameToJoin.getGamePlayerKey().getPlayerId()).equals("D")
            		&& !getGameStatusForPlayer(gameToJoin.getGamePlayerKey().getPlayerId()).equals("C")) {
            	
	        	Game currGame = gameDao.findGameById(gameDto.getGameId());
	        	String prevStatus = currGame.getStatus();
	        	currGame.setStatus(GameStatus.DECKSHUFFLING.getStatus());
	        	gameDao.save(currGame);

	        	String[] cards = PlayingCardDealer.shuffle();

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
			        	
			        	if(PlayingCardDealer.TWO_CLUBS.equals(cards[i])) {
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

	        	currGame.setStatus(prevStatus);
	        	gameDao.save(currGame);
			}
			
			if(playersInGame != null && playersInGame.size() == 4 && whoseTurnId < 0 
            		&& !getGameStatusForPlayer(gameToJoin.getGamePlayerKey().getPlayerId()).equals("O")
            		&& !getGameStatusForPlayer(gameToJoin.getGamePlayerKey().getPlayerId()).equals("D")
            		&& !getGameStatusForPlayer(gameToJoin.getGamePlayerKey().getPlayerId()).equals("C")) {
				// We need to figure out whose playing next - based on max(roundId)
				// else check to see who has the 2 clubs, if no player played in current round

				playerCards = PlayingCardDealer.sort(playerCards);
				
				cardsInRound = cardsInCurrRound(gameToJoin.getGamePlayerKey().getGameId());

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
						int index = PlayingCardDealer.loser(cardsInRound);
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
					gameDto.setGameMsg(whoseTurn.getSsoId() + ": must start the game with the two of clubs.<br>" + gamePlayerDao.getGameMessage(gameToJoin.getGamePlayerKey().getPlayerId(), gameToJoin.getGamePlayerKey().getGameId()));
				} else {
					gameDto.setGameMsg(gamePlayerDao.getGameMessage(gameToJoin.getGamePlayerKey().getPlayerId(), gameToJoin.getGamePlayerKey().getGameId()));
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
	
	public List<String> cardsInCurrRound(int gameId) {
		
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

	public List<String> cardsInRoundById(int gameId, int roundId) {
		
		List<Object[]> result = gameMoveDao.getRoundById(gameId, roundId);
		
		List<String> cardsInRound = new ArrayList<String>();
		
		for (Object[] tuple : result) {
			cardsInRound.add((String)tuple[2]);
		}
		
		for(String card_id : cardsInRound) {
		    System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ       cards_id = " + card_id);
		}
		
		return cardsInRound;
    }
	
	public void setCheaterMsg(int playerId, int gameId, String gameMsg) {
		gamePlayerDao.updateGameMessage(playerId, gameId, gameMsg);
	}

	public int play(int playerId, int gameId, String cardId) {
				
		// We need to change the status of the card 
		// if round_id has some value then this indicate that the card has already been played in that round
		
		Integer currHand = gameMoveDao.getCurrHand(gameId);
		Integer currRound = currRound(gameId);
		List<Integer> playersInRound = playersInRound(gameId);
	    int totalPlayersInRound = playersInRound.size();
	    List<GameMoveDto> gameMoves = getGameMoves(playerId);
		Player p = playerDao.findPlayerById(playerId);
		List<String> pCards = getPlayerCards(playerId,gameId);
		List<String> cardsInRound = cardsInCurrRound(gameId);

		System.out.println("+++++++++++++++++ current roundId = " + currRound + " for handId = " + currHand + " and gameId = " + gameId);
		System.out.println("+++++++++++++++++ current card = " + cardId);
		System.out.println("+++++++++++++++++ totalplayersinround = " + totalPlayersInRound);
		
		//validate card is in player's hand
		//validate card has not yet been played
		
		if(currRound == null) {
			//check card validity for first round
			if (!cardId.equals("2 clubs")) {  //first card must be two clubs
				return -1;
			}
		} else if (currRound == 1) {
			//no player can play point card in first round
			if (cardId.contains("hearts") || cardId.contains("q spades")){
				gamePlayerDao.updateGameMessage(playerId, gameId, p.getSsoId() + ": cannot play point card in first round.");
				return -1;
			}
		} else if (totalPlayersInRound == 4) {
			//check card validity for first person in all other rounds
			//cannot start with heart unless heart or queen of spades has been played
			//however, queen of spades can be played at any time
			if (cardId.contains("hearts")){
				int hearts = 0;
				for (int i = 0; i < gameMoves.size(); i++) {
					if (gameMoves.get(i).getCardId().contains("hearts") || gameMoves.get(i).getCardId().contains("q spades")) {
						hearts = 1;
						i = gameMoves.size();
					}
				}
				if (hearts == 0) {
					gamePlayerDao.updateGameMessage(playerId, gameId, p.getSsoId() + ": cannot start with that card if no point cards have been played.");
					return -1;
				}
			}
		} else {
			//must play same suit if player has it in their hand
			System.out.println("+++++++++++++++++ cardsinround = " + cardsInRound);
			//check if same suit as first card
			if(!cardId.substring(cardId.lastIndexOf(" ") + 1).equals(cardsInRound.get(0).substring(cardsInRound.get(0).lastIndexOf(" ") + 1))){
				int match = 0;

				//if not same suit, then ensure that player does not have a matching suit in their hand
				for (int i = 0; i < pCards.size(); i++) {
					if(pCards.get(i).substring(pCards.get(i).lastIndexOf(" ") + 1).equals(cardsInRound.get(0).substring(cardsInRound.get(0).lastIndexOf(" ") + 1))){
						match = 1;
						i = pCards.size();
					}
				}
				
				//prevent player from using card
				if (match == 1) {
				    gamePlayerDao.updateGameMessage(playerId, gameId, p.getSsoId() + ": must play suit: " + cardsInRound.get(0).substring(cardsInRound.get(0).lastIndexOf(" ") + 1));
				    return -1;
				}
			}
		}
		
		//update round criteria
		
		//card is ok.  progress the round.
		if(currRound == null) {
			currRound = 1;
		}
		else if(totalPlayersInRound == 4) {
			// everytime the current player count who played a round reaches 4 then start a new round
			currRound += 1;
		} else if (currRound == 13 && totalPlayersInRound == 4) {
			//Add TOCTOU Game Status for Checking Score.  We do this before playing the last card so that 
			// if a user refreshes the browser and sees that no one has any cards, then they will trigger
			// the shuffle and deal in playAGame
        	Game currGame = gameDao.findGameById(gameId);
        	currGame.setStatus(GameStatus.CALCULATING.getStatus());
        	gameDao.save(currGame);			
		}

		//play card
		gameMoveDao.updateCardStatus(playerId, gameId, currHand, cardId, currRound);
		
		if (endPlay(gameId) != 1) {
			//if game not complete, set it back to "started"
        	Game currGame = gameDao.findGameById(gameId);
        	currGame.setStatus(GameStatus.STARTED.getStatus());
        	gameDao.save(currGame);						
		}
		
		return 0;
	}
	
	//if 
	public int endPlay(int gameId) {
	//return -1 if not end of hand.
	//return 0 if hand ended
	//return 1 if hand ended and game over

		Integer currRound = currRound(gameId);
		List<Integer> playersInRound = playersInRound(gameId);
	    int totalPlayersInRound = playersInRound.size();
		
		//determine if hand is over
		if (currRound == 13 && totalPlayersInRound == 4) {
			//update scores
			updateScores(gameId);
		
		    //check if end of game
			if (isGameOver(gameId) == 1) {
		
		        //if game over then declare winner
				determineWinner(gameId);  //uncertain if we want to do anything else with this data
		
		        //if game over then change game status to over
	        	Game currGame = gameDao.findGameById(gameId);
	        	currGame.setStatus(GameStatus.OVER.getStatus());
	        	gameDao.save(currGame);
				return 1;
			}
			return 0;
		}
		
		return -1;
	}
	
	public int isGameOver(int gameId) {
		//get all users in game
		List<Integer> players;
		players = playersInRound(gameId);
		
		//look at scores, if one user is above 100, then game over
		for (int i = 0; i < players.size(); i++) {
    		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(players.get(i));
			if (playerOpenGame.getScore() >= 100) {
				return 1;
			}
		}
		
		//game not over yet
		return 0;
	}
	
	public List<Integer> determineWinner(int gameId) {
		List<Integer> winners = new ArrayList<Integer>();
		int min = 1000;
		
		//get all users in game
		List<Integer> players;
		players = playersInRound(gameId);
		
		//look at scores, find min value, if better than current min, clear list.  if equal to current min, add to list
		for (int i = 0; i < players.size(); i++) {
    		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(players.get(i));
			if (playerOpenGame.getScore() < min) {
				min = playerOpenGame.getScore();
				winners.clear();
				winners.add(players.get(i));
			} else if (playerOpenGame.getScore() == min) {
				winners.add(players.get(i));
			}
		}
		
		//go through winning users and print message telling who won
		for (int i = 0; i < players.size(); i++) {
    		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(players.get(i));
    		if (playerOpenGame.getScore() == min) {
				Player p = playerDao.findPlayerById(players.get(i));
				setCheaterMsg(players.get(i), gameId, p.getSsoId() + ": WINNER!!");
			} else {
				Player p = playerDao.findPlayerById(players.get(i));
				setCheaterMsg(players.get(i), gameId, p.getSsoId() + ": LOSER.");				
			}
		}
	
		return winners;
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
	
	public void updateScores(int gameId) {

    	Integer currRound = currRound(gameId);
		List<String> cardsInRound; 
		int totalPlayersInRound;
		List<Integer> players;
		List<Integer> scoresForPlayers = new ArrayList<Integer>();
		int loser;
		int loserPoints;
		
		scoresForPlayers.add(0);
		scoresForPlayers.add(0);
		scoresForPlayers.add(0);
		scoresForPlayers.add(0);
    	
    	if (currRound == 13) {
    		//check to see if everyone has played
    		players = playersInRound(gameId);
    	    totalPlayersInRound = players.size();
    	    if (totalPlayersInRound == 4) {
    		
	    		//for each round, calculate the points and assign them to the round loser
	    		for (int i = 1; i <= 13; i++) {
	    			cardsInRound = cardsInRoundById(gameId,i);
	    			loser = PlayingCardDealer.loser(cardsInRound);
	    			for (int j = 0; j < totalPlayersInRound; j++) {
	    				if (loser == players.get(j)) {
	    				    loserPoints = scoresForPlayers.get(j) + PlayingCardDealer.loserPoints(cardsInRound);
	    				    scoresForPlayers.set(j, loserPoints);
	    				}
	    			}
	    		}
	    		
	    		//if one player has 26, then subtract 26 from their score and add 26 to everyone else's score
	    		for (int i = 0; i < totalPlayersInRound; i++) {
	    			if (scoresForPlayers.get(i) == 26) {
	    				Player p = playerDao.findPlayerById(players.get(i));
	    				setCheaterMsg(players.get(i), gameId, p.getSsoId() + ": RAN THE TABLE!!");
	    				for (int j = 0; j < totalPlayersInRound; j++) {
	    					if (i == j) {
		    				    scoresForPlayers.set(j, 0);
	    					} else {
		    				    scoresForPlayers.set(j, 26);	    						
	    					}
	    				}
	    				//break out of loop if we found 26
	    				i = totalPlayersInRound;  
	    			}
	    		}
	    		
	    		//update everyone's score in the DB
	    		for (int i = 0; i < totalPlayersInRound; i++) {
		    		//add scores to current values
	        		GamePlayer playerOpenGame = gamePlayerDao.getPlayerOpenGame(players.get(i));
		    		loserPoints = scoresForPlayers.get(i) + playerOpenGame.getScore();
		    		//update scores in gamePlayerDao
	    			gamePlayerDao.updatePlayerScore(players.get(i), gameId, loserPoints);
	    		} 
    	    }
    	}
	}
}