package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class BtnFreeNodeMaker extends Shape{

	public BtnFreeNodeMaker(Vector2 pos, Vector2 scale) {
		super(pos, scale, Color.lightGray);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		Node n = new Node(Main.mouse.mousePos,false);
		n.layer = 10;
		n.interactible = true;
		n.draggable = true;
		Main.mouse.setSelected(n);
	}
}
