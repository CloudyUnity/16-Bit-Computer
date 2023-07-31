package CompPack;

import java.awt.Color;

public class SceneBuilder {

	// SCREEN SIZE = (1000, 500)
	public static void initialise() {
		
		// Decor
		Shape temp = new Shape(new Vector2(0, 0), new Vector2(100, 3000), Color.gray);
		temp.layer = -10;
		
		temp = new Shape(new Vector2(900, 0), new Vector2(100, 3000), Color.gray);
		temp.layer = -10;
		
		temp = new Shape(new Vector2(0, 450), new Vector2(3000, 100), Color.gray);
		temp.layer = -10;
		
		// Buttons		
		BtnChangeNodes btn = new BtnChangeNodes(new Vector2(60, 450), new Vector2(20, 20), Color.lightGray, false, true);
		btn.layer = 60;
		btn.text = "+";
		
		btn = new BtnChangeNodes(new Vector2(20, 450), new Vector2(20, 20), Color.lightGray, true, true);
		btn.layer = 60;
		btn.text = "-";
		
		btn = new BtnChangeNodes(new Vector2(960, 450), new Vector2(20, 20), Color.lightGray, false, false);
		btn.layer = 60;
		btn.text = "+";
		
		btn = new BtnChangeNodes(new Vector2(920, 450), new Vector2(20, 20), Color.lightGray, true, false);
		btn.layer = 60;
		btn.text = "-";
		
		BtnSaveBlock save = new BtnSaveBlock(new Vector2(100, 460), new Vector2(50, 25));
		save.layer = 100;
		save.text = "Save";
		
		BtnRemoveBlock remove = new BtnRemoveBlock(new Vector2(530, 460), new Vector2(50, 25));
		remove.layer = 100;
		remove.text = "Delete";
		
		BtnANDMaker andMake = new BtnANDMaker(new Vector2(155, 460), new Vector2(50, 25));
		andMake.layer = 100;
		andMake.text = "AND";
		
		BtnNOTMaker notMake = new BtnNOTMaker(new Vector2(210, 460), new Vector2(50, 25));
		notMake.layer = 100;
		notMake.text = "NOT";
		
		BtnBlockMaker blockMake = new BtnBlockMaker(new Vector2(265, 460), new Vector2(50, 25));
		blockMake.layer = 100;
		blockMake.text = "Get";
		
		BtnRestart restart = new BtnRestart(new Vector2(585, 460), new Vector2(50, 25));
		restart.layer = 100;
		restart.text = "Restart";
		
		BtnFreeNodeMaker freeNode = new BtnFreeNodeMaker(new Vector2(640, 460), new Vector2(50, 25));
		freeNode.layer = 100;
		freeNode.text = "Node";
	}
}
