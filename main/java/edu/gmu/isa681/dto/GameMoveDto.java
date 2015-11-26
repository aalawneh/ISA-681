package edu.gmu.isa681.dto;

public class GameMoveDto {
	private String playerName;
	private int handId;
	private String cardId;
	private int roundId;
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getHandId() {
		return handId;
	}
	public void setHandId(int handId) {
		this.handId = handId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public int getRoundId() {
		return roundId;
	}
	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}
}
