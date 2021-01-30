package javascriptInterpreter.visitors;

import javascriptInterpreter.tree.*;


public class DeclarationVisitor extends JavascriptDefaultVisitor {

    public JavascriptType visit(ASTvariableDefinition node, Context data){
        node.childrenAccept(this, data);
        return null;
    }

    public JavascriptType visit(ASTvariableDeclarationList node, Context data){
        node.childrenAccept(this, data);
        return null;
    }

    public JavascriptType visit(ASTvariableDeclaration node, Context data){
        String identifierName = node.jjtGetFirstToken().image;
        JavascriptType identifierValue = new JavascriptType();

        if(node.jjtGetNumChildren() > 0){
            ASTassignmentExpression valueNode = (ASTassignmentExpression) node.jjtGetChild(0);
            identifierValue = valueNode.jjtAccept(new EvaluationVisitor(), data);
        }

        identifierValue.setIdentifierName(identifierName);
        System.out.println("adding identifier : " + identifierName);
        data.addIdentifier(identifierName, identifierValue);

        return defaultVisit(node, data);
    }

    /*public JavascriptType visit(ASTnamedFunction node, Context data){
        data.addIdentifier(node.jjtGetFirstToken().next.image, new JavascriptType(node));
        EvaluationVisitor v = new EvaluationVisitor();

        return (v, data);
    }*/

}
