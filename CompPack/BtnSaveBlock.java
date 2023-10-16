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
		
		Long timeBefore = System.currentTimeMillis();			
		
		Block block = new Block();

		block.nodes = ProgramLogicGateBuilder.node.linkedListSize;
		
		ProgramLogicGateBuilder.node.initialiseHashMap();
		
		System.out.println("1: Saving gates");

		for (GateAND gate : ProgramLogicGateBuilder.node.andList) {

			int i = ProgramLogicGateBuilder.node.indexOf(gate.input1);
			if (i == -1)
				continue;
			
			DataAND and = new DataAND();
			and.input1 = i;
			and.input2 = ProgramLogicGateBuilder.node.indexOf(gate.input2);
			and.output = ProgramLogicGateBuilder.node.indexOf(gate.output);
			block.andList.add(and);
		}

		for (GateNOT gate : ProgramLogicGateBuilder.node.notList) {

			int i = ProgramLogicGateBuilder.node.indexOf(gate.input);
			if (i == -1)
				continue;
			
			DataNOT not = new DataNOT();
			not.input = i;
			not.output = ProgramLogicGateBuilder.node.indexOf(gate.output);
			block.notList.add(not);
		}
		
		for (GateROM gate : ProgramLogicGateBuilder.node.romList) {

			int i = ProgramLogicGateBuilder.node.indexOf(gate.input);
			if (i == -1)
				continue;
			
			DataROM rom = new DataROM();
			rom.input = i;
			rom.output = ProgramLogicGateBuilder.node.indexOf(gate.output);
			block.romList.add(rom);
		}
		
		for (GateInBus gate : ProgramLogicGateBuilder.node.inList) {

			int oI = ProgramLogicGateBuilder.node.indexOf(gate.output);
			if (oI == -1)
				continue;
			
			DataINBUS in = new DataINBUS();
			
			List<Integer> inputs = new ArrayList<Integer>();
			for (int i = 0; i < gate.inputs.length; i++) {
				int index = ProgramLogicGateBuilder.node.indexOf(gate.inputs[i]);
				inputs.add(index);
			}
			in.inputs = inputs;
			in.output = oI;
			block.inList.add(in);
		}
		
		for (GateOutBus gate : ProgramLogicGateBuilder.node.outList) {

			int oI = ProgramLogicGateBuilder.node.indexOf(gate.input);
			if (oI == -1)
				continue;
			
			DataOUTBUS out = new DataOUTBUS();
			
			List<Integer> outputs = new ArrayList<Integer>();
			for (int i = 0; i < gate.outputs.length; i++) {
				int index = ProgramLogicGateBuilder.node.indexOf(gate.outputs[i]);
				outputs.add(index);
			}
			out.outputs = outputs;
			out.input = oI;
			block.outList.add(out);
		}
		
		String timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		System.out.println("Time taken: " + timeTaken);
		timeBefore = System.currentTimeMillis();	
		
		System.out.println("2: Saving nodes");
		
		Node n = ProgramLogicGateBuilder.node.startNode;
		int i = 0;
		while (n != null) {
			
			if (n.visible) {
				NodeVisible vis = (NodeVisible)n;
				if (vis.isInput)
					block.inputs.add(i);
				if (vis.isOutput)
					block.outputs.add(i);
			}			
			
			for (Node out : n.outputs) {
				int b = ProgramLogicGateBuilder.node.indexOf(out);
				block.connections.add(new Vector2(i, b));
			}
			
			block.states.add(n.state.length);
			
			n = n.nextNode;
			i++;
		}

		SaveManager.saveAsBlock(block);
		
		timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		System.out.println("New block saved in: " + timeTaken);		
		
		ProgramLogicGateBuilder.restartProgram();
	}
}
