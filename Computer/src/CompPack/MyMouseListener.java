package CompPack;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

public class MyMouseListener implements MouseListener {

	public Shape selected;
	
	public Shape hovered;

	public Vector2 mousePos;
	
	double lastPressTime = 0;
	
	public int selectedDefaultLayer;
	
	public Boolean draggingScene = false;
	
	Vector2 lastMousePos = Vector2.zero;

	public void update() {

		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mousePoint, Main.draw);
		mousePos = new Vector2(mousePoint.x, mousePoint.y);
		
		hovered = Main.draw.closestInteract();
		
		if (draggingScene) {
			Vector2 dif = mousePos.subtract(lastMousePos);
			selected.position = selected.position.add(dif);
		}
		
		lastMousePos = mousePos;
		
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
				
		if (e.getButton() == MouseEvent.BUTTON2) {
			draggingScene = true;
			selected = SceneBuilder.getScene();
			return;
		}
		
		if (hovered == null)
			return;
			
		if (e.getButton() == MouseEvent.BUTTON1) {
			
			if (hovered instanceof NodeDragger) {
				if (!((NodeDragger)hovered).conNode.interactible)
					return;
			}
			
			selected = hovered;
			hovered.onMousePressed(e);	
			return;
		}
		
		if (e.getButton() == MouseEvent.BUTTON3)
			hovered.onMouse3Pressed();	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		double clickDur = System.currentTimeMillis() - lastPressTime;
		
		if (selected == null)
			return;
		
		selected.onMouseRelease(clickDur);
		selected = null;
		draggingScene = false;
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
