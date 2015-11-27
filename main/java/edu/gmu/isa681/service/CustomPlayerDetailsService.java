package edu.gmu.isa681.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gmu.isa681.model.Player;

@Service("customPlayerDetailsService")
public class CustomPlayerDetailsService implements UserDetailsService{

	@Autowired
	private PlayerService playerService;
	
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String ssoId)
			throws UsernameNotFoundException {
		Player player = playerService.findBySso(ssoId);
		System.out.println("Player : "+player);
		if(player==null){
			System.out.println("Player not found");
			throw new UsernameNotFoundException("Username not found"); 
		}
				
        AuthenticatedPlayer authPlayer = new AuthenticatedPlayer(player.getSsoId(), player.getPassword(), player.getState().equals("Active"), true, true, true, getGrantedAuthorities(player));
        authPlayer.setPlayerId(player.getPlayerId());
        authPlayer.setPlayerName(player.getFirstName() + " " + player.getLastName());
        authPlayer.setPlayerSso(player.getSsoId());
        return authPlayer;		
	}

	
	private List<GrantedAuthority> getGrantedAuthorities(Player player){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		//for(UserProfile userProfile : user.getUserProfiles()){
			//System.out.println("UserProfile : "+userProfile);
			authorities.add(new SimpleGrantedAuthority("ROLE_PLAYER"));
		//}
		System.out.print("authorities :"+authorities);
		return authorities;
	}
	
}