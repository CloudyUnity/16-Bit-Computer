package CompPack;

public class Main {

	public static Drawing draw;
	public static NodeManager node;
	public static MyMouseListener mouse;
	public static UpdateManager up;
	public static BlockBuilder blockBuilder;
	
	public static void main(String[] args) {		
		
		startProgram();
	}
	
	public static void startProgram() {
		
		draw = new Drawing();	
		node = new NodeManager();
		mouse = new MyMouseListener();
		up = new UpdateManager();
		blockBuilder = new BlockBuilder();
				
		draw.addMouseListener(mouse);
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
