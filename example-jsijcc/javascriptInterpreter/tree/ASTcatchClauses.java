/* Generated By:JJTree: Do not edit this line. ASTcatchClauses.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package javascriptInterpreter.tree;

import javascriptInterpreter.parser.*;

public
class ASTcatchClauses extends SimpleNode {
  public ASTcatchClauses(int id) {
    super(id);
  }

  public ASTcatchClauses(Javascript p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public javascriptInterpreter.visitors.JavascriptType jjtAccept(JavascriptVisitor visitor, javascriptInterpreter.visitors.Context data) {

    return
    visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=1415a1ca0c7cbdeb554818da0d4a0184 (do not edit this line) */
