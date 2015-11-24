package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.Player;
import edu.gmu.isa681.model.UnregisteredPlayer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("unregisteredPlayerDao")
public class UnregisteredPlayerDaoImpl extends AbstractDao<Integer, UnregisteredPlayer> implements UnregisteredPlayerDao {

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
}
