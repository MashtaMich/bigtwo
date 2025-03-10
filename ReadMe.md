## Directory diagram

BigTwoFinal
  +- assets\
  |  +- images\  // contains file of card images
  |  +- sound\  // contains file of sound for GUI
  |
  |  +- classes\
  |       +- Big2\  // contains the classes for the game
  |
  |  +- src\
  |       +- Big2\
  |            +- cardinfo\
  |            |  +- Card.java
  |            |  +- Deck.java
  |            |  +- Rank.java
  |            |  +- Suit.java
  |            |
  |            +- exception\
  |            |  +- IllegalNumberOfPlayersException.java
  |            |
  |            +- cardinfo\
  |            |  +- Combinations.java
  |            |  +- Player.java
  |            |  +- Table.java
  |            |
  |            +- GUI\
  |            |  +- FourPlayers.java
  |            |  +- TwoPlayers.java
  |            |
  |            +- hand\
  |            |  +- Hand.java
  |            |  +- PlayingHand.java
  |            |
  |            +- sound\
  |            |  +- Sounds.java
  |            |
  |            +- App.java
  |            +- FourPlayerMode.java
  |            +- TwoPlayerMode.java
  |
  +- compile.bat
  +- run.bat

## How to run ? 
1. Open a new terminal
2. Run compile.bat
3. Run run.bat

## How does this work?
1. App.java runs to start the entire program
    - Program takes in user input in the console, asking how many players want to play the game
    - If user inputs non-integer, program catches and handles InputMismatchExceptino 
    - Since our program only accepts 2 or 4 players, if user inputs an integer that is not 2 or 4, 
    program will throw IllegalNumberOfPlayersException (Exception class we created)
2. TwoPlayerMode.java will run if user inputs 2 in the console
    - TwoPlayerMode.java will then create an instance of TwoPlayers, running the Two player mode Big2 game
3. FourPlayerMode.java will run if user inputs 4 in the console
    - FourPlayerMode.java will then create an instance of FourPlayers, running the Four player mode Big2 game

## Brief description of our program 
    Our game and program is built on the Java Swing GUI toolkit. By using JPanels and JLayeredPanes, 
we were able to wrap and lay out each individual UI component, combining them to create a functional, 
runnable version of Big Two. In the constructor of our main GUI class, PlayBig2GUI.java, 
we initialize a few important variables and set the Swing interface up: We set the layout to a BorderLayout, 
set the GUI’s title, set the background color to a green hex color (#35654d), set the background music of the app, 
as well as initialize all the starting game logic. 
    This game logic includes initializing an array of Players who will be participating in the game, 
setting up counter variables such as the starting turn number, 
and dealing a hand to each player from the deck after shuffling it.
    As mentioned earlier, we mainly utilize JPanels that contain other Java Swing UI elements to represent our game.
The current player’s hand is stored within a Panel at the bottom of the screen, 
along with buttons to facilitate game logic.    
    Every other player’s hand is also visible on the screen, 
being seen at the west, north and east of the UI. 
    Game logic is primarily handled through our individually-created classes, 
such as Hand.java to act as a holder of a player’s hand, 
Card.java to contain all the properties of any given card, 
and Player.java to store all the information related to any player. 
    Instances of these classes can be created and manipulated when necessary throughout the game. 
    Running game logic utilizes all of these classes as well as Java Swing GUI methods. 
    For example, each time a player successfully plays a turn, 
the ‘play’ button’s action listener prompts the program to proceed.
    By checking against our internal logic, we validate whether the hand is valid, and if it is, 
 it gets played to the table and removed from the current player’s hand. 
    To move to the next player, we increment the current turn and call our own updateCardDisplay() method. 
    This method removes all current elements from the cards panel at the bottom of the screen, 
 retrieves the current player’s hand, 
 uses a for loop to iterate through it and freshly display each card on the card panel at the bottom, 
 and revalidates & repaints the card panel to update it accordingly.


## Big 2 Rules 

### Game Objective
The objective of the game is to clear your hand 


### Big 2 Gameplay
This game uses the standard 52-card international card deck, without the Jokers.
Each player starts of with 13 cards, which are randomly dealt before tthe game begins. Players take turns playing cards, and each play must either match or beat the previous play in rank and quantity. The player who played the last highest combination of cards starts the next round. The game continues until one player runs out of cards.

### Hands & Rankings
#### Single
Cards are ranked by Rank, followed by Suit<br>For example, "6 of Hearts" > "5 of Hearts" and "6 of Hearts" > "6 of Diamonds"

#### Pair
A pair consists of two cards of the same rank, such as "3 of Diamonds" and "3 of Hearts".

#### Trips
Trips, also known as three of a kind, refers to a set of three cards of the same rank, like "4 of Spades", "4 of Hearts", and "4 of Clubs".

#### Straight
A straight is a set of 5 cards in consecutive increasing order, such as "2 of Clubs", "3 of Diamonds", "4 of Hearts", "5 of Spades", and "6 of Diamonds".

#### Flush
A flush is a set of 5 cards that share the same suit, such as "2 of Hearts", "5 of Hearts", "7 of Hearts", "10 of Hearts", and "King of Hearts".

#### Full House
A full house consists of a set of 5 cards, including a pair and a trips. For example, "6 of Spades", "6 of Hearts", "6 of Clubs", "Jack of Diamonds", and "Jack of Spades".

#### Four of a Kind
Four of a kind refers to a set of 5 cards, with four cards of the same rank, such as "8 of Diamonds", "8 of Clubs", "8 of Hearts", and "8 of Spades".

#### Straight Flush
A straight flush is a set of 5 cards that are both a straight and a flush, meaning they are in consecutive order and share the same suit, like "3 of Hearts", "4 of Hearts", "5 of Hearts", "6 of Hearts", and "7 of Hearts".



### How to Win

A player wins by emptying all the cards in their hand. 