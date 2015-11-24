package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.Player;

import java.util.List;

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

	@SuppressWarnings("unchecked")
	public List<Player> findListSSO(String sso){
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("ssoId", sso));
		return (List<Player>) crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Player> findListEmail(String email){
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("email", email));
		return (List<Player>) crit.list();
	}
}
