package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class Shape {

	public Shape parent;

	public Color color;
	public Vector2 position;
	public Vector2 scale;
	
	public Boolean relativeToWidth = false;
	public Boolean relativeToHeight = false;
	public Boolean relativeToWidthScale = false;
	public Boolean relativeToHeightScale = false;

	public Boolean visible = true;
	public Boolean isCircle = false;
	public Boolean filled = true;
	public Boolean draggable = false;
	public Boolean interactible = false;
	public Boolean deletable = false;

	public String text = "";

	public int layer = 0;

	public Shape(Vector2 pos, Vector2 scale, int layer, Color color) {
		this.position = pos;
		this.scale = scale;
		this.layer = layer;
		this.color = color;

		Main.draw.addToList(this);
	}

	public Shape(Vector2 pos, Vector2 scale, int layer, String color) {
		this.position = pos;
		this.scale = scale;
		this.layer = layer;
		this.setColor(color);

		Main.draw.addToList(this);
	}

	public void setColor(String hex) {
		this.color = ColorManager.parseColor(hex);
	}

	public Vector2 max() {
		return relativePos().add(scale);
	}

	public Vector2 min() {
		return relativePos();
	}

	public Vector2 center() {
		return relativePos().add(scale.mult(0.5f));
	}

	public Vector2 worldPosition() {

		Shape current = this;
		Vector2 pos = relativePos();

		while (current.parent != null) {
			pos = pos.add(current.parent.position);
			current = current.parent;
		}

		return pos;
	}

	public Vector2 toLocalPosition(Vector2 pos) {

		Shape current = this;

		while (current.parent != null) {
			pos = pos.add(current.parent.position.mult(-1));
			current = current.parent;
		}

		return pos;
	}

	public Vector2 worldCenter() {

		Shape current = this;
		Vector2 pos = center();

		while (current.parent != null) {
			pos = pos.add(current.parent.position);
			current = current.parent;
		}

		return pos;
	}

	public Boolean contains(Vector2 pos) {
		
		pos = toLocalPosition(new Vector2(pos));
		
		Vector2 myPos = relativePos();

		if (isCircle) {

			double powX = Math.pow(pos.x - myPos.x, 2);
			double powY = Math.pow(pos.y - myPos.y, 2);
			return powX + powY <= scale.x * scale.y * 0.25f;
		}

		Boolean withinX = pos.x <= max().x && pos.x >= min().x;
		Boolean withinY = pos.y <= max().y && pos.y >= min().y;

		return withinX && withinY;
	}
	
	public Vector2 relativePos() {
		Vector2 screenSize = Main.draw.getWindowSize();
		Vector2 pos = new Vector2(position);
		
		if (relativeToWidth)
			pos.x = screenSize.x - pos.x;
		
		if (relativeToHeight)
			pos.y = screenSize.y - pos.y;
		
		return pos;
	}

	public void moveCenterTo(Vector2 pos) {
		position = toLocalPosition(pos.add(scale.mult(-0.5f)));
	}

	protected void update() {
	}

	protected void onMouseRelease(double dur) {
	}

	protected void onMousePressed(MouseEvent e) {
	}

	protected void onMouse3Pressed() {
		if (!deletable)
			return;

		Main.draw.shapeList.remove(this);
	}
}
