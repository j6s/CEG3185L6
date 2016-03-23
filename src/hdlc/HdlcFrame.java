package hdlc;


import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Builder class for an HDLC Frame:
 *
 * Takes different parts of a frame  and can be used to generate the final binary string
 */
public class HdlcFrame {

    /**
     * The data that is to be transmitted
     */
    private String data;

    /**
     * The address this frame is supposed to be sent to
     */
    private String address;

    /**
     * The control data object
     */
    private ControlDataInterface control;

    /**
     * The flag sequence that is to be used
     */
    private String flag;

    /**
     * constructor for when no flag is given. Calls the main constructor with a default flag of "01111110"
     *
     * @see HdlcFrame#HdlcFrame(String, String, ControlDataInterface, String)
     *
     * @throws InvalidArgumentException
     */
    public HdlcFrame(String data, String address, ControlDataInterface control) throws InvalidArgumentException {
        this(data, address, control, "01111110");
    }

    /**
     * Main constructor
     *
     * @param data      The data that is to be transmitted
     * @param address   The address of the recipient
     * @param control   The control data
     * @param flag      The flag that will be used
     *
     * @throws InvalidArgumentException     If the address is malformed
     */
    public HdlcFrame(String data, String address, ControlDataInterface control, String flag) throws InvalidArgumentException {
        if (address.length() != 8) {
            throw new InvalidArgumentException(new String[]{"address must be of length 8 but is of length " + address.length()});
        }
        this.data = data;
        this.address = address;
        this.control = control;
        this.flag = flag;
    }

    /**
     * Gets the binary string resembling this frame
     */
    public String toString() {
        String out = this.flag;

        out += this.address;
        out += this.control.getBinaryString();
        out += this.data;
        out += "0000000000000000"; // TODO insert 16bit hash
        out += this.flag;

        return out;
    }

    public static int extractSequenceNumber(String input, int ns) throws InvalidSequenceNumberException {
        String num = input.substring(18,21);
        int number = Integer.parseInt(num, 2);

        if (number < ns || number > (ns + 3) % 8) {
            throw new InvalidSequenceNumberException("Sequence number of " + number + " is out of bounds");
        }

        return number;
    }

}
