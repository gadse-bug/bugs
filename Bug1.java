import com.github.situx.compiler.parser.C1;
import com.github.situx.compiler.parser.NullWriter;
import com.github.situx.compiler.parser.ParseException;
import com.github.situx.compiler.treej.Node;
import com.github.situx.compiler.visitorj.AsHTML;
import com.github.situx.compiler.visitorj.AsLatex;

import java.io.StringReader;
import java.io.Writer;

public class Bug1 {

    public static void main(String[] args) throws ParseException {
        String s = "class SampleInter { SampleInter ( ) { return null ; 0L ;";
        StringReader reader = new StringReader(s);
        C1 parser = new C1(reader);
        Node i = parser.program();
        Writer w = new NullWriter();
        AsHTML html = new AsHTML(w,"null.file", 4,0);
        i.welcome(html);
        Writer wL = new NullWriter();
        AsLatex latex = new AsLatex(wL,"null.file",4,0);
        i.welcome(latex);
    }
}
