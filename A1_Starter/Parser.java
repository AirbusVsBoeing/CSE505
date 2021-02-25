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

//function -> int id '(' [ pars ] ')' '{' body '}'
class Function { 
	String fname; 	// name of the function
	Pars p;
	Body b;
	String header;

	public Function() {
		
		// Fill in code here
		// Must invoke:  FunTab.add(fname);
		// Code ends with following two statements:

		header = "int " + fname + "(" + p.types + ");";
		return;
	}
}

// pars --> int id { ',' int id }
class Pars { 

	String types = ""; // comma-separated sequence of types, e.g., int,int
	int npars = 0;	   // the number of parameters

	public Pars() {
		 	// Fill in code here
			// Must insert each id that is parsed 
			// into the symbol table using:
			// SymTab.add(id)
	}
}

// body -> [ decls ] stmts
class Body { 
	Decls d;
	Stmts s;

	public Body() {
		// Fill in code here	
	}
}

// decls -> int idlist ';'
class Decls { 
	Idlist il;

	public Decls() {
		// Fill in code here
	}
}

// idlist -> id { ',' id }
class Idlist { 
	String id;
	Idlist il;

	public Idlist() {
		// Fill in code here
		// Must insert each id that is parsed
		// into the symbol table using:
		// SymTab.add(id);
	}
}

// stmts -> stmt [ stmts ]
class Stmts { 
	Stmt s;
	Stmts ss;

	public Stmts() { 
		// Fill in code here
	}
}

// stmt -> assign ';' | loop | cond | cmpd | return ';' | print expr ';'
class Stmt { 
	Stmt s;

	public Stmt() {
		// Fill in code here
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

	public Assign() {
		super(0); // superclass initialization
		// Fill in code here.
		// End with this statement:
		ByteCode.gen("istore", SymTab.index(id));
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
class Cond extends Stmt { 
	Relexp r;
	Stmt s1;
	Stmt s2;
	public Cond() {
		super(0);
		// Fill in code here.  Refer to
		// code in class Loop for guidance
	}
}

// cmpd -> '{' stmts '}'
class Cmpd extends Stmt { 
	Stmts s;

	public Cmpd() {
		super(0);
		// Fill in code here
	}
}

// return -> 'return' expr
class Return extends Stmt { 
	Expr e;

	public Return() {
		super(0);
		// Fill in code here.  End with:
		ByteCode.gen_return();
	}
}

// print -> 'print' expr
class Print extends Stmt { 
	Expr e;

	public Print() {
		super(0);
		// Fill in code here.  End with:
		ByteCode.gen_print();
	}
}

// relexp -> expr ('<' | '>' | '<=' | '>=' | '==' | '!= ') expr
class Relexp { 
	Expr e1;
	Expr e2;
	String op = "";

	public Relexp() {
		// Fill in code here
	}
}

// expr -> term (+ | -) expr | term
class Expr { 
	Term t;
	Expr e;
	char op;

	public Expr() {
		// Fill in code here
	}
}

// term -> factor (* | /) term | factor
class Term { 
	Factor f;
	Term t;
	char op;

	public Term() {
		// Fill in code here
	}
}

// factor -> int_lit | id | '(' expr ')' | funcall
class Factor { 
	int i;
	String id;
	Funcall fc;
	Expr e;
	
	public Factor() {
		// Fill in code here
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
		// Fill in code here
	}
}
