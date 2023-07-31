package CompPack;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
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

		// BG
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		drawShapes(g, true);
		drawWires(g);
		drawShapes(g, false);
	}
	
	public void drawWires(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;

		for (Node n : new ArrayList<Node>(Main.node.nodeList)) {

			if (!n.interactible || !n.visible)
				continue;
			
			Vector2 start = n.center();
			if (n.parent != null)
				start = start.add(n.parent.position);

			List<Vector2> ends = n.endWirePoint();
			
			g2d.setColor(n.state ? Node.ON_COLOR : Node.OFF_COLOR);
			
			for (Vector2 end : ends) {
				Vector2 mid = start.midPoint(end);				

				g2d.setStroke(new BasicStroke(5));
				g2d.drawLine((int) start.x, (int) start.y, (int) mid.x, (int) start.y);
				g2d.drawLine((int) mid.x, (int) start.y, (int) mid.x, (int) end.y);
				g2d.drawLine((int) mid.x, (int) end.y, (int) end.x, (int) end.y);
			}
			
		}
	}
	
	public void drawShapes(Graphics g, Boolean belowZero) {
		
		for (Shape s : new ArrayList<Shape>(shapeList)) {

			if ((s.layer > 0 && belowZero) || (s.layer < 0 && !belowZero))
				continue;
			
			if (!s.visible)
				continue;

			g.setColor(s.color);

			Vector2 pos = new Vector2(s.position);
			if (s.parent != null)
				pos = pos.add(s.parent.position);			

			if (s.isCircle)
				drawCircle(g, s, pos);
			else
				drawSquare(g, s, pos);
			
			pos = pos.add(9, 15);

			drawText(g, pos, s.text);
		}
	}

	public void drawSquare(Graphics g, Shape s, Vector2 pos) {

		if (s.filled)
			g.fillRect((int) pos.x, (int) pos.y, (int) s.scale.x, (int) s.scale.y);
		else
			g.drawRect((int) pos.x, (int) pos.y, (int) s.scale.x, (int) s.scale.y);
	}

	public void drawCircle(Graphics g, Shape s, Vector2 pos) {

		if (s.filled)
			g.fillOval((int) pos.x, (int) pos.y, (int) s.scale.x, (int) s.scale.y);
		else
			g.drawOval((int) pos.x, (int) pos.y, (int) s.scale.x, (int) s.scale.y);
	}

	public void drawText(Graphics g, Vector2 pos, String text) {
		g.setColor(Color.white);
		g.drawString(text, (int) pos.x, (int) pos.y);
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
}
