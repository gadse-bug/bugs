package javascriptInterpreter.visitors;

import javascriptInterpreter.parser.*;
import javascriptInterpreter.tree.*;

import static javascriptInterpreter.parser.JavascriptConstants.*;
import static javascriptInterpreter.tree.JavascriptTreeConstants.*;
import static javascriptInterpreter.visitors.JSToJavaUtils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EvaluationVisitor extends JavascriptDefaultVisitor {

    static private JavascriptType undefinedObject = null;

    //todo implement this
    public JavascriptType visit(ASTprimaryExpression node, Context data) {
        if(node.jjtGetNumChildren() == 0){
            String identifierName = node.jjtGetFirstToken().image;
            //this token
            if(identifierName.equals("this")){
                return undefinedObject;
            }
            //identifier name for variables
            else {
                return data.getValue(identifierName);
            }
        }
        //objects or literals
        else{
            Node child = node.jjtGetChild(0);
            return child.jjtAccept(this, data);
        }
    }

    public JavascriptType visit(ASTliteral node, Context data){
        Token t = node.jjtGetFirstToken();

        switch(t.kind){
            case NULL_LITERAL : return new JavascriptType(JavascriptType.Type.NULL, t.image);
            case BOOLEAN_LITERAL : return new JavascriptType(Boolean.parseBoolean(t.image));
            case NUMERIC_LITERAL : return new JavascriptType(Double.parseDouble(t.image));
            case STRING_LITERAL : return new JavascriptType(JavascriptType.Type.STRING, t.image);
            default : return undefinedObject;
        }

    }

    public JavascriptType visit(ASTarrayLiteral node, Context data){
        //create the array
        ArrayList<JavascriptType> arrayLiteral = new ArrayList<>();
        if(node.jjtGetNumChildren() == 0){
            return new JavascriptType(arrayLiteral);
        }

        ASTellision first = null;
        ASTelementList second = null;
        ASTellision third = null;

        //all three children
        if(node.jjtGetNumChildren() == 3){
            first = (ASTellision)node.jjtGetChild(0);
            second = (ASTelementList)node.jjtGetChild(1);
            third = (ASTellision)node.jjtGetChild(2);
        }
        //two children
        else if (node.jjtGetNumChildren() == 2){
            //ellision first then elements
            if(node.jjtGetFirstToken().next.image.equals(",")){
                first = (ASTellision)node.jjtGetChild(0);
                second = (ASTelementList)node.jjtGetChild(1);
            }
            //elements first then ellision
            else {
                second = (ASTelementList)node.jjtGetChild(0);
                third = (ASTellision)node.jjtGetChild(1);
            }
        }
        //only one child
        else{
            //only ellision
            if(node.jjtGetFirstToken().next.image.equals(",")){
                first = (ASTellision)node.jjtGetChild(0);
            }
            //only element list
            else{
                second = (ASTelementList)node.jjtGetChild(0);
            }
        }


        if(first != null){
            //pad the array with empty elements for each comma
            JavascriptType emptyElement = new JavascriptType();
            for(int i = 0; i < countCommas(first); i++){
                arrayLiteral.add(emptyElement);
            }
        }

        if(second != null){
            JavascriptType emptyElement = new JavascriptType();
            JavascriptType arrayElement;

            //only one element
            if(second.jjtGetNumChildren() == 1){
                arrayElement = second.jjtGetChild(0).jjtGetChild(0).jjtAccept(this, data);
                arrayLiteral.add(arrayElement);
            }
            //several elements
            else{
                arrayElement = second.jjtGetChild(0).jjtGetChild(0).jjtAccept(this, data);
                arrayLiteral.add(arrayElement);
                for(int i = 1; i < second.jjtGetNumChildren(); i += 2){

                    //pad the array with empty elements if there is more than one comma
                    for(int j = 1; j < countCommas((ASTellision)second.jjtGetChild(i)); j++){
                        arrayLiteral.add(emptyElement);
                    }

                    arrayElement = second.jjtGetChild(i + 1).jjtGetChild(0).jjtAccept(this, data);
                    arrayLiteral.add(arrayElement);

                }
            }
        }

        if(third != null){
            //pad the array with empty elements if there is more than one comma also decrease by 1
            JavascriptType emptyElement = new JavascriptType();
            for(int i = 0; i < countCommas(third) - 1; i++){
                arrayLiteral.add(emptyElement);
            }
        }

        return new JavascriptType(arrayLiteral);
    }

    public JavascriptType visit(ASTobjectLiteral node, Context data){
        HashMap<String, JavascriptType> properties = new HashMap<>();
        String key;
        JavascriptType value;
        for(int i = 0; i < node.jjtGetNumChildren(); i++){
            ASTpropertyDefinition child = (ASTpropertyDefinition)node.jjtGetChild(i);
            key = child.jjtGetFirstToken().image;
            value = child.jjtAccept(this, data);

            properties.put(key, value);
        }

        return new JavascriptType(properties);
    }

    public JavascriptType visit(ASTpropertyDefinition node, Context data){
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    //todo implement
    public JavascriptType visit(ASTfunctionExpression node, Context data){
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    //todo fix
    public JavascriptType visit(ASTparenthesizedExpression node, Context data) {
        ASTexpression child = (ASTexpression)node.jjtGetChild(0);
        return child.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTleftSideExpression node, Context data){
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    //todo implement and fix syntax
    public JavascriptType visit(ASTcallExpression node, Context data){
        ASTmemberExpression child = (ASTmemberExpression) node.jjtGetChild(0);

        return child.jjtAccept(this, data);
    }

    //todo implement and fix syntax
    public JavascriptType visit(ASTmemberExpression node, Context data){
        ASTprimaryExpression child = (ASTprimaryExpression) node.jjtGetChild(0);

        return child.jjtAccept(this, data);
    }


    //todo fix assignment in postfix update
    public JavascriptType visit(ASTupdateExpression node, Context data){
        ASTleftSideExpression operand = (ASTleftSideExpression)node.jjtGetChild(0);

        //there is an operator
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken() || node.jjtGetLastToken() != operand.jjtGetLastToken()){
            JavascriptType variableReference = operand.jjtAccept(this, data);
            double x = variableReference.getDouble();
            String operator;

            //postfix update
            if(node.jjtGetLastToken() != operand.jjtGetLastToken()){
                operator = node.jjtGetLastToken().image;
                System.out.print(x + operator + " = ");
                switch(operator){
                    case "++" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x + 1));
                                }
                                System.out.println(x);
                                return new JavascriptType(x);
                    case "--" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x - 1));
                                }
                                System.out.println(x);
                                return new JavascriptType(x);
                }
            }

            //prefix update
            else{
                operator = node.jjtGetFirstToken().image;
                System.out.print(operator + x + " = ");
                switch(operator){
                    case "++" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x + 1));
                                }
                                System.out.println(x + 1);
                                return new JavascriptType(x + 1);
                    case "--" : if(variableReference.getIdentifierName() != null){
                                    data.assignIdentifier(variableReference.getIdentifierName(), new JavascriptType(x - 1));
                                }
                                System.out.println(x - 1);
                                return new JavascriptType(x - 1);
                }
            }
        }
        //no operator
        return operand.jjtAccept(this, data);
    }


    //todo implement delete, void and typeof
    public JavascriptType visit(ASTunaryExpression node, Context data){
        ASTupdateExpression operand = (ASTupdateExpression)node.jjtGetChild(0);
        if(node.jjtGetFirstToken() != operand.jjtGetFirstToken()){
            double x = operand.jjtAccept(this, data).getDouble();
            String operator = node.jjtGetFirstToken().image;
            switch(operator){
                case "+" :
                    System.out.println(+x);
                    return new JavascriptType(+x);
                case "-" :
                    System.out.println(-x);
                    return new JavascriptType(-x);
                case "!" :
                    System.out.println(!doubleToBoolean(x));
                    return new JavascriptType(!doubleToBoolean(x));
                case "~" :
                    System.out.println(~(int)x);
                    return new JavascriptType(~(int)x);
                default : return undefinedObject;
            }
        }
        return operand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTexponentiationExpression node, Context data){
        ASTunaryExpression firstOperand = (ASTunaryExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTexponentiationExpression secondOperand = (ASTexponentiationExpression)node.jjtGetChild(1);
            double x = firstOperand.jjtAccept(this, data).getDouble();
            double y = secondOperand.jjtAccept(this, data).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "**" :
                    System.out.println(Math.pow(x, y));
                    return new JavascriptType(Math.pow(x, y));
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTmultiplicativeExpression node, Context data){
        //two or more operands
        if(node.jjtGetNumChildren() > 1){
            double result = node.jjtGetChild(0).jjtAccept(this, data).getDouble();
            for(int i = 1; i < node.jjtGetNumChildren(); i++){
                String nextOperator = ((SimpleNode)node.jjtGetChild(i - 1)).jjtGetLastToken().next.image;
                double nextOperand = node.jjtGetChild(i).jjtAccept(this, data).getDouble();
                System.out.print(result + " " + nextOperator + " " + nextOperand + " = ");
                switch(nextOperator){
                    case "*" :
                        System.out.println(result * nextOperand);
                        result *= nextOperand;
                        break;
                    case "/" :
                        System.out.println(result / nextOperand);
                        result /= nextOperand;
                        break;
                    case "%" :
                        System.out.println(result % nextOperand);
                        result %= nextOperand;
                        break;
                }
            }
            return new JavascriptType(result);
        }
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    public JavascriptType visit(ASTadditiveExpression node, Context data){

        ASTmultiplicativeExpression firstOperand = (ASTmultiplicativeExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTadditiveExpression secondOperand = (ASTadditiveExpression)node.jjtGetChild(1);
            double x = firstOperand.jjtAccept(this, data).getDouble();
            double y = secondOperand.jjtAccept(this, data).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "+" :
                    System.out.println(x + y);
                    return new JavascriptType(x + y);
                case "-" :
                    System.out.println(x - y);
                    return new JavascriptType(x - y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTshiftExpression node, Context data){

        ASTadditiveExpression firstOperand = (ASTadditiveExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTshiftExpression secondOperand = (ASTshiftExpression)node.jjtGetChild(1);
            int x = firstOperand.jjtAccept(this, data).getInt();
            int y = secondOperand.jjtAccept(this, data).getInt();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "<<" :
                    System.out.println(x << y);
                    return new JavascriptType(x << y);
                case ">>" :
                    System.out.println(x >> y);
                    return new JavascriptType(x >> y);
                case ">>>" :
                    System.out.println(x >>> y);
                    return new JavascriptType(x >>> y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    //todo implement in and instanceof
    public JavascriptType visit(ASTrelationalExpression node, Context data){

        ASTshiftExpression firstOperand = (ASTshiftExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTrelationalExpression secondOperand = (ASTrelationalExpression)node.jjtGetChild(1);
            double x = firstOperand.jjtAccept(this, data).getDouble();
            double y = secondOperand.jjtAccept(this, data).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "<" :
                    System.out.println(x < y);
                    return new JavascriptType(x < y);
                case ">" :
                    System.out.println(x > y);
                    return new JavascriptType(x > y);
                case "<=" :
                    System.out.println(x <= y);
                    return new JavascriptType(x <= y);
                case ">=" :
                    System.out.println(x >= y);
                    return new JavascriptType(x >= y);
                case "in" :
                    /*System.out.println(x + " in " + y);
                    return new JavascriptType(() ? : );*/
                    return undefinedObject;
                case "instanceof" :
                    return undefinedObject;
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    //todo implement === and !== and add tolerance for double equality
    public JavascriptType visit(ASTequalityExpression node, Context data){

        ASTrelationalExpression firstOperand = (ASTrelationalExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTequalityExpression secondOperand = (ASTequalityExpression)node.jjtGetChild(1);
            double x = firstOperand.jjtAccept(this, data).getDouble();
            double y = secondOperand.jjtAccept(this, data).getDouble();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "==" :
                    System.out.println(x < y);
                    return new JavascriptType(x == y);
                case "!=" :
                    System.out.println(x > y);
                    return new JavascriptType(x != y);
                case "===" :
                    return undefinedObject;
                case "!==" :
                    return undefinedObject;
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTbitwiseAndExpression node, Context data){

        ASTequalityExpression firstOperand = (ASTequalityExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTbitwiseAndExpression secondOperand = (ASTbitwiseAndExpression)node.jjtGetChild(1);
            int x = firstOperand.jjtAccept(this, data).getInt();
            int y = secondOperand.jjtAccept(this, data).getInt();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "&" :
                    System.out.println(x & y);
                    return new JavascriptType(x & y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTbitwiseXorExpression node, Context data){

        ASTbitwiseAndExpression firstOperand = (ASTbitwiseAndExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTbitwiseXorExpression secondOperand = (ASTbitwiseXorExpression)node.jjtGetChild(1);
            int x = firstOperand.jjtAccept(this, data).getInt();
            int y = secondOperand.jjtAccept(this, data).getInt();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "^" :
                    System.out.println(x ^ y);
                    return new JavascriptType(x ^ y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTbitwiseOrExpression node, Context data){

        ASTbitwiseXorExpression firstOperand = (ASTbitwiseXorExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTbitwiseOrExpression secondOperand = (ASTbitwiseOrExpression)node.jjtGetChild(1);
            int x = firstOperand.jjtAccept(this, data).getInt();
            int y = secondOperand.jjtAccept(this, data).getInt();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "|" :
                    System.out.println(x | y);
                    return new JavascriptType(x | y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTlogicalAndExpression node, Context data){

        ASTbitwiseOrExpression firstOperand = (ASTbitwiseOrExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTlogicalAndExpression secondOperand = (ASTlogicalAndExpression)node.jjtGetChild(1);
            boolean x = firstOperand.jjtAccept(this, data).getBoolean();
            boolean y = secondOperand.jjtAccept(this, data).getBoolean();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "&&" :
                    System.out.println(x && y);
                    return new JavascriptType(x && y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTlogicalOrExpression node, Context data){

        ASTlogicalAndExpression firstOperand = (ASTlogicalAndExpression)node.jjtGetChild(0);
        if(node.jjtGetNumChildren() > 1){
            ASTlogicalOrExpression secondOperand = (ASTlogicalOrExpression)node.jjtGetChild(1);
            boolean x = firstOperand.jjtAccept(this, data).getBoolean();
            boolean y = secondOperand.jjtAccept(this, data).getBoolean();
            String operator = ((SimpleNode)node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.print(x + " " + operator + " " + y + " = ");
            switch(operator){
                case "||" :
                    System.out.println(x || y);
                    return new JavascriptType(x || y);
            }
        }
        return firstOperand.jjtAccept(this, data);
    }

    public JavascriptType visit(ASTconditionalExpression node, Context data){
        ASTlogicalOrExpression firstChild = (ASTlogicalOrExpression)node.jjtGetChild(0);

        if(node.jjtGetNumChildren() > 1){
            ASTassignmentExpression secondChild = (ASTassignmentExpression)node.jjtGetChild(1);
            ASTassignmentExpression thirdChild = (ASTassignmentExpression)node.jjtGetChild(2);

            boolean condition = firstChild.jjtAccept(this, data).getBoolean();

            if(condition){
                System.out.println(true + " : choosing second operand");
                return secondChild.jjtAccept(this, data);
            }
            else{
                System.out.println(false + " : choosing third operand");
                return thirdChild.jjtAccept(this, data);
            }
        }

        return firstChild.jjtAccept(this, data);
    }

    //todo fix and implement compound assignment
    public JavascriptType visit(ASTassignmentExpression node, Context data){
        //all three children
        if(node.jjtGetChild(0).getId() == JJTLEFTSIDEEXPRESSION){
            ASTleftSideExpression firstChild = (ASTleftSideExpression)node.jjtGetChild(0);
            JavascriptType variableReference = firstChild.jjtAccept(this, data);

            ASTassignmentExpression second = (ASTassignmentExpression)node.jjtGetChild(1);
            JavascriptType assignedValue = second.jjtAccept(this, data);

            String operator = ((ASTleftSideExpression) node.jjtGetChild(0)).jjtGetLastToken().next.image;
            System.out.println(operator);
            //assignment
            switch(operator){
                case "=" : if(variableReference.getIdentifierName() == null){
                                return assignedValue;
                            }
                            else{
                                data.assignIdentifier(variableReference.getIdentifierName(), assignedValue);
                                return assignedValue;
                            }
                default : return undefinedObject;
            }
        }
        //only one child conditional
        else{
            ASTconditionalExpression child = (ASTconditionalExpression)node.jjtGetChild(0);

            return child.jjtAccept(this, data);
        }
    }

    //todo fix
    public JavascriptType visit(ASTexpression node, Context data){
        ASTassignmentExpression firstChild = (ASTassignmentExpression)node.jjtGetChild(0);
        JavascriptType expressionValue = firstChild.jjtAccept(this, data);
        for(int i = 1; i < node.jjtGetNumChildren(); i++){
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return expressionValue;
    }

}
