package Big2.exception;

// If user were to input any integer other than 2 or 4, we want to immediately catch and throw an exception
public class IllegalNumberOfPlayersException extends Exception {
    public IllegalNumberOfPlayersException(String msg) {
        super(msg);
    }
}
