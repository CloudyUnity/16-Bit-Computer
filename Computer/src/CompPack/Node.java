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
	
	public static final Vector2 BASE_SCALE = new Vector2(15, 15);
	
	public Node(Vector2 pos, Boolean outputDisabled) {
		super(pos, BASE_SCALE, SceneBuilder.NODE, Color.black);		
		
		if (!outputDisabled) {
			drag = new NodeDragger(this);
		}		
		
		isCircle = true;
		
		Main.node.nodeList.add(this);
	}
	
	public void update() {
		
		if (drag != null && Main.mouse.selected != drag) {
			drag.position = new Vector2(worldPosition());
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
		
		if (state)
			setColor(ColorManager.RED);
		else
			setColor(ColorManager.BLACK);
	}
	
	public List<Vector2> endWirePoint() {
		
		List<Vector2> results = new ArrayList<Vector2>();
		
		if (drag == null)
			return results;			
		
		if (Main.mouse.selected == drag) {
			results.add(drag.center());
		}
		
		if (outputs.size() != 0) {
			
			for (Node output : outputs) {
				results.add(new Vector2(output.worldCenter()));
			}
		}
		
		return results;
	}
	
	protected void onClick() {

		state = !state;
	}
}
