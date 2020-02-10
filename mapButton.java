import java.awt.Panel;
import java.awt.Graphics;
import java.awt.image.VolatileImage;
import java.awt.geom.Ellipse2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class mapButton extends Panel {
	private final int x;
	private final int y;
	private boolean empty = true;
	private boolean playerPiece = false;
	private boolean available = false;
	private VolatileImage v;
	private boolean redX = false;
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(0,0,50,50);
		g.setColor(Color.black);
		g.draw3DRect(2,2,46,46,true);
		if (!empty&&!playerPiece) g.setColor(Color.white);
		if (!empty) {
			g.fillOval(4,4,42,42);
		} else if (available) {
			g.drawLine(4,4,42,42);
			g.drawLine(42,4,4,42);
		}
		if (redX) {
			g.setColor(Color.red);
			g.drawLine(4,4,42,42);
			g.drawLine(42,4,4,42);
		}
		super.paint(g);
	}

	/**
	 * Method renderImage
	 *
	 *
	 */
	public void renderImage() {
		do {
			Graphics2D g = v.createGraphics();
			g.setColor(Color.black);
			g.draw3DRect(2,2,46,46,true);
			if (!empty&&!playerPiece) g.setColor(Color.white);
			if (!empty) {
				Ellipse2D.Double e = new Ellipse2D.Double(4.0,4.0,42.0,42.0);
				g.fill(e);
			} else if (available) {
				Line2D.Double line1 = new Line2D.Double(4.0,4.0,42.0,42.0);
				Line2D.Double line2 = new Line2D.Double(42.0,4.0,4.0,42.0);
				g.draw(line1);
				g.draw(line2);
			}
			if (redX) {
				g.setColor(Color.red);
				Line2D.Double line1 = new Line2D.Double(4.0,4.0,42.0,42.0);
				Line2D.Double line2 = new Line2D.Double(42.0,4.0,4.0,42.0);
				g.draw(line1);
				g.draw(line2);
			}
			if (v.validate(getGraphicsConfiguration())==VolatileImage.IMAGE_INCOMPATIBLE) {
				v = createVolatileImage(50,50);
			}
			g.dispose();
		} while (v.contentsLost());
	}

	/**
	 * Method available
	 *
	 *
	 * @return
	 *
	 */
	public boolean available() {
		return available;
	}

	/**
	 * Method empty
	 *
	 *
	 * @return
	 *
	 */
	public boolean empty() {
		return empty;
	}

	/**
	 * Method playerPiece
	 *
	 *
	 * @return
	 *
	 */
	public boolean playerPiece() {
		return playerPiece;
	}

	/**
	 * Method setEmpty
	 *
	 *
	 * @param empty
	 *
	 */
	public void setEmpty(boolean empty) {
		this.empty=empty;
		if (empty) playerPiece=false;
		else available=false;
		paint(getGraphics());
	}

	/**
	 * Method setAvailable
	 *
	 *
	 * @param available
	 *
	 */
	public void setAvailable(boolean available) {
		this.available=available;
		paint(getGraphics());
	}

	/**
	 * Method setPlayer
	 *
	 *
	 * @param player
	 *
	 */
	public void setPlayer(boolean player) {
		playerPiece=player;
		paint(getGraphics());
	};

	/**
	 * Method mapButton
	 *
	 *
	 * @param x
	 * @param y
	 *
	 */
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public mapButton(int x, int y) {
		this.x=x;
		this.y=y;
		setBackground(Color.green);
	}
	public void setRedX(boolean redX) {
		this.redX=redX;
		paint(getGraphics());
	}
}
