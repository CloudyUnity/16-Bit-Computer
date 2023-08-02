package CompPack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GateBlock extends Shape{

	public List<Node> inputs = new ArrayList<Node>();
	public List<Node> outputs = new ArrayList<Node>();
	
	public static final int BLOCK_SIZE = 18;
	
	public GateBlock(Block block, Vector2 pos, Color color) {
		super(pos, new Vector2(50, BLOCK_SIZE * Math.max(block.inputs.size(), block.outputs.size())), SceneBuilder.BLOCK, color);
		
		text = block.name;
		draggable = true;
		interactible = true;
		deletable = true;
		parent = SceneBuilder.getScene();
		
		double spacing = scale.y / block.inputs.size();
		
		for (int i = 0; i < block.inputs.size(); i++) {
			Vector2 nPos = new Vector2(-Node.BASE_SCALE.x, (int)(scale.y - BLOCK_SIZE - i * spacing));
			Node n = new Node(nPos, block.states.get(i));
			n.parent = this;
			n.text = Character.toString('a' + i);			
			inputs.add(n);
		}
		
		spacing = scale.y / block.outputs.size();
		
		for (int i = 0; i < block.outputs.size(); i++) {
			Vector2 nPos = new Vector2(scale.x, (int)(scale.y - BLOCK_SIZE - i * spacing));
			Node n = new Node(nPos, block.states.get(i));
			n.parent = this;
			n.interactible = true;
			n.inputDisabled = true;
			n.text = "" + i;
			outputs.add(n);
		}
		
		BlockBuilder.makeBlock(block, inputs, outputs);
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		for (Node n : inputs) {
			n.deactivated = true;
			Main.draw.shapeList.remove(n);
			Main.node.nodeList.remove(n);
		}
		for (Node n : outputs) {
			n.deactivated = true;
			Main.draw.shapeList.remove(n);
			Main.node.nodeList.remove(n);
		}
	}
}
