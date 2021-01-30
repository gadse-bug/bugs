package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Sem implements Node{
	public Node op1;
	public boolean setit;
	public int prevtoken;
	public String comment;
	public Sem(Node op1,boolean setit,String comment,int prevtoken){
		this.op1 = op1;
		this.setit=setit;
		this.prevtoken=prevtoken;
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
