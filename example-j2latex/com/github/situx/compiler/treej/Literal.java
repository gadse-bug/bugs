package com.github.situx.compiler.treej;
import com.github.situx.compiler.visitorj.Visitor;


public class Literal implements Node {
	public int n;

	public Literal(int n) {
		super();
		this.n = n;
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
