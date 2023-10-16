package CompPack;

import java.util.Hashtable;
import java.util.List;

public class _Assembler {

	Hashtable<String, String> dict = new Hashtable<String, String>();
	
	public static final Boolean DEBUG_LABELS = false;	

	public void assemble(List<String> instructions) {

		String content = "";

		int ram = _ProgramAssemblyBuilder.VARS;
		int counter = 0;
		for (String ins : instructions) {

			if (ins.isEmpty() || ins.charAt(0) == '/')
				continue;
			
			ins = ins.replaceAll(" ", "");

			if (ins.charAt(0) == '(') {
				String label = ins.substring(1, ins.length() - 1);
				dict.put(label, "" + counter);
			} else
				counter++;

			Boolean notRegister = ins.charAt(1) != 'R' || Character.isLetter(ins.charAt(2));
			if (ins.charAt(0) == '@' && notRegister && Character.isLetter(ins.charAt(1))) {
				String var = ins.substring(1);
				if (dict.containsKey(var))
					continue;
				dict.put(var, "" + ram);
				ram++;
			}
		}
		
		if (DEBUG_LABELS) {
			for (String key : dict.keySet()) {
				String val = dict.get(key);
				int parsed = Integer.parseInt(val);
				if (parsed < 1000 || parsed > 2500)
					continue;
				System.out.println("\nK: " + key + " \nV: " + val);
			}
		}

		for (String ins : instructions) {

			if (ins.isEmpty() || ins.charAt(0) == '(' || ins.charAt(0) == '/')
				continue;

			ins = ins.replaceAll(" ", "");

			if (ins.charAt(0) == '@') {
				content += aInstruction(ins);
				continue;
			}

			String binary = "1000000000000000";
			StringBuilder str = new StringBuilder(binary);

			int i = ins.indexOf("=");
			if (i != -1) {
				destBits(ins, str, i);
			}

			jumpBits(ins, str);

			String sum = sumSubString(ins);

			if (sum.contains("M"))
				str.setCharAt(3, '1');

			sum = sum.replace("M", "Y").replace("A", "Y");

			aluBits(sum, str);

			content += str.toString() + "\n";
		}

		SaveManager.saveProgram("Instructions", content);
	}

	public String aInstruction(String s) {

		String key = s.substring(1);
		if (dict.containsKey(key))
			s = s.replace(key, dict.get(key));
		else
			s = s.replace("R", "");

		short i = Short.parseShort(s.replace("@", ""));
		String binary = "0" + _Computer.shortToBinaryString(i, 15, false);
		return binary + "\n";
	}

	public void destBits(String ins, StringBuilder str, int i) {

		String dest = ins.substring(0, i);
		if (dest.contains("A"))
			str.setCharAt(10, '1');
		if (dest.contains("D"))
			str.setCharAt(11, '1');
		if (dest.contains("M"))
			str.setCharAt(12, '1');
	}

	public void jumpBits(String ins, StringBuilder str) {

		String jump = ins.substring(ins.length() - 3);
		if (jump.equals("JMP") || jump.equals("JLT") || jump.equals("JNE") || jump.equals("JLE")) {
			str.setCharAt(13, '1');
		}
		if (jump.equals("JMP") || jump.equals("JEQ") || jump.equals("JGE") || jump.equals("JLE")) {
			str.setCharAt(14, '1');
		}
		if (jump.equals("JMP") || jump.equals("JGT") || jump.equals("JGE") || jump.equals("JNE")) {
			str.setCharAt(15, '1');
		}
	}

	public String sumSubString(String s) {

		int i1 = s.indexOf(';');
		int i2 = s.indexOf('=');
		String sum = "";
		if (i2 == -1)
			sum = s.substring(0, i1);
		else if (i1 == -1)
			sum = s.substring(i2 + 1);
		else
			sum = s.substring(i2 + 1, i1);

		return sum;
	}

	public void aluBits(String sum, StringBuilder b) {
		
		if (sum.equals("0")) {
			b.setCharAt(4, '1');
			b.setCharAt(6, '1');
			b.setCharAt(8, '1');
			return;
		}
		
		if (sum.equals("1")) {
			for (int i = 4; i <= 9; i++)
				b.setCharAt(i, '1');
			return;
		}
		
		if (sum.equals("-1")) {
			b.setCharAt(4, '1');
			b.setCharAt(5, '1');
			b.setCharAt(6, '1');
			b.setCharAt(8, '1');
			return;
		}

		if (sum.contains("&")) {
			return;
		}

		if (sum.contains("|")) {
			b.setCharAt(5, '1');
			b.setCharAt(7, '1');
			b.setCharAt(9, '1');
			return;
		}

		if (sum.contains("+") || sum.contains("-")) {
			b.setCharAt(8, '1');
		}

		Boolean hasD = sum.contains("D");
		Boolean hasY = sum.contains("Y");

		if (hasY && !hasD)
			b.setCharAt(4, '1');

		if (hasD && !hasY)
			b.setCharAt(6, '1');

		Boolean pos = sum.contains("+") && sum.contains("1");
		Boolean neg = (sum.contains("-") || sum.contains("!")) && !sum.contains("1");
		if (pos || neg)
			b.setCharAt(9, '1');

		if (sum.contains("+1") || (hasY && !hasD) || sum.equals("D-Y"))
			b.setCharAt(5, '1');

		if (sum.contains("+1") || (hasD && !hasY) || sum.equals("Y-D"))
			b.setCharAt(7, '1');
	}
}
