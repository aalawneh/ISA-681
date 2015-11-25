package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.UnregisteredPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("unregisteredPlayerDao")
public class UnregisteredPlayerDaoImpl extends AbstractDao<Integer, UnregisteredPlayer> implements UnregisteredPlayerDao {

	@Autowired
	private PlayerDao dao;

	public Integer isAvailPlayerSso(String sso) {
		return dao.isAvailPlayerSSO(sso);
	}

	public Integer isAvailPlayerEmail(String email) {
		return dao.isAvailPlayerEmail(email);
	}
	
}
