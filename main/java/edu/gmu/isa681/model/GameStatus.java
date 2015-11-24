package edu.gmu.isa681.model;

public enum GameStatus {

	WAITING("Waiting"), STARTED("Started"), Over("Over");

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
