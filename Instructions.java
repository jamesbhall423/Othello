
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Instructions extends Frame {
	private String[] showFrame = {"Instructions","Objective","Playing the Game","Taking a turn","Tips"};
	private int toShow=0;
	/**
	 * Method Instructions
	 *
	 *
	 */
	public Instructions(final MainMenu menu) {
		setTitle("Instructions");
		setBackground(Color.green);
		setMinimumSize(new Dimension(410,470));
		setLocation(275,180);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				setEnabled(false);
				menu.setVisible(true);
			}
		});
		MenuBar bar = new MenuBar();
		Menu file = new Menu("File");
		MenuItem exit = new MenuItem("MainMenu");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				setEnabled(false);
				menu.setVisible(true);
			}
		});
		file.add(exit);
		bar.add(file);
		Menu view = new Menu("view");
		for (int i = 0; i < showFrame.length; i++) {
			final int I = i;
			MenuItem next = new MenuItem(showFrame[I]);
			next.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					toShow=I;
					paint(getGraphics());
				}
			});
			view.add(next);
		}
		bar.add(view);
		setMenuBar(bar);
		setResizable(false);
		setVisible(true);
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.green);
		g.clearRect(0,0,410,470);
		g.setColor(Color.black);
		g.setFont(new Font("Normal",Font.PLAIN,15));
		switch (toShow) {
			case 0: //Instructions
			g.drawString("Access instructions through the view menu.",10,200);
			break;
			case 1: //Objective
			g.drawString("Your peices are black circles.",10,70);
			g.drawString("The computers peices are white circles",10,90);
			g.drawString("The objective is to have more pieces ",10,110);
			g.drawString("than the computer at the end of the game.",10,130);
			g.fillOval(10,200,100,100);
			g.setColor(Color.white);
			g.fillOval(290,200,100,100);
			g.setColor(Color.black);
			g.drawString("Your piece",10,320);
			g.drawString("Computer's piece",290,320);
			break;
			case 2: //Playing the Game
			g.drawString("The game alternates in a series of turns",10,70);
			g.drawString("Between you and the computer.",10,90);
			g.drawString("Players that can't move are skipped.",10,110);
			g.drawString("The game ends when neither player can move.",10,130);
			g.drawString("Spaces where you can move are marked with an X.",10,150);
			g.drawString("Take your turn by clicking ",10,170);
			g.drawString("one of the availible squares.",10,190);
			g.drawString("The last placed pice is marked with a red X.",10,210);
			g.drawLine(10,220,110,320);
			g.drawLine(110,220,10,320);
			g.drawString("Availible spot.",10,340);
			g.fillOval(290,220,100,100);
			g.drawString("Placed piece.",290,340);
			g.setColor(Color.red);
			g.drawLine(290,220,390,320);
			g.drawLine(390,220,290,320);
			break;
			case 3: //Taking a turn
			g.drawString("When you take a turn, one of your pieces",10,70);
			g.drawString("Marks its location.",10,90);
			g.drawString("Every computer peice on a direct horezontal, virtical,",10,110);
			g.drawString("or diaganal line between the placed peice",10,130);
			g.drawString("and one of your other pieces becomes your piece.",10,150);
			g.drawString("A move is availible if and only if",10,170);
			g.drawString("Such a change takes place. ",10,190);
			g.drawString("A mirror proccess works for the computer.",10,210);
			g.fillOval(10,335,100,100);
			g.fillOval(150,335,100,100);
			g.fillOval(290,335,100,100);
			g.fillOval(290,215,100,100);
			g.drawString("Available",10,327);
			g.drawString("Computer",150,327);
			g.drawString("Your Piece",290,327);
			g.drawString("Placed",10,452);
			g.drawString("Flipped",150,452);
			g.drawString("Your Piece",290,452);
			g.drawLine(10,215,110,315);
			g.drawLine(110,215,10,315);
			g.setColor(Color.white);
			g.fillOval(150,215,100,100);
			g.setColor(Color.red);
			g.drawLine(10,335,110,435);
			g.drawLine(110,335,10,435);
			break;
			case 4: //Tips
			g.drawString("Try to put your peices in the corners.",10,70);
			g.drawString("It is not usually advantageous to take as",10,90);
			g.drawString("many peices as possible early in the game",10,110);
			g.drawString("You can learn a lot from the computer.",10,130);
			break;
		}
		super.paint(g);
	}
}
