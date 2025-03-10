package Big2;

import Big2.GUI.FourPlayers;
import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


// If user selects 4 players, runs the Four players mode game
// Line 14-19 necessary for mac users, because for mac, colors will not show up if not for lines 14-19. Lines 14-19 not necessary for Windows
public class FourPlayerMode {
    public static void main(String[] args) {
        try { 
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Pushes the events, ensure everything in our constructor is initialised first before running our program
        EventQueue.invokeLater(() -> {
            FourPlayers frame = new FourPlayers();
            frame.setVisible(true);
        });
    }
}