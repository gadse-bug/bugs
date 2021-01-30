package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class Imps implements Node {
	public String classname,comment;
	public List<Node> generic;
	public Imps(String classname, List<Node> generic,String comment) {
		this.classname = classname;
		this.generic = generic;
		this.comment=comment;
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
