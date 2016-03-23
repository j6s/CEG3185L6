package util;

import java.io.UnsupportedEncodingException;

/**
 * String Utility:
 *
 * Handles encoding and decoding of strings.
 */
public class StringUtil {

    /**
     * The encoding that is to be used for encoding and decoding.
     * Note, that while UTF-8 is the default, there seems to be an issue
     * when using special characters
     */
    private static String ENCODING = "UTF-8";

    /**
     * Encodes a given string and returns a "binary" string that only contains
     * ones and zeros (representing a real binary stream)
     *
     * @param input     The string that is to be encoded
     * @return
     *
     * @throws UnsupportedEncodingException
     */
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

    /**
     * Decodes a binary string such as the one created by the encoding function
     *
     * @see StringUtil#binaryEncode(String)
     *
     * @param input     The binary string that is to be decoded
     * @return
     *
     * @throws UnsupportedEncodingException
     */
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
