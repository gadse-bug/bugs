package com.github.situx.compiler.visitorj;


import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import com.github.situx.compiler.treej.Add;
import com.github.situx.compiler.treej.AndOr;
import com.github.situx.compiler.treej.Assignment;
import com.github.situx.compiler.treej.Atom;
import com.github.situx.compiler.treej.Casebody;
import com.github.situx.compiler.treej.ClassDef;
import com.github.situx.compiler.treej.Comment;
import com.github.situx.compiler.treej.Div;
import com.github.situx.compiler.treej.EnumDef;
import com.github.situx.compiler.treej.Equals;
import com.github.situx.compiler.treej.Except;
import com.github.situx.compiler.treej.FieldDef;
import com.github.situx.compiler.treej.FunCall;
import com.github.situx.compiler.treej.FunctionDef;
import com.github.situx.compiler.treej.Greater;
import com.github.situx.compiler.treej.IfDef;
import com.github.situx.compiler.treej.Import;
import com.github.situx.compiler.treej.Imps;
import com.github.situx.compiler.treej.Literal;
import com.github.situx.compiler.treej.Loop;
import com.github.situx.compiler.treej.Minus;
import com.github.situx.compiler.treej.Modifier;
import com.github.situx.compiler.treej.Modulo;
import com.github.situx.compiler.treej.Mult;
import com.github.situx.compiler.treej.Node;
import com.github.situx.compiler.treej.NotDef;
import com.github.situx.compiler.treej.Number;
import com.github.situx.compiler.treej.Params;
import com.github.situx.compiler.treej.Program;
import com.github.situx.compiler.treej.ReturnType;
import com.github.situx.compiler.treej.Sem;
import com.github.situx.compiler.treej.Shift;
import com.github.situx.compiler.treej.SingleCase;
import com.github.situx.compiler.treej.StringOrCharConst;
import com.github.situx.compiler.treej.Switch;
import com.github.situx.compiler.treej.TryCatch;
import com.github.situx.compiler.treej.Variable;

public class AsHTML implements Visitor{
public Writer w;
public String filename,blank;
public int ident,identcounter,linecounter,minus=6,border=10;
public static final String KEYWORD_COLOR="09095c";
public static final String CODE_COLOR="black";
public static final String COMMENT_COLOR="e5231f";
public static final String COMMENTKEY_COLOR="#FFFA00";
public static final String INTEGER_COLOR="blue";
public static final String DOUBLE_COLOR="blue";
public static final String STRING_COLOR="purple";
public static final String CHAR_COLOR="cyan";
public static final String ANNOTATION_COLOR="16f7e3";

public AsHTML(Writer w,String filename,int ident,int lines) {
	super();
	this.w = w;
	this.filename=filename;
	this.ident=ident;
	this.identcounter=0;
	this.linecounter=lines;
	if(lines==-1)
		minus=0;
	this.blank="&nbsp;";
	for(int i=1;i<ident;i++)
	this.blank+="&nbsp;";
}

	public void write(String s){
		try {
		w.write(s);
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
	public void ident(){
		int i=0;
		if(this.linecounter!=-1){
			if(++this.linecounter==this.border){
				this.border*=10;
				minus-=2;
			}
			write(this.linecounter+"");
			for(i=0;i<minus;i++){
				write("&nbsp;");
			}
		}
		for(i=0;i<identcounter;i++){
			write(blank);
		}
	}
	
	public void formatComment(String comment){
		write("<b style=\"color:"+COMMENT_COLOR+"\">");
		comment=comment.replace("@*","<b style=\"color:"+COMMENTKEY_COLOR+">@</b>");
		String[] commentarray=comment.split("\n");
		for(int i=0;i<commentarray.length;i++){
			write(commentarray[i]);
			if(i<commentarray.length-1){
				write("</b><br>");
				ident();
				write("<b style=\"color:"+COMMENT_COLOR+"\">");
			}
		}
		write("</b><br>");
		ident();
	}
	@Override
	public void visit(Add n) {
		n.op1.welcome(this);
		if(n.addeq)
			write("<b>+=</b>");
		else
			write("<b>+</b>");
		n.op2.welcome(this);	
	}

	@Override
	public void visit(AndOr and) {
		and.op1.welcome(this);
		switch(and.mode){
		case 1: write("<b>&</b>");break;
		case 2: write("<b> && </b>");break;
		case 3: write("<b>&=</b>");break;
		case 4:	write("<b>|</b>");break;
		case 5: write("<b> || </b>");break;
		case 6: write("<b>|=</b>");break;
		case 7: write("<b>^</b>");break;
		case 8: write("<b>^=</b>");break;
		default:break;
		}
		and.op2.welcome(this);
	}
	@Override
	public void visit(Assignment assignment) {
		assignment.n1.welcome(this);
		write("<b>=</b>");
		if(assignment.insertnew)
			write("<b style=\"color:"+KEYWORD_COLOR+"\">new </b>");
		assignment.n2.welcome(this);
	}

	@Override
	public void visit(Atom atom) {
		if(atom.hastypecast){
			write("(");
			atom.typecast.welcome(this);
			write(")");
		}
		if(atom.brackets)
			write("(");
		atom.content.welcome(this);
		if(atom.brackets)
			write(")");
	}

	@Override
	public void visit(Casebody casebody) {
		this.identcounter++;
		for(Node n:casebody.cases){
			n.welcome(this);
		}
		this.identcounter--;
	}

	@Override
	public void visit(ClassDef classdef) {
		ident();
		if(!"".equals(classdef.comment)){
			formatComment(classdef.comment);
		}
		classdef.mod.welcome(this);
		write("<b style=\"color:"+KEYWORD_COLOR+"\">");
		if(classdef.isinterface)
			write("interface</b> ");
		else
			write("class</b> ");
		classdef.name.welcome(this);
		if(!classdef.eximp.isEmpty() && !classdef.isinterface){
			write(" <b style=\"color:"+KEYWORD_COLOR+"\">implements</b> ");
			for(Iterator<Node> iter = classdef.eximp.iterator();iter.hasNext();){
				iter.next().welcome(this);
				if(iter.hasNext())
					write(",");
			}
		}

		if(!classdef.ext.equals("")){
			write(" <b style=\"color:"+KEYWORD_COLOR+"\">extends</b> ");
			classdef.ext.welcome(this);
		}
		write("{<br/>");
		this.identcounter++;
		for(Node n:classdef.exps){
			n.welcome(this);
		}
		this.identcounter--;
		ident();
		write("}<br/>");
	}
	@Override
	public void visit(Comment comment) {
		ident();
		write("<span style=\"color:"+COMMENT_COLOR+";\">/**"+comment.comment+" </span><br/>");
		for(int i=0;i<comment.attokens.size();i++){
			ident();
			write("<span style=\"color:"+COMMENT_COLOR+";\"> * </span><span style=\"color:"+COMMENTKEY_COLOR+";\">"+comment.attokens.get(i++)+"</span> ");
			write("<span style=\"color:"+COMMENT_COLOR+";\"> ");
			while(i<comment.attokens.size() && comment.attokens.get(i).charAt(0)!='@'){
				write(comment.attokens.get(i));
				i++;
			}
			i--;
			write("</span><br/>");
		}
		ident();
		write("<span style=\"color:"+COMMENT_COLOR+";\"> */</span><br/>");
	}
	@Override
	public void visit(Div n) {
		n.op1.welcome(this);
		if(n.diveq)
			write("<b>/=</b>");
		else
			write("<b>/</b>");
		n.op2.welcome(this);
	}

	@Override
	public void visit(EnumDef enumDef) {
		write("<b style=\"color:"+KEYWORD_COLOR+"\">enum </b>");
		Iterator<String> it=enumDef.enumdefs.iterator();
		write(enumDef.enumdefs.get(0)+"{");
		for(it.next();it.hasNext();){
			write(it.next());
			if(it.hasNext())
				write(", ");
		}
		write("}");
	}
	@Override
	public void visit(Equals equals) {
		equals.op1.welcome(this);
		switch(equals.mode){
		case 1:	write("<b>==</b>");break;
		case 2:	write("<b>!=</b>");break;
		case 3: write("<b style=\"color:"+KEYWORD_COLOR+"\"> instanceof </b>");
		default:break;
		}
		equals.op2.welcome(this);		
	}
	@Override
	public void visit(Except except) {
		write("<b style=\"color:"+KEYWORD_COLOR+"\">throw ");
		if(except.isnew)
			write("new ");
		write("</b>");
		except.except.welcome(this);
	}
	@Override
	public void visit(FieldDef fieldDef) {
		if(!fieldDef.forloop && !fieldDef.local){
			ident();
		}
		fieldDef.mod.welcome(this);
		fieldDef.dat.welcome(this);
		for(Variable n:fieldDef.identlist){
			if(n.name.equals("null"))
				n.name="";
			n.welcome(this);
		}
		for(Number n:fieldDef.arraylen){
			if(n.intvalue!=-1){
				write("[");
				n.welcome(this);
				write("]");
			}
		}
		for(Node n:fieldDef.assignments){
			n.welcome(this);
		}
		if(!fieldDef.forloop)
			write(";<br/>");
	}
	@Override
	public void visit(FunCall funCall) {
		if("super".equals(funCall.name)){
			write("<b style=\"color:"+KEYWORD_COLOR+"\">");
			write(funCall.name+"</b>");
		}
		else if(funCall.name.contains("this")){
			int index=funCall.name.indexOf("this");
			write(funCall.name.substring(0, index));
			write("<b style=\"color:"+KEYWORD_COLOR+"\">this</b>");
			write(funCall.name.substring(index+4, funCall.name.length()));
		}
		else
			write(funCall.name+"");
		if(!"null".equals(funCall.generic)){
			write("&lt;"+funCall.generic+"&gt;");
		}
		write("(");
		for(Iterator<Node> it = funCall.args.iterator(); it.hasNext();){
			it.next().welcome(this);
			if(it.hasNext())
				write(",");
		}
		write(")");
		if(!funCall.varexps.isEmpty()){
			write("{<br/>");
			this.identcounter++;
			for(Node n:funCall.varexps){
				n.welcome(this);
			}
			this.identcounter--;
			ident();
			write("}");
		}
	}
	@Override
	public void visit(FunctionDef functionDef) {
		ident();
		if(!functionDef.annotations.isEmpty()){
			write("<span style=\"color:"+ANNOTATION_COLOR+"\">");
			for(Iterator<String> iter =functionDef.annotations.iterator();iter.hasNext();){
				write(iter.next()+"<br>");
				if(iter.hasNext())
					ident();
			}
			write("</span>");
			ident();
		}
		functionDef.modifier.welcome(this);
		functionDef.returntype.welcome(this);
		if(!"null".equals(functionDef.name))
			write(functionDef.name);
		write("(");
		functionDef.params.welcome(this);
		write(")");
		if(!functionDef.exceptions.isEmpty()){
			write("<b style=\"color:"+KEYWORD_COLOR+"\"> throws </b>");
			for(int i=0;i<functionDef.exceptions.size();i++){
				functionDef.exceptions.get(i).welcome(this);
				if(i<functionDef.exceptions.size()-1)
					write(", ");
			}
		}
		if(functionDef.isabstract){
			write(";");
		}
		else{
			write("{<br/>");
			this.identcounter++;
			for(Node n:functionDef.body)
				n.welcome(this);
			this.identcounter--;
			ident();
			write("}<br/>");
			ident();
		}
		write("<br/>");
	}
	@Override
	public void visit(Greater greater) {
		greater.op1.welcome(this);
		if(greater.leftright && greater.eq)
			write("<b>&lt;=</b>");
		else if(greater.leftright && !greater.eq)
			write("<b>&lt;</b>");
		else if(!greater.leftright && greater.eq)
			write("<b>&gt;=</b>");
		else
			write("<b>&gt;</b>");
		greater.op2.welcome(this);	
	}
	@Override
	public void visit(IfDef ifDef) {
		if(ifDef.ternary){
			ifDef.exprlist.get(0).welcome(this);
			write(" <b>?</b> ");
			ifDef.bodylist.get(0).welcome(this);
			write(" <b>:</b> ");
			ifDef.bodylist.get(1).welcome(this);
		}
		else{
			write("<b style=\"color:"+KEYWORD_COLOR+"\">if </b>");
			for(int i=0;i<ifDef.bodylist.size();i++){
				if(i==ifDef.exprlist.size()){
					ident();
					write("<b style=\"color:"+KEYWORD_COLOR+"\">else </b>");
				}
				else{
					if(i>0){
						ident();
						write("<b style=\"color:"+KEYWORD_COLOR+"\">else if </b>");
					}
					write("(");
					ifDef.exprlist.get(i).welcome(this);
					write(")");
				}
				write("{<br/>");
				this.identcounter++;
				ifDef.bodylist.get(i).welcome(this);
				this.identcounter--;
				ident();
				write("}<br/>");
			}
		}
	}
	@Override
	public void visit(Import import1) {
		for(String s:import1.imports){
			ident();
			write("<b style=\"color:"+KEYWORD_COLOR+"\">import </b>"+s+";<br/>");
		}
		
	}
	@Override
	public void visit(Imps imps) {
		if(!"".equals(imps.comment))
			this.formatComment(imps.comment);
		write(imps.classname);
		if(!imps.generic.isEmpty())
			write("&lt;");
		for(Iterator<Node> iter=imps.generic.iterator();iter.hasNext();){
			iter.next().welcome(this);
			if(iter.hasNext())
				write(",");
		}
		if(!imps.generic.isEmpty())
			write("&gt;");
	}

	@Override
	public void visit(Literal literal) {
		write(""+literal.n);
	}
	@Override
	public void visit(Loop loop) {
		if(loop.type==-1){
			write("<b style=\"color:"+KEYWORD_COLOR+"\">for</b>(");
			if(loop.exps.isEmpty()){
				write(";;");
			}
			for(int i=0;i<loop.exps.size();i++){
				loop.exps.get(i).welcome(this);
				if(i<2)
					write(";");
			}
			write("){<br/>");
		}
		else if(loop.type==0){
			write("<b style=\"color:"+KEYWORD_COLOR+"\">while</b>(");
			for(Node n:loop.exps)
				n.welcome(this);
			write("){<br/>");
		}
		else if(loop.type==2){
			write("<b style=\"color:"+KEYWORD_COLOR+"\">for</b>(");
			for(int i=0;i<loop.exps.size();i++){
				loop.exps.get(i).welcome(this);
				if(i==loop.exps.size()-2)
					write(" : ");
			}
			write("){<br/>");
		}
		else
			write("<b style=\"color:"+KEYWORD_COLOR+"\">do</b>{<br/>");
		this.identcounter++;
		for(Node n:loop.body)
			n.welcome(this);
		this.identcounter--;
		if(loop.type==1){
			ident();
			write("}<b style=\"color:"+KEYWORD_COLOR+"\">while</b>(");
			for(Node n:loop.exps)
				n.welcome(this);
			write(");<br/>");
		}
		else{
			ident();
			write("}<br/>");
		}
	}
	@Override
	public void visit(Minus n) {
		n.op1.welcome(this);
		if(n.mineq)
			write("<b>-=</b>");
		else
			write("<b>-</b>");
		n.op2.welcome(this);
		
	}

	@Override
	public void visit(Mult n) {
		n.op1.welcome(this);
		if(n.multeq)
			write("<b>*=</b>");
		else
			write("<b>*</b>");
		n.op2.welcome(this);
	}

	@Override
	public void visit(Modifier modifier) {
		if(!"".equals(modifier.comment)){
			formatComment(modifier.comment);
		}
		write("<b style=\"color:"+KEYWORD_COLOR+"\">");
		if(modifier.vis==1)
			write("public ");
		else if(modifier.vis==0)
			write("protected ");
		else if(modifier.vis==-1)
			write("private ");
		if(modifier.state==-1)
			write("static ");
		else if(modifier.state==1)
			write("abstract ");
		if(modifier.fin==1)
			write("final ");
		if(modifier.grade==-1)
			write("transient ");
		else if(modifier.grade==0)
			write("volatile ");
		else if(modifier.grade==1)
			write("synchronized ");
		else if(modifier.grade==2)
			write("native ");
		else if(modifier.grade==3)
			write("strictfp ");
		write("</b>");
	}
	@Override
	public void visit(Modulo modulo) {
		modulo.op1.welcome(this);
		if(modulo.modeq)
			write("<b>%=</b>");
		else
			write("<b>%</b>");
		modulo.op2.welcome(this);
	}
	@Override
	public void visit(NotDef notDef) {
		if(notDef.notorbitwise)
			write("<b>!</b>");
		else
			write("<b>~</b>");
		notDef.op1.welcome(this);
	}

	@Override
	public void visit(Number number) {
		switch(number.choose){
		case 0: write("<span style=\"color:"+INTEGER_COLOR+"\">");
				if(number.hexoroct==1)
					write("0x"+Integer.toHexString(number.intvalue));
				else if(number.hexoroct==2)
					write("0"+Integer.toOctalString(number.intvalue));
				else
					write(number.intvalue.toString());
				write("</span>");break;
		case 1: write("<span style=\"color:"+INTEGER_COLOR+"\">");
				if(number.hexoroct==1)
					write("0x"+Long.toHexString(number.longvalue));
				else if(number.hexoroct==2)
					write("0"+Long.toOctalString(number.longvalue));
				else
					write(number.longvalue.toString());
				write("</span>");break;
		case 2: write("<span style=\"color:"+AsHTML.DOUBLE_COLOR+"\">"+number.floatvalue.toString()+"f</span>");break;
		case 3: write("<span style=\"color:"+AsHTML.DOUBLE_COLOR+"\">"+number.doublevalue.toString()+"</span>");
		default:break;
		}
		
	}

	@Override
	public void visit(Params params) {
		for(int i=0;i<params.types.size();i++){
			params.types.get(i).welcome(this);
			params.modlist.get(i).welcome(this);
			write(" "+params.idents.get(i));
			if(i<params.types.size()-1)
				write(", ");
		}
	}
	@Override
	public void visit(Program program) {
		write("<html><head><title>J2HTML Compiler</title></head><body><table bgcolor=\"grey\" cellspacing=0 cellpadding=0><tr><th>"+this.filename+"<br/></th></tr><tr><td>");
		ident();
		write("<b style=\"color:"+KEYWORD_COLOR+"\">package </b>"+program.packagename+";<br/>");
		ident();
		write("<br/>");
		program.imports.welcome(this);
		for(ClassDef c:program.classes){
			ident();
			write("<br/>");
			c.welcome(this);
		}
		write("</td></tr></table></body></html>");
	}
	@Override
	public void visit(ReturnType returnType) {
		if(!"".equals(returnType.comment)){
			formatComment(returnType.comment);
		}
		if(returnType.type!=0){
			write("<b style=\"color:"+KEYWORD_COLOR+"\">");
			returnType.classid.welcome(this);
			write("</b>");
		}
		else{
			returnType.classid.welcome(this);
			write(" ");
		}
		if(returnType.isarray)
			write("[] ");
		else
			write(" ");
	}
	@Override
	public void visit(Sem sem) {
		ident();
		switch(sem.prevtoken){
			case 0:write("<b style=\"color:"+KEYWORD_COLOR+"\">return </b>");break;
			case 1:write("<b style=\"color:"+KEYWORD_COLOR+"\">new </b>");break;
			case 2:write("<b style=\"color:"+KEYWORD_COLOR+"\">return new </b>");break;
			default:break;
		}
		sem.op1.welcome(this);
		if(sem.setit)
			write(";<br/>");
		
	}
	@Override
	public void visit(SingleCase singleCase) {
		write("<b>"+singleCase.name+"</b>");
		singleCase.rhs.welcome(this);
		
	}
	@Override
	public void visit(Shift shift) {
		shift.op1.welcome(this);
		if(shift.leftright && shift.shifteq==0)
			write("<b>&lt;&lt;</b>");
		else if(shift.leftright && shift.shifteq>0)
			write("<b>&lt;&lt;=</b>");
		else if(!shift.leftright && shift.shifteq==0)
			write("<b>&gt;&gt;</b>");
		else if(!shift.leftright && shift.shifteq==1)
			write("<b>&gt;&gt;=</b>");
		else if(!shift.leftright && shift.shifteq==2)
			write("<b>&gt;&gt;&gt;</b>");
		else
			write("<b>&gt;&gt;&gt;=</b>");
		shift.op2.welcome(this);
	}
	@Override
	public void visit(StringOrCharConst stringOrCharConst) {
		if(stringOrCharConst.choose){
			if("true".equals(stringOrCharConst.str) || "false".equals(stringOrCharConst.str) || "null".equals(stringOrCharConst.str)){
				write("<b style=\"color:"+KEYWORD_COLOR+"\">"+stringOrCharConst.str+"</b>");
			}
			else{
				write("<span style=\"color:"+STRING_COLOR+"\">"+stringOrCharConst.str+"</span>");
			}
		}
		else{
			write("<span style=\"color:"+CHAR_COLOR+"\">"+stringOrCharConst.str+"</span>");
		}
	}
	@Override
	public void visit(Switch switch1) {
		int j=0;
		write("<b style=\"color:"+KEYWORD_COLOR+"\">switch </b>(");
		switch1.exp.welcome(this);
		write("){<br/>");
		this.identcounter++;
		for(int i=0;i<switch1.cases.size();i++){
			ident();
			if(i==switch1.defaultpos)
				write("<b style=\"color:"+KEYWORD_COLOR+"\">default</b>: <br/>");
			else{
				write("<b style=\"color:"+KEYWORD_COLOR+"\">case </b>");
				switch1.constants.get(j++).welcome(this);
				write(":<br/>");
			}
			this.identcounter+=4;
			switch1.cases.get(i).welcome(this);
			if(switch1.hasbreak.contains(i)){
				ident();
				write("<b style=\"color:"+KEYWORD_COLOR+"\">break</b>;<br/>");
			}
			this.identcounter-=4;
		}
		this.identcounter--;
		ident();
		write("}");
		write("<br/>");
	}
	@Override
	public void visit(TryCatch tryCatch) {
		int i=0;
		write("<b style=\"color:"+KEYWORD_COLOR+"\">try </b>{<br/>");
		this.identcounter++;
		tryCatch.trybody.welcome(this);
		this.identcounter--;
		ident();
		write("}");
		for(i=0;i<tryCatch.catchbodies.size();i++){
			 if(tryCatch.hasfinally && i==tryCatch.catchbodies.size()-1)
				 write("<b style=\"color:"+KEYWORD_COLOR+"\">finally</b>{<br/>");
			 else{
				 write("<b style=\"color:"+KEYWORD_COLOR+"\">catch</b>(");
				 tryCatch.catchargs.get(i).welcome(this);
				 write("){<br/>");
			 }
			 this.identcounter++;
			 tryCatch.catchbodies.get(i).welcome(this);
			 this.identcounter--;
			 ident();
			 write("}");
		}
		write("<br/>");
	}
	@Override
	public void visit(Variable variable) {
		if("null".equals(variable.name) || "this".equals(variable.name)){
			write("<b style=\"color:"+KEYWORD_COLOR+"\">"+variable.name+"</b>");
		}
		else if("+".equals(variable.name) || "-".equals(variable.name)){
			write("<b>"+variable.name+"</b>");
		}
		else if(variable.name.contains("this")){
			int index=variable.name.indexOf("this");
			write(variable.name.substring(0, index));
			write("<b style=\"color:"+KEYWORD_COLOR+"\">this</b>");
			write(variable.name.substring(index+4, variable.name.length()));
		}
		else{
			write(variable.name);
		}
		if(variable.array.intvalue!=-1){
			write("["+variable.array.intvalue+"]");
		}
	}
}
