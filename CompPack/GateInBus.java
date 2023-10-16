package CompPack;

import java.util.List;

public class GateInBus extends Shape{

	public Node[] inputs;
	public Node output;
	
	public GateInBus(Vector2 pos, int size) {
		super(pos, new Vector2(50, GateBlock.BLOCK_SIZE * size), SceneBuilder.BLOCK, ColorManager.ORANGE);
		ProgramLogicGateBuilder.node.inList.add(this);
		
		text = "in[" + size + "]";
		draggable = true;
		interactible = true;
		deletable = true;
		parent = SceneBuilder.getScene();	
		
		inputs = new Node[size];
		
		double spacing = scale.y / size;
		
		for (int i = 0; i < size; i++) {
			Node input = new NodeVisible(new Vector2(-NodeVisible.BASE_SCALE.x, (int)(scale.y - GateBlock.BLOCK_SIZE - i * spacing)));
			input.parent = this;
			input.text = Character.toString('a' + i);	
			inputs[i] = input;
		}
		
		output = new NodeVisible(new Vector2(scale.x, scale.y * 0.5f), size);
		output.parent = this;
		output.interactible = true;
		((NodeVisible)output).inputDisabled = true;
	}
	
	public GateInBus(List<Node> inputs, Node o) {
		super();
		ProgramLogicGateBuilder.node.inList.add(this);
		
		visible = false;
		
		this.inputs = new Node[inputs.size()];
		for (int j = 0; j < inputs.size(); j++)
			this.inputs[j] = inputs.get(j);
		
		output = o;
	}
	
	@Override
	protected void update() {
		
		for (int i = 0; i < inputs.length; i++) {
			output.state[i] = inputs[i].state[0];
		}
		output.checkOutputs();
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		ProgramLogicGateBuilder.node.inList.remove(this);

		((NodeVisible)output).deactivated = true;
		ProgramLogicGateBuilder.node.removeNode(output);
		ProgramLogicGateBuilder.draw.shapeList.remove(output);
		
		for (int i = 0; i < inputs.length; i++) {
			((NodeVisible)inputs[i]).deactivated = true;
			ProgramLogicGateBuilder.node.removeNode(inputs[i]);
			ProgramLogicGateBuilder.draw.shapeList.remove(inputs[i]);
		}
	}
}
