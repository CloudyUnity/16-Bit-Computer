package CompPack;

import java.awt.event.MouseEvent;

public class BtnNOTMaker extends Shape{

	public BtnNOTMaker(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.DARKER_BLUE);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		GateNOT gate = new GateNOT(ProgramLogicGateBuilder.mouse.mousePos);
		ProgramLogicGateBuilder.mouse.setSelected(gate);
	}
}
