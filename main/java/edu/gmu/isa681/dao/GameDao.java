package edu.gmu.isa681.dao;

import edu.gmu.isa681.model.Game;

public interface GameDao {

	public void save(Game game);

	public Game findGameById(int gameId);

	public Integer getGamesCount();
}
