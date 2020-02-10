import java.util.ArrayList;
import java.util.Random;

public class MoveCalculator {
	private static boolean displayTimes = false;
	private static final int endGame = 46;
	private static final double multiplier = 10.0;
	private final int difficulty;
	private static final Random random = new Random();	
	
	public Movement move(mapButton[][] buttons, int turns) {
		long l = System.nanoTime();
		map mp = new map(buttons);
		double power = multiplier*difficulty;
		ArrayList<Movement> pmoves = mp.setAvailable(false);
		power/=pmoves.size();
		power-=1.0;
		Movement last = null;
		double vLast = -10000.0;
		for (Movement next: pmoves) {
			double d = value(mp,next,power,pmoves.size(), true, turns);
			if (d>vLast) {
				last=next;
				vLast=d;
			}
		}
		if (displayTimes) System.out.println((System.nanoTime()-l)/1000);
		return last;
	}
	//Designed as a constant time function where power = time;
	//Throughout most of the game, maximizes diference in the number of options between the players.
	//Recursive method - calls itself to determine what it expects the player will do.
	//Small element of randomness, player cannot play two games and get the same result.
	private double value(map m, Movement movement, double power, int optionsLast, boolean player, int turns) {
		map mp = m.clone();
		if (movement!=null) mp.place(movement.x, movement.y, !player);
		ArrayList<Movement> pmoves = mp.setAvailable(player);
		if (pmoves.size()==0) {
			if (optionsLast==0) {
				int t = mp.squareDifference();
				if (t<0) return 200-t;
				else if (t==0) return 0;
				else return -200-t;
			} else return value(mp,null,power-1.0,0,!player,turns);
		}
		power/=pmoves.size();
		power-=1.0;
		if (power<1.0) {
			if (turns<=endGame) {
				int optionsDifference;
				if (player) optionsDifference = optionsLast-pmoves.size();
				else optionsDifference = pmoves.size()-optionsLast;
				if (turns<=32) return corner(mp,turns)+optionsDifference+mp.squareDifference()/8.1;
				else return corner(mp,turns)+optionsDifference;
			} else return corner(mp,turns)-mp.squareDifference();
		} else if (power<pmoves.size()) {
			int t1 = random.nextInt(pmoves.size());
			int t2 = random.nextInt(pmoves.size());
			double v1=value(mp,pmoves.get(t1),1.0,pmoves.size(),!player,turns);
			double v2=value(mp,pmoves.get(t2),1.0,pmoves.size(),!player,turns);
			double average=(v1+v2)/200;
			if (player==v1<v2) return v1+average;
			else return v2+average;
		} else {
			Movement last = null;
			double vLast;
			double sum=0;
			if (player) vLast=10000;
			else vLast=-10000;
			for (Movement next: pmoves) {
				double d = value(mp,next,power,pmoves.size(), !player, turns);
				sum+=d;
				if (player==d<vLast) {
					last=next;
					vLast=d;
				}
			}
			return vLast+((sum/pmoves.size())/100);
		}
	}
	private double corner(map mp,int numTurns) {
		double out=0;
		int x=0;
		int y=0;
		if (mp.get(x,y).empty()) {
			if (!mp.get(x+1,y).empty()) if (mp.get(x+1,y).player()) out+=2;
			else out-=2;
			if (!mp.get(x+1,y+1).empty()) if (mp.get(x+1,y+1).player()) out+=4;
			else out-=4;
			if (!mp.get(x,y+1).empty()) if (mp.get(x,y+1).player()) out+=2;
			else out-=2;
		} else if (mp.get(x,y).player()) out-=20;
		else out+=20;
		x=0;
		y=7;
		if (mp.get(x,y).empty()) {
			if (!mp.get(x+1,y).empty()) if (mp.get(x+1,y).player()) out+=2;
			else out-=2;
			if (!mp.get(x+1,y-1).empty()) if (mp.get(x+1,y-1).player()) out+=4;
			else out-=4;
			if (!mp.get(x,y-1).empty()) if (mp.get(x,y-1).player()) out+=2;
			else out-=2;
		} else if (mp.get(x,y).player()) out-=20;
		else out+=20;
		x=7;
		y=0;
		if (mp.get(x,y).empty()) {
			if (!mp.get(x-1,y).empty()) if (mp.get(x-1,y).player()) out+=2;
			else out-=2;
			if (!mp.get(x-1,y+1).empty()) if (mp.get(x-1,y+1).player()) out+=4;
			else out-=4;
			if (!mp.get(x,y+1).empty()) if (mp.get(x,y+1).player()) out+=2;
			else out-=2;
		} else if (mp.get(x,y).player()) out-=20;
		else out+=20;
		x=7;
		y=7;
		if (mp.get(x,y).empty()) {
			if (!mp.get(x-1,y).empty()) if (mp.get(x-1,y).player()) out+=2;
			else out-=2;
			if (!mp.get(x-1,y-1).empty()) if (mp.get(x-1,y-1).player()) out+=4;
			else out-=4;
			if (!mp.get(x,y-1).empty()) if (mp.get(x,y-1).player()) out+=2;
			else out-=2;
		} else if (mp.get(x,y).player()) out-=20;
		else out+=20;
		if (numTurns>endGame) out/=2;
		return out;
	}

	public MoveCalculator(int difficulty) {
		this.difficulty=(1<<(2*(difficulty-1)));
	}	
	
	//New algorithims
	private static final double nmultiplier = 0.025; //Time Equality = 0.25, Logical Equality = 0.05
	private boolean[][] originalPerimiter;
	public Movement move2(mapButton[][] buttons, boolean player) {
		long l = System.nanoTime();
		map mp = new map(buttons);
		double power = nmultiplier*difficulty;
		originalPerimiter = originalPerimiter(mp);
		ArrayList<Movement> pmoves = mp.setAvailable(player);
		Movement last = null;
		double vLast = -1000000.0;
		for (Movement next: pmoves) {
			double d = value2(mp,next,power/pmoves.size(), !player);
			if (d>vLast) {
				last=next;
				vLast=d;
			}
		}
		if (displayTimes) System.out.println("new: " +((System.nanoTime()-l)/1000));
		return last;
	}
	private double value2(map m, Movement movement, double power, boolean player) {
		map mp = m.clone();
		double out = -1000000;
		if (movement!=null) mp.place(movement.x, movement.y, !player);
		if (power<1.0) out = valueMap(mp,player);
		else {
			ArrayList<Movement> pmoves = mp.setAvailable(player);
			if (pmoves.size()==0) {
				if (movement==null) out = endGame(mp,player);
				else out = value2(mp,null,power,!player);
			} else {
				for (Movement next: pmoves) {
					double d = value2(mp,next,power/pmoves.size(), !player);
					if (d>out) out=d;
				}
			}
		}
		
		return -out;
	}
	private double endGame(map mp, boolean player) {
		int squareDif = mp.squareDifference();
		double out = 1000*squareDif;
		if (squareDif>0) out+=100000;
		else if (squareDif<0) out -=100000;
		if (player) return out;
		else return -out;
	}
	private boolean[][] originalPerimiter(map mp) {
		boolean[][] out = new boolean[8][8];
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++)  out[x][y]=onPerimiter(mp,x,y);
		return out;
	}
	private boolean onPerimiter(map mp, int x, int y) {
		if (mp.get(x,y).empty()) return true;
		else if ((x==0||x==7)&&(y==0||y==7)) return false;
		else if (x==0||x==7) return mp.get(x,y-1).empty()||mp.get(x,y+1).empty();
		else if (y==0||y==7) return mp.get(x-1,y).empty()||mp.get(x+1,y).empty();
		else return mp.get(x,y-1).empty()||mp.get(x,y+1).empty()||mp.get(x-1,y).empty()||mp.get(x+1,y).empty() || mp.get(x-1,y-1).empty()||mp.get(x-1,y+1).empty()||mp.get(x+1,y-1).empty()||mp.get(x+1,y+1).empty();
	}
	private double valueMap(map mp, boolean player) {
		double total = random.nextDouble();
		
		//perimeter
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) if (!mp.get(x,y).empty()&&originalPerimiter[x][y]&&onPerimiter(mp,x,y)) {
			double d;
			if ((x==1||x==6)&&(y==1||y==6)) d = -4;
			else if ((x<=1||x>=6)&&(y<=1||y<=6)) d = -3;
			else if ((x<=2||x>=5)&&(y<=2||y<=5)) d = -0.5;
			else d = -1.0;
			if (mp.get(x,y).player()) total+=d;
			else total-=d;
		}
		
		//edges
		total+=4*doEdge(mp,0,0,1,0,true);
		total+=4*doEdge(mp,0,0,0,1,true);
		total+=4*doEdge(mp,0,7,1,0,true);
		total+=4*doEdge(mp,7,0,0,1,true);
		
		//Corners
		if (!mp.get(0,0).empty()) total += (mp.get(0,0).player() ? 12 : -12);
		if (!mp.get(0,7).empty()) total += (mp.get(0,7).player() ? 12 : -12);
		if (!mp.get(7,0).empty()) total += (mp.get(7,0).player() ? 12 : -12);
		if (!mp.get(7,7).empty()) total += (mp.get(7,7).player() ? 12 : -12);
		if (player) return total;
		else return -total;
	}
	private double doEdge(map mp, final int x1, final int y1, int xinc, int yinc,boolean first) {
		if (mp.get(x1,y1).empty()) return doReverse(mp,x1,y1,xinc,yinc,first);
		else {
			boolean allWay = true;
			boolean inStreak = true;
			boolean player = mp.get(x1,y1).player();
			int x = x1+xinc;
			int y = y1+yinc;
			int streak = 0;
			int total = 0;
			while (isValid(x+xinc,y+yinc)&&allWay) {
				if (mp.get(x,y).empty()) allWay=false;
				else {
					int piece = mp.get(x,y).player() ? 1 : -1;
					if (player!=(piece>0)) inStreak=false;
					if (inStreak) streak+=piece;
					total+=piece;
				}
				x+=xinc;
				y+=yinc;
			}
			if (mp.get(x,y).empty()) allWay=false;
			if (allWay) return total;
			else return streak+doReverse(mp,x1,y1,xinc,yinc,first);
		}
	}
	private boolean isValid(int x,int y) {
		return x>=0&&x<=7&&y>=0&&y<=7;
	}
	private double doReverse(map mp, int x1, int y1, int xinc, int yinc,boolean first) {
		if (!first) return 0.0;
		int x2 = x1+7*xinc;
		int y2 = y1+7*yinc;
		return doEdge(mp,x2,y2,-xinc,-yinc,false);
	}
	//move for first player
	public Movement move3(mapButton[][] buttons, int turns) {
		long l = System.nanoTime();
		map mp = new map(buttons);
		double power = multiplier*difficulty;
		if (turns>46) power *= 64;
		ArrayList<Movement> pmoves = mp.setAvailable(true);
		power/=pmoves.size();
		power-=1.0;
		Movement last = null;
		double vLast = 10000.0;
		for (Movement next: pmoves) {
			double d = value(mp,next,power,pmoves.size(), false, turns);
			if (d<vLast) {
				last=next;
				vLast=d;
			}
		}
		if (displayTimes) System.out.println((System.nanoTime()-l)/1000);
		return last;
	}
	
	private class VMove {
		public Movement move;
		public double value;
		public VMove(Movement move, double value) {
			this.move=move;
			this.value=value;
		}
	}
	private static int[] useOptions = useOptions();
	private double[] values(map m, Movement[] movements, double power, int optionsLast, int optionsNext, boolean player, int turns) {
		double[] out = new double[movements.length];
		power /= movements.length;
		for (int i = 0; i < movements.length;i++) {
			map mp = m.clone();
			mp.place(movements[i].x,movements[i].y,player);
			out[i] = value4(mp,power,optionsNext,optionsLast,!player,turns).value; //next and last intentionally reverted
		}
		return out;
	}
	private VMove value4(map mp, double power, int optionsLast, int optionsNext, boolean player, int turns) {
		if (power<1.0||power<optionsNext&&power<random.nextDouble()*optionsNext) {
			if (player) return new VMove(null,valueMap1(mp,optionsLast-optionsNext,turns));
			else return new VMove(null,valueMap1(mp,optionsNext-optionsLast,turns));
		} else {
			ArrayList<Movement> pmoves = mp.setAvailable(player);
			optionsNext = pmoves.size();
			if (optionsNext==0) {
				if (optionsLast==0) {
					int t = mp.squareDifference();
					if (t<0) return new VMove(null,200-t);
					else if (t==0) return new VMove(null,0);
					else return new VMove(null,-200-t);
				} else return value4(mp,power-1.0,optionsNext,optionsLast,!player,turns);
			}
			int numTest = useOptions[optionsNext];
			boolean doubleSelect = (power>400&&optionsNext>=6);
			double firstPower = power;
			if (doubleSelect) firstPower/=4;
			Movement[] moves = pmoves.toArray(new Movement[0]);
			double[] values = values(mp,moves,firstPower,optionsLast,optionsNext,player,turns);
			if (doubleSelect) {
				moves = new Movement[numTest];
				for (int i = 0; i < numTest; i++) moves[i] = pmoves.get(select(values,player,true));
				values = values(mp,moves,power-firstPower,optionsLast,optionsNext,player,turns);
			}
			int index = select(values,player,false);
			return new VMove(moves[index],values[index]);
		}
	}
	private static int[] useOptions() {
		int[] out = new int[64];
		for (int i = 0; i < 64; i++) out[i] = (int) (Math.sqrt(i));
		return out;
	}
	private int select(double[] values, boolean player,boolean remove) {
		if (!player) return selectMax(values,remove);
		else return selectMin(values,remove);
	}
	private int selectMin(double[] values,boolean remove) {
		double min = 10000000;
		int index = -1;
		for (int i = 0; i < values.length; i++) if (values[i]<min) {
			min=values[i];
			index=i;
		}
		if (remove&&index>=0) values[index]=10000000;
		return index;
	}
	private int selectMax(double[] values,boolean remove) {
		double max = -10000000;
		int index = -1;
		for (int i = 0; i < values.length; i++) if (values[i]>max) {
			max=values[i];
			index=i;
		}
		if (remove&&index>=0) values[index]=-10000000;
		return index;
	}
	private double valueMap1(map mp,int optionsDifference,int turns) {
		if (turns<=endGame) {
			if (turns<=32) return corner(mp,turns)+optionsDifference+mp.squareDifference()/8.1;
			else return corner(mp,turns)+optionsDifference;
		} else return corner(mp,turns)-mp.squareDifference();
	}
	private double mult4 = 1.0;
	public Movement move4(mapButton[][] buttons, int turns,boolean player,double boost) {
		long l = System.nanoTime();
		map mp = new map(buttons);
		double power = mult4*difficulty*boost;
		ArrayList<Movement> pmoves = mp.setAvailable(player);
		ArrayList<Movement> cmoves = mp.setAvailable(!player);
		Movement out = value4(mp,power,cmoves.size(),pmoves.size(),player,turns).move;
		if (out==null) {
			if (pmoves.size()==1) return pmoves.get(0);
			else return pmoves.get(random.nextInt(pmoves.size()));
		}
		if (displayTimes) System.out.println("move4 "+((System.nanoTime()-l)/1000));
		return out;
	}
	private void printMap(map mp) {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) System.out.print(mp.get(x,y));
			System.out.println();
		}
	}
}
