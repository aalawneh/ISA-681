package edu.gmu.isa681.model;

public enum PlayerState {

	ACTIVE("Active"), INACTIVE("Inactive"), DELETED("Deleted"), LOCKED("Locked");

	private String state;

	private PlayerState(final String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	@Override
	public String toString() {
		return this.state;
	}

	public String getName() {
		return this.name();
	}
}
