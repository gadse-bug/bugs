import rong.CMMParser;
import rong.ParseException;

import java.io.StringReader;

public class Bug3 {

    public static void main(String[] args) throws ParseException {
        String s = "int ff36 = ( 3 / - ff36 <>";
        StringReader reader = new StringReader(s);
        CMMParser parser = new CMMParser(reader);
        parser.procedure();
    }


}
