package edu.gmu.isa681.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class AuthenticatedPlayer extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	private int playerId;
	private String playerName;

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

	public AuthenticatedPlayer(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
					throws IllegalArgumentException {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, true, authorities);
	}
	// setters and getters
}