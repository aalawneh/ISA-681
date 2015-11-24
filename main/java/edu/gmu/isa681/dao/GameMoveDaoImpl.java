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
	
	public Integer getCurrHand(int playerId, int gameId) {
		String hql = "select max(gameMoveKey.handId) from GameMove where gameMoveKey.playerId = :playerId and gameMoveKey.gameId = :gameId";
		Query query = getSession().createQuery(hql);
		query.setInteger("playerId", new Integer(playerId));
		query.setInteger("gameId", new Integer(gameId));
		
		Integer handId = (Integer) query.list().get(0);

		System.out.println("+++++++++++++++++ current handId=" + handId + " for gameId=" + gameId + " and playerId=" + playerId);

		return handId;		
	}
	
	//
	public List<Object[]> getCurrRound(int gameId) {
		String hql = "select round_id, count(player_id) "
				+ " from game_move where round_id = (select max(round_id) from game_move where game_id = :gameId) "
				+ " group by round_id having count(player_id) <= 4";
		Query query = getSession().createSQLQuery(hql);
		query.setInteger("gameId", new Integer(gameId));

		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.list();
				
		return result;		
	}
	
	public void updateCardStatus(int playerId, int gameId, int handId, String cardId, int roundId) {

		System.out.println("++++++++++++++++ in updateCardStatus method");

    	System.out.println("+++++++++++++++++++++++++ playerId = " + playerId);
    	System.out.println("+++++++++++++++++++++++++ gameId = " + gameId);
    	System.out.println("+++++++++++++++++++++++++ handId = " + handId);
    	System.out.println("+++++++++++++++++++++++++ cardId = " + cardId);

		String hql = "update GameMove gameMove set gameMove.roundId = :roundId "
				+ " where gameMove.gameMoveKey.playerId = :playerId "
				+ " and gameMove.gameMoveKey.gameId = :gameId "
				+ " and gameMove.gameMoveKey.handId = :handId "
				+ " and gameMove.gameMoveKey.cardId = :cardId";
	    
		Query query = getSession().createQuery(hql)
				.setInteger("roundId", new Integer(roundId))
				.setInteger("playerId", new Integer(playerId))
				.setInteger("gameId", new Integer(gameId))
				.setInteger("handId", new Integer(handId))
				.setString("cardId", cardId.trim().toLowerCase());
		
		int result = query.executeUpdate();
		
		System.out.println("Rows affected: " + result);		
	}
}