package levels;

public class Levels {

	private int [][] lvlData;
	
	public Levels(int[][] lvlData) {
		this.lvlData = lvlData;
		
	}
	
	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}
	
	public int[][] getLevelData() {
		return lvlData;
	}
	
}
