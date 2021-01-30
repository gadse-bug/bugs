package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Assignment implements Node{
public Node n1;
public Node n2;
public boolean insertnew;

/**
 * @param n1
 * @param n2
 */
public Assignment(Node n1, Node n2,boolean insertnew) {
	super();
	this.n1 = n1;
	this.n2 = n2;
	this.insertnew=insertnew;
}
@Override
public String toString() {
	return this.getClass().getSimpleName()+" : "+this.n1+" = "+this.n2;
}

@Override
public void welcome(Visitor v) {
	v.visit(this);	
}

}
