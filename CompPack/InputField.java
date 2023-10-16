package CompPack;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

class InputField extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JTextField saveBlockName;
	public static JTextField loadBlockName;

	public static void initialise() {
		saveBlockName = new JTextField(30);
		loadBlockName = new JTextField(30);
		
		Font font = new Font("TimesRoman", Font.BOLD, 25);
		saveBlockName.setFont(font);
		loadBlockName.setFont(font);
		
		saveBlockName.setBackground(ColorManager.parseColor(ColorManager.WHITE));
		loadBlockName.setBackground(ColorManager.parseColor(ColorManager.WHITE));
		
		saveBlockName.setBorder(new LineBorder(Color.black, 2));
		loadBlockName.setBorder(new LineBorder(Color.black, 2));
		
		setPlaceholderText(saveBlockName, " Block name");
		setPlaceholderText(loadBlockName, " Search");
		
		saveBlockName.setBounds(320, 10, 200, 30);
		loadBlockName.setBounds(320, 460, 200, 30);

		ProgramLogicGateBuilder.draw.add(saveBlockName);
		ProgramLogicGateBuilder.draw.add(loadBlockName);
	}
	
	public static void update() {
		if (loadBlockName == null)
			return;
		
		Vector2 screenSize = ProgramLogicGateBuilder.draw.getWindowSize();
		
		loadBlockName.setBounds(320, (int)screenSize.y - 40, 200, 30);
	}
	
	public static void setPlaceholderText(JTextField textField, String text) {
		
        String placeholderText = text;
        textField.setText(placeholderText);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
        	
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholderText)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholderText);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
	}
}