package com.github.situx.compiler.treej;
import com.github.situx.compiler.visitorj.Visitor;


public class Minus implements Node {
	public Node op1;
	public Node op2;
	public boolean mineq;
	public Minus(Node op1, Node op2, boolean mineq){
		super();
		this.op1 = op1;
		this.op2 = op2;
		this.mineq=mineq;
	}
	public Minus(Node op1, Node op2) {
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
