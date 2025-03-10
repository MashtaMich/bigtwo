package Big2.hand;
import java.util.ArrayList;

import Big2.gamelogic.Combinations;
import Big2.cardinfo.Card;

public class PlayingHand extends Hand implements Comparable<PlayingHand> {
    public PlayingHand(ArrayList<Card> playingHand) {
        super(playingHand); // this.hand = playinghand
    }

    public PlayingHand() {
        super();
    }

    public ArrayList<Card> getPlayingHand() {
        return super.getHand();
    }

    // Method to check if the current playing hand is valid based on predefined combinations
    public boolean isValid() {
        int size = this.getHand().size();
        // Check for valid combinations
        return (size > 0 && size <= 5 && (Combinations.isStraight(this.getHand()) || 
                              Combinations.isFlush(this.getHand()) || 
                              Combinations.isFullHouse(this.getHand()) || 
                              Combinations.isFourOfAKind(this.getHand()) || 
                              Combinations.isTriple(this.getHand()) || 
                              Combinations.isPair(this.getHand()) || 
                              Combinations.isSingle(this.getHand())));
    }

    public Card getHighestCard(ArrayList<Card> ph) {
        Card highest = ph.get(0);
        for (Card c:ph) {
            if (highest.compareTo(c) < 0) {
                highest = c;
            }
        }
        return highest;
    }

    public int getHandValue(ArrayList<Card> ph) {
        int value = 0;
        if (Combinations.isStraight(ph)) {
            value += 3;
        }
        if (Combinations.isFlush(ph)) {
            value += 4;
        }
        if (Combinations.isFullHouse(ph)) {
            value += 5;
        }
        if (Combinations.isFourOfAKind(ph)) {
            value += 6;
        }
        return value;
    }

    public void clear() {
        this.getHand().clear();
    }

    // This method compares the current player's playinghand to the previous playinghand
    @Override
    //if returns > 0, means this playingHand is stronger than prevhand
    public int compareTo(PlayingHand prevhand) {
        ArrayList<Card> ph1 = this.getHand();
        ArrayList<Card> ph2 = prevhand.getHand();

        if (ph2.size() == 1) {
            return ph1.get(0).compareTo(ph2.get(0));
        }

        if (ph2.size() == 2 || ph2.size() == 3) {
            Card highest1 = this.getHighestCard(ph1);
            Card highest2 = prevhand.getHighestCard(ph2);
            return highest1.compareTo(highest2);
        }

        else {
            int v1 = this.getHandValue(ph1);
            int v2 = prevhand.getHandValue(ph2);
            if (v1 != v2) { // checks if the current playinghand has a better combination than the previous hand
                return v1 - v2;
            }
            // If current playinghand has same combi as previous hand\
            if (Combinations.isFullHouse(ph1) && Combinations.isFullHouse(ph2)) {
                Card trip1rank = Combinations.frontTrips(ph1) ? ph1.get(0):ph1.get(4);                
                Card trip2rank = Combinations.frontTrips(ph2) ? ph2.get(0):ph2.get(4);
                return trip1rank.getRank().compareTo(trip2rank.getRank());
            }
            if (Combinations.isFlush(ph1) && Combinations.isFlush(ph2)) {
                return ph1.get(0).getSuit().compareTo(ph2.get(0).getSuit());  
            }
            if (Combinations.isFourOfAKind(ph1) && Combinations.isFourOfAKind(ph2)) {
                return ph1.get(2).compareTo(ph2.get(2));
            }
            Card highest1 = getHighestCard(ph1);
            Card highest2 = getHighestCard(ph2);
            return highest1.compareTo(highest2);
        }
    }
}