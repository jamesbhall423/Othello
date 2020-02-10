
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JOptionPane;

public class MainMenu extends Frame {
	
	/**
	 * Method MainMenu
	 *
	 *
	 */
	public MainMenu() {
		setResizable(false);
		setLayout(new GridLayout(2,2));
		setSize(400,200);
		setLocation(275,280);
		setTitle("Othello");
		setMinimumSize(new Dimension(400,200));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		final Panel newGame = new Panel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.red);
				g.setFont(new Font("Normal",Font.BOLD,20));
				g.drawString("NEW GAME", 20,60);
				super.paint(g);
			}
		};
		newGame.setBackground(Color.white);
		newGame.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent e) {
				newGame.setBackground(Color.black);
			}
			public void mouseExited(MouseEvent e) {
				newGame.setBackground(Color.white);
			}
			public void mouseClicked(MouseEvent e) {
				int difNext = 0;
				boolean gotGoodInput = false;
				do {
					try {
						difNext = Integer.parseInt(JOptionPane.showInputDialog(MainMenu.this,"What difficulty would you like to play at? This may be a number between 1 and 10.","Dificulty Level",JOptionPane.QUESTION_MESSAGE));
						if (difNext>=1&&difNext<=10) gotGoodInput=true;
					} catch (NumberFormatException E) {
					}
				} while (!gotGoodInput);
				setVisible(false);
				new GameFrame(difNext, MainMenu.this);
			}
			public void mousePressed(MouseEvent e) {
				
			}
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		final Panel loadGame = new Panel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.red);
				g.setFont(new Font("Normal",Font.BOLD,20));
				g.drawString("LOAD GAME", 20,60);
				super.paint(g);
			}
		};
		loadGame.setBackground(Color.white);
		loadGame.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent e) {
				loadGame.setBackground(Color.black);
			}
			public void mouseExited(MouseEvent e) {
				loadGame.setBackground(Color.white);
			}
			public void mouseClicked(MouseEvent e) {
				GameFrame frameNext = null;
				boolean gotGoodInput = false;
				do {
					try {
						frameNext = new GameFrame(JOptionPane.showInputDialog(MainMenu.this,"Which file would you like to load?.","Dificulty Level",JOptionPane.QUESTION_MESSAGE),MainMenu.this);
						gotGoodInput=true;
					} catch (IOException E) {
						if (JOptionPane.showConfirmDialog(MainMenu.this,"The system cannot load the given file. Would you like to try again?","Loading Failure",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION) gotGoodInput=true;
					}
				} while (!gotGoodInput);
				setVisible(false);
			}
			public void mousePressed(MouseEvent e) {
				
			}
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		final Panel instructions = new Panel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.red);
				g.setFont(new Font("Normal",Font.BOLD,15));
				g.drawString("INSTRUCTIONS", 20,60);
				super.paint(g);
			}
		};
		instructions.setBackground(Color.white);
		instructions.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent e) {
				instructions.setBackground(Color.black);
			}
			public void mouseExited(MouseEvent e) {
				instructions.setBackground(Color.white);
			}
			public void mouseClicked(MouseEvent e) {
				new Instructions(MainMenu.this);
				setVisible(false);
			}
			public void mousePressed(MouseEvent e) {
				
			}
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		final Panel exit = new Panel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(Color.red);
				g.setFont(new Font("Normal",Font.BOLD,20));
				g.drawString("EXIT", 60,60);
				super.paint(g);
			}
		};
		exit.setBackground(Color.white);
		exit.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent e) {
				exit.setBackground(Color.black);
			}
			public void mouseExited(MouseEvent e) {
				exit.setBackground(Color.white);
			}
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
			public void mousePressed(MouseEvent e) {
				
			}
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		add(newGame);
		add(loadGame);
		add(instructions);
		add(exit);
		pack();
		setVisible(true);
	}	
}
