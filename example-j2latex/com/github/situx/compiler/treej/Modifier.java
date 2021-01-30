package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Modifier implements Node {
	public int vis,grade,state,fin;
	public String comment;
	
	/**
	 * @param vis
	 * @param grade
	 * @param state
	 */
	public Modifier(int vis, int grade, int state,int fin,String comment) {
		super();
		this.vis = vis;
		this.grade = grade;
		this.state = state;
		this.fin=fin;
		this.comment=comment;
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
