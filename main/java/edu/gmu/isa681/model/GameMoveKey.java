package edu.gmu.isa681.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class GameMoveKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "PLAYER_ID", nullable = false)
	private Integer playerId;

	@NotNull
	@Column(name = "GAME_ID", nullable = false)
	private Integer gameId;
	
	@NotNull
	@Column(name = "HAND_ID", nullable = false)
	private Integer handId;

	@NotNull
	@Column(name = "CARD_ID", nullable = false)
	private String cardId;
	
	public GameMoveKey() {
		super();
	}
	
	public GameMoveKey(Integer playerId, Integer gameId, Integer handId, String cardId) {
		super();
		this.playerId = playerId;
		this.gameId = gameId;
		this.handId = handId;
		this.cardId = cardId;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public Integer getHandId() {
		return handId;
	}

	public void setHandId(Integer handId) {
		this.handId = handId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
} 

