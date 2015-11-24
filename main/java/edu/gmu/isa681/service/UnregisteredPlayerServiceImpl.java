package edu.gmu.isa681.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gmu.isa681.dao.UnregisteredPlayerDao;

@Service("unregisteredPlayerService")
@Transactional
public class UnregisteredPlayerServiceImpl implements UnregisteredPlayerService{

	@Autowired
	private UnregisteredPlayerDao dao;
	
	public Integer isValidPlayerFirstName(String name) {
		return dao.isValidPlayerFirstName(name);
	}
	
	public Integer isValidPlayerLastName(String name) {
		return dao.isValidPlayerLastName(name);
	}

	public Integer isValidPlayerSso(String sso) {
		return dao.isValidPlayerSso(sso);
	}

	public Integer isValidPlayerPassword(String pwd1, String pwd2) {
		return dao.isValidPlayerPassword(pwd1, pwd2);
	}
	
	public Integer isValidPlayerEmail(String email) {
		return dao.isValidPlayerEmail(email);
	}
}
