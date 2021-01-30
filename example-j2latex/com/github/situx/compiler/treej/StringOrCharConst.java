package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class StringOrCharConst implements Node{
public String str;
public boolean choose;

/**
 * @param str
 * @param charconst
 */
public StringOrCharConst(String str,boolean choose) {
	super();
	this.str = str;
	this.choose=choose;
}
@Override
public String toString() {
	return this.getClass().getSimpleName()+": "+this.str;
}

	@Override
	public void welcome(Visitor v) {
		v.visit(this);
	}
	
}
