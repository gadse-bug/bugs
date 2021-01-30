package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Atom implements Node {
	public Node content,typecast;
	public boolean brackets,hastypecast;
	
	/**
	 * @param content
	 * @param typecast
	 */
	public Atom(Node content, Node typecast,boolean brackets,boolean hastypecast) {
		super();
		this.content = content;
		this.typecast = typecast;
		this.brackets=brackets;
		this.hastypecast=hastypecast;
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
