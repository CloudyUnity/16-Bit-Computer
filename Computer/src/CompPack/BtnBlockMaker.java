package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class BtnBlockMaker extends Shape{

	public BtnBlockMaker(Vector2 pos, Vector2 scale) {
		super(pos, scale, Color.lightGray);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		Block block = SaveManager.loadBlock(InputField.loadBlockName.getText());
		if (block == null)
			return;
		
		GateBlock gate = new GateBlock(block, Main.mouse.mousePos, Color.lightGray);
		Main.mouse.setSelected(gate);
	}
}
