package javascriptInterpreter.visitors;

import javascriptInterpreter.parser.Token;
import javascriptInterpreter.tree.ASTellision;

public class JSToJavaUtils {
    public static boolean doubleToBoolean(double d){
        return d != 0;
    }

    public static double booleanToDouble(boolean b){
        return b ? 1 : 0;
    }

    public static int countCommas(ASTellision ellision){
        int count = 1;

        Token currentToken = ellision.jjtGetFirstToken();

        while(currentToken != ellision.jjtGetLastToken()){
            count++;
            currentToken = currentToken.next;
        }
        System.out.println("I counted " + count + " commas");
        return count;
    }
}
