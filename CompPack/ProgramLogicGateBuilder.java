package CompPack;

public class ProgramLogicGateBuilder {

	public static Drawing draw;
	public static NodeManager node;
	public static MyMouseListener mouse;
	public static UpdateManager up;
	public static BlockBuilder blockBuilder;
	public static KeyboardInputs keyboard;
	
	public static void startProgram() {
				
		Long timeBefore = System.currentTimeMillis();	
		
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
		
		up.initialise();			
		
		InputField.initialise();
		
		String timeTaken = Long.toString(System.currentTimeMillis() - timeBefore);
		System.out.println("Program initialised in: " + timeTaken);
	}
	
	public static void restartProgram() {
		
		up.endTimer();
		draw.closeFrame();
		draw.removeAll();
		
		startProgram();
	}
}
