package edu.gmu.isa681.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GAME_MOVE")
public class GameMove implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private GameMoveKey gameMoveKey;

	@Column(name = "round_id", nullable = true)
	private Integer roundId;	
	
	@Column(name = "time_stamp", nullable = true)
	private Integer timestamp;	

	public GameMove() {
		super();
	}
	
	public GameMove(GameMoveKey gameMoveKey, Integer roundId) {
		super();
		this.gameMoveKey = gameMoveKey;
		this.roundId = roundId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameMoveKey == null) ? 0 : gameMoveKey.hashCode());
		result = prime * result + ((roundId == null) ? 0 : roundId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameMove other = (GameMove) obj;
		if (gameMoveKey == null) {
			if (other.gameMoveKey != null)
				return false;
		} else if (!gameMoveKey.equals(other.gameMoveKey))
			return false;
		if (roundId == null) {
			if (other.roundId != null)
				return false;
		} else if (!roundId.equals(other.roundId))
			return false;
		return true;
	}

	public GameMoveKey getGameMoveKey() {
		return gameMoveKey;
	}

	public void setGameMoveKey(GameMoveKey gameMoveKey) {
		this.gameMoveKey = gameMoveKey;
	}

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public Integer getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Integer timestamp) {
		this.timestamp = timestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "GameMove [gameMoveKey=" + gameMoveKey + ", roundId=" + roundId + ", hashCode()=" + hashCode()
				+ ", getGameMoveKey()=" + getGameMoveKey() + ", getRoundId()=" + getRoundId() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}
}





