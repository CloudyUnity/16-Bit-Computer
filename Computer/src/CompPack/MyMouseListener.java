package CompPack;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

public class MyMouseListener implements MouseListener {

	public static Shape selected;

	public Vector2 mousePos;
	
	double lastPressTime = 0;

	public void update() {

		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mousePoint, Main.draw);
		mousePos = new Vector2(mousePoint.x, mousePoint.y);
		
		if (selected == null || !selected.draggable)
			return;
		
		drag();
	}
	
	public void drag() {

		selected.moveCenterTo(mousePos);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		lastPressTime = System.currentTimeMillis();
		
		selected = Main.draw.closestInteract();
		if (selected != null) {
			if (e.getButton() == MouseEvent.BUTTON1)
				selected.onMousePressed(e);
			else if (e.getButton() == MouseEvent.BUTTON3)
				selected.onMouse3Pressed();
		}			
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		double clickDur = System.currentTimeMillis() - lastPressTime;
		
		if (selected == null)
			return;
		
		selected.onMouseRelease(clickDur);
		selected = null;
	}
	
	public void setSelected(Shape shape) {
		selected = shape;
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
