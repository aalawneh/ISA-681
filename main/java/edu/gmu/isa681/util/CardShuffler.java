package edu.gmu.isa681.util;

import java.util.Arrays;
import java.util.Collections;

public class CardShuffler {

	public static String[] shuffle() {
		
		final String HEARTS = "hearts";
		final String SPADES = "spades";
		final String DIAMS = "diams";
		final String CLUBS = "clubs";
		
		String[] suit = new String[4];
		suit[0] = HEARTS;  
		suit[1] = SPADES;  
		suit[2] = DIAMS;  
		suit[3] = CLUBS;  
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
		
		// After shuffling now sort the first 13 cards by suite then the next 13 then the next etc.
		String[] tempCards = new String[52];
		int tempCardsIndex = 0;
    	int start = 0, end = 13;
    	for(int i=0; i<4; i++) {
	    	for(int j=start; j<end; j++) {
	    		if(cards[j].contains(HEARTS)) {
	    			tempCards[tempCardsIndex++] = cards[j];
	    		}
	    	}
	    	//Arrays.sort(tempCards, start, tempCardsIndex);
	    	for(int j=start; j<end; j++) {
	    		if(cards[j].contains(SPADES)) {
	    			tempCards[tempCardsIndex++] = cards[j];
	    		}    		
	    	}
	    	//Arrays.sort(tempCards, start, tempCardsIndex);
	    	for(int j=start; j<end; j++) {
	    		if(cards[j].contains(DIAMS)) {
	    			tempCards[tempCardsIndex++] = cards[j];
	    		}    
	    	}	
	    	//Arrays.sort(tempCards, start, tempCardsIndex);
	    	for(int j=start; j<end; j++) {
	    		if(cards[j].contains(CLUBS)) {
	    			tempCards[tempCardsIndex++] = cards[j];
	    		}
	    	}
	    	//Arrays.sort(tempCards, start, tempCardsIndex);
    		
    		start = end;
    		end += 13;
    	}
    	
		System.out.println(Arrays.toString(cards));
    	System.out.println(Arrays.toString(tempCards));
    	cards = tempCards;
		return cards;
	}

	public static void main(String[] args) {

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