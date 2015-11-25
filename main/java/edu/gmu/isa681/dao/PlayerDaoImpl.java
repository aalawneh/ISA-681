package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.Player;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("playerDao")
public class PlayerDaoImpl extends AbstractDao<Integer, Player> implements PlayerDao {

	public void save(Player player) {
		persist(player);
	}
	
	public Player findPlayerById(int id) {
		return getByKey(id);
	}

	public Player findBySSO(String sso) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("ssoId", sso));
		return (Player) crit.uniqueResult();
	}
	
	public Player findByEmail(String email) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eqOrIsNull("email", email));
		return (Player) crit.uniqueResult();
	}

	public Integer isAvailPlayerSSO(String sso){
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("ssoId", sso));
		if (crit.list().isEmpty())
			return 1;
		return 0;
	}
	
	public Integer isAvailPlayerEmail(String email){
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("email", email));
		if (crit.list().isEmpty())
			return 1;
		return 0;
	}
}
