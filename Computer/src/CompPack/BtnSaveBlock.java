package CompPack;

import java.util.ArrayList;
import java.util.List;

public class BtnSaveBlock extends Shape {

	public BtnSaveBlock(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.PURPLE);
		
		interactible = true;
	}

	@Override
	protected void onMouseRelease(double dur) {

		if (dur > 200)
			return;
		
		Block block = new Block();

		block.nodes = Main.node.nodeList.size();

		for (GateAND gate : Main.node.andList) {

			if (!Main.node.nodeList.contains(gate.input1))
				continue;
			
			DataAND and = new DataAND();
			and.input1 = Main.node.nodeList.indexOf(gate.input1);
			and.input2 = Main.node.nodeList.indexOf(gate.input2);
			and.output = Main.node.nodeList.indexOf(gate.output);
			block.andList.add(and);
		}

		for (GateNOT gate : Main.node.notList) {

			if (!Main.node.nodeList.contains(gate.input))
				continue;
			
			DataNOT not = new DataNOT();
			not.input = Main.node.nodeList.indexOf(gate.input);
			not.output = Main.node.nodeList.indexOf(gate.output);
			block.notList.add(not);
		}
		
		for (GateInBus gate : Main.node.inList) {

			if (!Main.node.nodeList.contains(gate.output))
				continue;
			
			DataINBUS in = new DataINBUS();
			
			List<Integer> inputs = new ArrayList<Integer>();
			for (int i = 0; i < gate.inputs.length; i++) {
				int index = Main.node.nodeList.indexOf(gate.inputs[i]);
				inputs.add(index);
			}
			in.inputs = inputs;
			in.output = Main.node.nodeList.indexOf(gate.output);
			block.inList.add(in);
		}
		
		for (GateOutBus gate : Main.node.outList) {

			if (!Main.node.nodeList.contains(gate.input))
				continue;
			
			DataOUTBUS out = new DataOUTBUS();
			
			List<Integer> outputs = new ArrayList<Integer>();
			for (int i = 0; i < gate.outputs.length; i++) {
				int index = Main.node.nodeList.indexOf(gate.outputs[i]);
				outputs.add(index);
			}
			out.outputs = outputs;
			out.input = Main.node.nodeList.indexOf(gate.input);
			block.outList.add(out);
		}
		
		for (Node n : Main.node.nodeList) {
			
			int a = Main.node.nodeList.indexOf(n);
			if (n.isInput)
				block.inputs.add(a);
			if (n.isOutput)
				block.outputs.add(a);
			
			for (Node out : n.outputs) {
				int b = Main.node.nodeList.indexOf(out);
				block.connections.add(new Vector2(a, b));
			}
		}
		
		for (int i = 0; i < Main.node.nodeList.size(); i++) {
			block.states.add(Main.node.nodeList.get(i).state.length);
		}

		SaveManager.saveBlock(block);
		Main.restartProgram();
	}
}
