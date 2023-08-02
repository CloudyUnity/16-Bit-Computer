package CompPack;

import java.util.ArrayList;
import java.util.List;

public class NodeManager {

	public List<Node> nodeList = new ArrayList<Node>();
	public List<Node> nodeListInput = new ArrayList<Node>();
	public List<Node> nodeListOutput = new ArrayList<Node>();
	public List<GateAND> andList = new ArrayList<GateAND>();
	public List<GateNOT> notList = new ArrayList<GateNOT>();
	public List<GateInBus> inList = new ArrayList<GateInBus>();
	public List<GateOutBus> outList = new ArrayList<GateOutBus>();

	public int inputNodeCount;
	public int outputNodeCount;
	
	public static final Vector2 IN_OUT_BASE_SCALE = new Vector2(20, 20);
	public static final int MAX_NODES = 48;
	public static final int DEEPEST_NODE_X = 425;
	public static final int NODE_LENGTH = 375;

	public void initialise(int inputCount, int outputCount) {

		inputNodeCount = inputCount;
		outputNodeCount = outputCount;
		
		for (int i = 0; i < inputCount; i++) {

			makeInput(i);
		}

		for (int i = 0; i < outputCount; i++) {

			makeOutput(i);
		}
	}

	public Node FindClosestNodeTo(Vector2 vec, Node self) {

		double dis = 10;
		Node node = null;

		for (Node n : nodeList) {

			if (n == self || n.inputDisabled)
				continue;

			double newDis = vec.distance(n.worldPosition());
			if (newDis < dis) {
				dis = newDis;
				node = n;
			}
		}

		return node;
	}

	public void update() {

		for (Node n : nodeList) {
			n.update();
		}
	}

	public void addInput() {
		
		if (inputNodeCount >= MAX_NODES)
			return;

		inputNodeCount++;	
		int maxNodeCount = inputNodeCount - 1;
		for (int i = 0; i < maxNodeCount; i++) {

			nodeListInput.get(i).position = inputPos(i);
		}

		makeInput(maxNodeCount);
	}

	public void removeInput() {
		
		if (inputNodeCount <= 1)
			return;
		
		inputNodeCount--;

		Node remove = nodeListInput.get(nodeListInput.size() - 1);
		nodeListInput.remove(remove);
		nodeList.remove(remove);
		Main.draw.shapeList.remove(remove);

		for (int i = 0; i < nodeListInput.size(); i++) {

			nodeListInput.get(i).position = inputPos(i);
		}
	}

	public void addOutput() {

		if (outputNodeCount >= MAX_NODES)
			return;
		
		outputNodeCount++;
		int maxNodeCount = outputNodeCount - 1;
		for (int i = 0; i < maxNodeCount; i++) {

			nodeListOutput.get(i).position = outputPos(i);
		}

		makeOutput(maxNodeCount);
	}

	public void removeOutput() {

		if (outputNodeCount <= 1)
			return;
		
		outputNodeCount--;

		Node remove = nodeListOutput.get(nodeListOutput.size() - 1);
		nodeListOutput.remove(remove);
		nodeList.remove(remove);
		Main.draw.shapeList.remove(remove);
		
		for (int i = 0; i < nodeListOutput.size(); i++) {

			nodeListOutput.get(i).position = outputPos(i);
		}
	}
	
	void makeInput(int i) {
			
		Node n = new Node(inputPos(i));
		n.interactible = true;
		n.inputDisabled = true;
		n.isInput = true;
		n.scale = IN_OUT_BASE_SCALE;
		n.text = Character.toString('a' + i);		
		n.layer = SceneBuilder.HUD_NODE;
		nodeListInput.add(n);
		
		if (i > 15 && n.state.length == 1)
			n.state[0] = true;
		
		Main.draw.reSortList();
	}
	
	Vector2 inputPos(int i) {
		int column = (int)Math.floor(i / 16);
		int spacing = NODE_LENGTH / (int)Extensions.clamp(inputNodeCount, 1, 16);
		int x = 80 - column * 30;		
		int y = DEEPEST_NODE_X - spacing * (i - column * 16);
		y -= 8 * column;
		return new Vector2(x, y);
	}
	
	void makeOutput(int i) {
		
		Node n = new Node(outputPos(i));
		n.isOutput = true;
		n.interactible = true;
		n.scale = IN_OUT_BASE_SCALE;
		n.text = "" + i;
		n.layer = SceneBuilder.HUD_NODE;
		nodeListOutput.add(n);
		
		Main.draw.reSortList();
	}
	
	Vector2 outputPos(int i) {
		int column = (int)Math.floor(i / 16);
		int spacing = NODE_LENGTH / (int)Extensions.clamp(outputNodeCount, 1, 16);
		int x = 900 + column * 30;		
		int y = DEEPEST_NODE_X - spacing * (i - column * 16);
		y -= 8 * column;
		return new Vector2(x, y);
	}
}
