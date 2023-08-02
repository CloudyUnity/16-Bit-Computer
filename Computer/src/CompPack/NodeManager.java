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
		
		for (int i = 0; i < inputNodeCount; i++) {
			nodeListInput.get(i).position = inputPos(i);
		}
		
		for (int i = 0; i < nodeListOutput.size(); i++) {
			nodeListOutput.get(i).position = outputPos(i);
		}
	}

	public void addInput() {
		
		inputNodeCount++;	

		makeInput(inputNodeCount - 1);
	}

	public void removeInput() {
		
		if (inputNodeCount <= 1)
			return;
		
		inputNodeCount--;

		Node remove = nodeListInput.get(nodeListInput.size() - 1);
		nodeListInput.remove(remove);
		nodeList.remove(remove);
		Main.draw.shapeList.remove(remove);
	}

	public void addOutput() {

		outputNodeCount++;

		makeOutput(outputNodeCount - 1);
	}

	public void removeOutput() {

		if (outputNodeCount <= 1)
			return;
		
		outputNodeCount--;

		Node remove = nodeListOutput.get(nodeListOutput.size() - 1);
		nodeListOutput.remove(remove);
		nodeList.remove(remove);
		Main.draw.shapeList.remove(remove);
		
		
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
		
		Vector2 screenSize = Main.draw.getWindowSize();
		
		int max = (int)(screenSize.y / 31.25f);
		
		int column = (int)Math.floor(i / max);
		int spacing = ((int)screenSize.y - 125) / (int)Extensions.clamp(inputNodeCount, 1, max);		
		
		int x = 80 - column * 30;		
		int y = (int)screenSize.y - 75 - spacing * (i - column * max);
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
		
		Vector2 screenSize = Main.draw.getWindowSize();
		
		int max = (int)(screenSize.y / 31.25f);
		
		int column = (int)Math.floor(i / max);
		int spacing = ((int)screenSize.y - 125) / (int)Extensions.clamp(outputNodeCount, 1, max);
		
		int x = (int)screenSize.x - 100 + column * 30;		
		int y = (int)screenSize.y - 75 - spacing * (i - column * max);
		y -= 8 * column;
		return new Vector2(x, y);
	}
}
