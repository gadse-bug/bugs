package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class Switch implements Node {
public Node exp;
public List<Node> cases;
public List<Node> constants;
public List<Integer> hasbreak;
public int defaultpos;
/**
 * @param casebody
 * @param exp
 */
public Switch(List<Node> cases, List<Node> constants,Node exp,int defaultpos,List<Integer> hasbreak) {
	super();
	this.cases = cases;
	this.exp = exp;
	this.defaultpos=defaultpos;
	this.constants=constants;
	this.hasbreak=hasbreak;
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
