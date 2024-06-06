package algorithm;

import java.awt.Color;
import java.awt.Graphics;

import application.Program;
import application.Screen;

public class Tile {
	static Editor editor;
	public Position position = new Position();
	int width, height;
	int codX, codY;
	Position pastCod = new Position();
	
	public int hCost, gCost;
	int value;
	
	boolean selected;
	boolean verified = false;
	public static final Color[] colors = {Screen.PANE, Screen.GREEN,Screen.RED, Screen.BLACK, Screen.LIGHT_BLUE, Screen.DARK_BLUE};
	int state = 0;
	
	public Tile(Editor editor,int x, int y) {
		Tile.editor = editor;
		codX = x;
		codY = y;
	}
	
	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
	}
	
	public void setSize(int size) {
		width = size;
		height = size;
	}
	
	public void setState(int state) {
		if(state == 0) {
			verified = false;
			pastCod = new Position();
			value = 0;
		}
		if(state < 3 && this.state > 2 && state != 0) {
			if(!Editor.simulating) {
				value = 0;
				pastCod = new Position();
				verified = false;
			}
			return;
		}
		if(!Editor.simulating) {
			value = 0;
			pastCod = new Position();
			verified = false;
		}
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	
	public void removeState(int state) {
		if(this.state == state) {
			setState(0);
		}
	}
	
	//----------------------------------------------------------
	// FUNÇÕES PARA SIMULAÇÃO :
	
	
	public void setValue() {
		// Se a posição da celula for igual a posição da celula
		
		if(codX == Editor.finish.x && codY == Editor.finish.y) {
			value = -1;
			return;
		}
		
		// Caso contrario seu valor será calculado ...
		//
		//
		// Gcost = distancia da celula inicial
		// Hcost = distancia da celula final
		// Value = Gcost + Hcost
		
		gCost = distance(Editor.start);
		hCost = distance(Editor.finish);
		value = gCost + hCost;
	}
	
	public void setPastPosition(int x, int y) {
		
		// Essa função define a posição da célula anterior que deu origem a essa
		// para depois se transformar em um possível caminho.
		//
		// Os parâmetros (x, y) da função representa as coordenadas da celula anterior
		
		
		// Caso a célula ja tenha um valor, ou seja, já tem uma célula de origem,
		// esta etapa é anulada:
		if(value != 0) {
			return;
		}
		
		// Célula de origem sendo definida
		pastCod.setPosition(x, y);
	}
	
	private int distance(Position position) {
		int distX =(position.x - codX)*10;
		int distY = (position.y - codY) *10;
		return (int)Math.sqrt((distX*distX) + (distY*distY));
	}
	
	//-------------------------------------------------------
	
	public void update() {

		selected = false;
		
		if(Editor.simulating) {
			return;
		}
		
		if(mouseOn()) {
			
			selected = true;
			
			if(Program.mouseListener.clicked) {
				if(Editor.brushType != Editor.WALL) {
					editor.removeByState(Editor.brushType);
					
					if(Editor.brushType == Editor.START) {
						Editor.start.setPosition(codX, codY);
					}else {
						Editor.finish.setPosition(codX, codY);
					}
				}
				state = Editor.brushType;
				return;
			}
			
			if(Program.mouseListener.rClicked) {
				state = 0;
			}
			return;
		}
		
	}
	
	private boolean mouseOn() {
		if(!(position.x <= Program.mouseX && position.x + width > Program.mouseX)){
			return false;
		}
		if(!(position.y <= Program.mouseY && position.y + height > Program.mouseY)){
			return false;
		}
		
		return true;
		
	}
	
	public void render(Graphics g) {
		if(selected) {
			g.setColor(Screen.WHITE);
		}else {
			g.setColor(colors[state]);
		}
		
		g.fillRect(position.x, position.y,width, height);
		
		if(Editor.options[2] && value>0) {
			g.setColor(Screen.BLACK);
			g.drawString(""+value, position.x + 3, position.y + 27);
		}
	}
}
