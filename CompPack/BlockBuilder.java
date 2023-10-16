package CompPack;

import java.util.ArrayList;
import java.util.List;

public class BlockBuilder {
	
	public static Boolean DEBUG_ON = false;

	public static void makeBlock(Block block, List<Node> inputs, List<Node> outputs) {

		Node[] nodeList = new Node[block.nodes];

		int iC = 0;
		int oC = 0;
		
		List<Integer> singleOutputs = new ArrayList<Integer>();
		List<Integer> sO = new ArrayList<Integer>();
		List<Integer> noOptimize = new ArrayList<Integer>();
		
		long timeBefore = System.currentTimeMillis();	
		
		Boolean optimizeOn = false;
		
		if (optimizeOn) {
			
			for (Vector2 con : block.connections) {

				if (con.x == -1 || con.y == -1)
					continue;
				
				int x = (int)con.x;
				int y = (int)con.y;
				
				if (singleOutputs.contains(x)) {
					
					if (noOptimize.contains(x))
						continue;
					
					int i = singleOutputs.indexOf(x);
					singleOutputs.remove(i);
					sO.remove(i);
					noOptimize.add(x);
					continue;
				}
				
				if (!singleOutputs.contains(y) && !block.outputs.contains(y) && !noOptimize.contains(x)){
					singleOutputs.add(x);
					sO.add(y);
				}				
			}
			
			String timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
			if (DEBUG_ON)
				System.out.println("CON: " + timeTaken);
		}
				
		timeBefore = System.currentTimeMillis();		

		for (int i = 0; i < block.nodes; i++) {
					
			if (optimizeOn && sO.contains(i)) {	
				continue;
			}
			
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

			Node n = new Node(block.states.get(i));
			n.visible = false;
			nodeList[i] = n;
		}			
		
		String timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		if (DEBUG_ON)
			System.out.println("Nodes: " + timeTaken + " , CurrentSize = " + ProgramLogicGateBuilder.node.linkedListSize);
		timeBefore = System.currentTimeMillis();
		
		for (int i = 0; i < sO.size(); i++) {
			
			int x = singleOutputs.get(i);
			nodeList[sO.get(i)] = nodeList[x];
		}
		
		timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		if (DEBUG_ON)
			System.out.println("sO: " + timeTaken);
		timeBefore = System.currentTimeMillis();
		
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
		
		if (block.romList != null) {
			for (DataROM rom : block.romList) {
				if (rom.output == -1)
					continue;

				Node i = nodeList[rom.input];
				Node o = nodeList[rom.output];
				new GateROM(i, o);
			}
		}		

		for (DataINBUS in : block.inList) {

			if (in.output == -1)
				continue;

			List<Node> iList = new ArrayList<Node>();
			for (int j = 0; j < in.inputs.size(); j++) {
				iList.add(nodeList[in.inputs.get(j)]);
			}
			
			Node o = nodeList[in.output];
			new GateInBus(iList, o);
		}
		
		for (DataOUTBUS out : block.outList) {

			if (out.input == -1)
				continue;

			List<Node> oList = new ArrayList<Node>();
			for (int j = 0; j < out.outputs.size(); j++) {
				oList.add(nodeList[out.outputs.get(j)]);
			}
			
			Node i = nodeList[out.input];
			new GateOutBus(i, oList);
		}	
		
		timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		if (DEBUG_ON)
			System.out.println("Gates: " + timeTaken);
		timeBefore = System.currentTimeMillis();
		
		for (Vector2 con : block.connections) {

			if (con.x == -1 || con.y == -1 || singleOutputs.contains((int)con.x))
				continue;
			
			Node a = nodeList[(int) con.x];
			Node b = nodeList[(int) con.y];
			a.outputs.add(b);
			//b.inputNode = a;	
		}
		
		timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		if (DEBUG_ON)
			System.out.println("CON2: " + timeTaken);
		timeBefore = System.currentTimeMillis();
	}
}
