package CompPack;

import java.util.ArrayList;
import java.util.List;

public class Node extends Shape {

	List<Node> outputs = new ArrayList<Node>();

	public Node prevNode;
	public Node nextNode;

	public Boolean[] state;
	
	public Node(Vector2 pos, Vector2 scale, int layer, String color) {
		super(pos, scale, layer, color);
	}

	public Node(int size) {
		super();
		
		visible = false;

		state = new Boolean[size];
		for (int i = 0; i < size; i++) {
			state[i] = false;
		}

		ProgramLogicGateBuilder.node.addNode(this);
	}
	
	public void changeState(Boolean[] states) {
		
		for (int i = 0; i < states.length; i++)
			state[i] = states[i];
		checkOutputs();
	}

	public void changeState(int j, Boolean newState) {

		state[j] = newState;
		
		checkOutputs();
	}
	
	public void checkOutputs() {
		
		for (int i = outputs.size() - 1; i >= 0; i--) {						

			Node output = outputs.get(i);
			
			Boolean equal = true;
			for (int j = 0; j < output.state.length; j++) {
				if (output.state[j] != state[j]) {
					equal = false;
					break;
				}
			}
			if (equal)
				continue;
			
			output.changeState(state);
		}
	}
}
