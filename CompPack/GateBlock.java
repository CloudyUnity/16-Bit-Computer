package CompPack;

import java.util.ArrayList;
import java.util.List;

public class GateBlock extends Shape{

	public List<Node> inputs = new ArrayList<Node>();
	public List<Node> outputs = new ArrayList<Node>();
	
	public Block block;
	
	public static final int BLOCK_SIZE = 18;
	
	public GateBlock(Block block, Vector2 pos, String color) {
		super(pos, new Vector2(50, BLOCK_SIZE * Math.max(block.inputs.size(), block.outputs.size())), SceneBuilder.BLOCK, color);
		
		text = block.name;
		draggable = true;
		interactible = true;
		deletable = true;
		parent = SceneBuilder.getScene();
		this.block = block;
		
		double spacing = scale.y / block.inputs.size();
		
		for (int i = 0; i < block.inputs.size(); i++) {
			Vector2 nPos = new Vector2(-NodeVisible.BASE_SCALE.x, (int)(scale.y - BLOCK_SIZE - i * spacing));
			int index = block.inputs.get(i);
			Node n = new NodeVisible(nPos, block.states.get(index));
			n.parent = this;
			n.text = Character.toString('a' + i);			
			inputs.add(n);
		}
		
		spacing = scale.y / block.outputs.size();
		
		for (int i = 0; i < block.outputs.size(); i++) {
			Vector2 nPos = new Vector2(scale.x, (int)(scale.y - BLOCK_SIZE - i * spacing));
			int index = block.outputs.get(i);
			NodeVisible n = new NodeVisible(nPos, block.states.get(index));
			n.parent = this;
			n.interactible = true;
			n.inputDisabled = true;
			n.text = "" + i;
			outputs.add(n);
		}
		
		long timeBefore = System.currentTimeMillis();
		BlockBuilder.makeBlock(block, inputs, outputs); // !
		String timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		System.out.println("Time to build " + block.name + " = " + timeTaken);		
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		for (Node n : inputs) {
			((NodeVisible)n).deactivated = true;
			ProgramLogicGateBuilder.draw.shapeList.remove(n);
			ProgramLogicGateBuilder.node.removeNode(n);
		}
		for (Node n : outputs) {
			((NodeVisible)n).deactivated = true;
			ProgramLogicGateBuilder.draw.shapeList.remove(n);
			ProgramLogicGateBuilder.node.removeNode(n);
		}
	}
}
