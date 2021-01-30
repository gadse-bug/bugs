package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Variable implements Node {
	public String name;
	public Number array;
	public Variable(String name){
		this(name,new Number(-1,-1,-1l,-1f,-1.,0));
	}
	public Variable(String name,Number array) {
		super();
		this.name = name;
		this.array=array;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
	@Override
	public void welcome(Visitor v) {
		v.visit(this);
	}

}
