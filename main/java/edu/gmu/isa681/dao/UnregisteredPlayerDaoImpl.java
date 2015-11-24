package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.UnregisteredPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("unregisteredPlayerDao")
public class UnregisteredPlayerDaoImpl extends AbstractDao<Integer, UnregisteredPlayer> implements UnregisteredPlayerDao {

	@Autowired
	private PlayerDao dao;

	public Integer isMatchPlayerSso(String sso) {
		if (!dao.findListSSO(sso).isEmpty()) {
			return 0;
		}
		return 1;
	}

	public Integer isMatchPlayerEmail(String email) {
		if (!dao.findListEmail(email).isEmpty()) {
			return 0;
		}
		return 1;
	}
	
}
