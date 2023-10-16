package CompPack;

import java.util.List;

public class GateOutBus extends Shape{

	public Node input;
	public Node[] outputs;
	
	public GateOutBus(Vector2 pos, int size) {
		super(pos, new Vector2(50, GateBlock.BLOCK_SIZE * size), SceneBuilder.BLOCK, ColorManager.ORANGE);
		ProgramLogicGateBuilder.node.outList.add(this);
		
		text = "out[" + size + "]";
		draggable = true;
		interactible = true;
		deletable = true;
		parent = SceneBuilder.getScene();	
		
		outputs = new Node[size];
		
		double spacing = scale.y / size;
		
		for (int i = 0; i < size; i++) {
			Node output = new NodeVisible(new Vector2(scale.x, (int)(scale.y - GateBlock.BLOCK_SIZE - i * spacing)));
			output.parent = this;
			output.interactible = true;
			((NodeVisible)output).inputDisabled = true;
			output.text = "" + i;
			outputs[i] = output;
		}
		
		input = new NodeVisible(new Vector2(-NodeVisible.BASE_SCALE.x, scale.y * 0.5f), size);
		input.parent = this;
	}
	
	public GateOutBus(Node i, List<Node> outputs) {
		super();
		ProgramLogicGateBuilder.node.outList.add(this);
		
		visible = false;
		
		this.outputs = new Node[outputs.size()];
		for (int j = 0; j < outputs.size(); j++)
			this.outputs[j] = outputs.get(j);
		
		input = i;
	}
	
	@Override
	protected void update() {
		
		for (int i = 0; i < input.state.length; i++) {
			outputs[i].changeState(0, input.state[i]);
		}
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		ProgramLogicGateBuilder.node.outList.remove(this);

		((NodeVisible)input).deactivated = true;
		ProgramLogicGateBuilder.node.removeNode(input);
		ProgramLogicGateBuilder.draw.shapeList.remove(input);
		
		for (int i = 0; i < outputs.length; i++) {
			((NodeVisible)outputs[i]).deactivated = true;
			ProgramLogicGateBuilder.node.removeNode(outputs[i]);
			ProgramLogicGateBuilder.draw.shapeList.remove(outputs[i]);
		}
	}
}