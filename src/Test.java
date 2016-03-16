

import util.StringUtil;

import java.io.IOException;

/**
 * Created by j on 3/8/16.
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
