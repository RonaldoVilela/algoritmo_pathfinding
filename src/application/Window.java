package application;

import javax.swing.JFrame;

public class Window extends JFrame{
	
	private static final long serialVersionUID = 1L;
	public static Screen screen;

	public Window() {
		screen = new Screen();
		setTitle("Algoritmo Pathfinding");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(screen);
		pack();

		setLocationRelativeTo(null);
		setVisible(true);
	}
}
