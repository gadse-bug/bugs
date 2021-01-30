package com.github.situx.compiler.treej;
import com.github.situx.compiler.visitorj.Visitor;


public class Div implements Node {
	public Node op1;
	public Node op2;
	public boolean diveq;
	
	public Div(Node op1, Node op2,boolean diveq) {
		super();
		this.op1 = op1;
		this.op2 = op2;
		this.diveq=diveq;
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
