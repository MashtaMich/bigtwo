package Big2.cardinfo;
import java.util.*;

public final class Suit implements Comparable<Suit> {
    private String suitSymbol;
    private int order; // Adjusted order based on game requirement
    
    // Adjusting the order based on your specification
    public final static Suit DIAMONDS = new Suit("d", 1);
    public final static Suit CLUBS = new Suit("c", 2);
    public final static Suit HEARTS = new Suit("h", 3);
    public final static Suit SPADES = new Suit("s", 4);
    
    private final static List<Suit> VALUES = 
        Collections.unmodifiableList(
            Arrays.asList(DIAMONDS, CLUBS, HEARTS, SPADES)
        );

    private Suit(String suitSymbol, int order) {
        this.suitSymbol = suitSymbol;
        this.order = order;
    }

    public String getSuitSymbol() {
        return suitSymbol;
    }

    //to get the suit of the Card based on the input value
    public static Suit getSuit(String suit) {
        switch (suit.toLowerCase()) {
            case "d":
                return DIAMONDS;
            case "c":
                return CLUBS;
            case "h":
                return HEARTS;
            case "s":
                return SPADES;
            default:
                return null;
        }
    }
    
    @Override
    public int compareTo(Suit other) {
        return Integer.compare(this.order, other.order);
    }
    
    public static List<Suit> values() {
        return VALUES;
    }
}