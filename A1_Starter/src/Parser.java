// This file requires several changes

// program -> { function } end
class Program {		 
	public Program() {

		while (Lexer.nextToken == Token.KEY_INT) {

			SymTab.initialize(); // initialize for every function parsed
			ByteCode.initialize(); // initialize for every function parsed

			Function f = new Function();
			
			ByteCode.output(f.header);
			
			Interpreter.initialize(f.fname, SymTab.idptr - 1, f.p.npars, ByteCode.code, ByteCode.arg, ByteCode.codeptr);
		}
		FunTab.output();
	}
}

//function -> int id '('  pars  ')' '{' body '}'
class Function { 
	String fname; 	// name of the function
	Pars p;
	Body b;
	String header;

	
	public Function() {
		
		// Must invoke:  FunTab.add(fname);
		// Code ends with following two statements:
		// done implementing
		Lexer.lex(); // skip int
		this.fname = Lexer.ident;
		FunTab.add(this.fname);
		Lexer.lex(); // skip '('
		p = new Pars();
		Lexer.lex(); // skip ')'
		Lexer.lex(); // skip '{'
		b = new Body();
		Lexer.lex(); // skip '}'
		
		header = "int " + fname + "(" + p.types + ");";
		return;
	}
}

// pars --> int id { ',' int id }
class Pars { 

	String types = ""; // comma-separated sequence of types, e.g., int,int
	int npars = 0;	   // the number of parameters

	public Pars() {
		 	// done implementing
			// Must insert each id that is parsed 
			// into the symbol table using:
			// SymTab.add(id)
		
		Lexer.lex();
		Lexer.lex();
		SymTab.add(Lexer.ident);
		types = types + "int";
		npars = 1;
		Lexer.lex();
		while(Lexer.nextToken == Token.COMMA) {
				++npars;
				types = types + ",int";
				Lexer.lex();
				Lexer.lex();
				SymTab.add(Lexer.ident);
				Lexer.lex();
			}
		}
	}

// body -> [ decls ] stmts
class Body { 
	Decls d;
	Stmts s;

	public Body() {
		if(Lexer.nextToken == Token.KEY_INT) {
			d = new Decls();
		}
		s = new Stmts();
		
	}
}

// decls -> int idlist ';'
class Decls {  // done implementing
	Idlist il;

	public Decls() {
		Lexer.lex(); // int
		il = new Idlist();
		Lexer.lex();
		
	}
}

// idlist -> id { ',' id }
class Idlist { 
	String id;
	Idlist il;

	public Idlist() {
		// done implementing
		// Must insert each id that is parsed
		// into the symbol table using:
		// SymTab.add(id);
		this.id = Lexer.ident;
		SymTab.add(this.id);
		Lexer.lex();
		while(Lexer.nextToken == Token.COMMA) {
			Lexer.lex();
			this.id = this.id + "," + Lexer.ident; 
			SymTab.add(Lexer.ident);
			Lexer.lex();
		}
	}
}

// stmts -> stmt [ stmts ]
class Stmts { 
	Stmt s;
	Stmts ss;

	public Stmts() { 
		s = new Stmt();
		if(Lexer.nextToken == Token.ID||
				Lexer.nextToken == Token.LEFT_BRACE||
				Lexer.nextToken == Token.KEY_IF||
				Lexer.nextToken == Token.KEY_WHILE||
				Lexer.nextToken == Token.KEY_RETURN||
				Lexer.nextToken == Token.KEY_PRINT){
			ss = new Stmts();
		}
	}
}

// stmt -> assign ';' | loop | cond | cmpd | return ';' | print expr ';'
class Stmt { 
	Stmt s;

	public Stmt() { //done implementing
		int token = Lexer.nextToken;
		switch(token) {
			case Token.ID:{
				s = new Assign();
				Lexer.lex();
				break;
			}
			case Token.KEY_IF:{
				s = new Cond();
				break;
			}
			case Token.KEY_WHILE:{
				s = new Loop();
				break;
			}
			case Token.LEFT_BRACE:{
				s = new Cmpd();
				break;
			}
			case Token.KEY_RETURN:{
				s = new Return();
				Lexer.lex();
				break;
			}
			case Token.KEY_PRINT:{
				s = new Print();
				Lexer.lex();
				break;
			}
		}
	}

	public Stmt(int d) {
	  // Leave the body empty.
	  // This helps avoid infinite loop - why? 
	}
}

// assign -> id '=' expr
class Assign extends Stmt { 
	String id;
	Expr e;

	public Assign() { //TODO: need to implement
		super(0); // superclass initialization
		// Fill in code here.
		int index = SymTab.index(Lexer.ident);
		if(index == -1) 
			System.out.println("Id not in symtab");
		this.id = Lexer.ident; //id
		Lexer.lex(); // '='
		Lexer.lex(); 
		e = new Expr();
		ByteCode.gen("istore",index); // for post fix
	}
}


// loop -> while '(' relexp ')' stmt
class Loop extends Stmt { 
	Relexp b;
	Stmt c;
	public Loop() {
		super(0);
		Lexer.lex(); // skip over 'while'
		Lexer.lex(); // skip over '('
		int boolpoint = ByteCode.str_codeptr;
		b = new Relexp();
		Lexer.lex(); // skip over ')'
		int whilepoint = ByteCode.skip(3);
		c = new Stmt();
		ByteCode.gen_goto(boolpoint);
		ByteCode.skip(2);
		ByteCode.patch(whilepoint, ByteCode.str_codeptr);
	}
}


// cond -> if '(' relexp ')' stmt [ else stmt ]
class Cond extends Stmt { //TODO: need to implement this
	Relexp r;
	Stmt s1;
	Stmt s2;
	public Cond() {
		super(0);
		// Fill in code here.  Refer to
		// code in class Loop for guidance
		
		Lexer.lex(); // skip if
		Lexer.lex(); // skip '('
		r = new Relexp(); // takes some number of bytes -- can't say
		Lexer.lex(); // skip ')'
		int else_start = ByteCode.skip(3); // for else_start 
		int ret_add = 0;
		s1 = new Stmt();
		
		//Lexer.lex();
		
		if(Lexer.nextToken == Token.KEY_ELSE){
			
			Lexer.lex(); // skip else
			
			ret_add = ByteCode.codeptr;
			ByteCode.gen_goto(-2); // add goto 
			ByteCode.skip(2); // for goto ret_add
			ByteCode.patch(else_start, ByteCode.str_codeptr);
			s2 = new Stmt();
			
		//	Lexer.lex(); // maybe '}' or return
			
			ByteCode.patch(ret_add, ByteCode.str_codeptr);
		}
		else 
			ByteCode.patch(else_start, ByteCode.str_codeptr);
//		System.out.println("Lexer.nextToken:" + Lexer.nextToken);
		
		//Lexer.lex();
		//Lexer.lex();
		
//		System.out.println("Lexer.nextToken:" + Lexer.nextToken);
	}

}

// cmpd -> '{' stmts '}'
class Cmpd extends Stmt { 
	Stmts s;

	public Cmpd() {
		super(0);
		// Fill in code here
		Lexer.lex(); // skip '{'
		s = new Stmts();
		Lexer.lex(); // skip '}'
	}
}

// return -> 'return' expr
class Return extends Stmt { 
	Expr e;

	public Return() {
		super(0);
		// Fill in code here.  
		Lexer.lex(); // skip return
		e = new Expr();
		// End with:
		ByteCode.gen_return();
	}
}

// print -> 'print' expr
class Print extends Stmt { 
	Expr e;

	public Print() {
		super(0);
		// Fill in code here.  End with:
		Lexer.lex(); // skip print
		e = new Expr();
		ByteCode.gen_print();
	}
}

// relexp -> expr ('<' | '>' | '<=' | '>=' | '==' | '!= ') expr
class Relexp { 
	Expr e1;
	Expr e2;
	String op = "";

	public Relexp() {
		e1 = new Expr();
		// don't need to lex here -- because  we lexed in factor
		int token = Lexer.nextToken;
		switch(token) {
			case Token.LESSER_OP:{
				Lexer.lex();
				e2 = new Expr();
				op = "<";
				ByteCode.gen_if(op);
				break;
			}
			case Token.LESSEQ_OP:{
				Lexer.lex();
				e2 = new Expr();
				op = "<=";
				ByteCode.gen_if(op);
				break;
			}
			case Token.GREATER_OP:{
				Lexer.lex();
				e2 = new Expr();
				op = ">";
				ByteCode.gen_if(op);
				break;
			}
			case Token.GREATEREQ_OP:{
				e2 = new Expr();
				op = ">=";
				ByteCode.gen_if(op);
				break;
			}
			case Token.EQ_OP:{
				Lexer.lex();
				e2 = new Expr();
				op = "==";
				ByteCode.gen_if(op);
				break;
			}
			case Token.NOT_EQ:{
				Lexer.lex();
				e2 = new Expr();
				op="!=";
				ByteCode.gen_if(op);
				break;
			}
		}
	}
}

// expr -> term (+ | -) expr | term
class Expr { 
	Term t;
	Expr e;
	char op;

	public Expr() {
		t = new Term();
		if(Lexer.nextChar == '+' || Lexer.nextChar == '-') {
			op = Lexer.nextChar; 
			Lexer.lex(); // skip + or -
			e = new Expr();
			ByteCode.gen(op);
		}
	}
}

// term -> factor (* | /) term | factor
class Term { 
	Factor f;
	Term t;
	char op;

	public Term() {
		f = new Factor();
		if(Lexer.nextChar == '*' || Lexer.nextChar == '/') {
			op = Lexer.nextChar;
			Lexer.lex();
			t = new Term();
			ByteCode.gen(op);
		}
	}
}

// factor -> int_lit | id | '(' expr ')' | funcall
class Factor { 
	int i;
	String id;
	Funcall fc;
	Expr e;
	
	public Factor() {
		int token = Lexer.nextToken;
		switch(token) {
			case Token.INT_LIT:{ //TODO: bytecode generation -- not sure
				i = Lexer.intValue;
				Lexer.lex(); 
				Code.gen(Code.intcode(i));
				break;
			}
			case Token.ID:{
				this.id = Lexer.ident;
				int index = SymTab.index(this.id);
				System.out.println(index);
				if(index< 0) {
					index = FunTab.index(this.id);
					if(index < 0) 
						System.out.println("id not recognized");
					fc = new Funcall(this.id);
				}
				Lexer.lex();
				ByteCode.gen("iload", index);
				
				break;
			}
			case Token.LEFT_PAREN:{
				Lexer.lex();
				e = new Expr();
				Lexer.lex();
				
				break;
			}
			default:{
				fc = new Funcall(Lexer.ident);
				Lexer.lex();
				break;
			}
		}
	}
}

// funcall -> id '(' [ exprlist ] ')'
class Funcall { 
	String id;
	ExprList el;

	public Funcall(String id) {
		this.id = id;
		Lexer.lex(); // (
		ByteCode.gen("aload", 0);
		el = new ExprList();
		Lexer.lex(); // skip over the )
		int funid = FunTab.index(id);
		ByteCode.gen_invoke(funid);
		ByteCode.skip(2);
	}
}

// exprlist -> expr [ , exprlist ]
class ExprList { 
	Expr e;
	ExprList el;

	public ExprList() {
		e = new Expr();
		Lexer.lex(); // skip ','
		System.out.println(Lexer.nextToken);
		while(Lexer.nextToken == Token.COMMA) {
			Lexer.lex();
			el = new ExprList();
			Lexer.lex();
		}
	}
}

class Code{
	
	public static void gen(String s) {
		ByteCode.code[ByteCode.codeptr] = s;
		ByteCode.codeptr++;
		
		ByteCode.str_code[ByteCode.str_codeptr] = s;
		ByteCode.str_codeptr++;
	}
	
	
	public static String intcode(int i) {
		if (i > 127) return "sipush " + i;
		if (i > 5) return "bipush " + i;
		return "iconst_" + i;
	}
}
