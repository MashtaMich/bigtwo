package Big2;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Big2.GUI.TwoPlayers;

// If user selects 2 players, this main method will run, to facilitate 2 player mode
// Line 14-19 necessary for mac users, because for mac, colors will not show up if not for lines 14-19. Lines 14-19 does not change the outcome for windows
public class TwoPlayerMode {
    public static void main(String[] args) {
        try { 
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Pushes the events, ensure everything in our constructor is initialised first before running our program
        EventQueue.invokeLater(() -> { 
            TwoPlayers frame = new TwoPlayers();
            frame.setVisible(true);
        });
    }
}