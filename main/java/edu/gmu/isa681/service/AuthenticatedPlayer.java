package edu.gmu.isa681.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class AuthenticatedPlayer extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	private int playerId;
	private String playerName;
	private String playerSso;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerSso() {
		return playerSso;
	}

	public void setPlayerSso(String playerSso) {
		this.playerSso = playerSso;
	}


	public AuthenticatedPlayer(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
					throws IllegalArgumentException {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, true, authorities);
	}
	// setters and getters

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + playerId;
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + ((playerSso == null) ? 0 : playerSso.hashCode());
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
		AuthenticatedPlayer other = (AuthenticatedPlayer) obj;
		if (playerId != other.playerId)
			return false;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		if (playerSso == null) {
			if (other.playerSso != null)
				return false;
		} else if (!playerSso.equals(other.playerSso))
			return false;
		return true;
	}
}