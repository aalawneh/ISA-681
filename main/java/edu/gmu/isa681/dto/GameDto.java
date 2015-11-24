package edu.gmu.isa681.dto;

import java.util.List;

public class GameDto {
	private int playerId;
	private int gameId;
	private String gameStatus;
	private int handId;
	private int playerPosition;
	private String playerWon;
	private int playerScore;
	private List<String> playerCards;
	
	private List<OpponentsDto> opponents;
	private StringBuffer playersScores;
	
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
	public int getHandId() {
		return handId;
	}
	public void setHandId(int handId) {
		this.handId = handId;
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
}
