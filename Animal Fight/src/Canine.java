
public class Canine extends Animal {
	
	/**
	 * @param x is x coordinate of Canine obj
	 * @param y is y coordinate of Canine obj
	 */
	public Canine (int x, int y){
		super(x,y);
		base = 90;
		abs=2;
		animalType = "Canie";
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
		int chance = 0;
		if(opponent.animalType == "Feline")
			chance = 50;
		if(opponent.animalType == "Turtle")
			chance = 20;
		if(getInt(100) < chance){
			niceAttack(opponent);
			System.out.println(animalName+" moves from "+oX+", "+oY+" to "+opponent.locX+", "+opponent.locY);
		}else{
			failAttack();
		}
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
 			if( getInt(1) == 1 ){
 				move[0]*=2;
 				move[1]*=2;
 			}
 			if(inRange(locX+move[0], locY+move[1]) == true){
 				legit = true;
 			}
 		}
		return move;
 	}
	
	/* (non-Javadoc)
	 * @see Animal#intendMove(int, int)
	 */
	public void intendMove(int x, int y){
		if(Math.abs(x-locX)==2 || Math.abs(y-locY)==2){
			int incX = x-locX;
			int incY = y-locY;
			incX/=2;
			incY/=2;
			subMove(locX+incX, locY+incY);
			subMove(locX+incX, locY+incY);
		}else{
			subMove(x, y);
		}
 	}
	
	/**
	 * @param x is x dest
	 * @param y is y dest
	 * This function is similar to "intendMove" for other animals
	 */
	private void subMove(int x, int y){
		if(status == false)
			return;
		if(AnimalGui.forest[x][y].equals(AnimalGui.vacant)){
 			AnimalGui.forest[locX][locY] = AnimalGui.vacant;
 			move(x, y);
 		}else if(AnimalGui.forest[x][y].status == false){
 			return;
 		}else{
 			System.out.print(animalName+" from "+locX+", "+locY+" attacks "+AnimalGui.forest[x][y].animalName+" at "+x+", "+y);
 			fight(AnimalGui.forest[x][y]);
 		}
	}
	
}
