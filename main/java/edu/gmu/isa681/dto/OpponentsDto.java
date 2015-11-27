package edu.gmu.isa681.dto;

public class OpponentsDto {
	
	private int playerId;
	private String playerSso;
	private String firstName;
	private String lastName;
	private int position;
	private int score;
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getPlayerSso() {
		return playerSso;
	}
	public void setPlayerSso(String playerSso) {
		this.playerSso = playerSso;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
