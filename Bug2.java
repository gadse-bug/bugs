import rong.CMMParser;
import rong.ParseException;

import java.io.StringReader;

public class Bug2 {

    public static void main(String[] args) throws ParseException {
        String s = "int clR = 04 - clR";
        StringReader reader = new StringReader(s);
        CMMParser parser = new CMMParser(reader);
        parser.procedure();
    }


}
