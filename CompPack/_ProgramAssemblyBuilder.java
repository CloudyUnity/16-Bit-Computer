package CompPack;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class _ProgramAssemblyBuilder {

	static List<Short> ins;
	static _Computer comp;
	static _Assembler assem;
	static _Translater trans;
	static _Compiler compiler;
	static _Screen screen;
	static _Keyboard keyboard;

	public static final Boolean COMPILING = true;
	public static final Boolean TRANSLATING = true;
	public static final Boolean ASSEMBLING = true;
	public static final Boolean RUNNING = true;

	public static final int SP = 256;
	public static final int ARG = 10000;
	public static final int LCL = 11000;
	public static final int THIS = 12000;
	public static final int THAT = 13000;
	public static final int VARS = 16;
	
	public static final int CALC_SPEED = 1000;
	
	public static final Boolean LOG_STACK = true;
	public static final Boolean LOG_REG = true;
	
	public static String logFile = "";

	public static void startProgram() {

		logFile = "";
		
		screen = new _Screen();
		keyboard = new _Keyboard();
		
		if (COMPILING)
			compile();
		
		System.out.println("Compilition complete");

		if (TRANSLATING)
			translate();
		
		System.out.println("Translation complete");

		if (ASSEMBLING)
			assemble();
		
		System.out.println("Assembly complete");
		
		System.out.println("Ready to run");

		if (!RUNNING)
			return;			

		comp = new _Computer(readInstructions());

		screen.initialise();
		screen.addKeyListener(keyboard);
		keyboard.initialise();

		update();
	}

	public static List<Short> readInstructions() {

		List<String> str = SaveManager.loadProgram("Instructions");

		for (int i = str.size() - 1; i >= 0; i--) {
			if (str.get(i).startsWith("//") || str.get(i).isEmpty())
				str.remove(i);
			str.set(i, str.get(i).replaceAll(" ", "").substring(0, 16));
		}

		ins = new ArrayList<Short>();
		for (String s : str) {
			ins.add(_Computer.binaryStringToShort(s));
		}

		return ins;
	}
	
	public static void compile() {
		
		SaveManager.clearVM();
		List<String> str = SaveManager.loadJack();

		compiler = new _Compiler();

		for (String s : str) {

			try {
				compiler.compile(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void translate() {

		List<List<String>> str = SaveManager.loadVM();

		trans = new _Translater();
		String startFunc = _Translater.USING_MAIN_BREAK ? "Main.main" : "Sys.init";
		String content = "@" + SP + "\nD=A \n@SP \nM=D \n@" + LCL + "\nD=A \n@LCL \nM=D \n@" + ARG
				+ "\nD=A \n@ARG \nM=D " + "\n@" + THIS + "\nD=A \n@THIS \nM=D \n@" + THAT + "\nD=A \n@THAT \nM=D"
				+ "\n\n@" + startFunc + "\n0;JMP \n\n";

		for (List<String> s : str) {

			try {
				content += trans.translate(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		content += "\n(END) \n@END \n";

		SaveManager.saveProgram("Hack", content);
	}

	public static void assemble() {

		List<String> str = SaveManager.loadProgram("Hack");

		assem = new _Assembler();
		assem.assemble(str);
	}

	static Timer timer;

	public static void update() {

		timer = new Timer(1, e -> {

			for (int i = 0; i < CALC_SPEED && timer.isRunning(); i++) {
				comp.PC();
				if (LOG_STACK)
					logShortStack();
				
				if (LOG_REG)
					logFile += "\n(T: " + comp.totalCounter + ") (C: " + (comp.counter - 1) + ") "
						+ "(A: " + comp.A + ") (D: " + comp.D + ") (M: " + comp.M() + ")\n";				
			}			

			System.out.println(comp.totalCounter);
			SaveManager.saveProgram("log", logFile);
			_ProgramAssemblyBuilder.screen.revalidate();
			_ProgramAssemblyBuilder.screen.repaint();

		});

		timer.start();
	}

	public static void stopProgram() {
		timer.stop();

		String stack = "Final stack: \n";
		for (int i = 256; i < 300; i++) {
			stack += "|" + comp.RAM[i] + "|";
			stack += (comp.RAM[16] == i ? " <-SP\n" : "\n");
		}
		System.out.println(stack);
	}
	
	public static void logShortStack() {
		
		String stack = "";
		for (int i = 256; i < 350; i++) {
			stack += "(" + comp.RAM[i] + ")";
			if (comp.RAM[16] == i)
				stack += "<";
		}
		logFile += stack;
	}
}
