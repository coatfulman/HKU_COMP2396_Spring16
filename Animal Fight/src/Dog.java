import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Dog extends Canine {
	/**
	 * @param x is the x coordinate of Dog obj
	 * @param y is the y coordinate of Dog obj
	 */
	public Dog(int x, int y){
		super(x,y);
		conNum = 1;
		animalType = "Canine";
		animalName = "Dog";
		brief = 'd';
	}
	
	public JPanel getJpanel(){
		return new DogPanel();
	}
	
	public JPanel deadIcon(){
		return new DeadDog();
	}
}
/**
 * @author 89551 This is the panel for dog
 *
 */
class DogPanel extends JPanel{
	String path = AnimalGui.newGame.dogPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,100,100,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}

/**
 * @author 89551 This is the panel for dead dog
 *
 */
class DeadDog extends JPanel{
	String path = AnimalGui.newGame.dogPath;
	public void paintComponent(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 100, 100);
		
		try{BufferedImage pic = ImageIO.read(new File(path));
		g.drawImage(pic,0,0,50,50,this);}catch(IOException ex){}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
}