package Big2.cardinfo;
import java.util.*;

public class Deck {
   private ArrayList<Card> deck;
   private int index;

   // Creates an empty deck of cards.
   public Deck() {
      deck = new ArrayList<>(); // create new deck of cards
      index = 0;
      initialize();
   }

   // initialise a deck of 52 cards
   private void initialize() {
      for (Suit suit : Suit.values()) {
          for (Rank rank : Rank.values()) {
              Card card = new Card(suit, rank);
              deck.add(card);
          }
      }
  }

   // Deal one card from the deck.
   public Card dealCard() {
      if (index >= deck.size())
         return null;
      else
         return (Card) deck.get(index++);
   }

   // Shuffles the cards present in the deck.
   public void shuffle() {
      Collections.shuffle(deck);
   }

   // no. of cards dealt
   public int getIndex() {
      return index;
   }
}