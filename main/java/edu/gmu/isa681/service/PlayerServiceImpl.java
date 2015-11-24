package edu.gmu.isa681.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gmu.isa681.dao.PlayerDao;
import edu.gmu.isa681.model.Player;

@Service("playerService")
@Transactional
public class PlayerServiceImpl implements PlayerService{

	@Autowired
	private PlayerDao dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	public void save(Player player){
		player.setPassword(passwordEncoder.encode(player.getPassword()));
		dao.save(player);
	}
	
	public Player findPlayerById(int id) {
		return dao.findPlayerById(id);
	}

	public Player findBySso(String sso) {
		return dao.findBySSO(sso);
	}

	public Player findByEmail(String email) {
		return dao.findByEmail(email);
	}
	
}
