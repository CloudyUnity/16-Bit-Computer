package CompPack;

import java.awt.Color;

public class BtnChangeNodes extends Shape{

	Boolean decrease;
	Boolean input;
	
	public BtnChangeNodes(Vector2 pos, Vector2 scale, Color color, Boolean decrease, Boolean input) {
		super(pos, scale, color);
		this.decrease = decrease;
		this.input = input;
		
		interactible = true;
	}

	@Override
	protected void onMouseRelease(double dur) {
		
		if (dur > 200)
			return;
		
		if (input) {
			if (decrease)
				Main.node.removeInput();
			else
				Main.node.addInput();
		}
		else
			if (decrease)
				Main.node.removeOutput();
			else
				Main.node.addOutput();
	}
}
