package com.github.situx.compiler.treej;
import com.github.situx.compiler.visitorj.Visitor;


public class Add implements Node {
	public Node op1;
	public Node op2;
	public boolean addeq;
	public Add(Node op1, Node op2, boolean addeq){
		super();
		this.op1 = op1;
		this.op2 = op2;
		this.addeq=addeq;
	}
	public Add(Node op1, Node op2) {
		this(op1,op2,false);
	}
	
	@Override
	public void welcome(Visitor v) {
		v.visit(this);
	}
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
