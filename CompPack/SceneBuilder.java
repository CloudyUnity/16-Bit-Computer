package CompPack;

public class SceneBuilder {

	public static final int INVISIBLE = -9999;	
	public static final int BG = -999;
	public static final int BLOCK = 30;
	public static final int WIRE = 40;
	public static final int NODE = 50;
	public static final int HUD = 150;
	public static final int HUD_NODE = 175;
	public static final int BUTTON = 200;
	public static final int SELECTED = 300;
	
	public static Shape scene;
	
	public static Shape getScene() {
		
		if (scene == null) {
			scene = new Shape(new Vector2(500, 250), Vector2.zero, INVISIBLE, ColorManager.BLACK);
		}			
		
		return scene;
	}
	
	// SCREEN SIZE = (1000, 500)
	public static void initialise() {
		
		// Left BG
		Shape s = new Shape(new Vector2(0, 0), new Vector2(100, 3000), HUD, ColorManager.DARK_BLUE);
		
		// Up BG
		new Shape(new Vector2(0, 0), new Vector2(3000, 50), HUD, ColorManager.DARK_BLUE);
		
		// Right BG
		s = new Shape(new Vector2(100, 0), new Vector2(3000, 3000), HUD, ColorManager.DARK_BLUE);
		s.relativeToWidth = true;
		
		// Down BG
		s = new Shape(new Vector2(0, 50), new Vector2(3000, 3000), HUD, ColorManager.DARK_BLUE);
		s.relativeToHeight = true;
		
		// BG
		new Shape(Vector2.zero, new Vector2(3000, 3000), BG, ColorManager.LIGHT_BLUE);
		
		s = new Shape(new Vector2(100, 50), new Vector2(201, 101), BG + 1, ColorManager.WHITE);
		s.filled = false;
		s.relativeToWidthScale = true;
		s.relativeToHeightScale = true;
		
		// Buttons		
		BtnChangeNodes btn = new BtnChangeNodes(new Vector2(60, 50), new Vector2(20, 20), BUTTON, false, true);
		btn.text = "+";
		btn.relativeToHeight = true;
		
		btn = new BtnChangeNodes(new Vector2(20, 50), new Vector2(20, 20), BUTTON, true, true);
		btn.text = "-";
		btn.relativeToHeight = true;
		
		btn = new BtnChangeNodes(new Vector2(40, 50), new Vector2(20, 20), BUTTON, false, false);
		btn.text = "+";
		btn.relativeToHeight = true;
		btn.relativeToWidth = true;
		
		btn = new BtnChangeNodes(new Vector2(80, 50), new Vector2(20, 20), BUTTON, true, false);
		btn.text = "-";
		btn.relativeToHeight = true;
		btn.relativeToWidth = true;
		
		BtnSaveBlock save = new BtnSaveBlock(new Vector2(100, 40), new Vector2(50, 25), BUTTON);
		save.text = "Save";
		save.relativeToHeight = true;
		
		BtnRemoveBlock remove = new BtnRemoveBlock(new Vector2(530, 40), new Vector2(50, 25), BUTTON);
		remove.text = "Delete";
		remove.relativeToHeight = true;
		
		BtnANDMaker andMake = new BtnANDMaker(new Vector2(155, 40), new Vector2(50, 25), BUTTON);
		andMake.text = "AND";
		andMake.relativeToHeight = true;
		
		BtnNOTMaker notMake = new BtnNOTMaker(new Vector2(210, 40), new Vector2(50, 25), BUTTON);
		notMake.text = "NOT";
		notMake.relativeToHeight = true;
		
		BtnBlockMaker blockMake = new BtnBlockMaker(new Vector2(265, 40), new Vector2(50, 25), BUTTON);
		blockMake.text = "Get";
		blockMake.relativeToHeight = true;
		
		BtnRestart restart = new BtnRestart(new Vector2(585, 40), new Vector2(50, 25), BUTTON);
		restart.text = "Restart";
		restart.relativeToHeight = true;
		
		/*
		BtnFreeNodeMaker freeNode = new BtnFreeNodeMaker(new Vector2(640, 40), new Vector2(50, 25), BUTTON);
		freeNode.text = "Node";
		freeNode.relativeToHeight = true;
		*/
	}
}
