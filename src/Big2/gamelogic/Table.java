package Big2.gamelogic;

import java.util.*;

import Big2.cardinfo.Card;
import Big2.hand.PlayingHand;

public class Table { // Keep track of what was last played 
    private ArrayList<Card> lastPlayingHand = new ArrayList<>();
    
    public ArrayList<Card> getLastPlayingHand() {
        return this.lastPlayingHand;
    }

    public void setLastPlayingHand(PlayingHand prevhand) {
        for (Card c:prevhand.getHand()) {
            lastPlayingHand.add(c);
        }
    }

    public void displayLastPlayedHand() {
        ArrayList<Card> lastPlayedHand = this.getLastPlayingHand();
        System.out.println("Last played hand...");
        for (Card card : lastPlayedHand) {
            System.out.println(card.toString());
        }
    }

    //Returns true if playingHand is weaker than the previouslyPlayedHand on table 
    public boolean beatsLastPlayed(PlayingHand playingHand) {
        if (lastPlayingHand.isEmpty()) {
            return false;
        }
        PlayingHand tempPH = new PlayingHand(lastPlayingHand);
        return (lastPlayingHand.size() == playingHand.getPlayingHand().size() && tempPH.compareTo(playingHand) > 0);
    }
}