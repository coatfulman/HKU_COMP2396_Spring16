import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Hippo extends Animal {
	/**
	 * @param x is the x coordinate of Hippo obj
	 * @param y is the y coordinate of Hippo obj
	 */
	public Hippo(int x, int y){
		super(x,y);	
		conNum = 3;
		abs=3;
		base = 90;
		brief = 'h';
		animalName = "Hippo";
		animalType = "Hippo";
		
	}
	
	/* (non-Javadoc)
	 * @see Animal#fight(Animal, int, int)
	 */
	public void fight(Animal opponent){	
		AnimalGui.forest[locX][locY] = AnimalGui.vacant;
		locX = opponent.locX;
		locY = opponent.locY;
		failAttack();
	}
	
	public JPanel getJpanel(){
		return new HippoPanel();
	}
	
	public JPanel deadIcon(){
		return new DeadHippo();
	}
	
}

/**
 * @author 89551 This is the panel for hippo
 *
 */
class HippoPanel extends JPanel{
	String path = AnimalGui.newGame.hippoPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		
		try{BufferedImage pic = ImageIO.read(new File(path));
		
		g.drawImage(pic,0,0,100,100,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}

/**
 * @author 89551 This is the panel for dead hippo
 *
 */
class DeadHippo extends JPanel{
	String path = AnimalGui.newGame.hippoPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);
		
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,50,50,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}