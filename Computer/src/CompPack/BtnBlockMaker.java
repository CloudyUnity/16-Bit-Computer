package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class BtnBlockMaker extends Shape{

	public BtnBlockMaker(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.DARKER_BLUE);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		Block block = SaveManager.loadBlock(InputField.loadBlockName.getText());
		if (block == null)
			return;
		
		GateBlock gate = new GateBlock(block, Main.mouse.mousePos, Color.lightGray);
		gate.setColor(ColorManager.GREEN);
		Main.mouse.setSelected(gate);
	}
}
