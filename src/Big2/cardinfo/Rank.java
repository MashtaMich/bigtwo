package Big2.cardinfo;
import java.util.*;

public class Rank implements Comparable<Rank> {
    private String rankSymbol;
    private int bigTwoValue; // This will determine the ordering in Big Two

    // Define all ranks for Big Two, where 2 is the highest
    public final static Rank THREE = new Rank("3", 1);
    public final static Rank FOUR = new Rank("4", 2);
    public final static Rank FIVE = new Rank("5", 3);
    public final static Rank SIX = new Rank("6", 4);
    public final static Rank SEVEN = new Rank("7", 5);
    public final static Rank EIGHT = new Rank("8", 6);
    public final static Rank NINE = new Rank("9", 7);
    public final static Rank TEN = new Rank("t", 8);
    public final static Rank JACK = new Rank("j", 9);
    public final static Rank QUEEN = new Rank("q", 10);
    public final static Rank KING = new Rank("k", 11);
    public final static Rank ACE = new Rank("a", 12);
    public final static Rank TWO = new Rank("2", 13);

    private final static List<Rank> VALUES = 
        Collections.unmodifiableList(
            Arrays.asList(THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE, TWO)
        );

    private Rank(String rankSymbol, int bigTwoValue) {
        this.rankSymbol = rankSymbol;
        this.bigTwoValue = bigTwoValue;
    }

    @Override
    public int compareTo(Rank other) {
        return Integer.compare(this.bigTwoValue, other.bigTwoValue);
    }

    public String getRankSymbol() {
        return rankSymbol;
    }

    public static Rank getRank(String rank) {
        switch (rank.toLowerCase()) {
            case "3":
                return THREE;
            case "4":
                return FOUR;
            case "5":
                return FIVE;
            case "6":
                return SIX;
            case "7":
                return SEVEN;
            case "8":
                return EIGHT;
            case "9":
                return NINE;
            case "t":
                return TEN;
            case "j":
                return JACK;
            case "q":
                return QUEEN;
            case "k":
                return KING;
            case "a":
                return ACE;
            case "2":
                return TWO;
            default:
                return null;
        }
    }
    
    public static List<Rank> values() {
        return VALUES;
    }

    public int getBigTwoValue() {
      return bigTwoValue;
    }
}