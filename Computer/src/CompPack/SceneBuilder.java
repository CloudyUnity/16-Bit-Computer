package CompPack;

import java.awt.Color;

public class SceneBuilder {

	// SCREEN SIZE = (1000, 500)
	public static void initialise() {
		
		// Decor
		new Shape(new Vector2(0, 0), new Vector2(100, 3000), -10, ColorManager.DARK_BLUE);
		
		new Shape(new Vector2(900, 0), new Vector2(100, 3000), -10, ColorManager.DARK_BLUE);		
		
		new Shape(new Vector2(0, 450), new Vector2(3000, 100), -10, ColorManager.DARK_BLUE);
		
		new Shape(Vector2.zero, new Vector2(3000, 3000), -999, ColorManager.LIGHT_BLUE);
		
		// Buttons		
		BtnChangeNodes btn = new BtnChangeNodes(new Vector2(60, 450), new Vector2(20, 20), 60, false, true);
		btn.text = "+";
		
		btn = new BtnChangeNodes(new Vector2(20, 450), new Vector2(20, 20), 60, true, true);
		btn.text = "-";
		
		btn = new BtnChangeNodes(new Vector2(960, 450), new Vector2(20, 20), 60, false, false);
		btn.text = "+";
		
		btn = new BtnChangeNodes(new Vector2(920, 450), new Vector2(20, 20), 60, true, false);
		btn.text = "-";
		
		BtnSaveBlock save = new BtnSaveBlock(new Vector2(100, 460), new Vector2(50, 25), 100);
		save.text = "Save";
		
		BtnRemoveBlock remove = new BtnRemoveBlock(new Vector2(530, 460), new Vector2(50, 25), 100);
		remove.text = "Delete";
		
		BtnANDMaker andMake = new BtnANDMaker(new Vector2(155, 460), new Vector2(50, 25), 100);
		andMake.text = "AND";
		
		BtnNOTMaker notMake = new BtnNOTMaker(new Vector2(210, 460), new Vector2(50, 25), 100);
		notMake.text = "NOT";
		
		BtnBlockMaker blockMake = new BtnBlockMaker(new Vector2(265, 460), new Vector2(50, 25), 100);
		blockMake.text = "Get";
		
		BtnRestart restart = new BtnRestart(new Vector2(585, 460), new Vector2(50, 25), 100);
		restart.text = "Restart";
		
		BtnFreeNodeMaker freeNode = new BtnFreeNodeMaker(new Vector2(640, 460), new Vector2(50, 25), 100);
		freeNode.text = "Node";
	}
}
