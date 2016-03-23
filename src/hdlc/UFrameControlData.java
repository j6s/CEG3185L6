package hdlc;

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Control Data of a U-Frame
 */
public class UFrameControlData implements ControlDataInterface {

    public static String TYPE_SNRM = "00001";
    public static String TYPE_UA = "00110";

    private String data;


    public UFrameControlData(String type) throws InvalidArgumentException {
        this(type, false);
    }

    public UFrameControlData(String type, boolean pf) throws InvalidArgumentException {
        if (type.length() != 5) {
            throw new InvalidArgumentException(new String[]{"type must be of length 5"});
        }
        //System.out.println("11." + type.substring(0,2) + "." + (pf ? "1" : "0") + "." + type.substring(2,5));
        this.data = "11" + type.substring(0,2) + (pf ? "1" : "0") + type.substring(2,5);
    }

    @Override
    public String getBinaryString() {
        return data;
    }
}
