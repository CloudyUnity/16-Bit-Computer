package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class BtnRestart extends Shape{

	public BtnRestart(Vector2 pos, Vector2 scale) {
		super(pos, scale, Color.lightGray);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		Main.restartProgram();
	}
}
