package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class ReturnType implements Node {
public int type;
public Node classid;
public String generic,comment;
public boolean isarray;
	/**
 * @param type
 * @param classid
 */
public ReturnType(int type, Node classid,String comment,boolean isarray,String generic) {
	super();
	this.type = type;
	this.classid = classid;
	this.comment=comment;
	this.isarray=isarray;
	this.generic=generic;
}

@Override
public String toString() {
	return this.getClass().getSimpleName()+": "+this.classid+"<"+this.generic+">";
}

	@Override
	public void welcome(Visitor v) {
		v.visit(this);

	}

}
