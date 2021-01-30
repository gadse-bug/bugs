package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class Casebody implements Node {
public List<Node> cases;
	/**
 * @param cases
 */
public Casebody(List<Node> cases) {
	super();
	this.cases = cases;
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
