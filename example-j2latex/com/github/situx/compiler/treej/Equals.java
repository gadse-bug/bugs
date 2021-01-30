package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Equals implements Node {
public Node op1;
public Node op2;
public int mode;

public Equals(Node op1,Node op2){
	this(op1,op2,0);
}

public Equals(Node op1, Node op2, int mode) {
	super();
	this.op1 = op1;
	this.op2 = op2;
	this.mode=mode;
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
