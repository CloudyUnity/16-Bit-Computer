package CompPack;

public class GateAND extends Shape{

	public Node input1;
	public Node input2;
	public Node output;
	
	public GateAND(Vector2 pos) {
		super(pos, new Vector2(50, 25), SceneBuilder.BLOCK, ColorManager.ORANGE);
		ProgramLogicGateBuilder.node.andList.add(this);
		
		text = "AND";
		draggable = true;
		interactible = true;
		deletable = true;
		parent = SceneBuilder.getScene();
		
		input1 = new NodeVisible(new Vector2(-NodeVisible.BASE_SCALE.x, 0));
		input1.parent = this;
		
		input2 = new NodeVisible(new Vector2(-NodeVisible.BASE_SCALE.x, scale.y * 0.5f));
		input2.parent = this;
		
		output = new NodeVisible(new Vector2(scale.x, scale.y * 0.25f));
		output.parent = this;
		output.interactible = true;
		((NodeVisible)output).inputDisabled = true;
	}
	
	public GateAND(Node i1, Node i2, Node o) {
		super();
		ProgramLogicGateBuilder.node.andList.add(this);
		
		visible = false;
		
		input1 = i1;
		input2 = i2;
		output = o;
	}
	
	@Override
	protected void update() {
		
		// Gate Delay (DISABLED)
		if (Math.random() < -1f)
			return;
		
		output.changeState(0, input1.state[0] && input2.state[0]);
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		ProgramLogicGateBuilder.node.andList.remove(this);
		
		((NodeVisible)input1).deactivated = true;
		((NodeVisible)input2).deactivated = true;
		((NodeVisible)output).deactivated = true;
		
		ProgramLogicGateBuilder.node.removeNode(input1);
		ProgramLogicGateBuilder.node.removeNode(input2);
		ProgramLogicGateBuilder.node.removeNode(output);
		
		ProgramLogicGateBuilder.draw.shapeList.remove(input1);
		ProgramLogicGateBuilder.draw.shapeList.remove(input2);
		ProgramLogicGateBuilder.draw.shapeList.remove(output);
	}
}
