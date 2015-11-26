package edu.gmu.isa681.dto;

import java.util.List;

public class GameDto {
	private int playerId;
	private int gameId;
	private String gameStatus;
	private int playerPosition;
	private String playerWon;
	private int playerScore;
	private List<String> playerCards;
	
	private int whoseTurnId;
	private String whoseTurnName;
	private List<OpponentsDto> opponents;
	private StringBuffer playersScores;
	private List<String> cardsInRound; 
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}
	public int getPlayerPosition() {
		return playerPosition;
	}
	public void setPlayerPosition(int playerPosition) {
		this.playerPosition = playerPosition;
	}
	public String getPlayerWon() {
		return playerWon;
	}
	public void setPlayerWon(String playerWon) {
		this.playerWon = playerWon;
	}
	public int getPlayerScore() {
		return playerScore;
	}
	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}
	public List<String> getPlayerCards() {
		return playerCards;
	}
	public void setPlayerCards(List<String> playerCards) {
		this.playerCards = playerCards;
	}
	public int getWhoseTurnId() {
		return whoseTurnId;
	}
	public void setWhoseTurnId(int whoseTurnId) {
		this.whoseTurnId = whoseTurnId;
	}
	public String getWhoseTurnName() {
		return whoseTurnName;
	}
	public void setWhoseTurnName(String whoseTurnName) {
		this.whoseTurnName = whoseTurnName;
	}
	public List<OpponentsDto> getOpponents() {
		return opponents;
	}
	public void setOpponents(List<OpponentsDto> opponents) {
		this.opponents = opponents;
	}
	public StringBuffer getPlayersScores() {
		return playersScores;
	}
	public void setPlayersScores(StringBuffer playersScores) {
		this.playersScores = playersScores;
	}
	public List<String> getCardsInRound() {
		return cardsInRound;
	}
	public void setCardsInRound(List<String> cardsInRound) {
		this.cardsInRound = cardsInRound;
	}
}
