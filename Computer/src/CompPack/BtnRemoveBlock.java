package CompPack;

import java.awt.Color;

public class BtnRemoveBlock extends Shape {

	public BtnRemoveBlock(Vector2 pos, Vector2 scale) {
		super(pos, scale, Color.lightGray);
		
		interactible = true;
	}

	@Override
	protected void onMouseRelease(double dur) {

		if (dur > 200)
			return;
		
		SaveManager.removeBlock(InputField.loadBlockName.getText());
	}
}
