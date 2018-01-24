
public class Feline extends Animal {
	
	/**
	 * @param x is the x coordinate of Feline obj
	 * @param y is the y coordinate of Feline obj
	 */
	public Feline(int x, int y){
		super(x,y);
		abs=1;
		base = 45;
		animalType = "Feline";
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
		if(opponent.animalType == "Canine")
			chance = 100;
		if(opponent.animalType == "Turtle")
			chance = 20;
		if(getInt(100) < chance){
			niceAttack(opponent);
			System.out.println(animalName+" moves from "+oX+", "+oY+" to "+opponent.locX+", "+opponent.locY);
		}else{
			failAttack();
		}
	}
	
}
