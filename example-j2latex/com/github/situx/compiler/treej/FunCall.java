package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class FunCall implements Node {
	public String name,generic;
	public List<Node> args,varexps;
	
	
	public FunCall(String name, List<Node> args,List<Node> varexps,String generic) {
		super();
		this.name = name;
		this.args = args;
		this.varexps=varexps;
		this.generic=generic;
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
