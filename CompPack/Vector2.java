package CompPack;

import java.io.Serializable;

public class Vector2 implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public float x;
	public float y;
	
	public static Vector2 zero = new Vector2(0, 0);
	public static Vector2 one = new Vector2(1, 1);
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 b) {
		this.x = b.x;
		this.y = b.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public Boolean equals(Vector2 b) {
		return x == b.x && y == b.y;
	}
	
	public Vector2 add(Vector2 b) {
		return new Vector2(x + b.x, y + b.y);
	}
	
	public Vector2 add(int a, int b) {
		return new Vector2(x + a, y + b);
	}
	
	public Vector2 subtract(Vector2 b) {
		return new Vector2(x - b.x, y - b.y);
	}
	
	public Vector2 subtract(int a, int b) {
		return new Vector2(x - a, y - b);
	}
	
	public Vector2 mult(float b) {
		return new Vector2(x * b, y * b);
	}
	
	public double distance(Vector2 b) {
		return Math.sqrt(Math.pow(b.y - y, 2) + Math.pow(b.x - x, 2));
	}
	
	public Vector2 midPoint(Vector2 b) {
		return new Vector2(x + b.x, y + b.y).mult(0.5f);
	}
}
