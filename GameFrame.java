
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GameFrame extends Frame {
	private final mapButton[][] buttons = new mapButton[8][8];
	private int difficulty;
	private MoveCalculator computer;
	private int numTurns;
	private boolean unlocked = true;
	private boolean changed = false;
	private static final String saveFiles = "saveFiles\\";
	private final MainMenu menu;
	/**
	 * Method GameFrame
	 *
	 *
	 */
	public GameFrame(int difficulty, MainMenu menu) {
		this.difficulty=difficulty;
		computer=new MoveCalculator(difficulty);
		numTurns=0;
		this.menu=menu;
		addComponents();
		buttons[3][3].setEmpty(false);
		buttons[3][4].setEmpty(false);
		buttons[4][3].setEmpty(false);
		buttons[4][4].setEmpty(false);
		buttons[3][3].setPlayer(false);
		buttons[3][4].setPlayer(true);
		buttons[4][3].setPlayer(true);
		buttons[4][4].setPlayer(false);
		setAvailable();
		//computerTest(1,0);
	}
	public GameFrame(String fileSave,MainMenu menu) throws IOException {
		FileInputStream input = new FileInputStream(saveFiles+fileSave+".dat");
		numTurns=input.read();
		difficulty=255*input.read()+input.read();
		computer=new MoveCalculator(difficulty);
		addComponents();
		try {
			for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) {
				int next = input.read();
				if (next>0) {
					buttons[x][y].setEmpty(false);
					buttons[x][y].setPlayer(next==1);
				}
			}
		} catch (IOException e) {
			setVisible(false);
			setEnabled(false);
			input.close();
			throw e;
		}
		input.close();
		this.menu=menu;
		setAvailable();
	}	
	private void addComponents() {
		setResizable(false);
		setLayout(new GridLayout(8,8));
		setTitle("Othello");
		setSize(410,445);
		setLocation(275,180);
		setMinimumSize(new Dimension(410,445));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		MenuBar b = new MenuBar();
		Menu m = new Menu("File");
		MenuItem i = new MenuItem("Menu");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.NO_OPTION;
				if (changed) answer = JOptionPane.showConfirmDialog(GameFrame.this,"Do you wish to save before returning to the main menu?","Save?",JOptionPane.YES_NO_CANCEL_OPTION);
				if (answer == JOptionPane.NO_OPTION||answer == JOptionPane.YES_OPTION) {
					if (answer == JOptionPane.YES_OPTION) {
						save();
					}
					setVisible(false);
					menu.setVisible(true);
					setEnabled(false);
				}
			}
		});
		m.add(i);
		i = new MenuItem("Save");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		m.add(i);
		i = new MenuItem("New");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.NO_OPTION;
				if (changed) answer = JOptionPane.showConfirmDialog(GameFrame.this,"Do you wish to save before starting a new game?","Save?",JOptionPane.YES_NO_CANCEL_OPTION);
				if (answer == JOptionPane.NO_OPTION||answer == JOptionPane.YES_OPTION) {
					if (answer == JOptionPane.YES_OPTION) {
						save();
					}
					int difNext = 0;
					boolean gotGoodInput = false;
					do {
						try {
							difNext = Integer.parseInt(JOptionPane.showInputDialog(GameFrame.this,"What difficulty would you like to play at? This may be a number between 1 and 10.","Dificulty Level",JOptionPane.QUESTION_MESSAGE));
							if (difNext>=1&&difNext<=10) gotGoodInput=true;
						} catch (NumberFormatException E) {
						}
					} while (!gotGoodInput);
					setVisible(false);
					new GameFrame(difNext, menu);
					setEnabled(false);
				}
			}
		});
		m.add(i);
		i = new MenuItem("Load");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.NO_OPTION;
				if (changed) answer = JOptionPane.showConfirmDialog(GameFrame.this,"Do you wish to save before starting a new game?","Save?",JOptionPane.YES_NO_CANCEL_OPTION);
				if (answer == JOptionPane.NO_OPTION||answer == JOptionPane.YES_OPTION) {
					if (answer == JOptionPane.YES_OPTION) {
						save();
					}
					GameFrame frameNext = null;
					boolean gotGoodInput = false;
					do {
						try {
							frameNext = new GameFrame(JOptionPane.showInputDialog(GameFrame.this,"Which file would you like to load?.","Dificulty Level",JOptionPane.QUESTION_MESSAGE),menu);
							gotGoodInput=true;
						} catch (IOException E) {
							if (JOptionPane.showConfirmDialog(GameFrame.this,"The system cannot load the given file. Would you like to try again?","Loading Failure",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION) gotGoodInput=true;
						}
					} while (!gotGoodInput);
					if (frameNext!=null) {
						setVisible(false);
						setEnabled(false);
					}
				}
			}
		});
		m.add(i);
		i = new MenuItem("Exit");
		i.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		m.add(i);
		b.add(m);
		setMenuBar(b);
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) {
			buttons[x][y] = new mapButton(x,y);
			final int X = x;
			final int Y = y;
			buttons[x][y].addMouseListener(new MouseListener() {
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mouseClicked(MouseEvent e) {
					if (!unlocked||!buttons[X][Y].available()) return;
					unlocked=false;
					changed=true;
					place(X,Y,true);
					numTurns++;
					do {
						if (getAvailable(false).size()>0) {
							Movement move = computer.move4(buttons,numTurns,false,1.0);
							try {
								Thread.sleep(1500);
							} catch (InterruptedException E) {
							}
							place(move.x,move.y,false);
							numTurns++;
						}
					} while (getAvailable(true).size()==0&&getAvailable(false).size()>0);
					if (getAvailable(true).size()==0&&getAvailable(false).size()==0) endGame();
					setAvailable();
					new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(50);
							} catch (InterruptedException E) {
							}
							unlocked=true;
						}
					}).start();
				}
			});
			add(buttons[x][y]);
		}
		pack();
		setVisible(true);
	}	
	private void setAvailable() {
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) if (buttons[x][y].empty()) {
			if (testRow(x,y,-1,-1,true)||testRow(x,y,-1,0,true)||testRow(x,y,-1,1,true)||testRow(x,y,0,-1,true)||testRow(x,y,0,1,true)||testRow(x,y,1,-1,true)||testRow(x,y,1,0,true)||testRow(x,y,1,1,true)) {
				buttons[x][y].setAvailable(true);
			}
		}
	}
	private ArrayList<Movement> getAvailable(boolean player) {
		ArrayList<Movement> out = new ArrayList<Movement>();
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) if (buttons[x][y].empty()) {
			if (testRow(x,y,-1,-1,player)||testRow(x,y,-1,0,player)||testRow(x,y,-1,1,player)||testRow(x,y,0,-1,player)||testRow(x,y,0,1,player)||testRow(x,y,1,-1,player)||testRow(x,y,1,0,player)||testRow(x,y,1,1,player)) {
				out.add(new Movement(x,y));
			}
		}
		return out;
	}
	private boolean testRow(int x1, int y1, int xinc, int yinc, boolean player) {
		
		//First piece on row
		int x2 = x1+xinc;
		int y2 = y1+yinc;
		
		//Test start to row
		if (x2>=8||x2<0||y2>=8||y2<0||buttons[x2][y2].empty()||buttons[x2][y2].playerPiece()==player) return false;
		
		//Update for next test
		x2+=xinc;
		y2+=yinc;
		
		//Go through row
		while (x2>=0&&x2<8&&y2>=0&&y2<8) {
			
			//If the line ends with a space nothing can be flipped
			if (buttons[x2][y2].empty()) return false;
			
			//If, however, the row ends with a peice belonging to the player doing the flipping,
			//The row can be flipped.
			else if (buttons[x2][y2].playerPiece()==player) return true;
			
			//Next piece in row
			x2+=xinc;
			y2+=yinc;
		}
		
		//The row cannot be flipped if it runs into a side.
		return false;
	}
	private void place(int x, int y, boolean player) {
		buttons[x][y].setEmpty(false);
		buttons[x][y].setPlayer(player);
		
		//Mark placed piece
		buttons[x][y].setRedX(true);
		
		//Invalidate extra user input
		for (int xx = 0; xx < 8; xx++) for (int yy = 0; yy < 8; yy++) buttons[xx][yy].setAvailable(false);
		
		//Pause to allow player to see move
		try {
			Thread.sleep(2000); //2000
		} catch (InterruptedException e) {
		}
		
		//Flip rows
		for (int xinc = -1; xinc <= 1; xinc++) for (int yinc = -1; yinc <= 1; yinc++)
			if (testRow(x,y,xinc,yinc,player)) changeRow(x,y,xinc,yinc,player);
		
		//Remove Mark for placed piece
		buttons[x][y].setRedX(false);
	}
	private void changeRow(int x1, int y1, int xinc, int yinc, boolean player) {
		
		//Pause to allow player to see move
		try {
			Thread.sleep(500); //500
		} catch (InterruptedException e) {
		}
		
		//Set first piece on row
		int x2 = x1+xinc;
		int y2 = y1+yinc;
		
		//Go through row
		while (buttons[x2][y2].playerPiece()!=player) {
			
			//Flip piece
			buttons[x2][y2].setPlayer(player);
			
			//Next piece
			x2+=xinc;
			y2+=yinc;
		}
	}
	private int squareDifference() {
		int out = 0;
		for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) if (!buttons[x][y].empty()) {
			if (buttons[x][y].playerPiece()) out++;
			else out--;
		}
		return out;
	}
	private void endGame() {
		int answer;
		int sqrdif = squareDifference();
		if (sqrdif>0) answer = JOptionPane.showConfirmDialog(this,"You Win! Do you want to start another game?","You Win!",JOptionPane.YES_NO_OPTION);
		else if (sqrdif==0) answer = JOptionPane.showConfirmDialog(this,"Stalemate. Do you want to start another game?","Stalemate",JOptionPane.YES_NO_OPTION);
		else answer = JOptionPane.showConfirmDialog(this,"Sorry, you lose. Do you want to start another game?","You Lose",JOptionPane.YES_NO_OPTION);
		if (answer==JOptionPane.YES_OPTION) {
			int difNext = 0;
			boolean gotGoodInput = false;
			do {
				try {
					difNext = Integer.parseInt(JOptionPane.showInputDialog(this,"What difficulty would you like to play at? This may be a number between 1 and 10.","Dificulty Level",JOptionPane.QUESTION_MESSAGE));
					if (difNext>=1&&difNext<=10) gotGoodInput=true;
				} catch (NumberFormatException E) {
				}
			} while (!gotGoodInput);
			setVisible(false);
			new GameFrame(difNext, menu);
			setEnabled(false);
		}
		changed=false;
	}
	private void save() {
		if (!new File(saveFiles).exists()) new File(saveFiles).mkdir();
		boolean gotGoodInput=false;
		boolean exceptionLast=false;
		do {
			try {
				int answer=JOptionPane.YES_OPTION;
				if (exceptionLast) answer = JOptionPane.showConfirmDialog(this, "There has been a failure trying to save the current game. Do you wish to try again?","Failure to save", JOptionPane.YES_NO_OPTION);
				exceptionLast=false;
				if (answer==JOptionPane.YES_OPTION) {
					String output = saveFiles+JOptionPane.showInputDialog(this,"What would you like to save the game as?","Name of file",JOptionPane.QUESTION_MESSAGE)+".dat";
					if (new File(output).exists()) answer=JOptionPane.showConfirmDialog(this,"File already exists. Overwrite?","Overwrite",JOptionPane.YES_NO_CANCEL_OPTION);
					else answer=JOptionPane.YES_OPTION;
					if (answer==JOptionPane.YES_OPTION) {
						FileOutputStream out=new FileOutputStream(output);
						out.write(numTurns);
						out.write(difficulty/255);
						out.write(difficulty);
						for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) {
							if (buttons[x][y].empty()) out.write(0);
							else if (buttons[x][y].playerPiece()) out.write(1);
							else out.write(2);
						}
						gotGoodInput=true;
					} else if (answer==JOptionPane.CANCEL_OPTION) gotGoodInput=true;
				} else gotGoodInput=true;
			} catch (IOException e) {
				exceptionLast=true;
			}
		} while (!gotGoodInput);		
	}
	private void exit() {
		int answer = JOptionPane.NO_OPTION;
		if (changed) answer = JOptionPane.showConfirmDialog(this,"Do you wish to save before exiting?","Save before exit?",JOptionPane.YES_NO_CANCEL_OPTION);
		if (answer == JOptionPane.NO_OPTION||answer == JOptionPane.YES_OPTION) {
			if (answer == JOptionPane.YES_OPTION) {
				save();
			}
			System.exit(0);
		}
	}
	private void computerTest(int number,int numWins) {
		numTurns = 6;
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			map mp1 = new map(buttons);
			ArrayList<Movement> pmoves1 = mp1.setAvailable(true);
			if (pmoves1.size()>0) {
				Movement move = pmoves1.get(0);
				if (pmoves1.size()>1) move = pmoves1.get(random.nextInt(pmoves1.size()));
				place(move.x,move.y,true);
			}
			map mp2 = new map(buttons);
			ArrayList<Movement> pmoves2 = mp2.setAvailable(false);
			if (pmoves2.size()>0) {
				Movement move = pmoves2.get(0);
				if (pmoves2.size()>1) move = pmoves2.get(random.nextInt(pmoves2.size()));
				place(move.x,move.y,false);
			}
		}
		long l;
		long out;
		while (getAvailable(true).size()!=0||getAvailable(false).size()!=0) {
			//System.out.println(numTurns);
			l = System.nanoTime();
			//for (int i = 0; i < 1000; i++) computer.move4(buttons,numTurns);
			out = System.nanoTime()-l;
			//System.out.println("move4: "+out);
			Movement move1 = computer.move4(buttons,numTurns,true,1.0);
			try {
				Thread.sleep(5);
			} catch (InterruptedException E) {
			}
			place(move1.x,move1.y,true);
			numTurns++;
			do {
				if (getAvailable(false).size()>0) {
					l = System.nanoTime();
					//for (int i = 0; i < 100; i++) computer.move(buttons,numTurns);
					out = System.nanoTime()-l;
					//System.out.println("move: "+out);
					Movement move = computer.move4(buttons,numTurns,false,16.0);
					try {
						Thread.sleep(5);
					} catch (InterruptedException E) {
					}
					place(move.x,move.y,false);
					numTurns++;
				}
			} while (getAvailable(true).size()==0&&getAvailable(false).size()>0);
		}
		int dif = squareDifference();
		if (dif>0) numWins++;
		else if (dif==0) number--;
		final int nw = numWins;
		final int nbr = number;
		if (number<40) SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				reset(nbr+1,nw);
			}
		});
		else {
			System.out.println("Difficulty: "+difficulty+" Times: "+number+" wins: "+numWins);
			System.exit(0);
		}
	}
	public void reset(int number,int numWins) {
		numTurns=0;
		unlocked=true;
		changed=false;
		for (int y = 0; y < 8; y++) for (int x = 0; x < 8; x++) buttons[y][x].setEmpty(true);
		buttons[3][3].setEmpty(false);
		buttons[3][4].setEmpty(false);
		buttons[4][3].setEmpty(false);
		buttons[4][4].setEmpty(false);
		buttons[3][3].setPlayer(false);
		buttons[3][4].setPlayer(true);
		buttons[4][3].setPlayer(true);
		buttons[4][4].setPlayer(false);
		//setAvailable();
		computerTest(number,numWins);
	}
}
