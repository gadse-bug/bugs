import javascriptInterpreter.parser.Javascript;
import javascriptInterpreter.parser.ParseException;
import javascriptInterpreter.tree.SimpleNode;
import javascriptInterpreter.visitors.Context;
import javascriptInterpreter.visitors.ExecutionVisitor;

import java.io.StringReader;

public class Bug5 {
    public static void main(String[] args) throws ParseException {
        String s = "1 + 1";
        StringReader reader = new StringReader(s);
        Javascript parser = new Javascript(reader);
        SimpleNode sn = parser.program();
        ExecutionVisitor v = new ExecutionVisitor();
        Context scope = new Context();
        sn.jjtAccept(v, scope);
    }
}
