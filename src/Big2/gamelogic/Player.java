package Big2.gamelogic;

import Big2.cardinfo.Card;
import Big2.hand.Hand;

public class Player {
    public Hand hand = new Hand();
    private String name;
    private boolean pass, hasNotThrown;

    public Player(String name) {
        this.name = name;
        this.pass = false;
    }

    public String getName() {
        return this.name;
    }
    
    public Hand getHand() {
        return hand;
    }

    //SORT PLAYERS HAND
    public void sortHand() {
        this.hand.sort();
    }

    public boolean isEmptyHand() {
        return (hand.isEmpty());
    }

    public void addToHand(Card card) {
        this.getHand().addCard(card);
    }

    public void remove(Card card) {
        this.hand.removeCard(card);
    }

    public boolean hasPassed() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public void setHasNotThrown(boolean hasNotThrown) {
        this.hasNotThrown = hasNotThrown;
    }

    public boolean hasNotThrown() {
        return hasNotThrown;
    }

    public String displayHasPassed() {
        return this.hasPassed() ? "Passed" : "In Play";
    }
}