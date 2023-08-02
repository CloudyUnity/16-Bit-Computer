package CompPack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Node extends Shape{
	
	List<Node> outputs = new ArrayList<Node>();
	public Node inputNode;
	
	NodeDragger drag;
	public Boolean[] state;
	public Boolean inputDisabled = false;		
	public Boolean deactivated = false;
	public Boolean isInput = false;
	public Boolean isOutput = false;
	
	public static final Vector2 BASE_SCALE = new Vector2(15, 15);
	
	public Node(Vector2 pos) {
		super(pos, BASE_SCALE, SceneBuilder.NODE, Color.black);	
		
		setStateSize(1);
		
		drag = new NodeDragger(this);
		
		isCircle = true;
		
		Main.node.nodeList.add(this);
	}
	
	public Node(Vector2 pos, int busSize) {
		super(pos, BASE_SCALE, SceneBuilder.NODE, Color.black);	
		
		setStateSize(busSize);
		
		drag = new NodeDragger(this);	
		
		isCircle = true;
		
		Main.node.nodeList.add(this);
	}
	
	public void update() {
		
		if (drag != null && Main.mouse.selected != drag) {
			drag.position = new Vector2(worldPosition());
		}	
		
		Boolean noInput = inputNode == null || inputNode.deactivated;
		if (noInput && !isInput && !inputDisabled && visible) {
			for (int i = 0; i < state.length; i++)
				state[i] = false;
		}
		
		for (int i = outputs.size() - 1; i >= 0; i--) {
			if (outputs.get(i) == null || outputs.get(i).deactivated)
				outputs.remove(i);
			else {
				for (int j = 0; j < state.length; j++)
					outputs.get(i).state[j] = state[j];
			}
		}
		
		if (state.length == 1 && state[0])
			setColor(ColorManager.RED);
		else
			setColor(ColorManager.BLACK);
	}
	
	public void setStateSize(int size) {
		outputs.clear();
		if (inputNode != null) {
			inputNode.outputs.remove(this);
			inputNode = null;
		}
		state = new Boolean[size];
		for (int i = 0; i < size; i++) {
			state[i] = false;
		}
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

		if (state.length == 1) {
			state[0] = !state[0];
			return;
		}
		
		for (int i = 0; i < state.length; i++) {
			state[i] = Math.random() < 0.5 ? true : false;
		}
	}
}
