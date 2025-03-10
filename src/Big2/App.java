package Big2;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.net.URI;
import java.net.URL;

import Big2.exception.IllegalNumberOfPlayersException;

public class App {
    public static void main(String[] args) {
        // Takes in user input, asking how many players want to play the game
        try (Scanner sc = new Scanner(System.in)) {
            // Our program only allows either 2 or 4 players
            System.out.print("Select number of players, 2 or 4 > ");
            int playersPlaying = sc.nextInt();
            // Link to guide on how to play Big2
            URL big2Guide = new URI("https://www.wikihow.com/Play-Big-Two").toURL();
            // Instructions for user on what to do next
            String instruction = "\nBIG2 COMMENCING, please head over to Swing and enjoy! \nclick the link -> " + big2Guide + " <- if unsure of how to play";
            if (playersPlaying == 2) {
                // If user inputs 2, runs the 2 player mode
                System.out.println(instruction);
                TwoPlayerMode.main(new String[0]);
            }
            else if (playersPlaying == 4) {
                // If user inputs 4, runs the 4 player mode
                System.out.println(instruction);
                FourPlayerMode.main(new String[0]);
            }
            else {
            // Any number other than 2 or 4 will not be accepted, hence we created an Exception class to handle other integers
                throw new IllegalNumberOfPlayersException("" + playersPlaying + " players not allowed to play, only 2 or 4 players allowed. Please rerun the program again and input 2 or 4 players!");
            }
            sc.close();
        }

        catch (InputMismatchException e) { // If user does not input an integer
            System.out.println("Please run the program again and input actual integer (2 or 4)");
        }

        catch (IllegalNumberOfPlayersException e) { // If user inputs another integer other an 2 or 4
            System.out.println(e.getMessage());
        }

        catch (Exception e) {
            System.out.println("Game cannot start");
        }
    }
}