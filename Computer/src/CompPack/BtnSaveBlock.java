package CompPack;

import java.awt.Color;

public class BtnSaveBlock extends Shape {

	public BtnSaveBlock(Vector2 pos, Vector2 scale) {
		super(pos, scale, Color.lightGray);
		
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
			
			AND and = new AND();
			and.input1 = Main.node.nodeList.indexOf(gate.input1);
			and.input2 = Main.node.nodeList.indexOf(gate.input2);
			and.output = Main.node.nodeList.indexOf(gate.output);
			block.andList.add(and);
		}

		for (GateNOT gate : Main.node.notList) {

			if (!Main.node.nodeList.contains(gate.input))
				continue;
			
			NOT not = new NOT();
			not.input = Main.node.nodeList.indexOf(gate.input);
			not.output = Main.node.nodeList.indexOf(gate.output);
			block.notList.add(not);
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

		SaveManager.saveBlock(block);
		Main.restartProgram();
	}
}
