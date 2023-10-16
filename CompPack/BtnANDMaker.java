package CompPack;

import java.awt.event.MouseEvent;

public class BtnANDMaker extends Shape{

	public BtnANDMaker(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.DARKER_BLUE);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		GateAND gate = new GateAND(ProgramLogicGateBuilder.mouse.mousePos);
		ProgramLogicGateBuilder.mouse.setSelected(gate);
	}
}
