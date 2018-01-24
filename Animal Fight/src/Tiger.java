import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Tiger extends Feline {
	/**
	 * @param x is the x coordinate of Tiger obj
	 * @param y is the y coordinate of Tiger obj
	 */
	public Tiger(int x, int y){
		super(x,y);
		conNum = 5;
		animalName = "Tiger";
		brief = 't';	
	}
	public JPanel getJpanel(){
		return new TigerPanel();
	}
	public JPanel deadIcon(){
		return new DeadTiger();
	}
}

/**
 * @author 89551 This is the panel for tiger
 *
 */
class TigerPanel extends JPanel{
	String path = AnimalGui.newGame.tigerPath;
	public void paintComponent(Graphics g){
		
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,100,100,this);
		}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}	
}

/**
 * @author 89551 This is the panel for dead tiger
 *
 */
class DeadTiger extends JPanel{
	String path = AnimalGui.newGame.tigerPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);
		
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,50,50,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}
