package CompPack;

public class BtnRemoveBlock extends Shape {

	public BtnRemoveBlock(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.RED);
		
		interactible = true;
	}

	@Override
	protected void onMouseRelease(double dur) {

		if (dur > 200)
			return;
		
		SaveManager.removeBlock(InputField.loadBlockName.getText());
	}
}
