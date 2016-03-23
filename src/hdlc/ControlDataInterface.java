package hdlc;

/**
 * Interface for control data classes to implement:
 *
 * Every control data class should implement this interface. It represents the
 * control block of the header of a HDLC Frame.
 */
public interface ControlDataInterface {

    /**
     * Returns the binary string for this header data
     * @return
     */
    public String getBinaryString();

}
