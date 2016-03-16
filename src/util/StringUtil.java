package util;

import java.io.UnsupportedEncodingException;

/**
 * Created by j on 3/15/16.
 */
public class StringUtil {

    private static String ENCODING = "UTF-8";

    public static String binaryEncode(String input) throws UnsupportedEncodingException {
        byte[] bytes = input.getBytes(ENCODING);
        StringBuilder out = new StringBuilder();
        String bitstring;

        for (byte b : bytes) {
            bitstring = Integer.toBinaryString(Byte.toUnsignedInt(b));
            bitstring = "00000000".substring(bitstring.length()) + bitstring;
            out.append(bitstring);
        }

        return out.toString();
    }

    public static String binaryDecode(String input) throws UnsupportedEncodingException {

        int length = (int)Math.ceil(input.length() / 8) + 1;
        byte[] bytes = new byte[length];

        String chunk;
        int max;


        int j = 0;
        for (int i = 0; i < input.length(); i += 8) {
            max = (i + 8 >= input.length()) ? input.length() : i+8;
            if (max == i) {
                break;
            }
            chunk = input.substring(i, max);
            bytes[j++] = (byte)Integer.parseInt(chunk, 2);
        }

        return new String(bytes, ENCODING);
    }

}
