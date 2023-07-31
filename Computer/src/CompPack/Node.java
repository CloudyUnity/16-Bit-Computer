package CompPack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Node extends Shape{
	
	List<Node> outputs = new ArrayList<Node>();
	public Node inputNode;
	
	NodeDragger drag;
	public Boolean state = false;
	public Boolean inputDisabled = false;		
	public Boolean deactivated = false;
	public Boolean isInput = false;
	public Boolean isOutput = false;
	
	public static final Color ON_COLOR = Color.red;
	public static final Color OFF_COLOR = Color.black;	
	public static final Vector2 BASE_SCALE = new Vector2(15, 15);
	
	public Node(Vector2 pos, Boolean outputDisabled) {
		super(pos, BASE_SCALE, ON_COLOR);		
		
		if (!outputDisabled) {
			drag = new NodeDragger(this);
		}		
		
		isCircle = true;
		
		Main.node.nodeList.add(this);
	}
	
	public void update() {
		
		if (drag != null && MyMouseListener.selected != drag) {
			Vector2 pos = new Vector2(position);
			if (parent != null)
				pos = pos.add(parent.position);
			drag.position = pos;
		}	
		
		Boolean noInput = inputNode == null || inputNode.deactivated;
		if (noInput && !isInput && !inputDisabled && visible)
			state = false;
		
		for (int i = outputs.size() - 1; i >= 0; i--) {
			if (outputs.get(i) == null || outputs.get(i).deactivated)
				outputs.remove(i);
			else
				outputs.get(i).state = state;
		}
		
		color = state ? ON_COLOR : OFF_COLOR;			
	}
	
	public List<Vector2> endWirePoint() {
		
		List<Vector2> results = new ArrayList<Vector2>();
		
		if (drag == null)
			return results;			
		
		if (MyMouseListener.selected == drag) {
			results.add(drag.center());
		}
		
		if (outputs.size() != 0) {
			
			for (Node output : outputs) {
				results.add(output.parent == null ? output.center() : output.center().add(output.parent.position));
			}
		}
		
		return results;
	}
	
	protected void onClick() {

		state = !state;
	}
}
