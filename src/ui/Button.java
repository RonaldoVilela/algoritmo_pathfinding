package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import application.Program;
import application.Screen;
import algorithm.Position;


public abstract class Button {
	
	Position position = new Position();
	int width, height;
	boolean selected;
	boolean active;
	public BufferedImage buttonImage;
	private boolean focusable;
	private boolean focused;
	private boolean borderless;
	private Color color = Screen.WHITE;
	private Color backgroundColor = Screen.PANE;
	private Color selectedColor = Screen.WHITE;
	private Color unfocusColor = Screen.PANE;
	
	public Button(int x, int y) {
		setPosition(x, y);
	}
	public Button(int x, int y, int widht, int height) {
		setPosition(x, y);
		setSize(widht, height);
	}
	public Button() {
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	public void setUnfocusedColor(Color color) {
		unfocusColor = color;
	}
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	public void setSelectedColor(Color color) {
		selectedColor = color;
	}
	public Color getColor() {
		return color;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean booleanValue) {
		active = booleanValue;
	}
	
	public void setFocusable(boolean booleanValue) {
		focusable = booleanValue;
	}
	
	public void setFocused(boolean booleanValue) {
		focused = booleanValue;
	}
	
	public void setBorderless(boolean booleanValue) {
		borderless = booleanValue;
	}
	
	public boolean isFocusable() {
		return focusable;
	}
	public boolean isFocused() {
		return focused;
	}
	
	public void setPosition(int x, int y) {
		position = new Position(x, y);
	}
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		buttonImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		setContent();
		
	}
	
	public void refactor() {
		setSize(buttonImage.getWidth(), buttonImage.getHeight());
	}
	
	
	
	protected void setContent() {
		Graphics2D g2 = buttonImage.createGraphics();
		//g2.setFont(GamePanel.pixelFont);
		paintContent(g2);
		g2.dispose();
	}
	
	public void update() {
		
	}
	public void checkClick() {
		if(mouseOn()) {
			selected = true;
			if(Program.mouseListener.clicked) {
				action();
				Program.mouseListener.clicked = false;
			}
		}
		else {
			selected = false;
		}
		
	}
	public abstract void action();
	protected void altAction() {
		
	}
	
	private boolean mouseOn() {
		if(!(position.x < Program.mouseX && position.x + width > Program.mouseX)){
			return false;
		}
		if(!(position.y < Program.mouseY && position.y + height > Program.mouseY)){
			return false;
		}
		
		return true;
		
	}
	protected abstract void paintContent(Graphics g);
	
	public boolean isSelected() {
		return selected;
	}
		
	public void render(Graphics g) {
		
		if(selected || (focusable && focused)) {
			g.setColor(selectedColor);
		}
		else {
			g.setColor(backgroundColor);
		}
		
		g.fillRect(position.x, position.y, width, height);
		
		
		g.drawImage(buttonImage,position.x , position.y, null);
		if(focusable && !focused) {
			g.setColor(unfocusColor);
		}
		else {
			g.setColor(color);
		}
		if(!borderless) {
			g.drawRect(position.x, position.y, width, height);
		}
		
		
	}
}
