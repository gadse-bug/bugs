package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Except implements Node{
/**
	 * @param except
	 */
	public Except(Node except,boolean isnew) {
		super();
		this.except = except;
		this.isnew=isnew;
	}
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

public Node except;
public boolean isnew;

@Override
public void welcome(Visitor v) {
	v.visit(this);
	
}

}
