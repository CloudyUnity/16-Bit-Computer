package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class Shape {

	public Shape parent;
	
	public Color color;
	public Vector2 position;
	public Vector2 scale;
	
	public Boolean visible = true;
	public Boolean isCircle = false;
	public Boolean filled = true;
	public Boolean draggable = false;
	public Boolean interactible = false;
	public Boolean deletable = false;
	
	public String text = "";
	
	public int layer = 0;

	public Vector2 max() {
		return position.add(scale);
	}

	public Vector2 min() {
		return position;
	}
	
	public Vector2 center() {
		return position.add(scale.mult(0.5f));
	}

	public Shape(Vector2 pos, Vector2 scale, Color color) {
		this.position = pos;
		this.scale = scale;
		this.color = color;
		
		Main.draw.addToList(this);
	}

	public Boolean contains(Vector2 pos) {

		if (parent != null)
			pos = pos.add(parent.position);
		
		if (isCircle) {

			double powX = Math.pow(pos.x - position.x, 2);
			double powY = Math.pow(pos.y - position.y, 2);
			return powX + powY <= scale.x * scale.y * 0.25f;
		}

		Boolean withinX = pos.x <= max().x && pos.x >= min().x;
		Boolean withinY = pos.y <= max().y && pos.y >= min().y;

		return withinX && withinY;
	}

	public void moveCenterTo(Vector2 pos) {		
		position = pos.add(scale.mult(-0.5f));
	}
	
	protected void update() {}
	
	protected void onMouseRelease(double dur) {}
	
	protected void onMousePressed(MouseEvent e) {}
	
	protected void onMouse3Pressed() {
		if (!deletable)
			return;
		
		Main.draw.shapeList.remove(this);
	}
}
