package application;


public class Program {
	
	public static Window window;
	public static MouseInput mouseListener = new MouseInput();
	public static Thread programThread;
	
	public static int mouseY, mouseX;
	public static void main(String[] args) {
		window = new Window();
		programThread = new Thread(Window.screen);
		programThread.start();
	}

}
