package edu.gmu.isa681.controller;

import org.springframework.security.core.context.SecurityContextHolder;

import edu.gmu.isa681.service.AuthenticatedPlayer;

public class LoggedInPlayer {

	public static int getLoggedInPlayerId() {
	    return getAuthenticatedPlayer().getPlayerId();
	}

	public static String getLoggedInPlayerName() {
	    return getAuthenticatedPlayer().getPlayerName();
	}

	private static AuthenticatedPlayer getAuthenticatedPlayer() {
	    return (AuthenticatedPlayer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/*
    private static String getPrincipal(){
        String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
    */
}

