package edu.gmu.isa681.model;


public class UnregisteredPlayer {

	private int playerId;

	private String ssoId;
	
	private String password;

	private String password2;
		
	private String firstName;

	private String lastName;

	private String email;

	private String state=PlayerState.ACTIVE.getState();

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getSsoId() {
		return ssoId;
	}

	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + playerId;
		result = prime * result + ((ssoId == null) ? 0 : ssoId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Player))
			return false;
		UnregisteredPlayer other = (UnregisteredPlayer) obj;
		if (playerId != other.playerId)
			return false;
		if (ssoId == null) {
			if (other.ssoId != null)
				return false;
		} else if (!ssoId.equals(other.ssoId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UnregisteredPlayer [playerId=" + playerId + ", ssoId=" + ssoId + ", password=" + password
				+ ", password2=" + password2 + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", state=" + state +"]";
	}
}
