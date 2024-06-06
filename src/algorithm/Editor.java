package algorithm;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import application.Scene;
import application.Screen;
import ui.Button;

public class Editor implements Scene{
	Tile[][] tiles;
	int tileSize = 36;
	int tileSpace = 2;
	Button[] buttons;
	
	public static Position start = new Position();
	public static Position finish = new Position();
	
	public static final int BLANK = 0;
	public static final int WALL = 3;
	public static final int START = 4;
	public static final int FINISH = 5;
	public static int brushType = START;
	
	public static int simulationMode = 0;
	public static String[] modes = {"Instantânea", "Passo a passo", "Tempo real"};
	public static boolean[] options = {true, false, false};
	public static boolean simulating = false;
	
	private String result = "Ainda não ocorreu nenhuma simulação";
	
	static final int INSTANT = 0;
	static final int STEP_BY_STEP = 1;
	static final int REAL_TIME = 2;
	
	int recordedSmaller = 0;
	
	List<Position> dots = new ArrayList<>();
	
	public Editor() {
		tiles = new Tile[33][16];
		
		
		buttons = new Button[11];
		
		buttons[0] = new Button(10,10,30,30) {
			
			@Override
			protected void paintContent(Graphics g) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void action() {
				clearPaths();
				simulating = true;
				if(simulationMode != INSTANT) {
					if(simulationMode == STEP_BY_STEP) {
						
						recordedSmaller = 0;
						int x = start.x, y = start.y;
						dots.add(finish);
						checkTile(x, y);
					}
					return;
				}
				runSimulation();
			}
		};
		
		buttons[1] = new Button(130,62,100,15) {
			@Override
			protected void paintContent(Graphics g) {
				g.setColor(Screen.SHADE);
				g.drawString(""+modes[simulationMode], 10,12);
			}
			@Override
			public void action() {
				simulationMode++;
				if(simulationMode > 2) {
					simulationMode = 0;
				}
				buttons[1].refactor();
			}
		};
		
		buttons[2] = new Button(50,10,30,30) {
			@Override
			protected void paintContent(Graphics g) {
				
			}
			@Override
			public void action() {
				stopSimulation();
			}
		};
		
		buttons[3] = new Button(410,10,16,16) {
			@Override
			protected void paintContent(Graphics g) {
				if(options[0]) {
					g.setColor(Screen.BLACK);
					g.drawString("x", 6, 12);
				}
			}
			@Override
			public void action() {
				
				options[0] = (options[0]) ? false : true;
				refactor();
			}
		};
		
		buttons[4] = new Button(410,30,16,16) {
			@Override
			protected void paintContent(Graphics g) {
				if(options[1]) {
					g.setColor(Screen.BLACK);
					g.drawString("x", 6, 12);
				}
			}
			@Override
			public void action() {
				
				options[1] = (options[1]) ? false : true;
				refactor();
			}
		};
		
		buttons[5] = new Button(410,50,16,16) {
			@Override
			protected void paintContent(Graphics g) {
				if(options[2]) {
					g.setColor(Screen.BLACK);
					g.drawString("x", 6, 12);
				}
			}
			@Override
			public void action() {
				
				options[2] = (options[2]) ? false : true;
				refactor();
			}
		};
		
		buttons[6] = new Button(650,16,50,50) {
			@Override
			protected void paintContent(Graphics g) {
				
			}
			@Override
			public void action() {
				brushType = START;
			}
		};
		
		buttons[7] = new Button(710,16,50,50) {
			@Override
			protected void paintContent(Graphics g) {
				
			}
			@Override
			public void action() {
				brushType = FINISH;
			}
		};
		
		buttons[8] = new Button(770,16,50,50) {
			@Override
			protected void paintContent(Graphics g) {
				
			}
			@Override
			public void action() {
				brushType = WALL;
			}
		};
		
		buttons[9] = new Button(850,10,160,28) {
			
			@Override
			protected void paintContent(Graphics g) {
				g.setColor(Screen.BLACK);
				g.drawString("Limpar tudo", 10, 15);
			}
			
			@Override
			public void action() {
				clear();
			}
		};
		
		buttons[10] = new Button(850,45,160,28) {
			
			@Override
			protected void paintContent(Graphics g) {
				g.setColor(Screen.BLACK);
				g.drawString("Limpar caminhos", 10, 15);
			}
			
			@Override
			public void action() {
				clearPaths();
			}
		};
	}
	@Override
	public void start() {
		
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				
				tiles[x][y] = new Tile(this,x,y);
				Tile tile = tiles[x][y];
				tile.setSize(tileSize);
				tile.setPosition(tileSize*x + tileSpace*x + 16, tileSize*y + tileSpace*y + 100);
			}
		}
	}

	@Override
	public void update() {
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].checkClick();
		}
		
		if(simulating) {
			if(simulationMode == STEP_BY_STEP) {
				try {
				Thread.sleep(500);
				} catch (InterruptedException e) {
				e.printStackTrace();
				}
			
				if(nextStep() <= 0) {
					createPath();
					result = "Caminho encontrado!";
					simulating = false;
				}
			}
			return;
		}
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				if(tiles[x][y] != null)
				tiles[x][y].update();
			}
		}
		
		
	}
	
	// :::
	// ===== [FUNÇÔES DE SIMULAÇÃO] ===========================================//
	//Aqui estão as funções essenciais para o funcionamento do algoritmo
	//
	//
	// Referências usadas:
	// > https://youtu.be/-L-WgKMFuhE?si=3BUXktFwvgQFm6N4
	
	// ::
	// Simulação: Instantânea -------------------------------//
	
	public void runSimulation() {
		
		long startTime = System.nanoTime();
		
		// começo
		
		int smaller = 0;
		int x = start.x, y = start.y;
		dots.add(finish);
		
		checkTile(x, y);
		
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				if(tiles[i][j].value != 0) {
				smaller = checkSmallerValue(i, j, smaller);
				}
			}
		}
		
		while(smaller > 0) {
			
			
			int runSmaller = 0;
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[0].length; j++) {
					if(tiles[i][j].value == smaller && tiles[i][j].value != 0) {
						tiles[i][j].verified = true;
						if(runSmaller < 0) {
							break;
						}
						checkTile(i,j);
					}
				}
			}
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[0].length; j++) {
					if(tiles[i][j].value != 0) {
						runSmaller = checkSmallerValue(i, j, runSmaller);
						
					}
				}
			}
			smaller = runSmaller;
			
			
		}
		if(smaller < 0) {
			createPath();
		}
		// Fim
		
		long endTime = System.nanoTime();
		float totalTime = (float)(endTime - startTime)/1000000;
		
		if(smaller < 0) {
			result = "Duração: "+ totalTime+" mills";
		}else {
			result = "** É impossivel alcançar o objetivo **";
		}
		
		if(options[1]) {
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[0].length; j++) {
					
					if(tiles[i][j].value != 0) {
						if(tiles[i][j].verified) {
							tiles[i][j].setState(2);
						}else if (tiles[i][j].value != 0){
							tiles[i][j].setState(1);
						}
					}
					
				}
			}
		}
		
		simulating = false;
		
	}
	//----------------------------------------------------------------//
	
	// ::
	// Simulação: Passo a Passo: ------------------------------------ //
	
	private void stopSimulation() {
		// Esta função interrompe a simulação que estiver ocorrendo
		
		if(!simulating) {
			return;
		}
		clearPaths();
		recordedSmaller = 0;
		simulating = false;
	}
	
	private int nextStep() {
		int smaller = recordedSmaller;
		int runSmaller = 0;
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				if(tiles[i][j].value == smaller && tiles[i][j].value != 0) {
					tiles[i][j].verified = true;
					if(runSmaller < 0) {
						break;
					}
					checkTile(i,j);
				}
			}
		}
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				if(tiles[i][j].value != 0) {
					runSmaller = checkSmallerValue(i, j, runSmaller);
				}
			}
		}
		smaller = runSmaller;
		recordedSmaller = smaller;
		
		if(options[1]) {
			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles[0].length; j++) {
					
					if(tiles[i][j].value != 0) {
						if(tiles[i][j].value == smaller) {
							tiles[i][j].setState(2);
						}else if (tiles[i][j].getState() == 0){
							tiles[i][j].setState(1);
						}
					}
					
				}
			}
		}
		result = "Próximo valor á verificar: "+ smaller;
		return smaller;
	}
	//-----------------------------------------------//

	//::
	// Operações de Celula --------------------------//
	public void checkTile(int posX, int posY) {
		int x = posX, y = posY;
		tiles[x][y].verified = true;
		if(options[1]) {
			tiles[x][y].setState(2);
		}
		
		setTileValue(x - 1, y, posX, posY);
		setTileValue(x - 1, y - 1, posX, posY);
		setTileValue(x - 1, y + 1, posX, posY);
		setTileValue(x, y - 1, posX, posY);
		setTileValue(x, y + 1, posX, posY);
		setTileValue(x + 1, y, posX, posY);
		setTileValue(x + 1, y - 1, posX, posY);
		setTileValue(x + 1, y + 1, posX, posY);
		
		
	}
	
	private void setTileValue(int x, int y, int orgX,int orgY) {
		// Esta função define o valor da celula[x][y] e define a coordenada de sua célula de origem:
		// a celula[orgX][orgY].
		
		
		// Caso a célula[x][y] não estiver dentro do quadro, for uma parede ou ja foi verificada,
		// a função é anulada;
		if(x >= tiles.length || x < 0 || y >= tiles[0].length || y < 0) {
			return;
		}
		if(tiles[x][y].state == WALL) {
			return;
		}
		if(tiles[x][y].verified) {
			return;
		}
		
		// Atribuição ods valores
		tiles[x][y].setPastPosition(orgX, orgY);
		tiles[x][y].setValue();
		
	}
	
	private boolean isPossible(int x, int y) {
		// Esta função verifica se é possivel que a celula[x][y] possa originar um caminho:
		//
		// Para que retorne verdadeiro, a celua deve ter pelo menos uma celula vizinha que:
		// 1- Não seja uma parede
		// 2- Não tenha sido verificada
		
		if(x > 0 && tiles[x-1][y].state != WALL) {
			x -= 1;
			if(y > 0 && tiles[x][y-1].state != WALL && !tiles[x][y-1].verified) {
				return true;
			}
			if(y < tiles[0].length - 1 && tiles[x][y+1].state != WALL && !tiles[x][y+1].verified) {
				return true;
			}
			if(!tiles[x][y].verified) {
				return true;
			}
			x += 1;
		}
		if(x < tiles.length - 1 && tiles[x+1][y].state != WALL) {
			x += 1;
			if(y > 0 && tiles[x][y-1].state != WALL && !tiles[x][y-1].verified) {
				return true;
			}
			if(y < tiles[0].length - 1 && tiles[x][y+1].state != WALL && !tiles[x][y+1].verified) {
				return true;
			}
			if(!tiles[x][y].verified) {
				return true;
			}
			x -= 1;
		}
		
		if(y > 0 && tiles[x][y-1].state != WALL && !tiles[x][y-1].verified) {
			return true;
		}
		if(y < tiles[0].length - 1 && tiles[x][y+1].state != WALL && !tiles[x][y+1].verified) {
			return true;
		}
		
		return false;
	}
	
	private int checkSmallerValue(int x, int y, int smaller) {
		// Esta função checa se a celula[x][y] tem um valor menor que o argumento "smaller"
		// e RETORNA O VALOR MENOR, para que possívelmente possa ser selecionada para gerar um caminho.
		//
		//
		// Caso o a célula[x][y]:
		// > ja tenha sido verificada, ou...
		// > não tiver valor, ou...
		// > não consiga criar um caminho novo
		// A célula é invalidada e a função retorna o próprio argumento
		
		
		// Caso o argumento tiver um valor abaixo de zero, significa que o caminho até objetivo
		//ja foi encontrado e não tem motivo checar a célula atual.
		if(smaller < 0) {
			return smaller;
		}
		
		
		if(tiles[x][y].verified) {
			return smaller;
		}
		if(tiles[x][y].value < 0) {
			return -1;
		}
		
		if(!isPossible(x,y)) {
			return smaller;
		}
		
		if(smaller == 0) {
			smaller = tiles[x][y].value;
		}else if(tiles[x][y].value < smaller){
			smaller = tiles[x][y].value;
		}
		return smaller;
	}
	
	//--------------------------------------------//
	
	// ::
	// Criação do caminho final ------------------//
	private void createPath() {
		Position pos = new Position(dots.get(0));
		
		while(pos.x != Editor.start.x || pos.y != Editor.start.y) {
			Tile tile = tiles[pos.x][pos.y];
			pos.setPosition(tile.pastCod);
			//System.out.println(tile.pastCod);
			dots.add(new Position(pos));
		}
		
	}
	//--------------------------------------------//
	
	// ==============================================================//
	// :::
	
	// :::
	// == [FUNÇÕES DE LIMPEZA] ===================//
	public void clear() {
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				if(tiles[x][y] != null) {
					tiles[x][y].setState(0);
				}
			}
		}
		
		dots.clear();
	}
	
	public void clearPaths() {
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				if(tiles[x][y] != null) {
					tiles[x][y].removeState(1);
					tiles[x][y].removeState(2);
					tiles[x][y].value = 0;
					tiles[x][y].verified = false;
				}
			}
		}
		
		dots.clear();
	}
	
	public void removeByState(int state) {
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				if(tiles[x][y] != null) {
					tiles[x][y].removeState(state);
				}
			}
		}
	}
	// ========================================//
	// :::

	@Override
	public void render(Graphics g) {
		
		
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				if(tiles[x][y] != null) {
					tiles[x][y].render(g);
				}
			}
		}
		g.setColor(Screen.DARK_BLUE);
		
		
		/*int numb = 0;
		for(Position p: dots) {
			g.fillRect(tiles[p.x][p.y].position.x + 15, tiles[p.x][p.y].position.y + 15, 4, 4);
			g.drawString(""+ numb,tiles[p.x][p.y].position.x, tiles[p.x][p.y].position.y);
			
			numb ++;
		}*/
		for(int i = 0; i < dots.size() - 1; i++) {
			g.drawLine(tiles[dots.get(i).x][dots.get(i).y].position.x + 18, tiles[dots.get(i).x][dots.get(i).y].position.y + 18,
					tiles[dots.get(i+1).x][dots.get(i+1).y].position.x + 18, tiles[dots.get(i+1).x][dots.get(i+1).y].position.y + 18);
		}
		g.setColor(Screen.SHADE);
		g.fillRect(0, 0, 1280, 86);
		
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].render(g);
		}
		
		g.setColor(Screen.PANE);
		
		g.drawRect(100, 10, 260, 36);
		g.drawString(result, 110, 28);
		
		g.drawString("Mostrar caminho final", 434, 23);
		g.drawString("Colorir celulas checadas", 434, 43);
		g.drawString("Mostrar os valores das células", 434, 63);
		
		g.drawString("Modo de Simulação: ", 12, 76);
	}

}
