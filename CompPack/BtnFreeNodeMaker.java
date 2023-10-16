package CompPack;

import java.awt.event.MouseEvent;

public class BtnFreeNodeMaker extends Shape{

	public BtnFreeNodeMaker(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.DARKER_BLUE);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		NodeVisible n = new NodeVisible(ProgramLogicGateBuilder.mouse.mousePos, 16);
		n.layer = 10;
		n.interactible = true;
		n.draggable = true;
		ProgramLogicGateBuilder.mouse.setSelected(n);
	}
}
