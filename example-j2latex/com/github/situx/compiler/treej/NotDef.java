package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class NotDef implements Node {

	public Node op1;
	public boolean notorbitwise;
	public NotDef(Node op1,boolean notorbitwise) {
		super();
		this.op1 = op1;
		this.notorbitwise=notorbitwise;
	}

	@Override
	public void welcome(Visitor v) {
		v.visit(this);

	}

}
