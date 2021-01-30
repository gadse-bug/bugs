package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class FieldDef implements Node{
public Node dat,mod;
public List<Variable> identlist;
public List<Number> arraylen;
public List<Node> assignments;
public String comment;
public boolean forloop,local;
	/**
 * @param modlist
 * @param identlist
 */
public FieldDef(List<Variable> identlist,Node mod,Node dat,List<Node>asslist,List<Number> arraylen,String comment,boolean forloop,boolean local) {
	super();
	this.dat = dat;
	this.mod=mod;
	this.identlist = identlist;
	this.arraylen=arraylen;
	this.assignments=asslist;
	this.comment=comment;
	this.forloop=forloop;
	this.local=local;
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
