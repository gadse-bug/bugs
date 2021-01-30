package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Modulo implements Node {
public Node op1, op2;
public boolean modeq;
public Modulo(Node op1, Node op2, boolean modeq){
	super();
	this.op1 = op1;
	this.op2 = op2;
	this.modeq=modeq;
}
public Modulo(Node op1, Node op2) {
	this(op1,op2,false);
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
