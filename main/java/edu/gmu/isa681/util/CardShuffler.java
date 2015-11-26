package edu.gmu.isa681.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class CardShuffler {
	
	public static final String TWO_CLUBS = "2 clubs";
	
	public static final String HEARTS = "hearts";
	public static final String SPADES = "spades";
	public static final String DIAMS = "diams";
	public static final String CLUBS = "clubs";

	public static String[] shuffle() {
		
		
		String[] suit = new String[4];
		suit[0] = CardShuffler.HEARTS;  
		suit[1] = CardShuffler.SPADES;  
		suit[2] = CardShuffler.DIAMS;  
		suit[3] = CardShuffler.CLUBS;  
		Collections.shuffle(Arrays.asList(suit));
		//System.out.println(Arrays.toString(suit));
		
		Integer[] rank = new Integer[52];
		for (int i = 0; i < rank.length; i++) {
			rank[i] = i;
		}
		Collections.shuffle(Arrays.asList(rank));
		//System.out.println(Arrays.toString(rank));

		String[] cards = new String[52];
		for (int i = 0; i < rank.length; i++) {
			if(rank[i] == 0) cards[i] = "a " + suit[0];
			if(rank[i] == 1) cards[i] = "2 " + suit[0];
			if(rank[i] == 2) cards[i] = "3 " + suit[0];
			if(rank[i] == 3) cards[i] = "4 " + suit[0];
			if(rank[i] == 4) cards[i] = "5 " + suit[0];
			if(rank[i] == 5) cards[i] = "6 " + suit[0];
			if(rank[i] == 6) cards[i] = "7 " + suit[0];
			if(rank[i] == 7) cards[i] = "8 " + suit[0];
			if(rank[i] == 8) cards[i] = "9 " + suit[0];
			if(rank[i] == 9) cards[i] = "10 " + suit[0];
			if(rank[i] == 10) cards[i] = "j " + suit[0];
			if(rank[i] == 11) cards[i] = "q " + suit[0];
			if(rank[i] == 12) cards[i] = "k " + suit[0];
			
			if(rank[i] == 13) cards[i] = "a " + suit[1];
			if(rank[i] == 14) cards[i] = "2 " + suit[1];
			if(rank[i] == 15) cards[i] = "3 " + suit[1];
			if(rank[i] == 16) cards[i] = "4 " + suit[1];
			if(rank[i] == 17) cards[i] = "5 " + suit[1];
			if(rank[i] == 18) cards[i] = "6 " + suit[1];
			if(rank[i] == 19) cards[i] = "7 " + suit[1];
			if(rank[i] == 20) cards[i] = "8 " + suit[1];
			if(rank[i] == 21) cards[i] = "9 " + suit[1];
			if(rank[i] == 22) cards[i] = "10 " + suit[1];
			if(rank[i] == 23) cards[i] = "j " + suit[1];
			if(rank[i] == 24) cards[i] = "q " + suit[1];
			if(rank[i] == 25) cards[i] = "k " + suit[1];

			if(rank[i] == 26) cards[i] = "a " + suit[2];
			if(rank[i] == 27) cards[i] = "2 " + suit[2];
			if(rank[i] == 28) cards[i] = "3 " + suit[2];
			if(rank[i] == 29) cards[i] = "4 " + suit[2];
			if(rank[i] == 30) cards[i] = "5 " + suit[2];
			if(rank[i] == 31) cards[i] = "6 " + suit[2];
			if(rank[i] == 32) cards[i] = "7 " + suit[2];
			if(rank[i] == 33) cards[i] = "8 " + suit[2];
			if(rank[i] == 34) cards[i] = "9 " + suit[2];
			if(rank[i] == 35) cards[i] = "10 " + suit[2];
			if(rank[i] == 36) cards[i] = "j " + suit[2];
			if(rank[i] == 37) cards[i] = "q " + suit[2];
			if(rank[i] == 38) cards[i] = "k " + suit[2];

			if(rank[i] == 39) cards[i] = "a " + suit[3];
			if(rank[i] == 40) cards[i] = "2 " + suit[3];
			if(rank[i] == 41) cards[i] = "3 " + suit[3];
			if(rank[i] == 42) cards[i] = "4 " + suit[3];
			if(rank[i] == 43) cards[i] = "5 " + suit[3];
			if(rank[i] == 44) cards[i] = "6 " + suit[3];
			if(rank[i] == 45) cards[i] = "7 " + suit[3];
			if(rank[i] == 46) cards[i] = "8 " + suit[3];
			if(rank[i] == 47) cards[i] = "9 " + suit[3];
			if(rank[i] == 48) cards[i] = "10 " + suit[3];
			if(rank[i] == 49) cards[i] = "j " + suit[3];
			if(rank[i] == 50) cards[i] = "q " + suit[3];
			if(rank[i] == 51) cards[i] = "k " + suit[3];
		}
		
		//System.out.println(Arrays.toString(cards));
		return cards;
	}

	public static int compare(String s1, String s2) {
		StringTokenizer st1 = new StringTokenizer(s1," ");  
		StringTokenizer st2 = new StringTokenizer(s2," ");
		
		String cardValue1 = st1.nextToken();
		String cardValue2 = st2.nextToken();
		
		String cardSuit1 = st1.nextToken();
		String cardSuit2 = st2.nextToken();
		
		if(!cardSuit1.equals(cardSuit2)) 
			return 0; // here it means they different so we do not want to sort them
		
		int cardRank1 = -1;
		if(cardValue1.equalsIgnoreCase("j"))
			cardRank1 = 11;		
		else if(cardValue1.equalsIgnoreCase("q"))
			cardRank1 = 12;
		else if(cardValue1.equalsIgnoreCase("k"))
			cardRank1 = 13;
		else if(cardValue1.equalsIgnoreCase("a"))
			cardRank1 = 14;
		else
			cardRank1 = new Integer(cardValue1).intValue();
		
		int cardRank2 = -1;
		if(cardValue2.equalsIgnoreCase("j")) 
			cardRank2 = 11;
		else if(cardValue2.equalsIgnoreCase("q")) 
			cardRank2 = 12;
		else if(cardValue2.equalsIgnoreCase("k")) 
			cardRank2 = 13;
		else if(cardValue2.equalsIgnoreCase("a")) 
			cardRank2 = 14;
		else 
			cardRank2 = new Integer(cardValue2).intValue();
		
		int result = 0;
		if(cardRank1 > cardRank2)
			result = 1;
		else
			result = -1;

		return result;
	}

	public static int loser(List<String> cardsList) {

		String[] cards = new String[cardsList.size()];
		cards = cardsList.toArray(cards);	
		
		String loserCard = cards[0];
		StringTokenizer st1 = new StringTokenizer(loserCard," ");
		st1.nextToken();
		String loserCardSuit = st1.nextToken();
		
		int loserIndex = 0;
		for(int i=1; i<cards.length; i++) {
			
			StringTokenizer st2 = new StringTokenizer(cards[i]," ");
			st2.nextToken();
			String cardSuit = st2.nextToken();
			
			if(!loserCardSuit.equals(cardSuit)) {
				continue;
			}

			int r = compare(loserCard, cards[i]);
    		if(r < 0) {
    			loserCard = cards[i];
    			loserIndex = i;    			
    		}
		}
		return loserIndex;
	}
	
	public static List<String> sort(List<String> playerCardsList) {
		
		String[] playerCards = new String[playerCardsList.size()];
		playerCards = playerCardsList.toArray(playerCards);
		
		int tempCardsIndex = playerCards.length;
		String[] tempCards = new String[tempCardsIndex];

    	int j=0;
		for(int i=0; i<tempCardsIndex; i++) {
    		if(playerCards[i].contains(HEARTS)) {
    			tempCards[j++] = playerCards[i];
    		}
    	}
		for(int i=0; i<tempCardsIndex; i++) {
    		if(playerCards[i].contains(SPADES)) {
    			tempCards[j++] = playerCards[i];
    		}    		
    	}
		for(int i=0; i<tempCardsIndex; i++) {
    		if(playerCards[i].contains(DIAMS)) {
    			tempCards[j++] = playerCards[i];
    		}    
    	}	
		for(int i=0; i<tempCardsIndex; i++) {
    		if(playerCards[i].contains(CLUBS)) {
    			tempCards[j++] = playerCards[i];
    		}
    	}
    	
    	for(int i=0; i<tempCardsIndex-1; i++) {
    		int r = compare(tempCards[i], tempCards[i+1]);
    		if(r > 0) {
    			String temp = tempCards[i];
    			tempCards[i] = tempCards[i+1];
    			tempCards[i+1] = temp;
    		}
    	}
		
		return Arrays.asList(tempCards);
	}

	public static void main(String[] args) {

/*		String[] cards = { "10 clubs", "2 clubs", "4 clubs", "6 diams", "7 clubs", 
				"7 diams", "8 hearts", "a clubs", "a spades", "k clubs", "k hearts", "q hearts", "q spades"};
		
		List<String> playerCardsList = Arrays.asList(cards);
		playerCardsList = CardShuffler.sort(playerCardsList);
		
		System.out.println(playerCardsList);
*/		
		String[] loserCards = {"a clubs", "7 clubs", "k hearts", "6 clubs"};
		int l = CardShuffler.loser(Arrays.asList(loserCards));
		System.out.println(l);

		//String[] cards = shuffle();

		//System.out.println(Arrays.toString(cards));
		/*
    	int start = 0, end = 13;
    	for(int j=0; j < 4; j++) {
    		
			System.out.println("+++++++++++++++++++++++++ cards for player id = " + j);
    		for(int i = start; i < end; i++) {
    			System.out.println(cards[i] + " ");
    		}
    		
    		start = end;
    		end += 13;
    		System.out.println();
    	}
    	*/
	}
}
