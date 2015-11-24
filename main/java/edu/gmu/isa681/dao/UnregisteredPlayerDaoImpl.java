package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.Player;
import edu.gmu.isa681.model.UnregisteredPlayer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("unregisteredPlayerDao")
public class UnregisteredPlayerDaoImpl extends AbstractDao<Integer, UnregisteredPlayer> implements UnregisteredPlayerDao {

	public Integer isMatchPlayerSso(String sso) {
		return 1;
	}

	public Integer isMatchPlayerEmail(String email) {
		return 1;
	}
	
}
