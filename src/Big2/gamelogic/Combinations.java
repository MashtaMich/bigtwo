package Big2.gamelogic;

import java.util.ArrayList;
import java.util.List;

import Big2.cardinfo.Card;
import Big2.cardinfo.Rank;
import Big2.cardinfo.Suit;

public class Combinations { // Playing hand always sorted first before checkin if it has a valid combo
    public static boolean isSingle(ArrayList<Card> playingHand) {
        return playingHand.size() == 1;
    }    

    public static boolean isPair(ArrayList<Card> playingHand) {
        ArrayList<Card> ph = playingHand;
        if (ph.size() != 2) {
            return false;
        }
        Rank r1 = ph.get(0).getRank();
        Rank r2 = ph.get(1).getRank();
        return (r1.compareTo(r2) == 0);
    }

    public static boolean isTriple(ArrayList<Card> playingHand) {
        List<Card> ph = playingHand;
        if (ph.size() != 3) {
            return false;
        }
        Rank r1 = ph.get(0).getRank();
        Rank r2 = ph.get(1).getRank();
        Rank r3 = ph.get(2).getRank();
        return (r1.compareTo(r2) == 0 && r2.compareTo(r3) == 0);
    }

    public static boolean isFourOfAKind(ArrayList<Card> playingHand) {
        ArrayList<Card> ph = playingHand;
        if (ph.size() != 5) {
            return false;
        }
        //Get the index of the Card played that is of a different Rank
        Rank start = ph.get(0).getRank();
        int idx = 0;
        for (int i = 1; i < 5; i++) {
            Rank diff = ph.get(i).getRank();
            if (diff.compareTo(start) != 0) {
                idx = i;
                break;
            }
        }
        //Store the 4 same cards into an ArrayList
        ArrayList<Card> otherFourCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i != idx) {
                otherFourCards.add(ph.get(i));
            }
        }
        //Check if they are all equal in Rank
        return (isQuads(otherFourCards));
    }

    public static boolean isStraight(ArrayList<Card> playingHand) {
        ArrayList<Card> ph = playingHand;
        if (ph.size() != 5) {
            return false;
        }
        int value = 0;
        for (Card c : ph) {
            if (value == 0) {
                value = c.getRank().getBigTwoValue();
            } else {
                // Check for incremental rank for consecutive cards
                if (c.getRank().getBigTwoValue() == value + 1) {
                    value++;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isFlush(ArrayList<Card> playingHand) {
        ArrayList<Card> ph = playingHand;
        if (ph.size() != 5) {
            return false;
        }
        Suit s = ph.get(0).getSuit();
        // Chekc if all suits are the same
        for (Card c : ph) {
            if (c.getSuit().compareTo(s) != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFullHouse(ArrayList<Card> playingHand) {
        ArrayList<Card> ph = playingHand;
        if (ph.size() != 5) {
            return false;
        }
        // Check if combination is either (Triple + Pair) or (Pair + Triple)
        return (frontTrips(ph) && backPair(ph)) || (frontPair(ph) && backTrips(ph)); 
    }

    public static boolean frontTrips(ArrayList<Card> cards) { // One of the methods used to validate a full house combo
        // Checks if first 3 cards of a full house are identical in rank
        Rank r1 = cards.get(0).getRank();
        Rank r2 = cards.get(1).getRank();
        Rank r3 = cards.get(2).getRank();
        return (r1.compareTo(r2) == 0 && r2.compareTo(r3) == 0);
    }

    public static boolean backPair(ArrayList<Card> cards) { // One of the methods used to validate a full house combo
        // Checks if last 2 cards of a full house are identical in rank
        Rank r1 = cards.get(3).getRank();
        Rank r2 = cards.get(4).getRank();
        return (r1.compareTo(r2) == 0);
    }

    public static boolean frontPair(ArrayList<Card> cards) { // One of the methods used to validate a full house combo
        // Checks if first 2 cards of a full house are identical in rank
        Rank r1 = cards.get(0).getRank();
        Rank r2 = cards.get(1).getRank();
        return (r1.compareTo(r2) == 0);
    }

    public static boolean backTrips(ArrayList<Card> cards) { // One of the methods used to validate a full house combo
        // Checks if last 3 cards of a full house are identical in rank
        Rank r1 = cards.get(2).getRank();
        Rank r2 = cards.get(3).getRank();
        Rank r3 = cards.get(4).getRank();
        return (r1.compareTo(r2) == 0 && r2.compareTo(r3) == 0);
    }

    public static boolean isQuads(ArrayList<Card> cards) {
        if (cards.size() != 4) {
            return false;
        }
        Rank r1 = cards.get(0).getRank();
        Rank r2 = cards.get(1).getRank();
        Rank r3 = cards.get(2).getRank();
        Rank r4 = cards.get(3).getRank();
        return (r1.compareTo(r2) == 0 && r2.compareTo(r3) == 0 && r3.compareTo(r4) == 0);
    }
}