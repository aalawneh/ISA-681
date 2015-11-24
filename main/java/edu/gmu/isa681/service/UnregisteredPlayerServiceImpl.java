package edu.gmu.isa681.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gmu.isa681.dao.UnregisteredPlayerDao;
import edu.gmu.isa681.model.UnregisteredPlayer;

@Service("unregisteredPlayerService")
@Transactional
public class UnregisteredPlayerServiceImpl implements UnregisteredPlayerService{

	@Autowired
	private UnregisteredPlayerDao dao;
	
	public String checkPlayer(UnregisteredPlayer uPlayer) {
		String errMsg = "";
		
        if (!uPlayer.getPassword().equals(uPlayer.getPassword2())) {
        	errMsg = "Passwords do not match";
        } if (isValidPlayerFirstName(uPlayer.getFirstName()) == 0) {
        	errMsg = "Invalid First Name.";
        } else if (isValidPlayerLastName(uPlayer.getLastName()) == 0) {
        	errMsg = "Invalid Last Name.";
        } else if (isValidPlayerSso(uPlayer.getSsoId()) == 0) {
        	errMsg = "Invalid SSO ID.";
        } else if (isMatchPlayerSso(uPlayer.getSsoId()) == 0) {
        	errMsg = "SSO ID already in use.";
        } else if (isValidPlayerPassword(uPlayer.getPassword(), uPlayer.getPassword2()) == 0) {
        	errMsg = "Invalid Password.";
        } else if (isValidPlayerEmail(uPlayer.getEmail()) == 0) {
        	errMsg = "Invalid Email Address.";
        } else if (isMatchPlayerEmail(uPlayer.getEmail()) == 0) {
        	errMsg = "Email Address already in use.";
        }
		
		return errMsg;
	}
	
	public Integer isValidPlayerFirstName(String name) {
		return 1;
	}
	
	public Integer isValidPlayerLastName(String name) {
		return 1;
	}

	public Integer isValidPlayerSso(String sso) {
		return 1;
	}
	
	public Integer isValidPlayerPassword(String pwd1, String pwd2) {
		return 1;
	}

	public Integer isValidPlayerEmail(String email) {
		return 1;
	}

	public Integer isMatchPlayerSso(String sso) {
		return dao.isMatchPlayerSso(sso);
	}

	public Integer isMatchPlayerEmail(String email) {
		return dao.isMatchPlayerEmail(email);
	}

}
