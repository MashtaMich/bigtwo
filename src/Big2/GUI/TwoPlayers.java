package Big2.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import Big2.gamelogic.*;
import Big2.sound.*;
import Big2.cardinfo.*;
import Big2.hand.PlayingHand;

public class TwoPlayers extends JFrame {
    // initialize the Swing UI elements to be displayed
    private JPanel cardsPanel, tablePanel, infoPanel, currentInfoPanel;
    private JButton playButton, passButton, quitButton;
    private JLabel currentPlayerTurn, lastPlayingHandName, messageToCurrentPlayer;
    private JLayeredPane northPane, southPane;
    private PlayingHand playingHand = new PlayingHand();
    private HashMap<String, JButton> cardButtons = new HashMap<>(); // track buttons by card name

    // game logic variables
    private static Player[] players;
    private int turnNo, currentPlayer, turnPassCount;
    public final static int playerCount = 2;
    private boolean firstEverTurn;
    Player roundWinner;
    Table table;
    Deck deck;
    Sounds backgroundMusic = new Sounds();

    public TwoPlayers() {
        // in our constructor, we initialize some of the Swing properties, such as the window's title and state.
        setTitle("Big Two");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#35654d"));

        backgroundMusic.setMusic("assets/sound/bgm.wav");
        backgroundMusic.playMusicContinuously();

        // initialize game logic variables

        deck = new Deck(); //Create new deck (empty arraylist of cards)
        //deck.initialize(); //Add 52 cards to deck
        deck.shuffle(); //randomise the distribution of cards

        // We only have two players in the two-player version.
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        players = new Player[]{p1, p2}; //Array of players

        turnNo = distribution(deck, players); // turn no determines which player starts first
        firstEverTurn = true;
        turnPassCount = 0;

        // table = new Table(p1, p2);
        table = new Table();
        currentPlayer = turnNo;

        cardsPanel = new JPanel(new FlowLayout());
        cardsPanel.setOpaque(false);
        cardsPanel.setPreferredSize(new Dimension(1150, 200));
        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create controlpanel to hold "play", "pass", "quit" buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);

        // Initialsise infopanel to hold textual information of the JLabels declared above
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Initialize tablepanel to hold the cards played to the table
        tablePanel = new JPanel(new FlowLayout());
        tablePanel.setPreferredSize(new Dimension(800, 200));
        tablePanel.setOpaque(false);

        // Initialize currentinfo panel to hold lastPlayingHandName and messageToCurrentPlayer
        currentInfoPanel = new JPanel(new BorderLayout());
        currentInfoPanel.setOpaque(false);
        currentInfoPanel.setMaximumSize(new Dimension(1200, 50));        

        northPane = new JLayeredPane();
        northPane.setPreferredSize(new Dimension(800, 300));

        southPane = new JLayeredPane();
        southPane.setPreferredSize(new Dimension(1150, 200));
        //southPane.setBounds(200, 0, 800, 300);
        cardsPanel.add(southPane);

        //Create centerpanel to hold table, northplayer and buttons(controlpanel)
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Initialise "play" button
        playButton = new JButton("Play");
        playButton.setBackground(Color.decode("#3B5998"));
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setForeground(Color.white);

        // Initialise "pass" button
        passButton = new JButton("Pass");
        passButton.setFont(new Font("Arial", Font.BOLD, 20));
        passButton.setEnabled(false);

        // Initialise "quit" button
        quitButton = new JButton("Quit Game");
        quitButton.setBackground(Color.decode("#C90606"));
        quitButton.setFont(new Font("Arial", Font.BOLD, 20));
        quitButton.setForeground(Color.white);

        // Add all 3 buttons to controlpanel
        controlPanel.add(playButton);
        controlPanel.add(passButton);
        controlPanel.add(quitButton);

        // Initialsise Label to display helper info to current player
        messageToCurrentPlayer = new JLabel("You must play a hand containing the 3 of Diamonds");
        messageToCurrentPlayer.setFont(new Font("Arial", Font.PLAIN, 14));
        messageToCurrentPlayer.setForeground(Color.decode("#cecece"));

        // Initialsise Label to display the type of hand currently on the table
        lastPlayingHandName = new JLabel("Current Table Hand: " + getCombinationName(table));
        lastPlayingHandName.setFont(new Font("Arial", Font.BOLD, 18));
        lastPlayingHandName.setForeground(Color.white);

        // Initialsise Label to display current player textually
        currentPlayerTurn = new JLabel("You (" + players[currentPlayer].getName() + ") (" + players[currentPlayer].getHand().getHand().size() + " C)");
        currentPlayerTurn.setFont(new Font("Arial", Font.BOLD, 18));
        currentPlayerTurn.setForeground(Color.white);

        currentInfoPanel.add(lastPlayingHandName, BorderLayout.NORTH);
        currentInfoPanel.add(messageToCurrentPlayer, BorderLayout.CENTER);
        currentInfoPanel.add(currentPlayerTurn, BorderLayout.LINE_END);

        // run an initial updateCardDisplay() to display the starting player's hand.
        updateCardDisplay();
        
        centerPanel.add(northPane);
        centerPanel.add(tablePanel);
        centerPanel.add(controlPanel);
        centerPanel.add(currentInfoPanel);

        // Add all panels to the JFrame
        add(cardsPanel, BorderLayout.SOUTH);
        add(infoPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // initialize button listeners
        initializeButtonListeners();
    }

    // function to display opponent player cards
    private void displayOpponentCards(Player opponent, String location) {
        int offset = 0;
        // for loop to retrieve the hand from the opponent and generate a button that can display each card.
        for (int i = 0; i < opponent.getHand().getHand().size(); i++) {
            ImageIcon cardBackImage = new ImageIcon("assets/images/b.png");
            Image resizedBackImage = cardBackImage.getImage().getScaledInstance(100, 145,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            cardBackImage = new ImageIcon(resizedBackImage);  // transform it back
            JButton cardBack = new JButton(cardBackImage);
            cardBack.setBorder(BorderFactory.createEmptyBorder());
            cardBack.setContentAreaFilled(false);
            cardBack.setFocusPainted(false);
            // with two players, the opponent will be at the "north" location.
            if (location.equals("north")) {
                cardBack.setBounds(offset, 0, 550, 150);
                northPane.add(cardBack);
            }
            offset += 40;
        }
    }

    // Display a visible label for the opponent whose cards are rendered above.
    private void displayOpponentLabel(Player opponent, JLayeredPane targetPane) {
        JLabel opponentLabel = new JLabel(opponent.getName() + " (" + opponent.displayHasPassed() + ") (" + opponent.getHand().getHand().size() + " C)");
        if (targetPane == northPane) {
            opponentLabel.setBounds(500, 150, 150, 50);
        }
        opponentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        opponentLabel.setForeground(Color.white);
        targetPane.add(opponentLabel);
    }

    // store each player's position in a HashMap.
    private HashMap<String, Player> getPlayerPositions(int currentPlayer) {
        HashMap<String, Player> map = new HashMap<>();
        map.put("north", players[(currentPlayer + 1) % playerCount]);
        return map;
    }

    // obtain the current playing hand's combination, to display on our label for the player to see.
    public String getCombinationName(Table table) {
        int len = table.getLastPlayingHand().size();
        if (len == 1) {
            return "Single";
        } else if (len == 2) {
            return "Pair";
        } else if (len == 3) {
            return "Trips";
        } else if (len == 5) {
            if (Combinations.isFullHouse(table.getLastPlayingHand())) {
                return "Full House";
            } else if (Combinations.isStraight(table.getLastPlayingHand()) && Combinations.isFlush(table.getLastPlayingHand())) {
                return "Straight Flush";
            } else if (Combinations.isFlush(table.getLastPlayingHand())) {
                return "Flush";
            } else if (Combinations.isStraight(table.getLastPlayingHand())) {
                return "Straight";
            } else if (Combinations.isFourOfAKind(table.getLastPlayingHand())) {
                return "Four of a Kind";
            }
        }
        return "None";
    }

    // distribute the deck and deal cards to each player at the start of a round.
    private int distribution(Deck deck, Player[] players) {
        turnNo = 0; //determines which player starts first
        
        while (deck.getIndex() < 52) {
            for (Player player : players) {
                player.addToHand(deck.dealCard());
            }
        }

        int len = players.length;
        for (int i = 0; i < len; i++) {
            players[i].sortHand();
            if (players[i].getHand().containsCard(new Card(Suit.DIAMONDS, Rank.THREE))) {
                turnNo = i % len;
            }
        }
        return turnNo;
    }

    private void initializeButtonListeners() {
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playingHand.sort();
                players[currentPlayer].setHasNotThrown(false);

                // Logic to play selected cards
                boolean validPlayingHand = checkLastPlayedHand();
                if (validPlayingHand) {
                    // logic to play
                    firstEverTurn = false;
                    passButton.setEnabled(true);
                    passButton.setBackground(Color.decode("#FC5A03"));
                    passButton.setForeground(Color.white);
                    messageToCurrentPlayer.setText("You may play a combination that beats the current hand or pass your turn");

                    // remove the played cards from the current player's hand
                    for (Card c : playingHand.getHand()) {
                        players[currentPlayer].getHand().removeCard(c);
                    }

                    // winner logic:
                    if (players[currentPlayer].getHand().getHand().size() == 0) {
                        updateTableDisplay();
                        JOptionPane.showMessageDialog(null, "Congratulations, Player " + players[currentPlayer].getName() + " won :)");
                        updateCardDisplay();
                        southPane.revalidate();
                        southPane.repaint();
                        currentPlayerTurn.setText("You (" + players[currentPlayer].getName() + ") Won!");
                        disableAllButtons();
                    } else {
                        showTransitionScreen();
                        updateTableDisplay();
                        proceedToNextTurn();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR: Please select a valid playing hand!");
                    updateCardDisplay();
                    cardsPanel.revalidate();
                    cardsPanel.repaint();
                }
                playingHand.clear();
            }
        });

        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // logic to prevent the very first player of the game or of a new turn from passing their turn.
                if (players[currentPlayer].getHand().containsCard(new Card(Suit.DIAMONDS, Rank.THREE))) {
                    JOptionPane.showMessageDialog(null, "You hold the three of diamonds! As you are the first player of the entire game, you must play a card!");
                } else if (players[currentPlayer] == roundWinner && players[currentPlayer].hasNotThrown()) {
                    JOptionPane.showMessageDialog(null, "You are the first player of this turn! You must play a card!");
                } else {
                    Object[] options = {"Confirm", "Cancel"};
                    var confirm = JOptionPane.showOptionDialog(null, "You turn will be passed. Are you sure?", null, JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
                    if (confirm == 0) {
                        turnPassCount++;
                        players[currentPlayer].setPass(true);
        
                        if (turnPassCount == 1) {
                            // clear previous playing hand
                            table.getLastPlayingHand().clear();

                            // clear table
                            tablePanel.removeAll();

                            while (players[currentPlayer % playerCount].hasPassed()) {
                                currentPlayer++;
                            }

                            JOptionPane.showMessageDialog(null, players[(currentPlayer + 1) % playerCount].getName() + " passed! New round starting for player " + players[currentPlayer % playerCount].getName() + "!");
                            roundWinner = players[currentPlayer % playerCount];
                            currentPlayer--;
                            playingHand.clear();
                            updateTableDisplay();
                            turnPassCount = 0;

                            roundEnd();
                        }
                        showTransitionScreen();
                        proceedToNextTurn();
        
                        // refresh panel to update UI
                        southPane.revalidate();
                        southPane.repaint();
                    }
                }
            }
        });

        // A simple exiting of the app upon quitting.
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    //Method that is called before moving to Next Player's Turn 
    //To ensure fairness in gameplay, so that each player cannot see the others' cards.
    private void showTransitionScreen() {
        clearCardDisplay();
        String message = "<html><center>Please pass to the next Player"
                        + "<br>Press OK when next Player is ready.</center><html>";
        
        JOptionPane.showMessageDialog(this, message, "Next Player's Turn", JOptionPane.INFORMATION_MESSAGE);
        
        updateCardDisplay();
    }
    
    private void clearCardDisplay() {
        southPane.removeAll();
        southPane.revalidate();
        southPane.repaint();
    }


    private void roundEnd() {
        for (int i = 0; i < players.length; i++) {
            players[i].setPass(false);
            players[i].setHasNotThrown(true);
        }
    }

    private boolean checkLastPlayedHand() {
        // the player on the very first turn must play the three of diamonds
        if (!playingHand.containsCard(new Card(Suit.DIAMONDS, Rank.THREE)) && firstEverTurn == true) {
            return false;
        }

        if (playingHand.isValid() && !table.beatsLastPlayed(playingHand)
            && (playingHand.getHand().size() == table.getLastPlayingHand().size() || table.getLastPlayingHand().size() == 0)) {
            table.getLastPlayingHand().clear();
            table.setLastPlayingHand(playingHand);
            return true;
        }
        return false;
    }

    private void proceedToNextTurn() {
        // the % players.length will ensure that currentPlayer is always a number between 0-1
        currentPlayer = (currentPlayer + 1) % players.length;
        while (players[currentPlayer].hasPassed()) {
            currentPlayer = (currentPlayer + 1) % players.length;
        }
        currentPlayerTurn.setText("You (" + players[currentPlayer].getName() + ") (" + players[currentPlayer].getHand().getHand().size() + " C)");
        lastPlayingHandName.setText("Current Table Hand: " + getCombinationName(table));
        updateCardDisplay();

        southPane.revalidate();
        southPane.repaint();
    }

    // method that can be perpetually called to update the cardsPanel display
    private void updateCardDisplay() {
        southPane.removeAll();

        Player player = players[currentPlayer];
        // 1. player.getHand() returns a Hand instance 
        // 2. From the Hand instance, it calls the getHand() method and returns an arraylist of cards 
        ArrayList<Card> currentCards = player.getHand().getHand();

        int cardOffset = 0;
        int layerNumber = 0;

        // iterate through the cards from our ArrayList, generating a JButton with an image to represent each card.
        for (Card c: currentCards) {
            String cardFilename = Card.getFilename(c.getSuit(), c.getRank());
            ImageIcon cardImage = new ImageIcon("assets/images/" + cardFilename);
            Image resizedCardImage = cardImage.getImage().getScaledInstance(100, 145,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            cardImage = new ImageIcon(resizedCardImage);  // transform it back
            JButton cardButton = new JButton(cardImage);
            cardButton.setBorder(BorderFactory.createEmptyBorder());
            cardButton.setContentAreaFilled(false);
            cardButton.setFocusPainted(false); 
            // Wrap the card button in a custom panel
            JPanel cardPanel = new JPanel(new BorderLayout());
            cardPanel.setOpaque(false);
            cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2)); // Initial border, simulating card's normal state
            cardPanel.add(cardButton);
            cardPanel.setBounds(cardOffset, 0, 112, 170);
            cardOffset += 40;
        
            // logic for selection and deselection of cards, with additional visual effects to indicate selection.
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!playingHand.containsCard(c)) {
                        playingHand.addCard(c);
                        cardPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Decrease top padding to "move up"
                    } else {
                        playingHand.removeCard(c);
                        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2)); // Reset to normal state
                        cardButton.setBorder(BorderFactory.createEmptyBorder());
                    }
                    southPane.revalidate();
                    southPane.repaint();
                }
            });
        
            southPane.add(cardPanel);
            southPane.setLayer(cardPanel, layerNumber++);
            // southPane.revalidate();
            // southPane.repaint();
            cardButtons.put(cardFilename, cardButton); 
        }

        // Remove cards from the opponent's pane so we can move them with each turn
        northPane.removeAll();

        HashMap<String, Player> playerPos = getPlayerPositions(currentPlayer);
        
        // //Create hand and display player name for north player
        displayOpponentCards(playerPos.get("north"), "north");
        displayOpponentLabel(playerPos.get("north"), northPane);
        northPane.revalidate();
        northPane.repaint();
    }

    // similar logic as the updateCardDisplay method, however here it's for the table storing the last played hand.
    private void updateTableDisplay() {
        // clear all initial cards
        tablePanel.removeAll();

        // for loop to iterate through each card in the last played playing hand, displaying it in the center table panel.
        for (Card c: playingHand.getHand()) {
            String cardFilename = Card.getFilename(c.getSuit(), c.getRank());
            ImageIcon cardImage = new ImageIcon("assets/images/" + cardFilename);
            Image resizedCardImage = cardImage.getImage().getScaledInstance(120, 174,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            cardImage = new ImageIcon(resizedCardImage);  
            JButton cardButton = new JButton(cardImage);
            cardButton.setBorder(BorderFactory.createEmptyBorder());
            cardButton.setContentAreaFilled(false);
            cardButton.setFocusPainted(false); // Optional: to prevent focus border from altering the appearance
            tablePanel.add(cardButton);
        }
        
        lastPlayingHandName.setText("Current Table Hand: " + getCombinationName(table));
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    // disable buttons once the game is over to prevent further gameplay.
    private void disableAllButtons() {
        playButton.setEnabled(false);
        passButton.setEnabled(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TwoPlayers frame = new TwoPlayers();
            frame.setVisible(true);

            System.out.println("Game start!");
            System.out.println("Player 1 hand: " + players[0].getHand().toString());
            System.out.println("Player 2 hand: " + players[1].getHand().toString());
        });
    }
}