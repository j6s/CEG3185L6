package hdlc;

/**
 * Created by j on 3/8/16.
 */
public class IFrameControlData implements ControlDataInterface {

    private String data;

    public IFrameControlData (int ns, boolean pf, int nr) {
        String s = Integer.toBinaryString(ns);
        String r = Integer.toBinaryString(nr);
        s = "000".substring(s.length()) + s;
        r = "000".substring(r.length()) + r;

        this.data = "0" + s + (pf ? "1" : "0") + r;
    }

    @Override
    public String getBinaryString() {
        return this.data;
    }
}
