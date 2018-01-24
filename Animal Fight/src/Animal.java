import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
/**
 * @author 89551 This is Animal super class
 *
 */
public class Animal {
	/**
	 *  Brief is abbreviation of animal, e.g, u/U
	 */
	public char brief;
	/**
	 *  animalType includes Fenine, Canine, etc.
	 */
	public String animalType;
	/**
	 *  animalName includes cat, lion, etc.
	 */
	public String animalName;
	/**
	 *  (locX, locY) is the current location of the animal object
	 */
	public int locX, locY;
	/**
	 *  True means alive, false means dead.
	 */
	public boolean status;
	/**
	 *  This is relevant to the move of animal. Feline is 45, others are 90.
	 */
	public int base = 0;
	
	/**
	 * This is used to convert animal class to animal panel
	 */
	public int conNum;
	
	public String path;
	
	/**
	 *  This is set to aid gui for user to choose move
	 *  1 for Feline
	 *  2 for Canine
	 *  3 for Hippo and Turtle
	 */
	public int abs;
	/**
	 *  This constructor is used to create "NULL animals" only.
	 */
	public Animal(){
		abs=4;
		conNum=8;
		animalName = ".";
		animalType = ".";
		brief = '.';
		status = true;
	}
	
 	/**
 	 * @param x is x coordinate
 	 * @param y is y coordinate
 	 */
 	public Animal(int x, int y) {
		locX = x;
		locY = y;
		status = true;
	}
 	
 	/**
 	 * @param x is the x coordinate the animal wants to move.
 	 * @param y is the y coordinate the animal wants to move.
 	 * It should be guaranteed that (x, y) is vacant;
 	 * Notice this function doesn't clear current position
 	 */
 	
 	public void move(int x, int y){
 		if(x!=locX || y!=locY)
 			System.out.println(animalName+" moved from "+locX+", "+locY+" to "+x+", "+y);
 		locX = x;
 		locY = y;
 		AnimalGui.forest[locX][locY] = this;    
 	}
 	
 	/**
 	 * @param x is dest of x coord
 	 * @param y is dest of y coord
 	 */
 	public void specialMove(int x, int y){
 		System.out.println(animalName+" dies at "+x+", "+y);
 		locX = x;
 		locY = y;
 		AnimalGui.forest[locX][locY] = this;
 	}
 	
 	/**
 	 * @param x is the x coordinate the animal wants to move to.
 	 * @param y is the y coordinate the animal wants to move to.
 	 * @return Whether is coordinate is out of the forest range.
 	 */
 	public boolean inRange(int x, int y){
 		if(x>=0 && x<15 && y>=0 && y<15)
 			return true;
 		else
 			return false;
 	}
 	
 	/**
 	 * @return a int array containing the next valid coordinate (x, y);
 	 */
 	public int[] nextMove(){
 		int[] move = new int[2];
 		boolean legit = false;
 		while(legit == false){
 			double radians = Math.toRadians(base*getInt(8));
 			move[0] = (int)(Math.cos(radians)*Math.sqrt(2));
 			move[1] = (int)(Math.sin(radians)*Math.sqrt(2));
 			if(inRange(locX+move[0], locY+move[1]) == true)
 				legit = true;
 		}
 		return move;
 	}
 	
 	/**
 	 * @param x is the range of the random number we want.
 	 * @return a integer in the range of [0,x).
 	 */
 	public int getInt(int x){
 		Random rand = new Random();
 		return rand.nextInt(x);
 	}
 	 	
 	/**
 	 * @param chance is the probability the attacter wins
 	 * @return whether the attacker wins, if win, return 1, else, return 0;
 	 */
 	public int getResult(int chance){
 		Random rand = new Random();
 		if(rand.nextInt(100)<chance){
 			return 1;
 		}else{
 			return 0;
 		}
 	}
 	
 	/**
 	 * @param opponent is the animal the obj wants to attack.
 	 * @param x is x coordinate of the animal attacked.
 	 * @param y is y coordinate of the animal attacked.
 	 */
 	public void fight(Animal opponent){
 		return;
 	}
 	
 	/**
 	 * @param x is x coordinate of move destination
 	 * @param y is similar to x
 	 * This function help to decide whether to attack, move or stop.
 	 */
 	public void intendMove(int x, int y){
 		if(x!=locX || y!=locY)
 		if(AnimalGui.forest[x][y].equals(AnimalGui.vacant)){
 			AnimalGui.forest[locX][locY] = AnimalGui.vacant;
 			move(x, y);
 		}else if(AnimalGui.forest[x][y].status == false){
 			return;
 		}else{
 			System.out.print(animalName+" from "+locX+", "+locY+" attacks "+AnimalGui.forest[x][y].animalName+" at "+x+", "+y);
 			fight(AnimalGui.forest[x][y]);
 		}
 		int cnt = 0;
 		for(Animal ani: AnimalGui.newGame.animal){
 			if(ani.status == true)
 				cnt++;
 		}
 		if(cnt == 1){
 			AnimalGui.newGame.init();
 		}

 	}
 	
 	/**
 	 * @param opponent is the attacked animal
 	 * The attack is successful
 	 */
 	public void niceAttack(Animal opponent){
 		System.out.println(" and wins ");
 		opponent.base = 45;
 		boolean legit = false;
 		int[] moveInc = new int[2];
 		while(legit == false){
 			moveInc = opponent.nextMove();
 			if(AnimalGui.forest[opponent.locX+moveInc[0]][opponent.locY+moveInc[1]].equals(AnimalGui.vacant)){
 				legit = true;
 			}
 		}
 		opponent.status = false;
 		opponent.brief = Character.toUpperCase(opponent.brief);
 		opponent.specialMove(opponent.locX+moveInc[0], opponent.locY+moveInc[1]);
 		AnimalGui.forest[locX][locY] = this;
 		
 	}
 	
 	/**
 	 *  The attack is failed
 	 */
 	public void failAttack(){
 		base = 45;
 		System.out.println(" and loses ");
 		boolean legit = false;
 		int[] moveInc = new int[2];
 		while(legit == false){
 			moveInc = nextMove();
 			if(AnimalGui.forest[locX+moveInc[0]][locY+moveInc[1]].equals(AnimalGui.vacant)){
 				legit = true;
 			}
 		}
 		status = false;
 		brief = Character.toUpperCase(brief);
 		specialMove(locX+moveInc[0], locY+moveInc[1]);
 	}
 	
 	/**
 	 *  The animal takes a next move
 	 */
 	public void moveOn(){
 		int[] moveInc = new int[2];
 		moveInc = nextMove();
 		intendMove(locX+moveInc[0], locY+moveInc[1]);
 	}
 	
 	/**
 	 * @return the specific panel for the animal alive
 	 */
 	public JPanel getJpanel(){
 		return new VoidPanel();
 	}
 	
 	/**
 	 * @return the specific panel for the animal dead
 	 */
 	public JPanel deadIcon(){
 		return new VoidPanel();
 	}
 	
}

/**
 * @author 89551 This is a vacant panel
 *
 */
class VoidPanel extends JPanel{
	
	public void paintComponent(Graphics g){
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 100, 100);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
}