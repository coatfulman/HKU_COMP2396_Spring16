import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Lion extends Feline{
	/**
	 * @param x is the x coordinate of Lion obj
	 * @param y is the y coordinate of Lion obj
	 */
	public Lion(int x, int y){
		super(x,y);
		conNum = 4;
		animalName = "Lion";
		brief = 'l';
	}
	
	/* (non-Javadoc)
	 * @see Feline#fight(Animal, int, int)
	 */
	public void fight(Animal opponent){
		int oX = locX;
		int oY = locY;
		AnimalGui.forest[locX][locY] = AnimalGui.vacant;
		locX = opponent.locX;
		locY = opponent.locY;
		int chance = 0;
		if(opponent.animalType == "Canine")
			chance = 100;
		if(opponent.animalType == "Turtle")
			chance = 20;
		if(opponent.animalName == "Hippo")
			chance = 100;
		if(getInt(100) < chance){
			niceAttack(opponent);
			System.out.println(animalName+" moves from "+oX+", "+oY+" to "+opponent.locX+", "+opponent.locY);
		}else{
			failAttack();
		}
	}
	public JPanel getJpanel(){
		return new LionPanel();
	}
	public JPanel deadIcon(){
		return new DeadLion();
	}
}


/**
 * @author 89551 This is the panel for lion
 *
 */
class LionPanel extends JPanel{
	String path = AnimalGui.newGame.lionPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		
		try{BufferedImage pic = ImageIO.read(new File(path));
		
		g.drawImage(pic,0,0,100,100,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}

/**
 * @author 89551 This is the panel for dead lion
 *
 */
class DeadLion extends JPanel{
	String path = AnimalGui.newGame.lionPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);
	
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,50,50,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}