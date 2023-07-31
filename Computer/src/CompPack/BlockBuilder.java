package CompPack;

import java.util.List;

public class BlockBuilder {

	public static void makeBlock(Block block, List<Node> inputs, List<Node> outputs) {
		
		Node[] nodeList = new Node[block.nodes];
		
		int iC = 0;
		int oC = 0;
		
		for (int i = 0; i < block.nodes; i++) {
			
			if (block.inputs.contains(i)) {
				nodeList[i] = inputs.get(iC);
				iC++;
				continue;
			}
			
			if (block.outputs.contains(i)) {
				nodeList[i] = outputs.get(oC);
				oC++;
				continue;
			}
			
			Node n = new Node(Vector2.zero, true);
			n.visible = false;
			nodeList[i] = n;
		}
		
		for (AND and : block.andList) {
			
			if (and.input1 == -1)
				continue;
			
			Node i1 = nodeList[and.input1];
			Node i2 = nodeList[and.input2];
			Node o = nodeList[and.output];
			new GateAND(i1, i2, o);
		}
		
		for (NOT not : block.notList) {
			
			if (not.input == -1)
				continue;
			
			Node i = nodeList[not.input];
			Node o = nodeList[not.output];
			new GateNOT(i, o);
		}
		
		for (Vector2 con : block.connections) {
			
			if (con.x == -1 || con.y == -1)
				continue;
			
			Node a = nodeList[(int)con.x];
			Node b = nodeList[(int)con.y];
			a.outputs.add(b);
			b.inputNode = a;
		}
	}
}
