package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class BtnANDMaker extends Shape{

	public BtnANDMaker(Vector2 pos, Vector2 scale) {
		super(pos, scale, Color.lightGray);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		GateAND gate = new GateAND(Main.mouse.mousePos);
		Main.mouse.setSelected(gate);
	}
}
