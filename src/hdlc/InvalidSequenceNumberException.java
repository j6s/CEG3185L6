package hdlc;

/**
 * Invalid SequenceNumber Exception:
 * Thrown, when a sequence number is out of range
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
