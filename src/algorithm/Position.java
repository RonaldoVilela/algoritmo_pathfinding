package algorithm;

public class Position {
	public int x, y;
	public Position() {}
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(Position position) {
		x = position.x;
		y = position.y;
	}
	
	public void setPosition(Position position) {
		x = position.x;
		y = position.y;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return x+" / "+y;
	}
}
