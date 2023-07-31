package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class BtnNOTMaker extends Shape{

	public BtnNOTMaker(Vector2 pos, Vector2 scale) {
		super(pos, scale, Color.lightGray);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		GateNOT gate = new GateNOT(Main.mouse.mousePos);
		Main.mouse.setSelected(gate);
	}
}
