package CompPack;

import javax.swing.Timer;

public class UpdateManager {

	final int FRAME_DELAY = 5;

	Timer timer;

	public Boolean paused = false;

	Shape pausedSymbol;
	Shape fps;

	public void initialise() {

		pausedSymbol = new Shape(new Vector2(75, 25), new Vector2(50, 50), SceneBuilder.BUTTON, ColorManager.GREEN);
		pausedSymbol.relativeToWidth = true;	
		pausedSymbol.isCircle = true;
		
		fps = new Shape(new Vector2(550, 10), new Vector2(75, 30), SceneBuilder.BUTTON, ColorManager.GREEN);
		
		update();
	}

	public void endTimer() {
		if (timer != null)
			timer.stop();
	}

	public void update() {

		timer = new Timer(FRAME_DELAY, e -> {

			Long timeBefore = System.currentTimeMillis();			
			
			ProgramLogicGateBuilder.mouse.update();

			ProgramLogicGateBuilder.keyboard.update();

			InputField.update();

			pausedSymbol.setColor(paused ? ColorManager.RED : ColorManager.GREEN);
			pausedSymbol.text = paused ? "Updating: Off" : "Updating: On";

			if (!paused) {

				ProgramLogicGateBuilder.node.update();

				for (Shape s : ProgramLogicGateBuilder.draw.shapeList) {
					s.update();
				}
			}

			ProgramLogicGateBuilder.draw.revalidate();
			ProgramLogicGateBuilder.draw.repaint();
			
			double timeTaken = System.currentTimeMillis() - timeBefore;
			timeTaken *= 0.001;
			timeTaken = Extensions.clamp(1 / timeTaken, 0, 60);
			timeTaken = Math.round(timeTaken);
			fps.text = "FPS: " + timeTaken;
		});

		timer.start();
	}
}
