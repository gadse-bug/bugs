/* Generated By:JJTree: Do not edit this line. ASTformalParameters.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javascriptInterpreter.tree;

import javascriptInterpreter.parser.*;

public
class ASTformalParameters extends SimpleNode {
  public ASTformalParameters(int id) {
    super(id);
  }

  public ASTformalParameters(Javascript p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public javascriptInterpreter.visitors.JavascriptType jjtAccept(JavascriptVisitor visitor, javascriptInterpreter.visitors.Context data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=9de64e592a6aae525ec2e2b44d9671b5 (do not edit this line) */
