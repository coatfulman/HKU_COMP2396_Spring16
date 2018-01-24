import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
public class Turtle extends Animal {
	/**
	 * @param x is the x coordinate of Turtle obj
	 * @param y is the y coordinate of Turtle obj
	 */
	public Turtle(int x, int y){
		super(x,y);
		conNum=6;
		abs=3;
		brief = 'u';
		animalType = "Turtle";
		animalName = "Turtle";
		base = 90;
	}
	
	/* (non-Javadoc)
	 * @see Animal#nextMove()
	 */
	public int[] nextMove(){
 		int[] move = new int[2];
		boolean legit = false;
 		while(legit == false){
 			double radians = Math.toRadians(base*getInt(8));
 			move[0] = (int)(Math.cos(radians)*Math.sqrt(2));
 			move[1] = (int)(Math.sin(radians)*Math.sqrt(2));
 			if(inRange(locX+move[0], locY+move[1]) == true){
 				legit = true;
 			}
 		}
 		Random rand = new Random();
		if(rand.nextBoolean() == true){
			move[0] = move[1] = 0;
		}
		return move;
 	}
	
	/* (non-Javadoc)
	 * @see Animal#fight(Animal, int, int)
	 */
	public void fight(Animal opponent){
		int oX = locX;
		int oY = locY;
		AnimalGui.forest[locX][locY] = AnimalGui.vacant;
		locX = opponent.locX;
		locY = opponent.locY;
		int chance = 50;
		if(getInt(100) < chance){
			niceAttack(opponent);
			System.out.println(animalName+" moves from "+oX+", "+oY+" to "+opponent.locX+", "+opponent.locY);
		}else{
			failAttack();
		}
	}
	
	public JPanel getJpanel(){
		return new TurtlePanel();
	}
	
	public JPanel deadIcon(){
		return new DeadTurtle();
	}
	
}

/**
 * @author 89551 This is the panel for turtle
 *
 */
class TurtlePanel extends JPanel{
	String path = AnimalGui.newGame.turtlePath;
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
	
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,100,100,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}

/**
 * @author 89551 This is the panel for dead turtle
 *
 */
class DeadTurtle extends JPanel{
	String path = AnimalGui.newGame.turtlePath;
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);
		
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,50,50,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}