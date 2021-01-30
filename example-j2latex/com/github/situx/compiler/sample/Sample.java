package com.github.situx.compiler.sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**A sample class to demonstrate the functionalities of the J2L Compiler.
 * @see C1.jj
 * @version 1.0*/
public abstract class Sample<T,E> extends Object implements SampleInter<T,E>,SampleInter2{
	/**Constant test for Octints.*/
	public static final int OCT_INT=0400;
	/**Constant test for Hexints.*/
	protected static final int HEX_INT=0x400;
	/**Constant test for Strings.*/
	private static final String KONSTANTE_TEST="Konstantentest";
	/**Tests the implementation of transient.*/
	public transient float floattest;
	/**Tests the implementation of final.*/
	protected boolean booltest;
	/**Tests the implementation of volatile.*/
	private volatile double doubletest;
	/**Tests the implementation of byte.*/
	private byte[] bytetest;
	/**Tests the implementation of char.*/
	private char chartest;
	/**Tests the implementation of int.*/
	private int inttest;
	/**Test the implementation of a class (String).*/
	private String stringtest;
	/**Tests the generic types of a field.*/
	private List<String> list;
	/**Tests the implementation of an enum definition.*/
	private enum enumtest{ABC,DEF,GHI};

	/**Tests the implementation of a Constructor.
	 * @param floattest float parameter
	 * @param booltest boolean parameter
	 * @param doubletest double parameter
	 */
	public Sample(float floattest, boolean booltest, double doubletest) {
		super();
		this.floattest = 15f;
		this.booltest = true;
		this.doubletest = 15.23;
		this.stringtest=new String("Hallo");
	}

	@Override
	public synchronized byte hello(int you) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double testExceptions(int test) {
		try{
			Integer.parseInt("Hallo");
		}catch(NumberFormatException event){
			event.getStackTrace();
		}finally{
			this.chartest='b';
		}
		if(--test>2){
			throw new NullPointerException();
		}
		new String();
		return 1.;
	}

	@Override
	public boolean testExpressions(int abc) {
		this.chartest='a';
		abc|=2;
		abc=abc|2;
		abc&=2;
		abc=abc&2;
		abc^=2;
		abc=abc^2;
		abc=abc<<2;
		abc<<=2;
		abc=abc>>2;
		abc>>=2;
		abc=abc>>>2;
		abc>>>=2;
		this.floattest-=2;
		this.floattest=this.floattest-2;
		this.floattest%=2;
		this.floattest=this.floattest%2;
		this.doubletest+=4;
		this.doubletest=this.doubletest+4;
		this.floattest/=2;
		this.floattest=this.floattest/2;
		this.floattest*=2;
		this.floattest=this.floattest*2;
		switch(4){
			case 'a': break;
			case 4:
			case OCT_INT: chartest='b';
			default:  break;
		}
		return true;
	}

	@Override
	public String testIfElse() {
		if(this.stringtest==null){
			this.stringtest=Sample.KONSTANTE_TEST;
		}
		else if(this.stringtest.equals("ABC") || this.stringtest.equals("DEF")){
			this.stringtest=Sample.KONSTANTE_TEST;
		}
		else if(this.stringtest instanceof String){
			this.stringtest="Cool!";
		}
		if(!this.booltest && this.inttest==4){
			this.floattest=0;
		}
		else if(~this.inttest>0){
			return "false";
		}
		else if(this.floattest!=5){
			this.stringtest="!5";
		}
		else if(this.doubletest<=2){
			this.doubletest=0.;
		}
		else if(this.doubletest>=2){
			this.chartest='t';
		}
		else if(this.doubletest<1){
			this.doubletest=1.;
		}
		else{
			this.stringtest="";
		}
		this.inttest=(int)this.floattest;
		final String str=this.booltest ? this.getStringtest() : null;
		return str;
	}

	@Override
	public List<String> testIncrements() {
		return new LinkedList<String>();
	}

	@Override
	public int testInnerClasses() {
		final ActionListener act = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				arg0.toString();
				
			}
		};
		return 0;
	}

	@Override
	public boolean testLoops(final float test,Sample<T,E> sample) throws NullPointerException {
		sample=this;
		final List<String> strlist=new LinkedList<String>();
		int count=0;
		String[] strarray = new String[5];
		while(count<100){
			testExpressions(count);
			++count;
		}
		do{
			testExpressions(count);
			count++;
		}while(count<100);
		for(String str:strlist){
			str.toString();
			testExpressions(count);
		}
		for(Iterator<String> iter=strlist.iterator();iter.hasNext();){
			iter.next();
		}
		for(int i=0;i<10;i++){
			i--;
		}
		for(;;){
			return testExpressions(count);
		}
	}

	/**Gets the bytetest value.
	 * @return the bytetest
	 */
	public byte[] getBytetest() {
		return bytetest;
	}

	/**Gets the chartest value.
	 * @return the chartest
	 */
	public char getChartest() {
		return chartest;
	}

	/**Gets the doubletest value.
	 * @return the doubletest
	 */
	public double getDoubletest() {
		return doubletest;
	}

	/**Gets the floattest value.
	 * @return the floattest
	 */
	public float getFloattest() {
		return floattest;
	}

	/**Gets the Stringtest value.
	 * @return the stringtest
	 */
	public String getStringtest() {
		return this.stringtest;
	}

	/**Returns the boolean value of booltest.
	 * @return the booltest value
	 */
	public boolean isBooltest() {
		return booltest;
	}

	/**Sets the bytetest value.
	 * @param bytetest the bytetest to set
	 */
	public void setBytetest(byte[] bytetest) {
		this.bytetest = bytetest;
	}

	/**Sets the chartest value.
	 * @param chartest the chartest to set
	 */
	public void setChartest(char chartest) {
		this.chartest = chartest;
	}

	/**Sets the doubletest value.
	 * @param doubletest the doubletest to set
	 */
	public void setDoubletest(double doubletest) {
		this.doubletest = doubletest;
	}

	/**Sets the floattest value.
	 * @param floattest the floattest to set
	 */
	public void setFloattest(float floattest) {
		this.floattest = floattest;
	}

	/**
	 * @param stringtest the stringtest to set
	 */
	public void setStringtest(String stringtest) {
		this.stringtest = stringtest;
	}
}
