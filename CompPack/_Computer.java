package CompPack;

import java.util.List;

import javax.swing.Timer;

public class _Computer {

	short A;
	short D;	
	short M() {
		return A < 0 ? 0 : RAM[A];
	}
	
	short[] RAM = new short[40000];
	int counter = 0;
	int totalCounter = 0;
	List<Short> ROM;
	
	Timer timer;
	
	public _Computer(List<Short> rom) {
		ROM = rom;
	}
	
	public short ALU(short x, short y, Boolean zx, Boolean nx, Boolean zy, Boolean ny, Boolean f, Boolean no) {
		
		if (zx)
			x = 0;
		if (nx)
			x = (short)~x;
		if (zy)
			y = 0;
		if (ny)
			y = (short)~y;
		
		short result = (short) (f ? x+y : x&y);
		if (no)
			result = (short)~result;
		
		return result;
	}
	
	public void PC() {
		
		if (ROM.size() <= counter) {
			System.out.println("Instructions finished, would enter endless loop. Now exiting");
			_ProgramAssemblyBuilder.stopProgram();
			return;
		}
		
		decode(ROM.get(counter));		
		totalCounter++;
	}
	
	public void decode(short instruction) {
		
		String str = shortToBinaryString(instruction, 16, true);
		//System.out.println("Decimal: " + instruction + " - Binary: " + str);		
		
		if (str.charAt(0) == '0') {
			A = instruction;
			counter++;
			return;
		}
		
		short yOrM = str.charAt(3) == '1' ? M() : A;				
		
		short alu = ALU(D, yOrM, str.charAt(4) == '1', str.charAt(5)== '1', str.charAt(6)== '1', 
				str.charAt(7)== '1', str.charAt(8)== '1', str.charAt(9)== '1');
		
		//System.out.println("ALU: " + alu);
		
		if (A == -1)
			System.out.println(instruction + " " + str + " " + counter);
		
		if (str.charAt(12) == '1')
			RAM[A] = alu;
		if (str.charAt(10) == '1')
			A = alu;
		if (str.charAt(11) == '1')
			D = alu;
		
		Boolean cJLT = str.charAt(13) == '1' && alu < 0;
		Boolean cJEQ = str.charAt(14) == '1' && alu == 0;
		Boolean cJGT = str.charAt(15) == '1' && alu > 0;
		
		if (cJLT || cJEQ || cJGT) {
			counter = A;
			return;
		}
		
		counter++;		
	}
	
	public static String shortToBinaryString(short value, int length, Boolean twosComplement) {
		StringBuilder binary = new StringBuilder(length);
	    
	    if (twosComplement && value < 0) {
	        value = (short) (Math.pow(2, length) + value); // Calculate two's complement
	    }
	    
	    for (int i = length - 1; i >= 0; i--) {
	        int bit = (value >> i) & 1;
	        binary.append(bit);
	    }
	    
	    return binary.toString();
    }
	
	public static short binaryStringToShort(String binaryString) {
        char signChar = binaryString.charAt(0);
        String valueString = binaryString.substring(1);
        int unsignedValue = Integer.parseUnsignedInt(valueString, 2);
        short result = (signChar == '0') ? (short) unsignedValue : (short) -((1 << valueString.length()) - unsignedValue);
        return result;
    }
	
	public void reset() {
		counter = 0;
	}
}
