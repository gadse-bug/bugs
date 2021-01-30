package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class FunctionDef implements Node {
	public String name,comment;
	public List<Node> body,exceptions;
	public List<String> annotations;
	public Node modifier,returntype,params;
	public boolean isabstract,cons;
	public FunctionDef(String name, Node params,List<Node> body,Node modifier,Node returntype,String comment,boolean cons,List<Node> exceptions,List<String> annotations,boolean isabstract) {
		super();
		this.name = name;
		this.params = params;
		this.body=body;
		this.modifier=modifier;
		this.returntype=returntype;
		this.comment=comment;
		this.cons=cons;
		this.exceptions=exceptions;
		this.annotations=annotations;
		this.isabstract=isabstract;
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
