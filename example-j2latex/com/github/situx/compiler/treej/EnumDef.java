package com.github.situx.compiler.treej;

import java.util.List;

import com.github.situx.compiler.visitorj.Visitor;

public class EnumDef implements Node{
public List<String> enumdefs;
/**
 * @param enumdefs
 */
public EnumDef(List<String> enumdefs) {
	super();
	this.enumdefs = enumdefs;
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
