package CompPack;

public class BtnChangeNodes extends Shape{

	Boolean decrease;
	Boolean input;
	
	public BtnChangeNodes(Vector2 pos, Vector2 scale, int layer, Boolean decrease, Boolean input) {
		super(pos, scale, layer, ColorManager.DARKER_BLUE);
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
				ProgramLogicGateBuilder.node.removeInput();
			else
				ProgramLogicGateBuilder.node.addInput();
		}
		else
			if (decrease)
				ProgramLogicGateBuilder.node.removeOutput();
			else
				ProgramLogicGateBuilder.node.addOutput();
	}
}
