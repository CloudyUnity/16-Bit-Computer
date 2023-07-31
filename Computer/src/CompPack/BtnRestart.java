package CompPack;

import java.awt.event.MouseEvent;

public class BtnRestart extends Shape{

	public BtnRestart(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.RED);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		Main.restartProgram();
	}
}
