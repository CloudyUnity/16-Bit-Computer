package CompPack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GateBlock extends Shape{

	public List<Node> inputs = new ArrayList<Node>();
	public List<Node> outputs = new ArrayList<Node>();
	
	public GateBlock(Block block, Vector2 pos, Color color) {
		super(pos, new Vector2(50, 25 * Math.max(block.inputs.size(), block.outputs.size())), color);
		
		text = block.name;
		draggable = true;
		interactible = true;
		deletable = true;
		
		double spacing = scale.y / block.inputs.size();
		
		for (int i = 0; i < block.inputs.size(); i++) {
			Node n = new Node(new Vector2(-Node.BASE_SCALE.x, (int)(scale.y - 25 - i * spacing)), true);
			n.layer = 55;
			n.parent = this;
			inputs.add(n);
		}
		
		spacing = scale.y / block.outputs.size();
		
		for (int i = 0; i < block.outputs.size(); i++) {
			Node n = new Node(new Vector2(scale.x, (int)(scale.y - 25 - i * spacing)), false);
			n.layer = 55;
			n.parent = this;
			n.interactible = true;
			n.inputDisabled = true;
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
