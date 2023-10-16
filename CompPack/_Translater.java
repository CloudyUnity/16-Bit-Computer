package CompPack;

import java.util.List;

public class _Translater {

	String currentFunc;

	public static final Boolean USING_MAIN_BREAK = false;
	public static final Boolean WRITE_COMMENTS = true;
	public static final Boolean DEBUG_SUBPUSH = false;

	public String subPush = "";

	public List<String> commands;
	public int i = 0;

	public String translate(List<String> c) throws Exception {

		String content = "";

		currentFunc = "";

		commands = c;

		int size = commands.size();
		for (i = 0; i < size; i++) {

			String com = commands.get(i);
			if (com.isEmpty() || com.startsWith("/"))
				continue;

			String[] terms = com.split(" ");

			if (WRITE_COMMENTS)
				content += "// " + com + "\n";

			if (terms[0].equals("push"))
				content += push(terms[1], Integer.parseInt(terms[2]));
			else if (terms[0].equals("pop")) {
				String pop = pop(terms[1], Integer.parseInt(terms[2]));
				content += pop;
				if (DEBUG_SUBPUSH && !subPush.isEmpty())
					System.out.println(pop);
				subPush = "";
			}

			else if (terms[0].equals("add"))
				content += add();
			else if (terms[0].equals("sub"))
				content += sub();
			else if (terms[0].equals("neg"))
				content += neg();

			else if (terms[0].equals("eq"))
				content += eq(i);
			else if (terms[0].equals("gt"))
				content += gt(i);
			else if (terms[0].equals("lt"))
				content += lt(i);

			else if (terms[0].equals("and"))
				content += and();
			else if (terms[0].equals("or"))
				content += or();
			else if (terms[0].equals("not"))
				content += not();

			else if (terms[0].equals("label"))
				content += label(terms[1]);
			else if (terms[0].equals("goto"))
				content += gotoCommand(terms[1]);
			else if (terms[0].equals("if-goto"))
				content += ifGotoCommand(terms[1]);

			else if (terms[0].equals("function"))
				content += func(terms[1], Integer.parseInt(terms[2]));
			else if (terms[0].equals("call"))
				content += call(terms[1], Integer.parseInt(terms[2]), i + 1);
			else if (terms[0].equals("return"))
				content += returnCommand(currentFunc.equals("main"));

			else
				throw new Exception("Translation error on line " + i + "\n With line " + com);

			content += "\n";
		}

		return content;
	}

	String push(String location, int num) throws Exception {

		Boolean isConstant = location.equals("constant");
		Boolean isStatic = location.equals("static");
		Boolean isTemp = location.equals("temp");
		Boolean isPointer = location.equals("pointer");
		Boolean isMem = !isConstant && !isStatic && !isTemp && !isPointer;

		String push = "";

		if (isStatic)
			push += "@Static_" + num + "\n";
		else if (isTemp)
			push += "@R" + (5 + num) + "\n";
		else if (isPointer)
			push += "@" + (num == 0 ? "THIS" : "THAT") + "\n";
		else if (num == 0 || num == 1) {
			if (isConstant)
				push += "D=" + num + "\n";
		} else
			push += "@" + num + "\n";

		if ((isConstant || isMem) && (num != 0 && num != 1))
			push += "D=A \n";
		
		if (isTemp)
			push += "D=A \n";

		if (isMem) {
			if (num == 0)
				push += "@" + locationToVar(location) + "\nA=M \n";
			else if (num == 1)
				push += "@" + locationToVar(location) + "\nA=M+1 \n";
			else
				push += "@" + locationToVar(location) + "\nA=M+D \n";
		}

		if (isPointer)
			push += "A=M \n";

		if (!isConstant && !isTemp)
			push += "D=M \n";

		String skip = skipPush(push, num, isConstant, isMem);
		if (skip.equals("skip"))
			return "";
		else if (!skip.isEmpty())
			return skip;

		push += "@SP \nA=M \nM=D \n@SP \nM=M+1 \n";

		return push;
	}

	String skipPush(String push, int num, Boolean isC, Boolean isM) throws Exception {

		String next = commands.get(i + 1);
		if (next.startsWith("pop")) {
			subPush = push;
			return "skip";
		}
		
		if (commands.size() <= i + 2)
			return "";

		String last = commands.get(i - 1);
		String nextNext = commands.get(i + 2);
		Boolean constMem = next.startsWith("push") && !next.startsWith("push constant") && nextNext.equals("add");
		Boolean memConst = last.startsWith("push") && !last.startsWith("push constant") && next.equals("add");

		if (next.startsWith("push constant") && nextNext.equals("add") && isM && num <= 1)
			return "skip";

		if (last.startsWith("push constant") && next.equals("//add") && isM && num <= 1)
			return "skip";

		if ((constMem || memConst) && isC) {
			String mem = constMem ? next : last;
			String memType = mem.split(" ")[1];
			int memNum = Integer.parseInt(mem.split(" ")[2]);
			int addInd = constMem ? i + 2 : i + 1;

			if (memNum > 1)
				return "";

			if (num > 1)
				subPush += "@" + num + "\nD=A \n";

			Boolean popIntoSame = commands.size() > addInd + 1 && commands.get(addInd + 1).equals("pop " + memType + " " + memNum);
			String dest = popIntoSame ? "M" : "D";

			if (memType.equals("static"))
				subPush += "@Static_" + memNum + "\n";
			else
				subPush += "@" + locationToVar(memType) + "\n";

			if (memNum == 1)
				subPush += "A=M+1 \n";
			else
				subPush += "A=M \n";

			if (num == 0)
				subPush += dest + "=M \n";
			else if (num == 1)
				subPush += dest + "=M+1 \n";
			else
				subPush += dest + "=M+D \n";
			
			commands.set(addInd, "//add");
			
			if (popIntoSame) {
				commands.set(addInd+1, "//pop skipped");
				push = subPush;
				subPush = "";
				return push;
			}

			return "skip";
		}
		return "";
	}

	String pop(String location, int num) throws Exception {

		if (location.equals("static")) {

			String pop = "";
			if (!subPush.isEmpty())
				pop += subPush;
			else
				pop += "@SP \nAM=M-1 \nD=M \n";
			pop += "@Static_" + num + "\nM=D \n";
			return pop;
		}

		if (location.equals("pointer")) {

			String pop = "";
			if (!subPush.isEmpty())
				pop += subPush;
			else
				pop += "@SP \nAM=M-1 \nD=M \n";
			pop += "@" + (num == 0 ? "THIS" : "THAT") + "\nA=M \nM=D \n";
			return pop;
		}

		if (location.equals("temp")) {
			String pop = "@R" + (num + 5) + "\nD=A \n@R13 \nM=D \n";
			if (!subPush.isEmpty())
				pop += subPush;
			else
				pop += "@SP \nAM=M-1 \nD=M \n";
			pop += "@R13 \nA=M \nM=D \n";
			return pop;
		}

		String pop = "@" + locationToVar(location) + "\n";
		if (num == 1)
			pop += "D=M+1 \n";
		else if (num == 0)
			pop += "D=M \n";
		else
			pop += "D=M \n@" + num + "\nD=A+D \n";
		pop += "@R13 \nM=D \n";
		if (!subPush.isEmpty())
			pop += subPush;
		else
			pop += "@SP \nAM=M-1 \nD=M \n";
		pop += "@R13 \nA=M \nM=D \n";
		return pop;
	}

	String add() {
		return "@SP \nAM=M-1 \nD=M \nA=A-1 \nM=M+D \n";
	}

	String sub() {
		return "@SP \nA=M-1 \nD=M \nA=A-1 \nM=M-D \n@SP \nM=M-1 \n";
	}

	String neg() {
		return "@SP \nAM=M-1 \nM=-M \n@SP \nM=M+1 \n";
	}

	String eq(int line) {
		return "@SP \nAM=M-1 \nD=M \nA=A-1 \nD=D-M \n@" + currentFunc + "$" + "IF_TRUE_" + line
				+ "\nD;JEQ \n\n@SP \nA=M-1 \nM=0 \n@" + currentFunc + "$" + "IF_THEN_" + line + "\n0;JMP " + "\n\n("
				+ currentFunc + "$" + "IF_TRUE_" + line + ") \n@SP \nA=M-1 \nM=-1 \n(" + currentFunc + "$" + "IF_THEN_"
				+ line + ") \n";
	}

	String gt(int line) {
		return "@SP \nAM=M-1 \nD=M \nA=A-1 \nD=D-M \n@" + currentFunc + "$" + "IF_TRUE_" + line
				+ "\nD;JGT \n\n@SP \nA=M-1 \nM=0 \n@" + currentFunc + "$" + "IF_THEN_" + line + "\n0;JMP " + "\n\n("
				+ currentFunc + "$" + "IF_TRUE_" + line + ") \n@SP \nA=M-1 \nM=-1 \n(" + currentFunc + "$" + "IF_THEN_"
				+ line + ") \n";
	}

	String lt(int line) {
		return "@SP \nAM=M-1 \nD=M \nA=A-1 \nD=D-M \n@" + currentFunc + "$" + "IF_TRUE_" + line
				+ "\nD;JLT \n\n@SP \nA=M-1 \nM=0 \n@" + currentFunc + "$" + "IF_THEN_" + line + "\n0;JMP " + "\n\n("
				+ currentFunc + "$" + "IF_TRUE_" + line + ") \n@SP \nA=M-1 \nM=-1 \n(" + currentFunc + "$" + "IF_THEN_"
				+ line + ") \n";
	}

	String and() {
		return "@SP \nAM=M-1 \nD=M \nA=A-1 \nM=D&M \n";
	}

	String or() {
		return "@SP \nAM=M-1 \nD=M \nA=A-1 \nM=D|M \n";
	}

	String not() {
		return "@SP \nA=M-1 \nM=!M \n";
	}

	String label(String name) {
		return "(" + currentFunc + "$" + name + ")\n";
	}

	String gotoCommand(String name) {
		return "@" + currentFunc + "$" + name + "\n0;JMP \n";
	}

	String ifGotoCommand(String name) {
		return "@SP \nAM=M-1 \nD=M \n@" + currentFunc + "$" + name + "\nD;JNE \n";
	}

	String func(String name, int num) {

		currentFunc = name;

		String s = "(" + name + ") \n";
		for (int i = 0; i < num; i++) {
			s += "@0 \nD=A \n@SP \nA=M \nM=D \n@SP \nM=M+1 \n";
		}
		return s;
	}

	String call(String name, int num, int returnAddress) {
		String label = currentFunc + "$" + "ret." + returnAddress;

		return "@" + label
				+ "\nD=A \n@SP \nA=M \nM=D \n@SP \nM=M+1 \n@LCL \nD=M \n@SP \nA=M \nM=D \n@SP \nM=M+1 \n@ARG \nD=M"
				+ "\n@SP \nA=M \nM=D \n@SP \nM=M+1 \n@THIS \nD=M \n@SP \nA=M \nM=D \n@SP \nM=M+1"
				+ "\n@THAT \nD=M \n@SP \nA=M \nM=D \n@SP \nM=M+1 \n@" + (5 + num) + "\nD=A \n@SP \nD=M-D \n@ARG \nM=D"
				+ "\n@SP \nD=M \n@LCL \nM=D \n@" + name + "\n0;JMP \n(" + label + ") \n";
	}

	String returnCommand(Boolean isMain) {
		if (isMain && USING_MAIN_BREAK)
			return "@END \n0;JMP \n";

		return "@LCL \nD=M \n@R13 \nM=D \n@5 \nA=D-A \nD=M \n@R14 \nM=D \n@SP \nA=M-1 \nD=M \n@ARG \nA=M \nM=D \n@ARG \nD=M+1 \n@SP \nM=D "
				+ "\n@R13 \nAM=M-1 \nD=M \n@THAT \nM=D \n@R13 \nAM=M-1 \nD=M \n@THIS \nM=D \n@R13 \nAM=M-1 \nD=M \n@ARG \nM=D \n@R13 \nAM=M-1"
				+ " \nD=M \n@LCL \nM=D" + "\n@R14 \nA=M \n0;JMP \n";
	}

	String locationToVar(String loc) throws Exception {

		switch (loc) {
		case "local":
			return "LCL";
		case "this":
			return "THIS";
		case "that":
			return "THAT";
		case "argument":
			return "ARG";
		default:
			throw new Exception("Translation Error, locationToVar not found: " + loc);
		}
	}
}
