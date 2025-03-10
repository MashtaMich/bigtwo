package Big2.cardinfo;

public class Card implements Comparable<Card> {
    
   // instance variables for the card 
   private Suit suitValue;
   private Rank rankValue;

   public Card( Suit suit, Rank rank ) { 
      suitValue = suit;
      rankValue = rank;
   }

   public Card (String rank, String suit) {
      Rank r = Rank.getRank(rank);
      Suit s = Suit.getSuit(suit);
      suitValue = s;
      rankValue = r;
   }
    
   public static String getFilename( Suit suit, Rank rank ) {
      return rank.getRankSymbol() + suit.getSuitSymbol() + ".png";
   }

   public Suit getSuit() {
      return suitValue;
   }

   public Rank getRank() {
      return rankValue;
   }

   @Override
  public int compareTo(Card other) {
   // Compare ranks first
   int rankComparison = this.rankValue.compareTo(other.rankValue);
   if (rankComparison != 0) {
       return rankComparison;
   }
   // If ranks are the same, compare suits
   return this.suitValue.compareTo(other.suitValue);
}

   public boolean equals(Card other) {
      return (this.compareTo(other) == 0);
   }

   // public int getCardWidth() {
   //    int width = screenResolution.getScreenWidth() * (1/16);
   //    return width;
   // }

   // public int getCardHeight() {
   //    int height = screenResolution.getScreenHeight() * (29/180);
   //    return height;
   // }
}