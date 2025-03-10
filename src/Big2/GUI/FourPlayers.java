package Big2.GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import Big2.cardinfo.*;
import Big2.hand.PlayingHand;
import Big2.gamelogic.*;
import Big2.sound.*;

public class FourPlayers extends JFrame {
    // initialize the Swing UI elements to be displayed
    private JPanel cardsPanel, tablePanel, infoPanel, currentInfoPanel;
    private JButton playButton, passButton, quitButton;
    private JLabel currentPlayerTurn, lastPlayingHandName, messageToCurrentPlayer;
    private JLayeredPane westPane, northPane, eastPane;
    private PlayingHand playingHand = new PlayingHand();
    private HashMap<String, JButton> cardButtons = new HashMap<>(); // track buttons by card name

    // game logic variables
    private static Player[] players;
    private int turnNo, currentPlayer, turnPassCount;
    private static int playerCount = 4;
    private boolean firstEverTurn;
    Player roundWinner;
    Table table;
    Deck deck;
    Sounds backgroundMusic = new Sounds();
    int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public FourPlayers() {
        // in our constructor, we initialize some of the Swing properties, such as the window's title and state.
        setTitle("Big Two");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#35654d"));

        backgroundMusic.setMusic("assets/sound/bgm.wav");
        backgroundMusic.playMusicContinuously();

        // initialize game logic variables

        deck = new Deck(); // Create new deck (empty arraylist of cards)
        //deck.initialize(); // Add 52 cards to deck
        deck.shuffle(); // randomise the distribution of cards

        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        players = new Player[] { p1, p2, p3, p4 }; // Array of players

        turnNo = distribution(deck, players); // turn no determines which player starts first
        firstEverTurn = true;
        turnPassCount = 0;

        //table = new Table(p1, p2, p3, p4);
        table = new Table();
        currentPlayer = turnNo;

        cardsPanel = new JPanel(new FlowLayout());
        cardsPanel.setOpaque(false);

        // Create controlpanel to hold "play", "pass", "quit" buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);

        // Initialsise infopanel to hold textual information of the JLabels declared above
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Initialise tablepanel to hold the cards played to the table
        tablePanel = new JPanel(new FlowLayout());
        tablePanel.setPreferredSize(new Dimension(800, 200));
        tablePanel.setOpaque(false);

        // Initialise currentinfo panel to hold lastPlayingHandName and messageToCurrentPlayer
        currentInfoPanel = new JPanel(new BorderLayout());
        currentInfoPanel.setOpaque(false);
        currentInfoPanel.setMaximumSize(new Dimension(1200, 50));

        // Initialsise opponent panes to show cards of other players
        westPane = new JLayeredPane();
        westPane.setPreferredSize(new Dimension(400, 500));

        eastPane = new JLayeredPane();
        eastPane.setPreferredSize(new Dimension(400, 500));

        northPane = new JLayeredPane();
        northPane.setPreferredSize(new Dimension(800, 300));

        // Create centerpanel to hold table, northplayer and buttons(controlpanel)
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

        // Initialize Label to display helper info to current player
        messageToCurrentPlayer = new JLabel("You must play a hand containing the 3 of Diamonds");
        messageToCurrentPlayer.setFont(new Font("Arial", Font.PLAIN, 14));
        messageToCurrentPlayer.setForeground(Color.decode("#cecece"));

        // Initialize Label to display the type of hand currently on the table
        lastPlayingHandName = new JLabel("Current Table Hand: " + getCombinationName(table));
        lastPlayingHandName.setFont(new Font("Arial", Font.BOLD, 18));
        lastPlayingHandName.setForeground(Color.white);

        // Initialize Label to display current player textually
        currentPlayerTurn = new JLabel("You (" + players[currentPlayer].getName() + ") ("
            + players[currentPlayer].getHand().getHand().size() + " C)");
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
        add(westPane, BorderLayout.WEST);
        add(eastPane, BorderLayout.EAST);
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
            Image resizedBackImage = cardBackImage.getImage().getScaledInstance(screenWidth/20, screenHeight/8, java.awt.Image.SCALE_SMOOTH); // scale the image
            cardBackImage = new ImageIcon(resizedBackImage); // transform it back
            JButton cardBack = new JButton(cardBackImage);
            cardBack.setBorder(BorderFactory.createEmptyBorder());
            cardBack.setContentAreaFilled(false);
            cardBack.setFocusPainted(false);
            // depending on the location of the player, display the rendered card accordingly by adding it to the relevant pane.
            if (location.equals("west")) {
                cardBack.setBounds(0, offset, 200, 500);
                westPane.add(cardBack);
            } else if (location.equals("north")) {
                cardBack.setBounds(offset, 0, (int)(screenWidth/3.5), 150);
                northPane.add(cardBack);
            } else if (location.equals("east")) {
                cardBack.setBounds(200, offset, 200, 500);
                eastPane.add(cardBack);
            }
            offset += 25;
        }
    }

    // Display a visible label for each opponent whose cards are rendered above.
    private void displayOpponentLabel(Player opponent, JLayeredPane targetPane) {
        JLabel opponentLabel = new JLabel(opponent.getName() + " (" + opponent.displayHasPassed() + ") ("
                + opponent.getHand().getHand().size() + " C)");
        if (targetPane == westPane) {
            opponentLabel.setBounds(200, (int)(screenHeight/2.5), 150, 50);
        } else if (targetPane == eastPane) {
            opponentLabel.setBounds(50, (int)(screenHeight/2.5), 150, 50);
        } else if (targetPane == northPane) {
            opponentLabel.setBounds((int)((screenWidth)/5), 150, 150, 50);
        }
        opponentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        opponentLabel.setForeground(Color.white);
        targetPane.add(opponentLabel);
    }

    // store each player's position in a HashMap.
    private HashMap<String, Player> getPlayerPositions(int currentPlayer) {
        HashMap<String, Player> map = new HashMap<>();
        map.put("west", players[(currentPlayer + 3) % playerCount]);
        map.put("north", players[(currentPlayer + 2) % playerCount]);
        map.put("east", players[(currentPlayer + 1) % playerCount]);
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
            } else if (Combinations.isStraight(table.getLastPlayingHand())
                    && Combinations.isFlush(table.getLastPlayingHand())) {
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
        turnNo = 0; // determines which player starts first

        while (deck.getIndex() < 52) {
            for (Player player : players) {
                player.addToHand(deck.dealCard());
            }
        }

        for (int i = 0; i < players.length; i++) {
            players[i].sortHand();
            if (players[i].getHand().containsCard(new Card(Suit.DIAMONDS, Rank.THREE))) {
                turnNo = i % playerCount;
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
                        cardsPanel.revalidate();
                        cardsPanel.repaint();
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
                        playingHand.clear();
        
                        if (turnPassCount == 3) {
                            
                            // clear previous playing hand
                            table.getLastPlayingHand().clear();
    
                            // clear table
                            tablePanel.removeAll();
    
                            while (players[currentPlayer % playerCount].hasPassed()) {
                                currentPlayer++;
                            }
    
                            JOptionPane.showMessageDialog(null, "Three players have passed! New round starting for player " + players[currentPlayer % playerCount].getName() + "!");
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
                        cardsPanel.revalidate();
                        cardsPanel.repaint();
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
    
    // straightforward method to clear cards.
    private void clearCardDisplay() {
        cardsPanel.removeAll();
        cardsPanel.revalidate();
        cardsPanel.repaint(); 
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
            && (playingHand.getHand().size() == table.getLastPlayingHand().size()
            || table.getLastPlayingHand().size() == 0)) {
            table.getLastPlayingHand().clear();
            table.setLastPlayingHand(playingHand);
            return true;
        }
        return false;
    }

    private void proceedToNextTurn() {
        // the % playerCount will ensure that currentPlayer is always a number between 0-3
        currentPlayer = (currentPlayer + 1) % playerCount;
        while (players[currentPlayer].hasPassed()) {
            currentPlayer = (currentPlayer + 1) % playerCount;
        }
        currentPlayerTurn.setText("You (" + players[currentPlayer].getName() + ") ("
            + players[currentPlayer].getHand().getHand().size() + " C)");
        lastPlayingHandName.setText("Current Table Hand: " + getCombinationName(table));
        updateCardDisplay();

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // method that can be perpetually called to update the cardsPanel display
    private void updateCardDisplay() {
        cardsPanel.removeAll();

        Player player = players[currentPlayer % playerCount];
        // 1. player.getHand() returns a Hand instance 
        // 2. From the Hand instance, it calls the getHand() method and returns an arraylist of cards 
        ArrayList<Card> currentCards = player.getHand().getHand();
        
        // iterate through the cards from our ArrayList, generating a JButton with an image to represent each card.
        for (Card c : currentCards) {
            String cardFilename = Card.getFilename(c.getSuit(), c.getRank());
            ImageIcon cardImage = new ImageIcon("assets/images/" + cardFilename);
            Image resizedCardImage = cardImage.getImage().getScaledInstance(100, 145, java.awt.Image.SCALE_SMOOTH); // scale it smoothly
            cardImage = new ImageIcon(resizedCardImage); // transform it back
            JButton cardButton = new JButton(cardImage);
            cardButton.setBorder(BorderFactory.createEmptyBorder());
            cardButton.setContentAreaFilled(false);
            cardButton.setFocusPainted(false); // Optional: to prevent focus border from altering the appearance

            // Wrap the card button in a custom panel
            JPanel cardPanel = new JPanel(new BorderLayout());
            cardPanel.setOpaque(false);
            cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2)); // Initial border, simulating card's normal state
            cardPanel.add(cardButton);

            // logic for selection and deselection of cards, with additional visual effects to indicate selection.
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!playingHand.containsCard(c)) {
                        playingHand.addCard(c);
                        cardPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Decrease top padding to "move up"
                        cardButton.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2)); // Highlight selection
                    } else {
                        playingHand.removeCard(c);
                        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2)); // Reset to normal state
                        cardButton.setBorder(BorderFactory.createEmptyBorder());
                    }
                    cardsPanel.revalidate();
                    cardsPanel.repaint();
                }
            });

            cardsPanel.add(cardPanel);
            cardButtons.put(cardFilename, cardButton); // Keep a reference to the button
        }

        // Remove cards from each player's (that is not current player's) pane so we can move them with each turn
        westPane.removeAll();
        eastPane.removeAll();
        northPane.removeAll();

        HashMap<String, Player> playerPos = getPlayerPositions(currentPlayer);

        // Create hand and display player name for west player
        displayOpponentCards(playerPos.get("west"), "west");
        displayOpponentLabel(playerPos.get("west"), westPane);
        westPane.revalidate();
        westPane.repaint();

        // Create hand and display player name for east player
        displayOpponentCards(playerPos.get("east"), "east");
        displayOpponentLabel(playerPos.get("east"), eastPane);
        eastPane.revalidate();
        eastPane.repaint();

        // Create hand and display player name for north player
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
        for (Card c : playingHand.getHand()) {
            String cardFilename = Card.getFilename(c.getSuit(), c.getRank());
            ImageIcon cardImage = new ImageIcon("assets/images/" + cardFilename);
            Image resizedCardImage = cardImage.getImage().getScaledInstance(screenWidth/16, (int)(screenHeight/6.4), java.awt.Image.SCALE_SMOOTH); // scale it smoothly
            cardImage = new ImageIcon(resizedCardImage); // transform it back
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
}