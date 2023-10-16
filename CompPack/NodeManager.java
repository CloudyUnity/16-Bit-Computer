package CompPack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeManager {

	public Node startNode = null;
	public Node endNode = null;
	public int linkedListSize = 0;
	public HashMap<Node, Integer> hashMap;
	
	public List<Node> nodeListInput = new ArrayList<Node>();
	public List<Node> nodeListOutput = new ArrayList<Node>();
	public List<GateAND> andList = new ArrayList<GateAND>();
	public List<GateNOT> notList = new ArrayList<GateNOT>();
	public List<GateInBus> inList = new ArrayList<GateInBus>();
	public List<GateOutBus> outList = new ArrayList<GateOutBus>();	
	public List<GateROM> romList = new ArrayList<GateROM>();

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
	
	public void addNode(Node n) {
				
		linkedListSize++;
		
		if (startNode == null) {
			startNode = n;
			endNode = n;
			return;
		}
		
		endNode.nextNode = n;
		n.prevNode = endNode;
		endNode = n;			
	}
	
	public void removeNode(Node n) {
		
		linkedListSize--;
		
		if (n.prevNode != null)
			n.prevNode.nextNode = n.nextNode;
		
		if (n.nextNode != null)
			n.nextNode.prevNode = n.prevNode;	
		
		if (n == startNode)
			startNode = n.nextNode;
		
		if (n == endNode)
			endNode = n.prevNode;
	}
	
	public void initialiseHashMap() {
		hashMap = new HashMap<Node, Integer>();
		
		Node m = startNode;
		int c = 0;
		
		while (m != null) {
			
			hashMap.put(m, c);
			
			c++;
			m = m.nextNode;
		}
	}
	
	public int indexOf(Node n) {
		
		if (hashMap != null && hashMap.containsKey(n)) {
			return hashMap.get(n);
		}
		
		Node m = startNode;
		int c = 0;
		
		while (m != null) {
			
			if (m == n)
				return c;
			c++;
			m = m.nextNode;
		}
		
		return -1;
	}

	public NodeVisible FindClosestNodeTo(Vector2 vec, Node self) {

		double dis = 10;
		NodeVisible node = null;

		Node n = startNode;
		while (n != null) {

			if (!n.visible || n == self) {
				n = n.nextNode;
				continue;
			}
			
			NodeVisible v = (NodeVisible)n;
			if (v.inputDisabled) {
				n = n.nextNode;
				continue;
			}

			double newDis = vec.distance(v.worldPosition());
			if (newDis < dis) {
				dis = newDis;
				node = v;
			}
			
			n = n.nextNode;
		}

		return node;
	}

	public void update() {

		/*
		Node n = startNode;
		while (n != null) {
			n.update();
			n = n.nextNode;
		}
		*/
		
		for (GateAND gate : andList)
			if (!gate.visible)
				gate.update();
		
		for (GateNOT gate : notList)
			if (!gate.visible)
				gate.update();
		
		for (GateROM gate : romList)
			if (!gate.visible)
				gate.update();
		
		for (GateInBus gate : inList)
			if (!gate.visible)
				gate.update();
		
		for (GateOutBus gate : outList)
			if (!gate.visible)
				gate.update();
		
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
		removeNode(remove);		
		ProgramLogicGateBuilder.draw.shapeList.remove(remove);
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
		removeNode(remove);
		ProgramLogicGateBuilder.draw.shapeList.remove(remove);
		
		NodeVisible vis = (NodeVisible)remove;
		if (vis.inputNode != null) {
			vis.inputNode.outputs.remove(vis);
			vis.inputNode = null;
		}
	}
	
	void makeInput(int i) {
			
		NodeVisible n = new NodeVisible(inputPos(i));
		n.interactible = true;
		n.inputDisabled = true;
		n.isInput = true;
		n.scale = IN_OUT_BASE_SCALE;
		n.text = Character.toString('a' + i);		
		n.layer = SceneBuilder.HUD_NODE;
		nodeListInput.add(n);
		
		if (i > 15 && n.state.length == 1)
			n.changeState(0, true);
		
		ProgramLogicGateBuilder.draw.reSortList();
	}
	
	Vector2 inputPos(int i) {
		
		Vector2 screenSize = ProgramLogicGateBuilder.draw.getWindowSize();
		
		int max = (int)(screenSize.y / 31.25f);
		
		int column = (int)Math.floor(i / max);
		int spacing = ((int)screenSize.y - 125) / (int)Extensions.clamp(inputNodeCount, 1, max);		
		
		int x = 80 - column * 30;		
		int y = (int)screenSize.y - 75 - spacing * (i - column * max);
		y -= 8 * column;
		return new Vector2(x, y);
	}
	
	void makeOutput(int i) {
		
		NodeVisible n = new NodeVisible(outputPos(i));
		n.isOutput = true;
		n.interactible = true;
		n.scale = IN_OUT_BASE_SCALE;
		n.text = "" + i;
		n.layer = SceneBuilder.HUD_NODE;
		nodeListOutput.add(n);
		
		ProgramLogicGateBuilder.draw.reSortList();
	}
	
	Vector2 outputPos(int i) {
		
		Vector2 screenSize = ProgramLogicGateBuilder.draw.getWindowSize();
		
		int max = (int)(screenSize.y / 31.25f);
		
		int column = (int)Math.floor(i / max);
		int spacing = ((int)screenSize.y - 125) / (int)Extensions.clamp(outputNodeCount, 1, max);
		
		int x = (int)screenSize.x - 100 + column * 30;		
		int y = (int)screenSize.y - 75 - spacing * (i - column * max);
		y -= 8 * column;
		return new Vector2(x, y);
	}
}
