import java.util.Scanner;

public class Scan {
	
	/**
	 *  keyboard is used for input
	 */
	private Scanner keyboard = new Scanner(System.in);
	
	/**
	 * @return whether the program should proceed to another round. 
	 * True for yes, false for no
	 */
	public boolean goOn() {
		String line = keyboard.nextLine();
			if(!line.equals("exit")) {
			return true;
		}else{
			return false;
		}
	}
}