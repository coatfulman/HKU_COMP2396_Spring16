import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
public class Cat extends Feline {
	/**
	 * @param x is the x coordinate of Cat obj
	 * @param y is the y coordinate of Cat obj
	 */
	public Cat(int x, int y){
		super(x,y);
		conNum = 0;
		animalName = "Cat";
		brief = 'c';
		
	}
	/* (non-Javadoc)
	 * @see Animal#getJpanel()
	 */
	public JPanel getJpanel(){
		return new CatPanel();
	}
	/* (non-Javadoc)
	 * @see Animal#deadIcon()
	 */
	public JPanel deadIcon(){
		return new DeadCat();
	}
	
}

/**
 * @author 89551 This is the panel for cat
 *
 */
class CatPanel extends JPanel{
	String path = AnimalGui.newGame.catPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,100,100,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}

/**
 * @author 89551 this is the panel for dead cat
 *
 */
class DeadCat extends JPanel{
	String path = AnimalGui.newGame.catPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,50,50,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}

