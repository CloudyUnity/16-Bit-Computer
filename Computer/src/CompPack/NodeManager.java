package CompPack;

import java.util.ArrayList;
import java.util.List;

public class NodeManager {

	public List<Node> nodeList = new ArrayList<Node>();
	public List<Node> nodeListInput = new ArrayList<Node>();
	public List<Node> nodeListOutput = new ArrayList<Node>();
	public List<GateAND> andList = new ArrayList<GateAND>();
	public List<GateNOT> notList = new ArrayList<GateNOT>();

	public int inputNodeCount;
	public int outputNodeCount;
	
	public static final Vector2 IN_OUT_BASE_SCALE = new Vector2(20, 20);

	public void initialise(int inputCount, int outputCount) {

		inputNodeCount = inputCount;
		outputNodeCount = outputCount;

		int iSpacing = 400 / inputCount;
		for (int i = 0; i < inputCount; i++) {

			Node n = new Node(new Vector2(50, 400 - iSpacing * i), false);
			n.layer = 55;
			n.interactible = true;
			n.inputDisabled = true;
			n.isInput = true;
			n.scale = IN_OUT_BASE_SCALE;
			n.text = "Input";
			nodeListInput.add(n);
		}

		int oSpacing = 400 / outputCount;
		for (int i = 0; i < outputCount; i++) {

			Node n = new Node(new Vector2(925, 400 - i * oSpacing), true);
			n.layer = 55;
			n.isOutput = true;
			n.scale = IN_OUT_BASE_SCALE;
			nodeListOutput.add(n);
		}
	}

	public Node FindClosestNodeTo(Vector2 vec, Node self) {

		double dis = 10;
		Node node = null;

		for (Node n : nodeList) {

			if (n == self || n.inputDisabled)
				continue;

			Vector2 pos = new Vector2(n.position);
			if (n.parent != null)
				pos = pos.add(n.parent.position);

			double newDis = vec.distance(pos);
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
		
		if (inputNodeCount >= 15)
			return;

		inputNodeCount++;

		int spacing = 400 / inputNodeCount;
		for (int i = 0; i < inputNodeCount - 1; i++) {

			nodeListInput.get(i).position.y = 400 - i * spacing;
		}

		Node n = new Node(new Vector2(50, spacing), false);
		n.layer = 55;
		n.interactible = true;
		n.scale = IN_OUT_BASE_SCALE;
		n.inputDisabled = true;
		n.isInput = true;
		nodeListInput.add(n);
	}

	public void removeInput() {
		
		if (inputNodeCount <= 1)
			return;
		
		inputNodeCount--;

		Node remove = nodeListInput.get(nodeListInput.size() - 1);
		nodeListInput.remove(remove);
		nodeList.remove(remove);
		Main.draw.shapeList.remove(remove);

		int spacing = 400 / inputNodeCount;
		for (int i = 0; i < nodeListInput.size(); i++) {

			nodeListInput.get(i).position.y = 400 - i * spacing;
		}
	}

	public void addOutput() {

		if (outputNodeCount >= 15)
			return;
		
		outputNodeCount++;

		int spacing = 400 / outputNodeCount;
		for (int i = 0; i < outputNodeCount - 1; i++) {

			nodeListOutput.get(i).position.y = 400 - i * spacing;
		}

		Node n = new Node(new Vector2(925, spacing), true);
		n.layer = 55;
		n.scale = IN_OUT_BASE_SCALE;
		n.isOutput = true;
		nodeListOutput.add(n);
	}

	public void removeOutput() {

		if (outputNodeCount <= 1)
			return;
		
		outputNodeCount--;

		Node remove = nodeListOutput.get(nodeListOutput.size() - 1);
		nodeListOutput.remove(remove);
		nodeList.remove(remove);
		Main.draw.shapeList.remove(remove);

		int spacing = 400 / outputNodeCount;
		for (int i = 0; i < nodeListOutput.size(); i++) {

			nodeListOutput.get(i).position.y = 400 - i * spacing;
		}
	}
}
