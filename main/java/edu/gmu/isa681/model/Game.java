package edu.gmu.isa681.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@Column(name="game_id")
	private int gameId;

	@NotNull
	@Column(name = "status", nullable = false, length = 1)
	private String status = GameStatus.WAITING.getStatus();

	public int getGameId() {
		return gameId;
	}


	public void setGameId(int gameId) {
		this.gameId = gameId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", status=" + status + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
}