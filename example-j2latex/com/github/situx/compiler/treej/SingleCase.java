package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class SingleCase implements Node {
	public String name;
	public List<String> vars;
	public Node rhs;
	
	public SingleCase(String string, List<String> vars, Node cexpr) {
		name=string;
		this.vars = vars;
		rhs = cexpr;
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
