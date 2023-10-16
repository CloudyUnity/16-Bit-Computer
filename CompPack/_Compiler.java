package CompPack;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class _Compiler {

	int current = 0;
	String[] tokens;
	List<String> xml = new ArrayList<String>();
	List<String> vmCode = new ArrayList<String>();

	class SymbolInfo {
		public String type;
		public String kind;
		public int index;
	}

	class SymbolTable {
		Hashtable<String, SymbolInfo> table = new Hashtable<String, SymbolInfo>();
		SymbolTable nextTable = null;
	}

	SymbolTable currentScope = new SymbolTable();

	String className = "Undefined";
	int localVarCount = 0;
	int fieldVarCount = 0;

	String[] allKeywords = new String[] { "this", "if", "let", "while", "class", "function", "constructor", "void",
			"var", "do", "return", "true", "false", "null", "field", "static", "method", "int", "char", "boolean",
			"else" };

	public void compile(String content) throws Exception {
		content = removeComments(content).replaceAll("&", "&amp").replaceAll("<", "&lt").replaceAll(">", "&gt");
		tokens = getTokens(content);
		List<String> list = new ArrayList<String>();
		for (String s : tokens) {
			if (s.isEmpty())
				continue;
			list.add(s);
		}
		tokens = list.toArray(new String[0]);	

		logTokens();
		
		current = 0;
		localVarCount = 0;
		fieldVarCount = 0;
		currentScope = new SymbolTable();
		xml.clear();
		vmCode.clear();
		className = "Undefined";
		
		compileClass();

		String code = "";
		for (String s : vmCode) {
			code += s + "\n";
		}

		SaveManager.saveProgram("VM\\" + className, code);
	}
	
	public void logTokens() {
		System.out.println("===============================");
		for (int i = 0; i < tokens.length; i++) {
			System.out.print(i + ":" + tokens[i] + ", ");
		}
		System.out.println("===============================");
	}

	public String removeComments(String input) {
		StringBuilder result = new StringBuilder();
		boolean insideBlockComment = false;
		String[] lines = input.split("\n");

		for (String line : lines) {
			String trimmedLine = line.trim();

			if (insideBlockComment) {
				if (trimmedLine.endsWith("*/")) {
					insideBlockComment = false;
				}
				continue;
			}

			if (trimmedLine.startsWith("/*")) {
				if (trimmedLine.endsWith("*/")) {
					continue;
				}
				insideBlockComment = true;
				continue;
			}

			int commentInd = line.indexOf("//");
			if (commentInd != -1)
				line = line.substring(0, commentInd);

			if (!trimmedLine.startsWith("//") && !trimmedLine.isEmpty())
				result.append(line).append("\n");
		}
		return result.toString();
	}

	public String[] getTokens(String content) throws Exception {
		return content.split("[\\s]+|(?<=[.,;~\\-(){}\\[\\]=*+\"/])|(?=[.,;~\\-(){}\\[\\]=*+\"/])");
	}

	public String extendToken(String token) throws Exception {
		String cat = getCategory(token);
		return "<" + cat + "> " + token.replaceAll("\"", "") + " </" + cat + ">";
	}

	// TODO: Change to DO WHILE
	public void advance() {
		current++;
		if (current == tokens.length)
			return;

		while (tokens[current].isEmpty()) {
			current++;
		}
	}

	public String next() {
		return tokens[current];
	}

	public Boolean nextIs(String... params) {
		return nextAheadIs(0, params);
	}

	public Boolean nextAheadIs(int ahead, String... params) {
		for (String s : params) {
			if (tokens[current + ahead].equals(s))
				return true;
		}
		return false;
	}

	public Boolean nextIsCategory(String cat) throws Exception {
		return getCategory(tokens[current]).equals(cat);
	}

	public Boolean nextIsOp() {
		return nextIs("+", "-", "|", "&gt", "&lt", "&amp", "*", "/", "=");
	}

	public String getFromTable(String name) throws Exception {

		SymbolInfo info = null;
		SymbolTable scope = currentScope;
		while (scope != null) {

			if (scope.table.containsKey(name)) {
				info = scope.table.get(name);
				break;
			}
			scope = scope.nextTable;
		}

		if (info == null)
			throw new Exception("Compiler error: identifier not found in current scope: " + name);

		// if (!type.equals(info.type))
		// throw new Exception("Compiler error: Type mismatch of " + type +" and " +
		// info.type +" on token: " + name);

		String i = !info.kind.equals("class") && !info.kind.equals("subroutine") ? " " + info.index : "";
		return info.kind + i;
	}

	public Boolean tableContains(String name) throws Exception {

		SymbolTable scope = currentScope;
		while (scope != null) {

			if (scope.table.containsKey(name)) {
				return true;
			}
			scope = scope.nextTable;
		}
		return false;
	}

	public void defineInTable(String name, String kind, String type) throws Exception {

		if (currentScope.table.containsKey(name))
			throw new Exception("Compiler error: identifer already defined: " + name);

		SymbolInfo info = new SymbolInfo();
		info.kind = kind;
		info.type = type;
		info.index = maxKindInScope(kind) + 1;
		currentScope.table.put(name, info);

		//System.out.println(name + " " + kind + " " + type + " " + info.index);
	}

	public int maxKindInScope(String kind) {
		int max = -1;
		for (SymbolInfo info : currentScope.table.values()) {
			if (kind.equals(info.kind) && info.index > max)
				max = info.index;
		}
		return max;
	}

	public void increaseScope() {
		SymbolTable newTable = new SymbolTable();
		newTable.nextTable = currentScope;
		currentScope = newTable;
		// System.out.println("Increased scope");
	}

	public void decreaseScope() {
		currentScope = currentScope.nextTable;
		// System.out.println("Decreased scope");
	}

	public void push(String name) throws Exception {
		vmCode.add("push " + getFromTable(name));
	}

	public void pop(String name) throws Exception {
		vmCode.add("pop " + getFromTable(name));
	}

	public void write(String command) {
		vmCode.add(command);
	}

	public void pushConstant(String constant) {
		if (constant.equals("this")) {			
			write("push pointer 0");
			return;
		}
		
		if (constant.equals("null")) {
			write("push constant 0");
			return;
		}
			
		if (constant.equals("true")) {
			write("push constant 1");
			write("neg");
			return;
		}
		
		if (constant.equals("false")) {
			write("push constant 0");
			return;
		}

		write("push constant " + constant);
	}

	public void writeSymbol(String command) {
		if (command.equals("+"))
			write("add");
		if (command.equals("-"))
			write("sub");
		if (command.equals("="))
			write("eq");
		if (command.equals("&"))
			write("and");
		if (command.equals("|"))
			write("or");
		if (command.equals("~"))
			write("neg");
		if (command.equals("&lt"))
			write("lt");
		if (command.equals("&gt"))
			write("gt");
		if (command.equals("*"))
			write("call Math.mult 2");
		if (command.equals("/"))
			write("call Math.div 2");
	}

	public void compileClass() throws Exception {

		xml.add("<class>");
		eat("class");
		className = next();
		eatIdentifier();
		eat("{");
		compileClassVarDec();
		compileSubRoutine();
		eat("}");
		xml.add("</class>");
	}

	public void compileClassVarDec() throws Exception {

		while (nextIs("static", "field")) {
			xml.add("<classVarDec>");
			String kind;
			if (nextIs("field")) {
				fieldVarCount++;
				kind = "this";
			} else
				kind = "static";

			eatAnything();
			String type = next();
			compileType();
			String name = next();
			eatIdentifier();
			defineInTable(name, kind, type);
			while (nextIs(",")) {
				eat(",");
				name = next();
				defineInTable(name, kind, type);
				eatIdentifier();
			}
			eat(";");
			xml.add("</classVarDec>");
		}
	}

	public void compileVarDec() throws Exception {

		while (nextIs("var")) {
			xml.add("<varDec>");
			String kind = "local";
			eat("var");
			String type = next();
			compileType();
			String name = next();
			eatIdentifier();
			defineInTable(name, kind, type);
			while (nextIs(",")) {
				eat(",");
				name = next();
				defineInTable(name, kind, type);
				eatIdentifier();
			}
			eat(";");
			localVarCount++;
			xml.add("</varDec>");
		}
	}

	public void compileSubRoutine() throws Exception {

		while (nextIs("constructor", "function", "method")) {
			String functionType = next();			
			increaseScope();
			xml.add("<subroutineDec>");
			eatAnything();
			if (nextIs("void"))
				eat("void");
			else
				compileType();
			localVarCount = 0;
			write("\nfunction " + className + "." + next() + " ");
			int vmIndex = vmCode.size() - 1;
			if (functionType.equals("constructor")) {
				write("push constant " + fieldVarCount);
				write("call Memory.alloc 1");
				write("pop pointer 0");
			}
			else {
				write("push argument 0");
				write("pop pointer 0");
			}
			eatIdentifier();
			eat("(");
			xml.add("<parameterList>");
			compileParameterList();
			xml.add("</parameterList>");
			eat(")");
			xml.add("<subroutineBody>");
			eat("{");
			compileVarDec();
			compileStatements();
			eat("}");
			vmCode.set(vmIndex, vmCode.get(vmIndex) + localVarCount);
			xml.add("</subroutineBody>");
			xml.add("</subroutineDec>");
			decreaseScope();
		}
	}

	public void compileParameterList() throws Exception {

		if (!nextIsCategory("keyword") && !nextIsCategory("identifier"))
			return;

		defineInTable("objectNull", "argument", className);
		String kind = "argument";
		String type = next();
		compileType();
		String name = next();
		eatIdentifier();
		defineInTable(name, kind, type);
		while (nextIs(",")) {
			eat(",");
			type = next();
			compileType();
			name = next();
			eatIdentifier();
			defineInTable(name, kind, type);
		}
	}

	public void compileStatements() throws Exception {

		xml.add("<statements>");
		while (nextIs("let", "while", "do", "if", "return", "var")) {
			if (nextIs("let"))
				compileLet();
			if (nextIs("while"))
				compileWhile();
			if (nextIs("do"))
				compileDo();
			if (nextIs("if"))
				compileIf();
			if (nextIs("return"))
				compileReturn();
			if (nextIs("var"))
				compileVarDec();
		}
		xml.add("</statements>");
	}

	public void compileWhile() throws Exception {

		int i = current;
		write("label WHILE_START_" + i);
		increaseScope();
		xml.add("<whileStatement>");
		eat("while");
		eat("(");
		compileExpression();
		write("not");
		write("if-goto WHILE_END_" + i);
		eat(")");
		eat("{");
		compileStatements();
		write("goto WHILE_START_" + i);
		eat("}");
		xml.add("</whileStatement>");
		write("label WHILE_END_" + i);
		decreaseScope();
	}

	public void compileIf() throws Exception {

		int i = current;
		String endLabel = "IF_END_" + i;
		increaseScope();
		xml.add("<ifStatement>");
		eat("if");
		eat("(");
		compileExpression();
		write("not");
		write("if-goto IF_L" + (i + 2));
		eat(")");
		eat("{");
		compileStatements();
		write("goto " + endLabel);
		eat("}");
		write("label IF_L" + (i + 2));
		decreaseScope();

		int counter = 0;
		while (nextIs("else") && nextAheadIs(1, "if")) {

			counter++;
			increaseScope();
			eat("else");
			eat("if");
			eat("(");
			compileExpression();
			write("not");
			write("if-goto IF_L" + (i + 2 + counter));
			eat(")");
			eat("{");
			compileStatements();
			write("goto " + endLabel);
			eat("}");
			decreaseScope();
			write("label IF_L" + (i + 2 + counter));
		}

		if (nextIs("else")) {

			increaseScope();
			eat("else");
			eat("{");
			compileStatements();
			eat("}");
			decreaseScope();
		}
		write("label " + endLabel);
		xml.add("</ifStatement>");
	}

	public void compileLet() throws Exception {

		xml.add("<letStatement>");
		eat("let");
		String name = next();
		eatIdentifier();
		Boolean isArr = nextIs("["); 
		if (isArr) {
			push(name);
			eat("[");
			compileExpression();
			eat("]");
			write("add");
		}
		eat("=");
		compileExpression();
		eat(";");
		if (isArr) {
			write("pop temp 0");
			write("pop pointer 1");
			write("push temp 0");
			write("pop that 0");
		}
		else
			pop(name);
		xml.add("</letStatement>");
	}

	public void compileReturn() throws Exception {

		xml.add("<returnStatement>");
		eat("return");

		if (nextIs(";")) {
			eat(";");
			write("push constant 0");
			write("return");
			xml.add("</returnStatement>");
			return;
		}

		compileExpression();
		eat(";");
		write("return");
		xml.add("</returnStatement>");
	}

	public void compileDo() throws Exception {

		xml.add("<doStatement>");
		eat("do");
		compileSubRoutineCall();
		eat(";");
		write("pop temp 0");
		xml.add("</doStatement>");
	}

	public void compileTerm() throws Exception {

		xml.add("<term>");

		if (nextIs("-")) {
			eat("-");
			compileTerm();
			write("neg");
			xml.add("</term>");
			return;
		} else if (nextIs("~")) {
			eat("~");
			compileTerm();
			write("not");
			xml.add("</term>");
			return;
		}

		if (nextIs(")")) {
			xml.add("</term>");
			return;
		}

		if (nextIs("(")) {
			eat("(");
			compileExpression();
			eat(")");
			xml.add("</term>");
			return;
		}
		
		if (nextIs("\"")) {
			eat("\"");
			String str = next();
			write("push constant " + str.length());
			write("call String.new 1");
			for (int i = 0; i < str.length(); i++) {
				write("push constant " + (int)str.charAt(i));
				write("call String.appendChar 2");
			}
			eatAnything();
			eat("\"");
			xml.add("</term>");
			return;
		}

		if (nextAheadIs(1, "(", ".")) {
			compileSubRoutineCall();
			xml.add("</term>");
			return;
		}

		if (nextIsCategory("identifier")) {
			push(next());
			eatIdentifier();
			if (nextIs("[")) {
				eat("[");
				compileExpression();
				eat("]");
				write("add");
				write("pop pointer 1");
				write("push that 0");
			}
			xml.add("</term>");
			return;
		} else {
			pushConstant(next());
			eatAnything();
		}
		xml.add("</term>");
	}

	public int compileExpressionList() throws Exception {

		xml.add("<expressionList>");
		int args = 0;
		if (!nextIs(")")) {
			compileExpression();
			args++;
			while (nextIs(",")) {
				eat(",");
				compileExpression();
				args++;
			}
		}
		xml.add("</expressionList>");
		return args;
	}

	public void compileExpression() throws Exception {

		xml.add("<expression>");
		compileTerm();
		while (nextIsOp()) {
			String op = next();
			eatAnything();
			compileTerm();
			writeSymbol(op);
		}
		xml.add("</expression>");
	}

	public void compileSubRoutineCall() throws Exception {
		
		String name = next();
		Boolean varCall = tableContains(name);
		if (varCall) {
			push(name);
			eatIdentifier();
			eat(".");
			name = next();
		}
		else {
			write("push constant 404");
		}
		while (nextAheadIs(1, ".")) {
			eatIdentifier();
			eat(".");
			name += "." + next();
		}
		eatIdentifier();
		eat("(");
		int args = compileExpressionList() + 1;
		eat(")");
		write("call " + name + " " + args);
	}

	public void compileType() throws Exception {
		if (nextIs("int", "boolean", "char"))
			eatAnything();
		else
			eatIdentifier();
	}

	public void eat(String token) throws Exception {
		if (!tokens[current].equals(token))
			throw new Exception(
					"Compiler error on token: " + tokens[current] + "\nCurrent = " + current + "\nExpected: " + token);

		xml.add(extendToken(tokens[current]));
		advance();
	}

	public void eatIdentifier() throws Exception {
		if (!nextIsCategory("identifier"))
			throw new Exception(
					"Compiler error on token: " + tokens[current] + "\nCurrent = " + current + "\nExpected identifier");

		xml.add(extendToken(tokens[current]));
		advance();
	}

	public void eatAnything() throws Exception {
		xml.add(extendToken(tokens[current]));
		advance();
	}

	public String tokenCategory(int i) {
		int end = tokens[i].indexOf('>');
		return tokens[i].substring(1, end);
	}

	public String getCategory(String s) throws Exception {
		try {
			Integer.parseInt(s);
			return "integerConstant";
		} catch (Exception e) {
		}

		if (s.charAt(0) == '\"')
			return "stringConstant";

		if (!Character.isLetterOrDigit(s.charAt(0)) && s.charAt(0) != '_')
			return "symbol";

		if (isKeyword(s))
			return "keyword";

		if (Character.isLetter(s.charAt(0)) || s.charAt(0) == '_')
			return "identifier";

		throw new Exception("Compiler error on token: " + s);
	}

	public Boolean isKeyword(String s) {
		for (String k : allKeywords) {
			if (s.equals(k))
				return true;
		}
		return false;
	}
}
