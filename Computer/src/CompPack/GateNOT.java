package CompPack;

import java.awt.Color;

public class GateNOT extends Shape{

	public Node input;
	public Node output;
	
	public GateNOT(Vector2 pos) {
		super(pos, new Vector2(50, 25), 50, ColorManager.ORANGE);
		Main.node.notList.add(this);
		
		text = "NOT";
		draggable = true;
		interactible = true;
		deletable = true;
		
		input = new Node(new Vector2(-Node.BASE_SCALE.x, scale.y * 0.25f), true);
		input.parent = this;
		
		output = new Node(new Vector2(50, scale.y * 0.25f), false);
		output.parent = this;
		output.interactible = true;
		output.inputDisabled = true;
	}
	
	public GateNOT(Node i, Node o) {
		super(Vector2.zero, Vector2.zero, -999999, Color.LIGHT_GRAY);
		Main.node.notList.add(this);
		
		visible = false;
		
		input = i;
		output = o;
	}
	
	@Override
	protected void update() {
		
		output.state = !input.state;
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		Main.node.notList.remove(this);
		
		input.deactivated = true;
		output.deactivated = true;
		
		Main.node.nodeList.remove(input);
		Main.node.nodeList.remove(output);
		Main.draw.shapeList.remove(input);
		Main.draw.shapeList.remove(output);
	}
}
