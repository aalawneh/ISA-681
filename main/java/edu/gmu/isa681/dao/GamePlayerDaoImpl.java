package edu.gmu.isa681.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.gmu.isa681.model.GamePlayer;
import edu.gmu.isa681.model.GameStatus;

@Repository("gamePlayerDoa")
public class GamePlayerDaoImpl extends AbstractDao<Integer, GamePlayer> implements GamePlayerDao {

	public void save(GamePlayer gamePlayer) {
		persist(gamePlayer);
	}

	/*
	 * mysql> select * from game_player gp, game g where gp.game_id = g.game_id and g.status = 'O' and gp.game_id in (select game_id from game_player where player_id = 1);
	 */
	public List<GamePlayer> getPlayerGamesResults(int playerId) {
		String hql = "select pg from GamePlayer pg, Game g "
				+ "where pg.gamePlayerKey.gameId = g.gameId "
				+ " and g.status = '" + GameStatus.Over.getStatus() + "' and pg.gamePlayerKey.gameId in "
				+ "(select pg2.gamePlayerKey.gameId from GamePlayer pg2 where pg2.gamePlayerKey.playerId = :playerId)";
		Query query = getSession().createQuery(hql);
		query.setLong("playerId", new Long(playerId));

		@SuppressWarnings("unchecked")
		List<GamePlayer> playerGames = (List<GamePlayer>) query.list();
		
		for(GamePlayer pg : playerGames) {
			pg.toString();
		}
		
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
	
	// Check if the player already in a game and the game is not over.
	// select * from game_player pg, game g where g.game_id = pg.game_id and player_id = 2 and status != 'O'; -- OVER
	@SuppressWarnings("unchecked")
	public GamePlayer getPlayerOpenGame(int playerId) {

		GamePlayer playerGame = null;
		
		String hql = "select pg from GamePlayer pg, Game g where pg.gamePlayerKey.gameId = g.gameId "
				+ " and g.status != '" + GameStatus.Over.getStatus() + "' and pg.gamePlayerKey.playerId = :playerId)";
		Query query = getSession().createQuery(hql);
		query.setLong("playerId", new Long(playerId));

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
		query.setLong("opponents", new Long(4));

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
                .setInteger("score", new Integer(score))
                .setInteger("playerId", new Integer(playerId))
                .setInteger("gameId", new Integer(gameId));

        int result = query.executeUpdate();
       
        System.out.println("Rows affected: " + result);       
    }    
}

/*
 * http://learningviacode.blogspot.com/2012/10/hql-functions.html
 * http://learningviacode.blogspot.com/2012/12/group-by-and-having-clauses-in-hql.html
 */