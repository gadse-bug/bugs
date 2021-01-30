package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Shift implements Node {
public Node op1,op2;
public boolean leftright;
public int shifteq;
public Shift(Node op1, Node op2,boolean leftright, int shifteq){
	super();
	this.op1 = op1;
	this.op2 = op2;
	this.leftright=leftright;
	this.shifteq=shifteq;
}
public Shift(Node op1, Node op2, boolean leftright) {
	this(op1,op2,leftright,0);
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
