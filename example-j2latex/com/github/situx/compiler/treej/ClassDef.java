package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class ClassDef implements Node {
public String generic,comment;
public List<Node> eximp;
public List<Node> exps;
public boolean isinterface;
public Node mod,varexp,name,ext;

/**
 * @param body
 * @param eximp
 */
public ClassDef(Node name, Node ext,List<Node> eximp,List<Node> varexps,String comment, Node mod,boolean isinterface) {
	super();
	this.name=name;
	this.ext=ext;
	this.eximp = eximp;
	this.comment=comment;
	this.mod=mod;
	this.isinterface=isinterface;
	this.exps=varexps;
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
