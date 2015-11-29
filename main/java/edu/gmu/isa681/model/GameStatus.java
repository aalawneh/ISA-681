package edu.gmu.isa681.model;

public enum GameStatus {

	WAITING("Waiting"), STARTED("Started"), OVER("Over"), DECKSHUFFLING("Deckshuffling"), CALCULATING("CalculatingScore");

	private String status;

	private GameStatus(final String status) {
		this.status = status;
	}

	public String getStatus() {
		return String.valueOf(this.status.charAt(0));
	}

	@Override
	public String toString() {
		return this.status;
	}

	public String getName() {
		return this.name();
	}
}
