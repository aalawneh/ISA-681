package edu.gmu.isa681.model;


public class GameBoard {
	private int gameId;
	private String cardId;
	private String[] trashCards;
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
	public String[] getTrashCards() {
		return trashCards;
	}
	public void setTrashCards(String[] trashCards) {
		this.trashCards = trashCards;
	}
}
