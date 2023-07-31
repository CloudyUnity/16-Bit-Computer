package CompPack;

import javax.swing.Timer;

public class UpdateManager {
	
	final int FRAME_DELAY = 5;
	
	Timer timer;
	
	public void endTimer() {
		if (timer != null)
			timer.stop();
	}
	
	public void update() {
		
		timer = new Timer(FRAME_DELAY, e -> {
			
			Main.mouse.update();
			
			Main.node.update();
			
			for (Shape s : Main.draw.shapeList) {
				s.update();
			}
			
			Main.draw.revalidate();
			Main.draw.repaint();				
		});
		
		timer.start();
	}
}
