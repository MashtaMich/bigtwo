package Big2.hand;

import java.util.ArrayList;
import java.util.Collections;

import Big2.cardinfo.Card;

public class Hand {
    private ArrayList<Card> hand = new ArrayList<>(); 

    // Constructor that accepts a list of cards
    public Hand(ArrayList<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }

    // Default constructor initializing an empty hand
    public Hand() {
        this.hand = new ArrayList<>();
    }

    // Returns a copy of the hand to prevent external modifications
    public ArrayList<Card> getHand() {
        return this.hand;
    }

    // Adds a card to the hand
    public void addCard(Card card) {
        hand.add(card);
    }

    // Returns the number of cards in the hand
    public int getNumberOfCards() {
        return hand.size();
    }

    // Removes the specified card from the hand
    public Card removeCard(Card card) {
        int index = hand.indexOf(card);
        if (index >= 0) {
            return hand.remove(index);
        }
        return null; // Return null if card is not found
    }
    
    public void sort() {
        Collections.sort(hand);
    }

    // // Checks if the hand is empty
    public boolean isEmpty() {
        return hand.isEmpty();
    }

    // Checks if the hand contains the specified card
    public boolean containsCard(Card card) {
        for (Card c:hand) {
            if (c.equals(card)) {
                return true;
            }
        }
        return false;
    }
}