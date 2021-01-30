package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class TryCatch implements Node {
public Node trybody;
public List<Node> catchbodies, catchargs;
public boolean hasfinally;
	/**
 * @param trybody
 * @param catchbodies
 * @param catchargs
 */
public TryCatch(Node trybody, List<Node> catchargs, List<Node> catchbodies,boolean hasfinally) {
	super();
	this.trybody = trybody;
	this.catchbodies = catchbodies;
	this.catchargs = catchargs;
	this.hasfinally=hasfinally;
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
