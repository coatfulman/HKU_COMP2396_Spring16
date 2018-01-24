import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Wolf extends Canine {
	/**
	 * @param x is the x coordinate of Wolf obj
	 * @param y is the y coordinate of Wolf obj
	 */
	public Wolf(int x, int y){
		super(x,y);
		conNum = 7;
		animalName = "Wolf";
		brief = 'w';
	}
	public JPanel getJpanel(){
		return new WolfPanel();
	}
	public JPanel deadIcon(){
		return new DeadWolf();
	}
}

/**
 * @author 89551 This is the panel for wolf
 *
 */
class WolfPanel extends JPanel{
	String path = AnimalGui.newGame.wolfPath;
	public void paintComponent(Graphics g){
		
		g.setColor(Color.white);

		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,100,100,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}

/**
 * @author 89551 This is the panel for dead wolf
 *
 */
class DeadWolf extends JPanel{
	String path = AnimalGui.newGame.wolfPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);
	
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,50,50,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}