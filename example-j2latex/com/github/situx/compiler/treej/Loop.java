package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class Loop implements Node{
public List<Node> exps,body;
public int type;
	/**
 * @param op1
 * @param type
 */
public Loop(List<Node> body, int type,List<Node> exps) {
	super();
	this.body = body;
	this.type = type;
	this.exps = exps;
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
