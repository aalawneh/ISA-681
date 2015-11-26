package edu.gmu.isa681.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.gmu.isa681.model.GameMove;
import edu.gmu.isa681.util.CardShuffler;

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
		
	public Integer whoHasTwoClubs(int gameId, int handId) {
		
		Criteria crit = getSession().createCriteria(GameMove.class)
				.setProjection(Projections.property("gameMoveKey.playerId"))
				.add(Restrictions.eq("gameMoveKey.gameId", gameId))
				.add(Restrictions.eq("gameMoveKey.handId", handId))
				.add(Restrictions.eq("gameMoveKey.cardId", CardShuffler.TWO_CLUBS))
				.add(Restrictions.isNull("roundId"));

		Integer playerId = (Integer) crit.list().get(0);
		System.out.println("++++++++++++++++ player who has 2 clubs is playerId = " + playerId);

		return playerId;
	}
	
	public Integer getCurrHand(int gameId) {
		String hql = "select max(gameMoveKey.handId) from GameMove where gameMoveKey.gameId = :gameId";
		Query query = getSession().createQuery(hql);
		query.setInteger("gameId", new Integer(gameId));
		
		Integer handId = (Integer) query.list().get(0);
		System.out.println("+++++++++++++++++ current handId=" + handId + " for gameId=" + gameId);

		return handId;		
	}
	
	/*
	 * select round_id, count(player_id), player_id from game_move where game_id = 10 and hand_id = (select max(hand_id) from game_move where game_id = 10) and round_id = (select max(round_id) from game_move where game_id = 10) group by round_id having count(player_id) <= 4 order by time_stamp;
	 * 
	 * select round_id, count(player_id), player_id from game_move 
	 * where game_id = 10 
	 * and hand_id = (select max(hand_id) from game_move where game_id = 10) 
	 * and round_id = (select max(round_id) from game_move where game_id = 10) 
	 * group by round_id having count(player_id) <= 4;
	 * 
	 *  
	 *  
	 * select * from game_move where round_id = (select round_id from game_move where game_id = 10 and hand_id = (select max(hand_id) from game_move where game_id = 10) and round_id =(select max(round_id) from game_move where game_id = 10) group by round_id having count(player_id) <= 4) order by time_stamp;
	 *
	 * select * from game_move where round_id = 
	 * (select round_id from game_move 
	 *  where  game_id = 10 
	 *  and    hand_id = (select max(hand_id) from game_move where game_id = 10) 
	 *  and    round_id =(select max(round_id) from game_move where game_id = 10) 
	 *  group by round_id having count(player_id) <= 4);
	 */
	public List<Object[]> getCurrRound(int gameId) {
		String hql = " select round_id, player_id, card_id from GAME_MOVE where round_id = "
				+ " (select round_id "
				+ " from GAME_MOVE where game_id = :gameId and "
				+ " hand_id = (select max(hand_id) from GAME_MOVE where game_id = :gameId) and "
				+ " round_id = (select max(round_id) from GAME_MOVE where game_id = :gameId) "
				+ " group by round_id having count(player_id) <= 4)"
				+ " order by time_stamp";
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
    	System.out.println("+++++++++++++++++++++++++ roundId = " + roundId);

		String hql = "update GameMove gameMove "
				+ " set gameMove.roundId = :roundId, gameMove.timestamp = :newTimeStamp "
				+ " where gameMove.gameMoveKey.playerId = :playerId "
				+ " and gameMove.gameMoveKey.gameId = :gameId "
				+ " and gameMove.gameMoveKey.handId = :handId "
				+ " and gameMove.gameMoveKey.cardId = :cardId ";
	    
		java.util.Date date= new java.util.Date();
		Query query = getSession().createQuery(hql)
				.setInteger("roundId", new Integer(roundId))
				.setTimestamp("newTimeStamp", new Timestamp(date.getTime()))
				.setInteger("playerId", new Integer(playerId))
				.setInteger("gameId", new Integer(gameId))
				.setInteger("handId", new Integer(handId))
				.setString("cardId", cardId.trim().toLowerCase());

		int result = query.executeUpdate();
		
		System.out.println("Rows affected: " + result);		
	}
	
	/*
	 * select CONCAT(first_name, ' ', last_name), hand_id, card_id, round_id from player p, game g, game_move gm where g.game_id = gm.game_id and p.player_id = gm.player_id and g.game_id = 10 and g.status = 'S' and round_id is not null order by round_id, time_stamp;
	 *
	 */
	public List<Object[]> getGameMoves(int gameId, String gameStatus) {
		String hql = " select CONCAT(first_name, ' ', last_name), hand_id, card_id, round_id "
				+ " from PLAYER p, GAME g, GAME_MOVE gm "
				+ " where g.game_id = gm.game_id and p.player_id = gm.player_id "
				+ " and g.game_id = :gameId and g.status = :gameStatus "
				+ " and round_id is not null "
				+ " order by round_id, time_stamp";
		Query query = getSession().createSQLQuery(hql);
		query.setInteger("gameId", new Integer(gameId));
		query.setString("gameStatus", gameStatus);

		@SuppressWarnings("unchecked")
		List<Object[]> result = (List<Object[]>) query.list();
				
		return result;				
	}
}