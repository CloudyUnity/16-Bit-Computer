package CompPack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class KeyboardInputs implements KeyListener {

	public List<Character> keysPressed = new ArrayList<Character>();

	public KeyboardInputs() {

	}

	public void update() {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (keysPressed.contains(e.getKeyChar()))
			return;

		keysPressed.add(e.getKeyChar());

		if (e.getKeyChar() == 'd')
			duplicateBlock();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!keysPressed.contains(e.getKeyChar()))
			return;

		Object ob = e.getKeyChar();
		keysPressed.remove(ob);

	}

	public void duplicateBlock() {

		Shape block = Main.mouse.selected;	
		
		if (block == null)
			return;

		if (block instanceof GateAND)
			block = new GateAND(Vector2.zero);

		if (block instanceof GateNOT)
			block = new GateNOT(Main.mouse.mousePos);
		
		if (block instanceof GateBlock) {
			block = new GateBlock(((GateBlock)block).block, Vector2.zero, ColorManager.GREEN);
		}
			
		
		block.moveCenterTo(Main.mouse.mousePos);
	}
}
