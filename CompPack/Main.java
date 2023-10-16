package CompPack;

public class Main {

	static Boolean assembly = false;
	
	public static void main(String[] args) {
		
		if (assembly)
			_ProgramAssemblyBuilder.startProgram();
		else
			ProgramLogicGateBuilder.startProgram();
	}
}
