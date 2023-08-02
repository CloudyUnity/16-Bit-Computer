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
		
		String txt = InputField.loadBlockName.getText();
		
		if (txt.startsWith("in")) {
			int busSize = Integer.parseInt(txt.substring(2));
			makeInBus(busSize);
			return;
		}
		if (txt.startsWith("out")) {
			int busSize = Integer.parseInt(txt.substring(3));
			makeOutBus(busSize);
			return;
		}
		
		Block block = SaveManager.loadBlock(txt);
		if (block == null)
			return;
		
		GateBlock gate = new GateBlock(block, Main.mouse.mousePos, Color.lightGray);
		gate.setColor(ColorManager.GREEN);
		Main.mouse.setSelected(gate);
	}
	
	void makeInBus(int size) {
		
		GateInBus bus = new GateInBus(Main.mouse.mousePos, size);
		bus.setColor(ColorManager.PURPLE);
		Main.mouse.setSelected(bus);
	}
	
	void makeOutBus(int size) {
		
		GateOutBus bus = new GateOutBus(Main.mouse.mousePos, size);
		bus.setColor(ColorManager.PURPLE);
		Main.mouse.setSelected(bus);
	}
}
