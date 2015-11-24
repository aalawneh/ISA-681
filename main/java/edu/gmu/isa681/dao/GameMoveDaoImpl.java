package edu.gmu.isa681.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.gmu.isa681.model.GameMove;

@Repository("gameMoveDao")
public class GameMoveDaoImpl extends AbstractDao<Integer, GameMove> implements GameMoveDao {
	
	public void save(GameMove gameMove) {
		persist(gameMove);
		getSession().flush(); 
	}

	public List<String> getPlayerCards(int playerId, int gameId) {
		System.out.println("++++++++++++++++ in getPlayerCards method");
		System.out.println("++++++++++++++++ GameId = " + gameId);
		System.out.println("++++++++++++++++ playerId = " + playerId);

		Criteria crit = getSession().createCriteria(GameMove.class)
				.setProjection(Projections.property("gameMoveKey.cardId"))
				.add(Restrictions.eq("gameMoveKey.gameId", gameId))
				.add(Restrictions.eq("gameMoveKey.playerId", playerId))
				.add(Restrictions.isNull("roundId"));
		//crit.addOrder(Order.asc("gameMoveKey.cardId"));
		@SuppressWarnings("unchecked")
		List<String> results = crit.list();
		
		System.out.println("++++++++++++++++ results = " + results.toString());

		return results;		
	}
	
	public Integer getHandId(int playerId, int gameId) {
		System.out.println("++++++++++++++++ in getHandId method");
		System.out.println("++++++++++++++++ GameId = " + gameId);
		System.out.println("++++++++++++++++ playerId = " + playerId);

		Criteria crit = getSession().createCriteria(GameMove.class)
				.setProjection(Projections.distinct(Projections.property("gameMoveKey.handId")))
				.add(Restrictions.eq("gameMoveKey.gameId", gameId))
				.add(Restrictions.eq("gameMoveKey.playerId", playerId))
				.add(Restrictions.eq("roundId", null));
		//crit.addOrder(Order.asc("gameMoveKey.cardId"));
		@SuppressWarnings("unchecked")
		List<Integer> results = crit.list();
		Integer handId = null;
		if(results != null && !results.isEmpty()) {
			 handId = results.get(0);
			 System.out.println("++++++++++++++++ handId = " + handId.toString());
		}

		return handId;		
	}
	
	public void updateCardStatus(int playerId, int gameId, int handId, String cardId) {

		System.out.println("++++++++++++++++ in updateCardStatus method");

    	System.out.println("+++++++++++++++++++++++++ playerId = " + playerId);
    	System.out.println("+++++++++++++++++++++++++ gameId = " + gameId);
    	System.out.println("+++++++++++++++++++++++++ cardId = " + cardId);

		String hql = "update gameMove GameMove set gameMove.roundId = :roundId "
				+ " where playCards.gameMoveKey.playerId = :playerId "
				+ " and playCards.gameMoveKey.gameId = :gameId "
				+ " and playCards.gameMoveKey.handId = :handId "
				+ " and playCards.gameMoveKey.cardId = :cardId";
	    
		/*Query query = getSession().createQuery(hql)
				.setString("roundId", )
				.setInteger("playerId", new Integer(playerId))
				.setInteger("gameId", new Integer(gameId))
				.setInteger("handId", new Integer(handId))
				.setString("cardId", cardId.trim().toLowerCase());*/
		
		//int result = query.executeUpdate();
		
		//System.out.println("Rows affected: " + result);		
	}
}