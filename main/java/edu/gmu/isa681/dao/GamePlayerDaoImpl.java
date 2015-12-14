package edu.gmu.isa681.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.gmu.isa681.model.GamePlayer;
import edu.gmu.isa681.model.GameStatus;

@Repository("gamePlayerDao")
public class GamePlayerDaoImpl extends AbstractDao<Integer, GamePlayer> implements GamePlayerDao {

	public void save(GamePlayer gamePlayer) {
		persist(gamePlayer);
        getSession().flush(); 
	}

	/*
	 * mysql> select * from game_player gp, game g where gp.game_id = g.game_id and g.status = 'O' and gp.game_id in (select game_id from game_player where player_id = 1);
	 */
	public List<GamePlayer> getPlayerGamesResults(int playerId) {
		String hql = "select pg from GamePlayer pg, Game g "
				+ "where pg.gamePlayerKey.gameId = g.gameId "
				+ " and g.status = '" + GameStatus.OVER.getStatus() + "' and pg.gamePlayerKey.gameId in "
				+ "(select pg2.gamePlayerKey.gameId from GamePlayer pg2 where pg2.gamePlayerKey.playerId = :playerId)";
		Query query = getSession().createQuery(hql);
		query.setLong("playerId", (long)playerId);

		@SuppressWarnings("unchecked")
		List<GamePlayer> playerGames = (List<GamePlayer>) query.list();
		
//		for(GamePlayer pg : playerGames) {
//			pg.toString();
//		}
		
		return playerGames;
	}

	public GamePlayer getPlayerInGame(int gameId, int playerId) {
		
		Criteria crit = getSession().createCriteria(GamePlayer.class)
				.add(Restrictions.eq("gamePlayerKey.gameId", gameId))
				.add(Restrictions.eq("gamePlayerKey.playerId", playerId));
		GamePlayer results = (GamePlayer) crit.list().get(0);

		return results;
	}
	
	
	public GamePlayer getPlayerPositionInGame(int gameId, int position) {
		
		Criteria crit = getSession().createCriteria(GamePlayer.class)
				.add(Restrictions.eq("gamePlayerKey.gameId", gameId))
				.add(Restrictions.eq("position", position));
		GamePlayer results = (GamePlayer) crit.list().get(0);

		return results;
	}

	// Check if the player played in this game, and the game is over.
	// select * from GAME_PLAYER pg, GAME g where g.game_id = 2 and player_id = 5 and status = 'O'; -- OVER
	@SuppressWarnings("unchecked")
	public GamePlayer getPlayerOldGame(int gameId, int playerId) {

		GamePlayer playerGame = null;
		
		String hql = "select pg from GamePlayer pg, Game g where pg.gamePlayerKey.gameId = :gameId "
				+ " and g.status = '" + GameStatus.OVER.getStatus() + "' and pg.gamePlayerKey.playerId = :playerId)";
		Query query = getSession().createQuery(hql);
		query.setInteger("playerId", (int)playerId)
             .setInteger("gameId", (int)gameId);

		List<GamePlayer> playerGameList = (List<GamePlayer>) query.list();
		if(playerGameList != null && !playerGameList.isEmpty()) {
			playerGame = (GamePlayer) query.list().get(0);
		}

		return playerGame;
	}
	
	// Check if the player already in a game and the game is not over.
	// select * from game_player pg, game g where g.game_id = pg.game_id and player_id = 2 and status != 'O'; -- OVER
	@SuppressWarnings("unchecked")
	public GamePlayer getPlayerOpenGame(int playerId) {

		GamePlayer playerGame = null;
		
		String hql = "select pg from GamePlayer pg, Game g where pg.gamePlayerKey.gameId = g.gameId "
				+ " and g.status != '" + GameStatus.OVER.getStatus() + "' and pg.gamePlayerKey.playerId = :playerId)";
		Query query = getSession().createQuery(hql);
		query.setLong("playerId", (long)playerId);

		List<GamePlayer> playerGameList = (List<GamePlayer>) query.list();
		if(playerGameList != null && !playerGameList.isEmpty()) {
			playerGame = (GamePlayer) query.list().get(0);
		}

		return playerGame;
	}

	// mysql> select game_id, count(player_id) from game_player group by game_id having count(player_id) < 4;
	@SuppressWarnings("unchecked")
	public List<Object[]> getOpenGame(int playerId) {

		String hql = "select pg.gamePlayerKey.gameId, count(pg.gamePlayerKey.playerId) "
				+ "from GamePlayer pg group by pg.gamePlayerKey.gameId having count(pg.gamePlayerKey.playerId) < :opponents";
		Query query = getSession().createQuery(hql);
		query.setLong("opponents", 4);

		List<Object[]> gameList = (List<Object[]>) query.list();

		return gameList;
	}

	public List<GamePlayer> getOpponentsInGame(int gameId, int playerId) {
		System.out.println("++++++++++++++++ in getOpponentsInGame method");
		System.out.println(gameId);

		Criteria crit = getSession().createCriteria(GamePlayer.class)
				.add(Restrictions.eq("gamePlayerKey.gameId", gameId))
				.add(Restrictions.ne("gamePlayerKey.playerId", playerId));
		@SuppressWarnings("unchecked")
		List<GamePlayer> results = crit.list();

		return results;
	}

	public List<GamePlayer> getPlayersInGame(int gameId) {
		System.out.println("++++++++++++++++ in getPlayersInGame method");
		System.out.println(gameId);

		Criteria crit = getSession().createCriteria(GamePlayer.class)
				.add(Restrictions.eq("gamePlayerKey.gameId", gameId));
		@SuppressWarnings("unchecked")
		List<GamePlayer> results = crit.list();

		return results;
	}
	
    public void updatePlayerScore(int playerId, int gameId, int score) {

        System.out.println("++++++++++++++++ in updatePlayerScore method");

        System.out.println("+++++++++++++++++++++++++ playerId = " + playerId);
        System.out.println("+++++++++++++++++++++++++ gameId = " + gameId);

        String hql = "update GamePlayer gamePlayer "
                + " set gamePlayer.score = :score "
                + " where gamePlayer.gamePlayerKey.playerId = :playerId "
                + " and gamePlayer.gamePlayerKey.gameId = :gameId ";
       
        Query query = getSession().createQuery(hql)
                .setInteger("score", (int)score)
                .setInteger("playerId", (int)playerId)
                .setInteger("gameId", (int)gameId);

        int result = query.executeUpdate();
       
        System.out.println("Rows affected: " + result);       
    }

	public String getGameMessage(int playerId, int gameId) {
		System.out.println("++++++++++++++++ in getGameMessage method");
		System.out.println(gameId);
		
		String result = "";
		int newline = 0;

		Criteria crit = getSession().createCriteria(GamePlayer.class)
				.setProjection(Projections.property("messages"))
				.add(Restrictions.eq("gamePlayerKey.gameId", gameId));
				//.add(Restrictions.ne("gamePlayerKey.playerId", playerId));
		
		@SuppressWarnings("unchecked")
		List<String> results = (List<String>) crit.list();
	    System.out.println("++++++++++++++++ in getGameMessage method: all results " + results);
		
		for (int i = 0; i < results.size(); i++) {
		    if (results.get(i) != null && !results.get(i).isEmpty()){
		    	if (newline == 1) {
		    		result = result.concat("<br>");
		    	}
			    System.out.println("++++++++++++++++ in getGameMessage method: results " + i + ": " + results.get(i));
		    	result = result.concat(results.get(i)); 
		    	newline=1;
		    }
		}
	    System.out.println("++++++++++++++++ in getGameMessage method: results: " + result);
		return result;		
	}

	public void updateGameMessage(int playerId, int gameId, String message) {
        System.out.println("++++++++++++++++ in updateGameMessage method");

        System.out.println("+++++++++++++++++++++++++ playerId = " + playerId);
        System.out.println("+++++++++++++++++++++++++ gameId = " + gameId);
        System.out.println("+++++++++++++++++++++++++ message = " + message);

        String hql = "update GamePlayer gamePlayer "
                + " set gamePlayer.messages = :message "
                + " where gamePlayer.gamePlayerKey.playerId = :playerId "
                + " and gamePlayer.gamePlayerKey.gameId = :gameId ";
       
        Query query = getSession().createQuery(hql)
                .setString("message", message)
                .setInteger("playerId", (int)playerId)
                .setInteger("gameId", (int)gameId);

        int result = query.executeUpdate();
       
        System.out.println("Rows affected: " + result); 		
	}    
}

/*
 * http://learningviacode.blogspot.com/2012/10/hql-functions.html
 * http://learningviacode.blogspot.com/2012/12/group-by-and-having-clauses-in-hql.html
 */