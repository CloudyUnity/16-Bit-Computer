package CompPack;

import java.awt.Color;

public class GateAND extends Shape{

	public Node input1;
	public Node input2;
	public Node output;
	
	public GateAND(Vector2 pos) {
		super(pos, new Vector2(50, 25), Color.LIGHT_GRAY);
		Main.node.andList.add(this);
		
		text = "AND";
		draggable = true;
		interactible = true;
		deletable = true;
		
		input1 = new Node(new Vector2(-Node.BASE_SCALE.x, 0), true);
		input1.layer = 55;
		input1.parent = this;
		
		input2 = new Node(new Vector2(-Node.BASE_SCALE.x, scale.y * 0.5f), true);
		input2.layer = 55;
		input2.parent = this;
		
		output = new Node(new Vector2(scale.x, scale.y * 0.25f), false);
		output.layer = 55;
		output.parent = this;
		output.interactible = true;
		output.inputDisabled = true;
	}
	
	public GateAND(Node i1, Node i2, Node o) {
		super(Vector2.zero, Vector2.zero, Color.LIGHT_GRAY);
		Main.node.andList.add(this);
		
		visible = false;
		
		input1 = i1;
		input2 = i2;
		output = o;
	}
	
	@Override
	protected void update() {
		
		// Gate Delay
		if (Math.random() < 0.1f)
			return;
		
		output.state = input1.state && input2.state;
	}
	
	@Override
	protected void onMouse3Pressed() {
		super.onMouse3Pressed();
		
		Main.node.andList.remove(this);
		
		input1.deactivated = true;
		input2.deactivated = true;
		output.deactivated = true;
		
		Main.node.nodeList.remove(input1);
		Main.node.nodeList.remove(input2);
		Main.node.nodeList.remove(output);
		
		Main.draw.shapeList.remove(input1);
		Main.draw.shapeList.remove(input2);
		Main.draw.shapeList.remove(output);
	}
}
