package edu.gmu.isa681.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "GAME_PLAYER")
public class GamePlayer implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private GamePlayerKey gamePlayerKey;

	@NotNull
	@Column(name = "POSITION", nullable = false)
	private Integer position;

	@NotNull
	@Column(name = "SCORE", nullable = false)
	private Integer score;
	
	public GamePlayer() {
		super();
	}
	
	public GamePlayer(GamePlayerKey gamePlayerKey, Integer position, Integer score) {
		super();
		this.gamePlayerKey = gamePlayerKey;
		this.position = position;
		this.score = score;
	}

	public GamePlayerKey getGamePlayerKey() {
		return gamePlayerKey;
	}

	public void setGamePlayerKey(GamePlayerKey gamePlayerKey) {
		this.gamePlayerKey = gamePlayerKey;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gamePlayerKey == null) ? 0 : gamePlayerKey.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
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
		GamePlayer other = (GamePlayer) obj;
		if (gamePlayerKey == null) {
			if (other.gamePlayerKey != null)
				return false;
		} else if (!gamePlayerKey.equals(other.gamePlayerKey))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GamePlayer [gamePlayerKey=" + gamePlayerKey + ", position=" + position + ", score=" + score
				+ ", getGamePlayerKey()=" + getGamePlayerKey() + ", getPosition()=" + getPosition() + ", getScore()="
				+ getScore() + ", hashCode()=" + hashCode() + ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}

}