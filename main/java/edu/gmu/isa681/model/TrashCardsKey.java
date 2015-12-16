package edu.gmu.isa681.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class TrashCardsKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "game_id", nullable = false)
	private Integer gameId;
	
	@NotNull
	@Column(name = "hand_id", nullable = false)
	private Integer handId;

	@NotNull
	@Column(name = "srcplayer_id", nullable = false)
	private Integer srcPlayerId;

	@NotNull
	@Column(name = "destplayer_id", nullable = false)
	private Integer destPlayerId;
	
	public TrashCardsKey() {
		super();
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

	public Integer getSrcPlayerId() {
		return srcPlayerId;
	}

	public void setSrcPlayerId(Integer srcPlayerId) {
		this.srcPlayerId = srcPlayerId;
	}

	public Integer getDestPlayerId() {
		return destPlayerId;
	}

	public void setDestPlayerId(Integer destPlayerId) {
		this.destPlayerId = destPlayerId;
	}
}