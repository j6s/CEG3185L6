package hdlc;

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Created by j on 3/8/16.
 */
public class SFrameControlData implements ControlDataInterface {

    public static String TYPE_RR= "00";

    private String data;

    public SFrameControlData(int sequence, boolean pf, String type) throws InvalidArgumentException {
        String seq = Integer.toBinaryString(sequence);
        seq = "000".substring(seq.length()) + seq;

        if (type.length() != 2) {
            throw new InvalidArgumentException(new String[]{"type must be of length 2"});
        }

        this.data = "10" + type + (pf ? "1" : "0") + seq;
    }

    @Override
    public String getBinaryString() {
        return this.data;
    }
}
