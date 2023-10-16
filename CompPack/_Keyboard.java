package CompPack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class _Keyboard implements KeyListener{

	int currentKey = 0; 
	
	_Computer comp;
	
	public static final int KEYBOARD_RAM = 24576;
	
	public void initialise() {
		comp = _ProgramAssemblyBuilder.comp;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		currentKey = e.getKeyCode();	
		comp.RAM[KEYBOARD_RAM] = (short)currentKey;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		currentKey = 0;
		comp.RAM[KEYBOARD_RAM] = 0;
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
