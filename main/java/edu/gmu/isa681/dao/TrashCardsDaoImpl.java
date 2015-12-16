package edu.gmu.isa681.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import edu.gmu.isa681.model.TrashCards;
import edu.gmu.isa681.model.TrashCardsKey;

@Repository("trashCardsDao")
public class TrashCardsDaoImpl extends AbstractDao<Integer, TrashCards> implements TrashCardsDao {
	// select game_id, hand_id, srcplayer_id, destplayer_id, cards from game_passing where game_id = 11 and hand_id = 1;
	public List<TrashCards> getAllPlayersTrashCards(int gameId, int handId) {
		
		
		String hql = "select tc from TrashCards tc "
				+ "where tc.trashCardsKey.gameId = :gameId "
				+ "and   tc.trashCardsKey.handId = :handId ";
		Query query = getSession().createQuery(hql);
		query.setLong("gameId", (long)gameId);
		query.setLong("handId", (long)handId);

		@SuppressWarnings("unchecked")
		List<TrashCards> trashCards = (List<TrashCards>) query.list();
		
		return trashCards;
	}		
	
	// select game_id, hand_id, srcplayer_id, destplayer_id, cards from game_passing where game_id = 11 and hand_id = 1 and srcplayer_id = 2;
	public List<TrashCards> getPlayerTrashCards(int gameId, int handId, int playerId) {
		String hql = "select tc from TrashCards tc "
				+ "where tc.trashCardsKey.gameId = :gameId "
				+ "and   tc.trashCardsKey.handId = :handId "
				+ "and   tc.trashCardsKey.srcPlayerId = :playerId ";
		Query query = getSession().createQuery(hql);
		query.setLong("gameId", (long)gameId);
		query.setLong("handId", (long)handId);
		query.setLong("playerId", (long)playerId);

		@SuppressWarnings("unchecked")
		List<TrashCards> gamePassing = (List<TrashCards>) query.list();
		
		return gamePassing;
	}		
	
	public void trashCards(int gameId, int handId, int srcPlayerId, int destPlayerId, String cards) {
		
		TrashCards trashCards = new TrashCards();
		TrashCardsKey trashCardsKey = new TrashCardsKey();
		trashCardsKey.setGameId(gameId);
		trashCardsKey.setHandId(handId);
		trashCardsKey.setSrcPlayerId(srcPlayerId);
		trashCardsKey.setDestPlayerId(destPlayerId);
		
		trashCards.setTrashCardsKey(trashCardsKey);
		trashCards.setCards(cards);
		
		persist(trashCards);
		getSession().flush(); 
	}
/*

	public void trashCards() {

		System.out.println("++++++++++++++++ in trashCards method ++++++++++++++++ ++++++++++++++++ ");

		
		String hql = " insert into TRASH_CARDS(game_id, hand_id, srcplayer_id, destplayer_id, cards) "
				+ "    values (:gameId, :handId, :srcPlayerId, :destPlayerId, :cards) ";
		Query query = getSession().createQuery(hql);
		query.setLong("gameId", (long)gameId);
		query.setLong("handId", (long)handId);
		query.setLong("srcPlayerId", (long)srcPlayerId);
		query.setLong("destPlayerId", (long)destPlayerId);
		query.setString("cards", cards);
		
		int result = query.executeUpdate();	
		if (result > 1)
			System.out.println("sucessfully inserted a record to trash cards!");
	}
*/
}
