package CompPack;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Drawing extends JPanel {

	private static final long serialVersionUID = 1L;

	List<Shape> shapeList;

	JFrame frame;

	public void initialise() {
		frame = new JFrame("Computer");

		Dimension d = new Dimension(1000, 500);

		frame.getContentPane().setPreferredSize(d);
		frame.getContentPane().setMinimumSize(d);
		frame.getContentPane().setMaximumSize(d);

		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setDoubleBuffered(true);
		setLayout(null);

		shapeList = new ArrayList<Shape>();
	}

	public void addToList(Shape s) {

		for (int i = 0; i < shapeList.size(); i++) {

			if (shapeList.get(i).layer >= s.layer) {
				shapeList.add(i, s);
				return;
			}
		}

		shapeList.add(s);
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (shapeList == null)
			return;

		drawShapes(g);
	}

	public void drawWires(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		
		List<Integer> usedMids = new ArrayList<Integer>();

		for (Node n : new ArrayList<Node>(Main.node.nodeList)) {

			if (!n.interactible || !n.visible)
				continue;

			Vector2 start = n.worldCenter();

			List<Vector2> ends = n.endWirePoint();

			g2d.setColor(n.color);

			for (Vector2 end : ends) {
				Vector2 mid = start.midPoint(end);
				int counter = 0;
				
				while (counter < 50 && usedMids.contains((int)mid.x)) {
					mid.x += 10;
					if (mid.x > end.x - 1)
						mid.x = start.x + 1;
					counter++;
				}
				
				usedMids.add((int)mid.x);

				g2d.setStroke(new BasicStroke(5));
				g2d.drawLine((int) start.x, (int) start.y, (int) mid.x, (int) start.y);
				g2d.drawLine((int) mid.x, (int) start.y, (int) mid.x, (int) end.y);
				g2d.drawLine((int) mid.x, (int) end.y, (int) end.x, (int) end.y);
			}

		}
	}

	public void drawShapes(Graphics g) {

		Boolean drawnWires = false;
		for (Shape s : new ArrayList<Shape>(shapeList)) {

			if ((s.layer >= SceneBuilder.WIRE && !drawnWires)) {
				drawWires(g);
				drawnWires = true;
			}

			if (!s.visible)
				continue;
			
			g.setColor(s.color);

			if (Main.mouse.hovered == s) {
				drawHovered(g, s);
				continue;
			}			

			Vector2 pos = s.worldPosition();

			if (s.isCircle)
				drawCircle(g, pos, s.scale, s.filled);
			else
				drawSquare(g, pos, s.scale, s.filled);

			drawText(g, s.text, pos, s.scale);
		}
	}

	public void drawHovered(Graphics g, Shape s) {

		Vector2 pos = s.worldPosition();
		
		Vector2 scale = s.scale.mult(1.1f);

		pos.x -= s.scale.x * 0.05f;
		pos.y -= s.scale.y * 0.05f;		

		if (s.isCircle)
			drawCircle(g, pos, scale, s.filled);
		else
			drawSquare(g, pos, scale, s.filled);

		drawText(g, s.text, pos, scale);
	}

	public void drawSquare(Graphics g, Vector2 pos, Vector2 scale, Boolean filled) {

		if (filled)
			g.fillRect((int) pos.x, (int) pos.y, (int) scale.x, (int) scale.y);
		else
			g.drawRect((int) pos.x, (int) pos.y, (int) scale.x, (int) scale.y);
	}

	public void drawCircle(Graphics g, Vector2 pos, Vector2 scale, Boolean filled) {

		if (filled)
			g.fillOval((int) pos.x, (int) pos.y, (int) scale.x, (int) scale.y);
		else
			g.drawOval((int) pos.x, (int) pos.y, (int) scale.x, (int) scale.y);
	}

	public void drawText(Graphics g, String txt, Vector2 pos, Vector2 scale) {

		if (txt.isBlank())
			return;

		int fontSize = 10;
		g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
		g.setColor(ColorManager.parseColor(ColorManager.WHITE));

		int width = g.getFontMetrics().stringWidth(txt);
		int height = g.getFontMetrics().getHeight();
		int ascent = g.getFontMetrics().getAscent();

		int size = (int) (Math.min(scale.x / width, scale.y / height) * fontSize);
		size = Math.max(size, 1);
		size *= 0.9f;

		g.setFont(new Font("TimesRoman", Font.BOLD, size));

		pos.x += (scale.x - width * (size / (float) fontSize)) * 0.5f;
		pos.y += (scale.y + ascent * (size / (float) fontSize)) * 0.5f;

		g.drawString(txt, (int) pos.x, (int) pos.y);
	}

	public Shape closestInteract() {

		for (Shape s : shapeList) {

			if (s.interactible && s.contains(Main.mouse.mousePos)) {
				return s;
			}
		}

		return null;
	}

	public void closeFrame() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowClosing);
	}
	
	public void reSortList() {
		
		Collections.sort(shapeList, new Comparator<Shape>(){
		     public int compare(Shape a, Shape b){
		         if(a.layer == b.layer)
		             return 0;
		         return a.layer < b.layer ? -1 : 1;
		     }
		});
	}
}
