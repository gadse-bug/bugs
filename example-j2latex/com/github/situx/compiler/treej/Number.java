package com.github.situx.compiler.treej;

import com.github.situx.compiler.visitorj.Visitor;

public class Number implements Node{
public int choose;
public Float floatvalue;
public Integer intvalue;
public Long longvalue;
public Double doublevalue;
public int hexoroct;


/**Constructor for Number.
 * @param choose
 * @param floatvalue
 * @param intvalue
 * @param longvalue
 * @param doublevalue
 */
public Number(int choose, Integer intvalue, Long longvalue,
		Float floatvalue,Double doublevalue,int hexoroct) {
	super();
	this.choose = choose;
	this.floatvalue = floatvalue;
	this.intvalue = intvalue;
	this.longvalue = longvalue;
	this.doublevalue = doublevalue;
	this.hexoroct=hexoroct;
}

	@Override
		public String toString() {
			String result="";
			switch(choose){
			case 0: if(this.hexoroct==1){
						result=Integer.toHexString(intvalue);
					}
					else if(this.hexoroct==2){
						result=Integer.toOctalString(intvalue);
					}result+=intvalue.toString();break;
			case 1: result+=longvalue.toString();break;
			case 2: result+=floatvalue.toString();break;
			case 3: result+=doublevalue.toString();break;
			default: break;
			}
			return result;
		}
	@Override
	public void welcome(Visitor v) {
		v.visit(this);
	}

}
