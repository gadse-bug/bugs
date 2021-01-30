package com.github.situx.compiler.sample;

import java.io.IOException;
import java.util.List;


/**Sample interface.*/
public interface SampleInter<T,E> extends SampleInter2{
	/**Tests the exception parsing.
	 * @param test param for throwing an exception*/
	public double testExceptions(int test);
	/**Tests the +=,-=,*=,/=, char constant,exceptions, and switch case parsing.*/
	public boolean testExpressions(int abc) throws IOException;
	/**Tests the increment/decrement parsing.*/
	public List<String> testIncrements();
	/**Tests the If/ElseIf/Else implementation including ternary and comparison operators.*/
	public String testIfElse();
	/**Tests the parsing of anonymous inner classes.*/
	public int testInnerClasses();
	/**Tests the parsing of all available loops (for/foreach/while/infinite for)
	 * and the throws expression
	 * @param good the good parameter
	 * @param sample the sample parameter*/
	public boolean testLoops(float good, Sample<T, E> sample) throws NullPointerException;
}
