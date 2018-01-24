import java.util.Random;
import java.io.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.event.*;
import java.io.*;
import javax.swing.JFileChooser;

public class AnimalGui {
	
	/**
	 *  This is an animal instance representing vacant position
	 */
	static Animal vacant = new Animal(); 
	/**
	 *  To verify whether the mouse is pressed. 1 stands for yes, 0 stands for no.
	 */
	int pressed;
	/**
	 *  If pressed, current x cor
	 */
	int pressX;
	/**
	 * If pressed, current y cor
	 */
	int pressY;
	/**
	 * 	x cor of mouse
	 */
	int x;
	/**
	 * y cor of mouse
	 */
	int y;
	/**
	 * The type of icon 
	 */
	int abs;
	/**
	 *  It's blue icon 
	 */
	JPanel trackBlue;
	/**
	 *  Button for auto ran
	 */
	JButton modeButton = new JButton("Auto run one round");
	/**
	 *  Button for reset
	 */
	JButton replayButton = new JButton("Start Over");
	/**
	 * Paths to get icon
	 */
	String path = new File("").getAbsolutePath();
	String catPath = path+"\\Cat.png";
	String dogPath = path+"\\Dog.png";
	String foxPath = path+"\\Fox.png";
	String hippoPath = path+"\\Hippo.png";
	String tigerPath = path+"\\Tiger.png";
	String turtlePath = path+"\\Turtle.png";
	String wolfPath = path+"\\Wolf.png";
	String lionPath = path+"\\Lion.png";
	/**
	 *  This stimulates the forest
	 */
	static Animal[][] forest = new Animal[15][15];
	static JPanel[][] jpanel = new JPanel[15][15];
	/**
	 *  The arraylist stores all eight animal objects
	 */
	
	public static ArrayList<Animal> animal = new ArrayList<Animal>();
	/**
	 * It can be used to generate random number
	 */
	Random rand = new Random();
	
	/**
	 *  This is the static instance
	 */
	static AnimalGui newGame = new AnimalGui();
	
	/**
	 * This is the frame to be used
	 */
	JFrame jf = new JFrame();
	JPanel jp = new JPanel();
	/**
	 * @param args is not used.s
	 */
	
	
	
	/**
	 * @author 89551 This is mouseListener
	 *
	 */
	class MyListener implements MouseListener{
		int locx;
		int locy;
		public MyListener(int x, int y){
			locx = x;
			locy = y;
		}
		public void mouseEntered(MouseEvent me){
			x = locx;
			y = locy;
			if(pressed == 1){
				if(abs == 1){
					if( !(x==pressX&&y==pressY) && !(((x-pressX)*(x-pressX)+(y-pressY)*(y-pressY))>2) ){
						mask(x,y,-1);
					}
				}
				if(abs == 2){
					if( !((x-pressX)*(y-pressY)!=0) && !(pressX==x && pressY==y) && !(((x-pressX)*(x-pressX)+(y-pressY)*(y-pressY))>4) ){
						if(((x-pressX)*(x-pressX)+(y-pressY)*(y-pressY))==4){
							if(x==pressX){
								if(forest[x][(y+pressY)/2].status==false && forest[x][(y+pressY)/2].animalName!=".")
									return;
							}
							if(y==pressY){
								if(forest[(x+pressX)/2][y].status==false && forest[(x+pressX)/2][y].animalName!=".")
									return;
							}
						}
							mask(x,y,-1);
					}
				}
				if(abs == 3){
					if(!( ( (x-pressX)*(x-pressX)+(y-pressY)*(y-pressY) )>1) && !(pressX==x && pressY==y)){
						mask(x,y,-1);
					}
				}
				
			}
		}
		
		public void mouseExited(MouseEvent me){
			x = locx;
			y = locy;
			if(pressed == 1){
				if(abs == 1){
					if( !(x==pressX&&y==pressY) && !(((x-pressX)*(x-pressX)+(y-pressY)*(y-pressY))>2) ){
						mask(x,y,0);
					}
				}
				else if(abs == 2){
					if( !((x-pressX)*(y-pressY)!=0) && !(pressX==x && pressY==y) && !(((x-pressX)*(x-pressX)+(y-pressY)*(y-pressY))>4) ){
						if(((x-pressX)*(x-pressX)+(y-pressY)*(y-pressY))==4){
							if(x==pressX){
								if(forest[x][(y+pressY)/2].status==false && forest[x][(y+pressY)/2].animalName!=".")
									return;
							}
							if(y==pressY){
								if(forest[(x+pressX)/2][y].status==false && forest[(x+pressX)/2][y].animalName!=".")
									return;
							}
						}
						mask(x,y,0);
					}
				}
				else if(abs == 3){
					if(!( ( (x-pressX)*(x-pressX)+(y-pressY)*(y-pressY) )>1) && !(pressX==x && pressY==y)){
						mask(x,y,0);
					}
				}
				else{
					return;
				}
			}
		}
		
		public void mousePressed(MouseEvent me){
			
			pressX = locx;
			pressY = locy;
			abs = forest[locx][locy].abs;
			if(forest[locx][locy].status == false)
				return;
			pressed = 1;
			if(abs == 1){
				for(int i=0;i<15;i++){
					for(int j=0;j<15;j++){
						if(((i-locx)*(i-locx)+(j-locy)*(j-locy) )> 2 || (locx==i && locy==j))					
							continue;
						else{
							mask(i,j,0);
						}
					}
				}
							
			}else if(abs == 2){
				for(int i=0;i<15;i++){
					for(int j=0;j<15;j++){
						
						if( ((i-locx)*(j-locy)!=0) || (locx==i && locy==j) || ((i-locx)*(i-locx)+(j-locy)*(j-locy))>4 )					
							continue;
						if(((i-locx)*(i-locx)+(j-locy)*(j-locy))==4){
							if(i==locx){
								if(forest[i][(j+locy)/2].status==false && forest[i][(j+locy)/2].animalName!=".")
									continue;
							}
							if(j==locy){
								if(forest[(i+locx)/2][j].status==false && forest[(i+locx)/2][j].animalName!=".")
									continue;
							}
						}
						mask(i,j,0);
							
							
					}
				}
			}else if(abs == 3){
				for(int i=0;i<15;i++){
					for(int j=0;j<15;j++){
						if(((i-locx)*(i-locx)+(j-locy)*(j-locy) )> 1 || (locx==i && locy==j))					
							continue;
						else{
							mask(i,j,0);	
						}
					}
				}
				
			}
		}
		
		public void mouseReleased(MouseEvent me){
			pressed = 0;
			abs = forest[locx][locy].abs;
			if(forest[locx][locy].status == false)
				return;
			if(abs == 1){
				if( !(x==locx&&y==locy) && !(((x-locx)*(x-locx)+(y-locy)*(y-locy))>2) ){
					forest[locx][locy].intendMove(x, y);
				}
			}
			if(abs == 2){
				if( !((x-locx)*(y-locy)!=0) && !(locx==x && locy==y) && !(((x-locx)*(x-locx)+(y-locy)*(y-locy))>4) ){
					forest[locx][locy].intendMove(x, y);
				}
			}
			if(abs == 3){
				if(!( ( (x-locx)*(x-locx)+(y-locy)*(y-locy) )>1) && !(locx==x && locy==y)){
					forest[locx][locy].intendMove(x, y);
				}
			}
			if(abs == 4){
				refreshPanel();
			}
			refreshPanel();
		}
		
		public void mouseClicked(MouseEvent me){
			// Leave it be
		}
	}

	
	public static void main(String[] args) {
		newGame.menu();
		//newGame.init();
		Scan key = new Scan();
		printForest();
		System.out.print("Press enter to iterate, type 'exit' to quit:");
		while(key.goOn() == true){
			goAround();
			newGame.refreshPanel();
			System.out.print("Press enter to iterate, type 'exit' to quit:");
		}
		
		for(Animal ani: animal){
			System.out.print(ani.animalName+" is ");
			if(ani.status == true)
				System.out.print("alive at ");
			else
				System.out.print("dead at ");
			System.out.println(ani.locX+","+ani.locY);
		}
	}
	
	/**
	 *  This generates start menu
	 */
	public void menu(){
		
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				forest[i][j] = vacant;
			}
		}
		
		int pos[] = new int[2];
		
		pos = getPos();
		Dog dog = new Dog(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = dog;
		
		pos = getPos();
		Fox fox = new Fox(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = fox;
		
		pos = getPos();
		Wolf wolf = new Wolf(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = wolf;
		
		pos = getPos();
		Cat cat = new Cat(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = cat;
		
		pos = getPos();
		Lion lion = new Lion(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = lion;
				
		pos = getPos();
		Tiger tiger = new Tiger(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = tiger;
		
		pos = getPos();
		Hippo hippo = new Hippo(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = hippo;
		
		pos = getPos();
		Turtle turtle = new Turtle(pos[0], pos[1]);
		forest[pos[0]][pos[1]] = turtle;
		
		JCheckBox checkCat = new JCheckBox("Cat");
		JCheckBox checkDog = new JCheckBox("Dog");
		JCheckBox checkFox = new JCheckBox("Fox");
		JCheckBox checkHippo = new JCheckBox("Hippo");
		JCheckBox checkLion = new JCheckBox("Lion");
		JCheckBox checkTiger = new JCheckBox("Tiger");
		JCheckBox checkTurtle = new JCheckBox("Turtle");
		JCheckBox checkWolf = new JCheckBox("Wolf");
		
		JButton catBu = new JButton();
		catBu.setText("Select your cat icon");
		catBu.setFont(new Font("Arial", Font.PLAIN, 40));
		catBu.addActionListener(new catLis());
		
		JButton dogBu = new JButton();
		dogBu.setText("Select your dog icon");
		dogBu.setFont(new Font("Arial", Font.PLAIN, 40));
		dogBu.addActionListener(new dogLis());
		
		JButton foxBu = new JButton();
		foxBu.setText("Select your fox icon");
		foxBu.setFont(new Font("Arial", Font.PLAIN, 40));
		foxBu.addActionListener(new foxLis());
		
		JButton hippoBu = new JButton();
		hippoBu.setText("Select your hippo icon");
		hippoBu.setFont(new Font("Arial", Font.PLAIN, 40));
		hippoBu.addActionListener(new hippoLis());
		
		JButton tigerBu = new JButton();
		tigerBu.setText("Select your tiger icon");
		tigerBu.setFont(new Font("Arial", Font.PLAIN, 40));
		tigerBu.addActionListener(new tigerLis());
		
		JButton turtleBu = new JButton();
		turtleBu.setText("Select your turtle icon");
		turtleBu.setFont(new Font("Arial", Font.PLAIN, 40));
		turtleBu.addActionListener(new turtleLis());
		
		JButton wolfBu = new JButton();
		wolfBu.setText("Select your wolf icon");
		wolfBu.setFont(new Font("Arial", Font.PLAIN, 40));
		wolfBu.addActionListener(new wolfLis());
		
		JButton lionBu = new JButton();
		lionBu.setText("Select your lion icon");
		lionBu.setFont(new Font("Arial", Font.PLAIN, 40));
		lionBu.addActionListener(new lionLis());
		
		
		checkCat.setFont(new Font("Arial", Font.PLAIN, 40));
		checkDog.setFont(new Font("Arial", Font.PLAIN, 40));
		checkFox.setFont(new Font("Arial", Font.PLAIN, 40));
		checkHippo.setFont(new Font("Arial", Font.PLAIN, 40));
		checkLion.setFont(new Font("Arial", Font.PLAIN, 40));
		checkTiger.setFont(new Font("Arial", Font.PLAIN, 40));
		checkTurtle.setFont(new Font("Arial", Font.PLAIN, 40));
		checkWolf.setFont(new Font("Arial", Font.PLAIN, 40));
		
	//	jf.setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
		jf.setLayout(new GridLayout(9,2));
		jf.setSize(1500, 1500);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		jf.add(checkCat);jf.add(catBu);
		jf.add(checkDog);jf.add(dogBu);
		jf.add(checkFox);jf.add(foxBu);
		jf.add(checkHippo);jf.add(lionBu);
		jf.add(checkLion);jf.add(turtleBu);
		jf.add(checkTiger);jf.add(tigerBu);
		jf.add(checkTurtle);jf.add(hippoBu);
		jf.add(checkWolf);jf.add(wolfBu);
			
		JButton start = new JButton("Start");
		start.setFont(new Font("Arial", Font.PLAIN, 80));
		jf.add(start);
		start.addActionListener(new StartListener());
	
		checkCat.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkCat.isSelected()){
					animal.add(cat);
				}else{
					animal.remove(cat);
				}
			}
		});
		checkDog.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkDog.isSelected()){
					animal.add(dog);
				}else{
					animal.remove(dog);
				}
			}
		});
		checkFox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkFox.isSelected()){
					animal.add(fox);
				}else{
					animal.remove(fox);
				}
			}
		});
		checkHippo.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkHippo.isSelected()){
					animal.add(hippo);
				}else{
					animal.remove(hippo);
				}
			}
		});
		checkLion.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkLion.isSelected()){
					animal.add(lion);
				}else{
					animal.remove(lion);
				}
			}
		});
		checkTiger.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkTiger.isSelected()){
					animal.add(tiger);
				}else{
					animal.remove(tiger);
				}
			}
		});
		checkTurtle.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkTurtle.isSelected()){
					animal.add(turtle);
				}else{
					animal.remove(turtle);
				}
			}
		});
		checkWolf.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if(checkWolf.isSelected()){
					animal.add(wolf);
				}else{
					animal.remove(wolf);
				}
			}
		});
		
	}
	
	/**
	 * @author 89551 Those are to choose file
	 *
	 */
	class FileListener implements ActionListener{
		String path;
		public void actionPerformed(ActionEvent ae){
			JFileChooser fc = new JFileChooser();
			int reva = fc.showOpenDialog(jf);
			if(reva == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				path = file.getAbsolutePath();
			}
			setPath();
		}
		public void setPath(){
			return;
		}
	}
	
	/**
	 * @author 89551 to choose cat icon
	 *
	 */
	class catLis extends FileListener{
		public void setPath(){
			System.out.println("done");
			catPath = path;
		}
	}
	/**
	 * @author 89551 to choose dog icon
	 *
	 */
	class dogLis extends FileListener{
		public void setPath(){
			dogPath = path;
		}
	}
	/**
	 * @author 89551 to choose fox icon
	 *
	 */
	class foxLis extends FileListener{
		public void setPath(){
			foxPath = path;
		}
	}
	/**
	 * @author 89551 to choose hippo icon
	 *
	 */
	class hippoLis extends FileListener{
		public void setPath(){
			hippoPath = path;
		}
	}
	/**
	 * @author 89551 to choose tiger icon
	 *
	 */
	class tigerLis extends FileListener{
		public void setPath(){
			tigerPath = path;
		}
	}
	/**
	 * @author 89551 to choose turtle icon
	 *
	 */
	class turtleLis extends FileListener{
		public void setPath(){
			turtlePath = path;
		}
	}
	/**
	 * @author 89551 to choose lion icon
	 *
	 */
	class lionLis extends FileListener{
		public void setPath(){
			lionPath = path;
		}
	}
	/**
	 * @author 89551 to choose wolf icon
	 *
	 */
	class wolfLis extends FileListener{
		public void setPath(){
			wolfPath = path;
		}
	}
	
	/**
	 *  This function is used when the program starts, to initialize the forest.
	 */
	public void init(){
		jf.setLayout(new BorderLayout());
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				forest[i][j] = vacant;
			}
		}
		
		int pos[] = new int[2];
		for(Animal ani: animal){
			ani.status = true;
			pos = getPos();
			ani.locX = pos[0];
			ani.locY = pos[1];
			forest[pos[0]][pos[1]] = ani;
		}
		
		jf.getContentPane().removeAll();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(1530, 1700);
		jp.setSize(1500,1500);
	
		jp.setLayout(new GridLayout(15,15));
		jf.getContentPane().add(BorderLayout.CENTER,jp); 
		modeButton.addActionListener(new ChangeMode());
		modeButton.setPreferredSize(new Dimension(1530,100));
		modeButton.setFont(new Font("Arial", Font.PLAIN, 80));
		replayButton.addActionListener(new StartOver());
		replayButton.setPreferredSize(new Dimension(1530,100));
		replayButton.setFont(new Font("Arial", Font.PLAIN, 80));
		JPanel control = new JPanel();
		control.setLayout(new GridLayout(1,2));
		control.add(modeButton);
		control.add(replayButton);
		jf.getContentPane().add(BorderLayout.SOUTH,control);
		refreshPanel();
		jf.setVisible(true);
	}
		
	/**
	 *  This funtions auto runs the forest
	 */
	public void autoRan(){
			for(Animal ani: animal){
				if(ani.status == true){
					ani.moveOn();
				}
			}
			refreshPanel();
			System.out.print("Press enter to iterate, type 'exit' to quit:");
		}
		
	
	/**
	 * @return Whether is position is occupied by another animal.
	 * This is used only in void init()
	 */
	private int[] getPos(){
		int pos[] = new int[2];
		boolean available = false;
		while(available == false){
			pos[0] = rand.nextInt(12);
			pos[1] = rand.nextInt(12);
			if(forest[pos[0]][pos[1]] == vacant)
				available = true;
		}
		return pos;
	}
	
	/**
	 *  This function is used to print the situation of the forest
	 */
	public static void printForest(){
		for(int i=0; i<15 ;i++){
			for(int j=0;j<15;j++){
				System.out.print(forest[i][j].brief);
			}
			System.out.println("");
		}
	}
	
	/**
	 *  This function is used to take another around
	 */
	public static void goAround(){
		for(Animal ani: animal){
			if(ani.status == true){
				ani.moveOn();
			}
		}
	
	}
	
	/**
	 * It refreshed the panel
	 */
	public void refreshPanel(){
		jp.removeAll();
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				if(forest[i][j].status == true)
					jpanel[i][j] = forest[i][j].getJpanel();
				else
					jpanel[i][j] = forest[i][j].deadIcon();
				jpanel[i][j].addMouseListener(new MyListener(i,j));
				jp.add(jpanel[i][j]);
			}
		}
		jf.validate();
	}
	
	/**
	 * @param i is x cor
	 * @param j is y cor
	 * @param ind is the type of color, -1 is blue, if the grid has animal on it, it's red, if not, it's orange
	 */
	public void mask(int i, int j, int ind){
		if(forest[i][j].status == false && forest[i][j].animalName!=".")
			return;
		if(ind == -1){
			jpanel[i][j].removeAll();
			jpanel[i][j].add(new BluePanel());
			jpanel[i][j].validate();
		}else
		if(forest[i][j].brief != '.'){
			RedPanel rp = new RedPanel();
			jpanel[i][j].removeAll();
			jpanel[i][j].setLayout(new GridLayout(1,1));
			jpanel[i][j].add(rp);
			jpanel[i][j].validate();
		}else{
			OrangePanel yp = new OrangePanel();
			jpanel[i][j].removeAll();
			jpanel[i][j].setLayout(new GridLayout(1,1));
			jpanel[i][j].add(yp);
			jpanel[i][j].validate();
		}
	}
	
	/**
	 * @author 89551 This is auto ran listener
	 *
	 */
	class ChangeMode implements ActionListener{
		public void actionPerformed(ActionEvent e){
				newGame.autoRan();
		}
	}
	
	/**
	 * @author 89551 This is start over listener
	 *
	 */
	class StartOver implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int pos[] = new int[2];
			for(Animal ani: animal){
				forest[ani.locX][ani.locY] = vacant;
				ani.status = true;
				pos = getPos();
				ani.locX = pos[0];
				ani.locY = pos[1];
				forest[pos[0]][pos[1]] = ani;
			}
			init();
		}
	}
	
	/**
	 * @author 89551 This is start listener for the menu
	 *
	 */
	class StartListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			init();
		}
	}
}

/**
 * @author 89551 This is a red panel
 *
 */
class RedPanel extends JPanel{
	public void paintComponent(Graphics g){
		g.setColor(Color.RED);
		g.fillRect(0, 0, 100, 100);
	}
}


/**
 * @author 89551 This is a blue panel
 *
 */
class BluePanel extends JPanel{
	public void paintComponent(Graphics g){
		g.setColor(Color.blue);
		g.fillRect(0, 0, 100, 100);
	}
}
/**
 * @author 89551 This is a orange panel
 *
 */
class OrangePanel extends JPanel{
	public void paintComponent(Graphics g){
		g.setColor(Color.orange);
		g.fillRect(0, 0, 100, 100);
	}
}

