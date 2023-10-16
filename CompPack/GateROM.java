package CompPack;

import java.util.List;

public class GateROM extends Shape{

	public Node input;
	public Node output;
	
	public List<String> instructions;
	
	public GateROM(Vector2 pos) {
		super(pos, new Vector2(50, 25), SceneBuilder.BLOCK, ColorManager.ORANGE);
		ProgramLogicGateBuilder.node.romList.add(this);
		
		text = "ROM";
		draggable = true;
		interactible = true;
		deletable = true;
		parent = SceneBuilder.getScene();
		
		loadInsts();
		
		input = new NodeVisible(new Vector2(-NodeVisible.BASE_SCALE.x, 0), 16);
		input.parent = this;
		
		output = new NodeVisible(new Vector2(scale.x, scale.y * 0.25f), 16);
		output.parent = this;
		output.interactible = true;
		((NodeVisible)output).inputDisabled = true;
	}
	
	public GateROM(Node i, Node o) {
		super();
		ProgramLogicGateBuilder.node.romList.add(this);
		
		visible = false;
		
		loadInsts();
		
		input = i;
		output = o;
	}
	
	void loadInsts() {
		
		instructions = SaveManager.loadProgram("Instructions");
		
		for (int i = instructions.size() - 1; i >= 0; i--) {
			if (instructions.get(i).startsWith("//") || instructions.get(i).isEmpty())
				instructions.remove(i);
		}
	}
	
	@Override
	protected void update() {
		
		int address = 0;
		for (int i = 0; i < input.state.length; i++)
			address += input.state[i] ? Math.pow(2, i) : 0;
		
		String inst = instructions.get(address);
		Boolean[] out = new Boolean[16];
		
		for (int i = 0; i < 16; i++)
			out[15 - i] = inst.charAt(i) == '1';
			
		output.changeState(out);
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		ProgramLogicGateBuilder.node.romList.remove(this);
		
		((NodeVisible)input).deactivated = true;
		((NodeVisible)output).deactivated = true;
		
		ProgramLogicGateBuilder.node.removeNode(input);
		ProgramLogicGateBuilder.node.removeNode(output);
		
		ProgramLogicGateBuilder.draw.shapeList.remove(input);
		ProgramLogicGateBuilder.draw.shapeList.remove(output);
	}
}

