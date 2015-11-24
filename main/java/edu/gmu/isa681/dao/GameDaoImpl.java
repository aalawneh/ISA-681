package edu.gmu.isa681.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import edu.gmu.isa681.model.Game;

@Repository("gameDoa")
public class GameDaoImpl extends AbstractDao<Integer, Game> implements GameDao {

	public void save(Game game) { 
		persist(game);
	}

	public Game findGameById(int gameId) {
		return getByKey(gameId);
	}
	
	public Integer getGamesCount() {

		String hql = "select max(g.gameId) from Game g";
		Query query = getSession().createQuery(hql);
		Integer max = 0;

		if (query.list().get(0) != null) {
			max = (Integer) query.list().get(0);
		}

		System.out.println("+++++++++++++++++ Total games so far is = " + max);
		System.out.println("+++++++++++++++++ next game id = " + (max + 1));

		return max + 1;
	}
}
