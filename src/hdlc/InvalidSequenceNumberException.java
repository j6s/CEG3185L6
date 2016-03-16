package hdlc;

/**
 * Created by j on 3/8/16.
 */
public class InvalidSequenceNumberException extends Exception {

    private String message;

    public InvalidSequenceNumberException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
