package application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import algorithm.Editor;

public class Screen extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	
	public static final Color BACKGROUND_COLOR = new Color(219, 186, 147);
	public static final Color SHADE = new Color(180, 143, 113);
	public static final Color PANE = new Color(233, 204, 169);
	
	public static final Color WHITE = new Color(249, 250, 228);
	public static final Color BLACK = new Color(36, 36, 51);
	public static final Color LIGHT_BLUE = new Color(46, 160, 177);
	public static final Color DARK_BLUE = new Color(35, 127, 163);
	public static final Color RED = new Color(199, 98, 58);
	public static final Color GREEN = new Color(140, 177, 77);
	
	
	public static Scene scene;
	
	public Screen() {
		setPreferredSize(new Dimension(1280, 720));
		setLayout(null);
		addMouseListener(Program.mouseListener);
		addMouseMotionListener(Program.mouseListener);
		setBackground(BACKGROUND_COLOR);
		setFocusable(true);
		scene = new Editor();
		scene.start();
		
	}
	
	public void run() {
		
		while(true) {
			update();
			repaint();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		if(scene != null) {
			scene.update();
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		if(scene != null) {
			scene.render(g);
		}
	}

}
