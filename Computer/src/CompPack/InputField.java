package CompPack;

import javax.swing.*;

class InputField extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JTextField saveBlockName;
	public static JTextField loadBlockName;

	public static void initialise() {
		saveBlockName = new JTextField(30);
		loadBlockName = new JTextField(30);
		
		saveBlockName.setBounds(120, 10, 200, 30);
		loadBlockName.setBounds(320, 460, 200, 30);

		Main.draw.add(saveBlockName);
		Main.draw.add(loadBlockName);
	}
}