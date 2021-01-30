package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class Comment implements Node {
public String comment;
public List<String> attokens;
	/**
 * @param comment
 */
public Comment(String comment,List<String> attokens) {
	super();
	this.comment = comment;
	this.attokens = attokens;
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
