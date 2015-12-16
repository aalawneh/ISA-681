package edu.gmu.isa681.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TRASH_CARDS")
public class TrashCards implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TrashCardsKey trashCardsKey;

	@Column(name = "cards", nullable = false)
	private String cards;

	public TrashCards()
	{}
	
	public TrashCards(TrashCardsKey trashCardsKey, String cards) {
		super();
		this.trashCardsKey = trashCardsKey;
		this.cards = cards;
	}

	public TrashCardsKey getTrashCardsKey() {
		return trashCardsKey;
	}

	public void setTrashCardsKey(TrashCardsKey trashCardsKey) {
		this.trashCardsKey = trashCardsKey;
	}

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cards == null) ? 0 : cards.hashCode());
		result = prime * result + ((trashCardsKey == null) ? 0 : trashCardsKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrashCards other = (TrashCards) obj;
		if (cards == null) {
			if (other.cards != null)
				return false;
		} else if (!cards.equals(other.cards))
			return false;
		if (trashCardsKey == null) {
			if (other.trashCardsKey != null)
				return false;
		} else if (!trashCardsKey.equals(other.trashCardsKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TrashCards [trashCardsKey=" + trashCardsKey + ", cards=" + cards + ", getTrashCardsKey()="
				+ getTrashCardsKey() + ", getCards()=" + getCards() + ", hashCode()=" + hashCode() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}
}
