package CompPack;

public class Main {

	public static Drawing draw;
	public static NodeManager node;
	public static MyMouseListener mouse;
	public static UpdateManager up;
	public static BlockBuilder blockBuilder;
	public static KeyboardInputs keyboard;
	
	public static void main(String[] args) {		
		
		startProgram();
	}
	
	public static void startProgram() {
		
		draw = new Drawing();	
		node = new NodeManager();
		mouse = new MyMouseListener();
		up = new UpdateManager();
		blockBuilder = new BlockBuilder();
		keyboard = new KeyboardInputs();
				
		draw.addMouseListener(mouse);
		draw.addKeyListener(keyboard);
		draw.initialise();	
		
		node.initialise(3, 1);
		
		SaveManager.initialise();
		
		SceneBuilder.initialise();	
				
		up.update();			
		
		InputField.initialise();
	}
	
	public static void restartProgram() {
		
		up.endTimer();
		draw.closeFrame();
		draw.removeAll();
		
		startProgram();
	}
}
