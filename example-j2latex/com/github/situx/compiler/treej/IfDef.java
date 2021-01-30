package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class IfDef implements Node {
public List<Node> bodylist,exprlist;
public boolean ternary;
/**
 * @param bodylist
 * @param exprlist
 */
public IfDef(List<Node> bodylist, List<Node> exprlist,boolean ternary) {
	super();
	this.bodylist = bodylist;
	this.exprlist = exprlist;
	this.ternary=ternary;
}

@Override
public String toString() {
	return this.getClass().getSimpleName();
}
	@Override
	public void welcome(Visitor v) {
		v.visit(this);

	}

}
