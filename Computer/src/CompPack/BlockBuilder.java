package CompPack;

import java.util.ArrayList;
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

			Node n = new Node(Vector2.zero, block.states.get(i));
			n.visible = false;
			nodeList[i] = n;
		}

		for (DataAND and : block.andList) {

			if (and.output == -1)
				continue;

			Node i1 = nodeList[and.input1];
			Node i2 = nodeList[and.input2];
			Node o = nodeList[and.output];
			new GateAND(i1, i2, o);
		}

		for (DataNOT not : block.notList) {

			if (not.output == -1)
				continue;

			Node i = nodeList[not.input];
			Node o = nodeList[not.output];
			new GateNOT(i, o);
		}

		for (DataINBUS in : block.inList) {

			if (in.output == -1)
				continue;

			List<Node> i = new ArrayList<Node>();
			for (int j = 0; j < in.inputs.size(); j++) {
				i.add(nodeList[in.inputs.get(j)]);
			}
			
			Node o = nodeList[in.output];
			new GateInBus(i, o);
		}
		
		for (DataOUTBUS out : block.outList) {

			if (out.input == -1)
				continue;

			List<Node> o = new ArrayList<Node>();
			for (int j = 0; j < out.outputs.size(); j++) {
				o.add(nodeList[out.outputs.get(j)]);
			}
			
			Node i = nodeList[out.input];
			new GateOutBus(i, o);
		}

		for (Vector2 con : block.connections) {

			if (con.x == -1 || con.y == -1)
				continue;

			Node a = nodeList[(int) con.x];
			Node b = nodeList[(int) con.y];
			a.outputs.add(b);
			b.inputNode = a;
		}
	}
}
