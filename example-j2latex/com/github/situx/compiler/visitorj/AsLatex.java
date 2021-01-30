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

public class AsLatex implements Visitor {
public Writer w;
public String filename,blank;
public int identcounter,ident,linecounter,minus=6,border=10;
public static final String KEYWORD_COLOR="keyword";
public static final String KEYWORD_COLOR_RGB="9,9,92";
public static final String CODE_COLOR="black";
public static final String COMMENT_COLOR = "red";
public static final String COMMENTKEY_COLOR = "cyan";
public static final String INTEGER_COLOR="blue";
public static final String DOUBLE_COLOR="blue";
public static final String STRING_COLOR="purple";

public static final String STRING_COLOR_RGB="128,0,128";
public static final String CHAR_COLOR="cyan";
public static final String ANNOTATION_COLOR="22,247,227";

public AsLatex(Writer w, String filename,int ident,int lines) {
	super();
	this.w = w;
	this.filename=filename;
	this.ident=ident;
	this.identcounter=0;
	this.linecounter=lines;
	if(lines==-1)
		minus=0;
	this.blank="\\hspace{2 mm}";
	for(int i=1;i<ident;i++)
		this.blank+="\\hspace{2 mm}";
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
				minus--;
			}
			write(this.linecounter+"");
			for(i=0;i<minus;i++){
				write(" ");
			}
		}
		for(i=0;i<identcounter;i++){
			write(blank);
		}
	}
	
	public void formatComment(String comment){
		write("\\textcolor{"+COMMENT_COLOR+"}{");
		comment=comment.replace("@*","<b style=\"color:"+COMMENTKEY_COLOR+">@</b>");
		String[] commentarray=comment.split("\n");
		for(int i=0;i<commentarray.length;i++){
			write(commentarray[i]);
			if(i<commentarray.length-1){
				write("}\\\\");
				ident();
				write("\\textcolor{"+COMMENT_COLOR+"}{");
			}
		}
		write("}\\\\");
		ident();
	}
	@Override
	public void visit(FunctionDef functionDef) {
		ident();
		if(!functionDef.annotations.isEmpty()){
			write("\\textcolor{anno}{");
			for(Iterator<String> iter =functionDef.annotations.iterator();iter.hasNext();){
				write(iter.next()+"\\\\");
				if(iter.hasNext())
					ident();
			}
			write("}");
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
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{throws}} ");
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
			write("\\{\\\\");
			this.identcounter++;
			for(Node n:functionDef.body)
				n.welcome(this);
			this.identcounter--;
			ident();
			write("\\}\\\\");
			ident();
		}
		write("\\\\");
	}

	@Override
	public void visit(Add n) {
		n.op1.welcome(this);
		if(n.addeq)
			write("+=");
		else
			write("+");
		n.op2.welcome(this);
	}

	@Override
	public void visit(Minus n) {
		n.op1.welcome(this);
		if(n.mineq)
			write("-=");
		else
			write("-");
		n.op2.welcome(this);
	}

	@Override
	public void visit(Mult n) {
		n.op1.welcome(this);
		if(n.multeq)
			write("*=");
		else
			write("*");
		n.op2.welcome(this);
	}

	@Override
	public void visit(Div n) {
		n.op1.welcome(this);
		if(n.diveq)
			write("/=");
		else
			write("/");
		n.op2.welcome(this);
	}

	@Override
	public void visit(Variable variable) {
		variable.name=variable.name.replaceAll("_", "\\\\_");
		if("null".equals(variable.name) || "this".equals(variable.name)){
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{"+variable.name+"}}");
		}
		else if(variable.name.contains("this")){
			int index=variable.name.indexOf("this");
			write(variable.name.substring(0, index));
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{this}}");
			write(variable.name.substring(index+4, variable.name.length()));
		}
		else{
			write(variable.name);
		}
		if(variable.array.intvalue!=-1){
			write("["+variable.array.intvalue+"]");
		}
	}

	@Override
	public void visit(FunCall funCall) {
		if("super".equals(funCall.name)){
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{");
			write(funCall.name+"}}");
		}
		else if(funCall.name.contains("this")){
			int index=funCall.name.indexOf("this");
			write(funCall.name.substring(0, index));
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{this}}");
			write(funCall.name.substring(index+4, funCall.name.length()));
		}
		else
			write(funCall.name);
		if(!"null".equals(funCall.generic)){
			write("$<$"+funCall.generic+"$>$");
		}
		write("(");
		for(Iterator<Node> it = funCall.args.iterator(); it.hasNext();){
			it.next().welcome(this);
			if(it.hasNext())
				write(",");
		}
		write(")");
		if(!funCall.varexps.isEmpty()){
			write("\\{\\\\");
			this.identcounter++;
			for(Node n:funCall.varexps){
				n.welcome(this);
			}
			this.identcounter--;
			ident();
			write("\\}");
		}
	}

	@Override
	public void visit(Literal literal) {
		write(""+literal.n);
	}

	@Override
	public void visit(Program program) {
	/*\\lstdefinestyle{Java}{language=Java,backgroundcolor=\\color{Grey},emphstyle=\\color{Red},"
+"keywordstyle=\\color{Green}\\bfseries,commentstyle=\\color{Red},stringstyle=\\color{Red},showstringspaces=false,basicstyle=\\footnotesize\\color{Black},"
+"numbers=none,captionpos=b,tabsize=4,breaklines=true,frame=tlRB}*/
	write("\\documentclass[7pt]{report}\\usepackage{color}\\definecolor{purple}{RGB}{"+STRING_COLOR_RGB+"}\\definecolor{anno}{RGB}{"+ANNOTATION_COLOR+"}\\definecolor{keyword}{RGB}{"+KEYWORD_COLOR_RGB+"}\\begin{document}\\begin{center}"+filename+"\\end{center}"/*\\begin{lstlisting}[style=Java]"*/);
	ident();
	write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{package}} "+program.packagename+";\\\\");
	ident();
	write("\\\\");
	program.imports.welcome(this);
	for(ClassDef c:program.classes){
		ident();
		write("\\\\");
		c.welcome(this);
	}
	write(/*\\end{lstlisting}*/"\\end {document}");
	}

	@Override
	public void visit(Greater greater) {
		greater.op1.welcome(this);
		if(greater.leftright && !greater.eq)
			write("$<$");
		else if(greater.leftright && greater.eq)
			write("$<$=");
		else if(!greater.leftright && greater.eq)
			write("$>$=");
		else
			write("$>$");
		greater.op2.welcome(this);
	}

	@Override
	public void visit(Equals equals) {
		equals.op1.welcome(this);
		switch(equals.mode){
		case 1:	write("==");break;
		case 2:	write("!=");break;
		case 3: write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{ instanceof }}");
		default:break;
		}
		equals.op2.welcome(this);
	}

	@Override
	public void visit(AndOr and) {
		and.op1.welcome(this);
		switch(and.mode){
		case 1: write("\\&");break;
		case 2: write(" \\&\\& ");break;
		case 3: write("\\&=");break;
		case 4:	write("$\\arrowvert$");break;
		case 5: write(" $\\arrowvert$$\\arrowvert$ ");break;
		case 6: write("$\\arrowvert$=");break;
		case 7: write("\\^{} ");break;
		case 8: write("\\^{} =");break;
		default:break;
		}
		and.op2.welcome(this);

	}



	@Override
	public void visit(SingleCase singleCase) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Sem sem) {
		ident();
		switch(sem.prevtoken){
		case 0:write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{return }}");;break;
		case 1:write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{new }}");break;
		case 2:write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{return new }}");break;
		default:break;
		}
		sem.op1.welcome(this);
		if(sem.setit)
			write(";\\\\");
		
	}

	@Override
	public void visit(Modulo modulo) {
		modulo.op1.welcome(this);
		if(modulo.modeq)
			write("\\%=");
		else
			write("\\%");
		modulo.op2.welcome(this);
	}



	@Override
	public void visit(Shift shift) {
		shift.op1.welcome(this);
		if(shift.leftright && shift.shifteq==0)
			write("$<<$");
		else if(shift.leftright && shift.shifteq>0)
			write("$<<$=");
		else if(!shift.leftright && shift.shifteq==0)
			write("$>>$");
		else if(!shift.leftright && shift.shifteq==1)
			write("$>>$=");
		else if(!shift.leftright && shift.shifteq==2)
			write("$>>>$");
		else
			write("$>>>$=");
		shift.op2.welcome(this);		
	}

	@Override
	public void visit(Loop loop) {
		if(loop.type==-1){
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{for}}(");
			if(loop.exps.isEmpty()){
				write(";;");
			}
			for(Iterator<Node> it = loop.exps.iterator();it.hasNext();){
				it.next().welcome(this);
				if(it.hasNext())
					write(";");
			}
			write(")\\{\\\\");
		}
		else if(loop.type==0){
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{while}}(");
			for(Node n:loop.exps)
				n.welcome(this);
			write(")\\{\\\\");
		}
		else if(loop.type==2){
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{for}}(");
			for(int i=0;i<loop.exps.size();i++){
				loop.exps.get(i).welcome(this);
				if(i==loop.exps.size()-2)
					write(" : ");
			}
			write(")\\{\\\\");
		}
		else
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{do}}\\{\\\\");
		this.identcounter++;
		for(Node n:loop.body)
			n.welcome(this);
		this.identcounter--;
		if(loop.type==1){
			ident();
			write("\\}\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{while}}(");
			for(Node n:loop.exps)
				n.welcome(this);
			write(");\\\\");
		}
		else{
			ident();
			write("\\}\\\\");
		}
		
	}

	@Override
	public void visit(Modifier modifier) {
		if(!"".equals(modifier.comment)){
			formatComment(modifier.comment);
		}
		write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{");
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
		else if(modifier.fin==1)
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
		write("}}");
	}

	@Override
	public void visit(ClassDef classdef) {
		ident();
		if(!"".equals(classdef.comment)){
			formatComment(classdef.comment);
		}
		classdef.mod.welcome(this);
		write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{");
		if(classdef.isinterface)
			write("interface}} ");
		else
			write("class}} ");
		classdef.name.welcome(this);
		if(!classdef.eximp.isEmpty() && !classdef.isinterface){
			write(" \\textcolor{"+KEYWORD_COLOR+"}{\\textbf{implements}} ");
			for(Iterator<Node> iter = classdef.eximp.iterator();iter.hasNext();){
				iter.next().welcome(this);
				if(iter.hasNext())
					write(",");
			}
		}
		if(!classdef.ext.equals("")){
			write(" \\textcolor{"+KEYWORD_COLOR+"}{\\textbf{extends}} ");
			classdef.ext.welcome(this);
		}
		write("\\{\\\\");
		this.identcounter++;
		for(Node n:classdef.exps){
			n.welcome(this);
		}
		this.identcounter--;
		ident();
		write("\\}\\\\");
		
	}

	@Override
	public void visit(IfDef ifDef) {
		if(ifDef.ternary){
			ifDef.exprlist.get(0).welcome(this);
			write(" ? ");
			ifDef.bodylist.get(0).welcome(this);
			write(" : ");
			ifDef.bodylist.get(1).welcome(this);
		}
		else{
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{if "+"}}");
			for(int i=0;i<ifDef.bodylist.size();i++){
				if(i==ifDef.exprlist.size()){
					ident();
					write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{else "+"}}");
				}
				else{
					if(i>0){
						ident();
						write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{else if "+"}}");
					}
					write("(");
					ifDef.exprlist.get(i).welcome(this);
					write(")");
				}
				write("\\{\\\\");
				this.identcounter++;
				ifDef.bodylist.get(i).welcome(this);
				this.identcounter--;
				ident();
				write("\\}\\\\");
			}
		}
	}

	@Override
	public void visit(ReturnType returnType) {
		if(!"".equals(returnType.comment)){
			formatComment(returnType.comment);
		}
		if(returnType.type!=0){
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{");
			returnType.classid.welcome(this);
			write("}}");
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
	public void visit(Comment comment) {
		ident();
		write("\\textcolor{"+COMMENT_COLOR+"}{/**"+comment.comment+"}\\\\");
		for(int i=0;i<comment.attokens.size();i++){
			ident();
			write("\\textcolor{"+COMMENT_COLOR+"}{ * }\\textcolor{"+COMMENTKEY_COLOR+"}{"+comment.attokens.get(i++)+"} ");
			write("\\textcolor{"+COMMENT_COLOR+"}{");
			while(i<comment.attokens.size() && comment.attokens.get(i).charAt(0)!='@'){
				write(comment.attokens.get(i));
				i++;
			}
			i--;
			write(" }\\\\");
		}
		ident();
		write("\\textcolor{"+COMMENT_COLOR+"}{*/}\\\\");
		
	}

	@Override
	public void visit(FieldDef fieldDef) {
		if(!fieldDef.forloop && !fieldDef.local)
			ident();
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
			write(";\\\\");
		
	}

	@Override
	public void visit(Import import1) {
		for(String s:import1.imports){
			ident();
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{import}} "+s+";\\\\");
		}
		
	}

	@Override
	public void visit(Switch switch1) {
		int j=0;
		write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{switch}} (");
		switch1.exp.welcome(this);
		write(")\\{\\\\");
		this.identcounter++;
		for(int i=0;i<switch1.cases.size();i++){
			ident();
			if(i==switch1.defaultpos)
				write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{default}}:\\\\");
			else{
				write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{case }}");
				switch1.constants.get(j++).welcome(this);
				write(":\\\\");
			}
			this.identcounter+=2;
			switch1.cases.get(i).welcome(this);
			if(switch1.hasbreak.contains(i)){
				ident();
				write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{break}};\\\\");
			}
			this.identcounter-=2;
		}
		this.identcounter--;
		ident();
		write("\\}\\\\");
		
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
	public void visit(TryCatch tryCatch) {
		int i=0;
		write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{try}} \\{\\\\");
		this.identcounter++;
		tryCatch.trybody.welcome(this);
		this.identcounter--;
		ident();
		write("\\}");
		for(i=0;i<tryCatch.catchbodies.size();i++){
			 if(tryCatch.hasfinally && i==tryCatch.catchbodies.size()-1)
				 write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{finally}}\\{\\\\");
			 else{
				 write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{catch}}(");
				 tryCatch.catchargs.get(i).welcome(this);
				 write(")\\{\\\\");
			 }
			 this.identcounter++;
			 tryCatch.catchbodies.get(i).welcome(this);
			 this.identcounter--;
			 ident();
			 write("\\}");
		}
		write("\\\\");
	}

	@Override
	public void visit(EnumDef enumDef) {
		write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{enum} } ");
		Iterator<String> it=enumDef.enumdefs.iterator();
		write(enumDef.enumdefs.get(0)+"\\{");
		for(it.next();it.hasNext();){
			write(it.next());
			if(it.hasNext())
				write(", ");
		}
		write("\\}");		
	}



	@Override
	public void visit(Number number) {
		switch(number.choose){
		case 0: write("\\textcolor{"+INTEGER_COLOR+"}{");
				if(number.hexoroct==1)
					write("0x"+Integer.toHexString(number.intvalue));
				else if(number.hexoroct==2)
					write("0"+Integer.toOctalString(number.intvalue));
				else
					write(number.intvalue.toString());
				write("}");break;
		case 1: write("\\textcolor{"+INTEGER_COLOR+"}{");
				if(number.hexoroct==1)
					write("0x"+Long.toHexString(number.longvalue));
				else if(number.hexoroct==2)
					write("0"+Long.toOctalString(number.longvalue));
				else
					write(number.longvalue.toString());
				write("}");break;
		case 2: write("\\textcolor{"+DOUBLE_COLOR+"}{"+number.floatvalue.toString()+"f}");break;
		case 3: write("\\textcolor{"+DOUBLE_COLOR+"}{"+number.doublevalue.toString()+"}");
		default:break;
		}
		
	}

	@Override
	public void visit(StringOrCharConst stringOrCharConst) {
		if(stringOrCharConst.choose){
			if("true".equals(stringOrCharConst.str) || "false".equals(stringOrCharConst.str) || "null".equals(stringOrCharConst.str))
				write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{"+stringOrCharConst.str+"}}");
			else
				write("\\textcolor{"+STRING_COLOR+"}{"+stringOrCharConst.str+"}");
		}
		else
			write("\\textcolor{"+CHAR_COLOR+"}{"+stringOrCharConst.str+"}");
		
	}

	@Override
	public void visit(Except except) {
		write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{throw ");
		if(except.isnew)
			write("new ");
		write("}} ");
		except.except.welcome(this);
	}

	@Override
	public void visit(Assignment assignment) {
		assignment.n1.welcome(this);
		write("=");
		if(assignment.insertnew)
			write("\\textcolor{"+KEYWORD_COLOR+"}{\\textbf{new }}");
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
	public void visit(NotDef notDef) {
		if(notDef.notorbitwise)
			write("!");
		else
			write("~");
		notDef.op1.welcome(this);
	}

	@Override
	public void visit(Imps imps) {
		write(imps.classname);
		if(!imps.generic.isEmpty())
			write("$<$");
		for(Iterator<Node> iter=imps.generic.iterator();iter.hasNext();){
			iter.next().welcome(this);
			if(iter.hasNext())
				write(",");
		}
		if(!imps.generic.isEmpty())
			write("$>$");
	}

}
