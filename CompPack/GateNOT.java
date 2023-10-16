package CompPack;

public class GateNOT extends Shape{

	public Node input;
	public Node output;
	
	public GateNOT(Vector2 pos) {
		super(pos, new Vector2(50, 25), SceneBuilder.BLOCK, ColorManager.ORANGE);
		ProgramLogicGateBuilder.node.notList.add(this);
		
		text = "NOT";
		draggable = true;
		interactible = true;
		deletable = true;
		parent = SceneBuilder.getScene();
		
		input = new NodeVisible(new Vector2(-NodeVisible.BASE_SCALE.x, scale.y * 0.25f));
		input.parent = this;
		
		output = new NodeVisible(new Vector2(50, scale.y * 0.25f));
		output.parent = this;
		output.interactible = true;
		((NodeVisible)output).inputDisabled = true;
	}
	
	public GateNOT(Node i, Node o) {
		super();
		ProgramLogicGateBuilder.node.notList.add(this);
		
		visible = false;
		
		input = i;
		output = o;
	}
	
	@Override
	protected void update() {
		
		output.changeState(0, !input.state[0]);
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		ProgramLogicGateBuilder.node.notList.remove(this);
		
		((NodeVisible)input).deactivated = true;
		((NodeVisible)output).deactivated = true;
		
		ProgramLogicGateBuilder.node.removeNode(input);
		ProgramLogicGateBuilder.node.removeNode(output);
		ProgramLogicGateBuilder.draw.shapeList.remove(input);
		ProgramLogicGateBuilder.draw.shapeList.remove(output);
	}
}
