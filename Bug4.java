import javascriptInterpreter.parser.Javascript;
import javascriptInterpreter.parser.ParseException;
import javascriptInterpreter.tree.SimpleNode;
import javascriptInterpreter.visitors.Context;
import javascriptInterpreter.visitors.ExecutionVisitor;

import java.io.StringReader;

public class Bug4 {
    public static void main(String[] args) throws ParseException {
        String s = "; { var fh8 ; new 0E0 new new new new new new fh8 = 0E0 ; } var fh8 = 0E0 for ( var fh8 = 0E0 ; fh8 < 0E0 ; ) { fh8 = 0E0 ; }";
        StringReader reader = new StringReader(s);
        Javascript parser = new Javascript(reader);
//		SimpleNode sn = null;
        SimpleNode sn = parser.program();
        ExecutionVisitor v = new ExecutionVisitor();
        Context scope = new Context();
        sn.jjtAccept(v, scope);
    }
}
