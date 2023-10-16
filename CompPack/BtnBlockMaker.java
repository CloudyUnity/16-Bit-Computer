package CompPack;

import java.awt.event.MouseEvent;

public class BtnBlockMaker extends Shape{

	public BtnBlockMaker(Vector2 pos, Vector2 scale, int layer) {
		super(pos, scale, layer, ColorManager.DARKER_BLUE);
		
		interactible = true;
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		
		String txt = InputField.loadBlockName.getText();
		
		if (txt.startsWith("in")) {
			int busSize = Integer.parseInt(txt.substring(2));
			makeInBus(busSize);
			return;
		}
		if (txt.startsWith("out")) {
			int busSize = Integer.parseInt(txt.substring(3));
			makeOutBus(busSize);
			return;
		}
		
		if (txt.equals("ROM")) {
			makeROM();
			return;
		}
		
		long timeBefore = System.currentTimeMillis();
		Block block = SaveManager.loadAsBlock(txt); // !
		String timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		System.out.println("Time to load " + txt + " = " + timeTaken);
		
		if (block == null)
			return;
		
		timeBefore = System.currentTimeMillis();
		GateBlock gate = new GateBlock(block, ProgramLogicGateBuilder.mouse.mousePos, ColorManager.GREEN); // !
		timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		System.out.println("Time to make " + block.name + " = " + timeTaken);
		
		ProgramLogicGateBuilder.mouse.setSelected(gate);
	}
	
	void makeInBus(int size) {
		
		GateInBus bus = new GateInBus(ProgramLogicGateBuilder.mouse.mousePos, size);
		bus.setColor(ColorManager.PURPLE);
		ProgramLogicGateBuilder.mouse.setSelected(bus);
	}
	
	void makeOutBus(int size) {
		
		GateOutBus bus = new GateOutBus(ProgramLogicGateBuilder.mouse.mousePos, size);
		bus.setColor(ColorManager.PURPLE);
		ProgramLogicGateBuilder.mouse.setSelected(bus);
	}
	
	void makeROM() {
		GateROM rom = new GateROM(ProgramLogicGateBuilder.mouse.mousePos);
		rom.setColor(ColorManager.ORANGE);
		ProgramLogicGateBuilder.mouse.setSelected(rom);
	}
}
