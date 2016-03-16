package hdlc;


import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Created by j on 3/8/16.
 */
public class HdlcFrame {

    private String data;
    private String address;
    private ControlDataInterface control;
    private String flag;

    public HdlcFrame(String data, String address, ControlDataInterface control) throws InvalidArgumentException {
        this(data, address, control, "01111110");
    }

    public HdlcFrame(String data, String address, ControlDataInterface control, String flag) throws InvalidArgumentException {
        if (address.length() != 8) {
            throw new InvalidArgumentException(new String[]{"address must be of length 8 but is of length " + address.length()});
        }
        this.data = data;
        this.address = address;
        this.control = control;
        this.flag = flag;
    }

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

        if (number < ns || number > (ns + 7) % 8) {
            throw new InvalidSequenceNumberException("Sequence number of " + number + " is out of bounds");
        }

        return number;
    }

}
