package CompPack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class _Screen extends JPanel {

	private static final long serialVersionUID = 1L;

	JFrame frame;
	BufferedImage canvas;
	_Computer comp;
	
	public static final int SCREEN_RAM_BASE = 16384;

	public void initialise() {
		frame = new JFrame("Hack Screen");

		Dimension d = new Dimension(512, 256);

		frame.getContentPane().setPreferredSize(d);
		frame.getContentPane().setMinimumSize(d);
		frame.getContentPane().setMaximumSize(d);
		
		canvas = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);

		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setDoubleBuffered(true);
		setLayout(null);
		setFocusable(true);
        requestFocusInWindow();
        
        comp = _ProgramAssemblyBuilder.comp;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		for (int i = 0; i < 8192; i++) {
			
			short s = comp.RAM[i + 16384];
			String binary = _Computer.shortToBinaryString(s, 16, false);
			
			int y = (int)Math.floor(i / 32);
			for (int c = 0; c < 16; c++) {
				if (binary.charAt(c) == '1') {
					int x = i + c - 32 * y;
					colorPixel(g, x, y);
				}
			}
		}
		
		((Graphics2D)g).drawImage(canvas, 0, 0, this);
	}
	
	public void colorPixel(Graphics g, int x, int y) {
		
		canvas.setRGB(x, y, Color.black.getRGB());
	}
	
	public Vector2 getWindowSize() {
		Dimension d = frame.getContentPane().getSize();
		return new Vector2(d.width, d.height);
	}

	public void closeFrame() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowClosing);
	}
}
