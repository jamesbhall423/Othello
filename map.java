import java.util.ArrayList;

public class map implements Cloneable {
	private final square[][] squares = new square[8][8];	
	/**
	 * Method clone
	 *
	 *
	 * @return
	 *
	 */
	public map clone() {
		return new map(squares);
	}
	public map(mapButton[][] buttons) {
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) squares[x][y]=new square(buttons[x][y].empty(),buttons[x][y].playerPiece(),buttons[x][y].available(),x,y);
	}
	public map(square[][] input) {
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) squares[x][y]=new square(input[x][y].empty(),input[x][y].player(),input[x][y].available(),x,y);
	}

	/**
	 * Method get
	 *
	 *
	 * @param x
	 * @param y
	 *
	 * @return
	 *
	 */
	public square get(int x, int y) {
		return squares[x][y];
	}
	public void set(int x, int y, square s) {
		squares[x][y]=s;
	}
	public ArrayList<Movement> setAvailable(boolean player) {
		ArrayList<Movement> out = new ArrayList<Movement>();
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) if (squares[x][y].empty()) {
			if (testRow(x,y,-1,-1,player)||testRow(x,y,-1,0,player)||testRow(x,y,-1,1,player)||testRow(x,y,0,-1,player)||testRow(x,y,0,1,player)||testRow(x,y,1,-1,player)||testRow(x,y,1,0,player)||testRow(x,y,1,1,player)) {
				squares[x][y].setAvailable(true);
				out.add(new Movement(x,y));
			}
		}
		return out;
	}
	private boolean testRow(int x1, int y1, int xinc, int yinc, boolean player) {
		int x2 = x1+xinc;
		int y2 = y1+yinc;
		if (x2>=8||x2<0||y2>=8||y2<0||squares[x2][y2].empty()||squares[x2][y2].player()==player) return false;
		x2+=xinc;
		y2+=yinc;
		while (x2>=0&&x2<8&&y2>=0&&y2<8) {
			if (squares[x2][y2].empty()) return false;
			else if (squares[x2][y2].player()==player) return true;
			x2+=xinc;
			y2+=yinc;
		}
		return false;
	}
	public void place(int x, int y, boolean player) {
		squares[x][y].setEmpty(false);
		squares[x][y].setPlayer(player);
		for (int xx = 0; xx < 8; xx++) for (int yy = 0; yy < 8; yy++) squares[xx][yy].setAvailable(false);
		if (testRow(x,y,-1,-1,player)) changeRow(x,y,-1,-1,player);
		if (testRow(x,y,-1,0,player)) changeRow(x,y,-1,0,player);
		if (testRow(x,y,-1,1,player)) changeRow(x,y,-1,1,player);
		if (testRow(x,y,0,-1,player)) changeRow(x,y,0,-1,player);
		if (testRow(x,y,0,1,player)) changeRow(x,y,0,1,player);
		if (testRow(x,y,1,-1,player)) changeRow(x,y,1,-1,player);
		if (testRow(x,y,1,0,player)) changeRow(x,y,1,0,player);
		if (testRow(x,y,1,1,player)) changeRow(x,y,1,1,player);
	}
	private void changeRow(int x1, int y1, int xinc, int yinc, boolean player) {
		int x2 = x1+xinc;
		int y2 = y1+yinc;
		boolean b = true;
		while (squares[x2][y2].player()!=player) {
			squares[x2][y2].setPlayer(player);
			x2+=xinc;
			y2+=yinc;
		}
	}
	public int squareDifference() {
		int out = 0;
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) if (!squares[x][y].empty()) {
			if (squares[x][y].player()) out++;
			else out--;
		}
		return out;
	}
}
