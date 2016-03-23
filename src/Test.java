import util.StringUtil;
import java.io.IOException;

/**
 * Simple Testing class to test the String en- and decoder.
 * The german umlauts are examples of special characters.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        String input = "this is a test string! äöü";
        System.out.println(input);
        String encoded = StringUtil.binaryEncode(input);
        System.out.println(encoded);
        String decoded = StringUtil.binaryDecode(encoded);
        System.out.println(decoded);
    }

}
