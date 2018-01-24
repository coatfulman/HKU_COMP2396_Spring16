import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author 89551 This is image server
 *
 */
public class ImageServer {
	/**
	 * Picture path from local machine
	 */
	String path;
	/**
	 * Name lists of clients
	 */
	ArrayList<String> names = new ArrayList<String>();
	/**
	 *  Peer list consisting of clients
	 */
	ArrayList<Peer> peerList = new ArrayList<Peer>();
	/**
	 * Just a Gui
	 */
	JFrame jf = new JFrame("Image Server");
	/**
	 *  Changed as main parameter
	 */
	int returnNum = 5;
	/**
	 *  Picture of blocks
	 */
	BufferedImage[][] imagePieces = new BufferedImage[20][20];
	/**
	 * Clients list
	 */
	ArrayList<PeerCon> inPeer = new ArrayList<PeerCon>();
	/**
	 *  Server socket of this class
	 */
	ServerSocket mainPeer;
	/**
	 * Port number of this server
	 */
	int mainPort;
	
	public static void main(String[] args) throws Exception {
		ImageServer imageServer = new ImageServer();
		if(args.length != 0){
			imageServer.returnNum = Integer.parseInt(args[0]);
		}
		imageServer.mainPeer = new ServerSocket(0);
		imageServer.mainPort = imageServer.mainPeer.getLocalPort();
		imageServer.go();
	}
		
	/**
	 * @throws IOException Initialize the program
	 */
	public void go() throws IOException{
		peerList.add(new Peer("localhost", mainPort));
		ServerSocket listener = new ServerSocket(8000);
		setGui();
		try{
			new Thread(new DT(mainPeer)).start();
	
			while(true){
				Socket cs = listener.accept();
				upPeer();
				Thread t = new Thread(new Handler(cs));
				t.start();
			}
		}finally{
			listener.close();
		}
	}

	/**
	 * @author 89551 Accept connect request
	 *
	 */
	class DT implements Runnable {	
		ServerSocket mainPeer;
		public DT(ServerSocket mainPeer){
			this.mainPeer = mainPeer;
		}
		public void run(){
			while(true){
				try{
					Socket sock = mainPeer.accept();
					new Thread(new clientServer(sock)).start();
				}catch(Exception e){}
			}
		}
	}
	
	/**
	 *  Update the peer.
	 *  This is used when:
	 *  1. Every 60 second updates the information
	 *  2. Every time loading the new picture
	 */
	public synchronized void upPeer(){
		String un;
		int pn;
		try{
			peerList.clear();
			peerList.add(new Peer("localhost", mainPort));
			for(PeerCon pc: inPeer){
				pc.out.writeUTF("CHECK");
			}
		}catch(Exception ex){}
		
	}
	
	/**
	 * @param jf This JFrame
	 * @param jp This JPanel
	 */
	public void ChooseImage(JFrame jf, JPanel jp){
		upPeer();
		JFileChooser fc = new JFileChooser();
		int reva = fc.showOpenDialog(jf);
		if(reva == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			path = file.getAbsolutePath();
			try{
				BufferedImage pic = ImageIO.read(file);
				for(int i=0;i<20;i++){
					for(int j=0;j<20;j++){
						imagePieces[i][j] = pic.getSubimage(i*(pic.getWidth()/20), j*(pic.getHeight()/20), pic.getWidth()/20, pic.getHeight()/20);
					}
				}
				jp.removeAll();
				ServerPanel tmp = new ServerPanel(imagePieces);
				tmp.setPreferredSize(new Dimension(600,600));
				jp.add(tmp);
				jp.validate();
				jf.validate();
				for(PeerCon pc: inPeer){
					new Thread(new SM(pc.out)).start();
				}
			}catch(Exception ex){
				System.out.println("Picture loading falied");
				return;
			}
		}	
	}
	
	/**
	 * @author 89551 Inform update when picture changed
	 *
	 */
	class SM implements Runnable {
		DataOutputStream os;
		public SM(DataOutputStream os){
			this.os = os;
		}
		public void run(){
			try{
				os.writeUTF("UPDATE");
				os.flush();
			}catch(Exception ex){}
			
		}
	}
	
	/**
	 * Set up gui
	 */
	public void setGui(){
		jf.setSize(600, 700);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		JPanel jp = new JPanel();
		jp.setPreferredSize(new Dimension(600,600));
		JButton jb = new JButton("Choose image");
		jb.setPreferredSize(new Dimension(600,50));
		jf.getContentPane().add(jp, BorderLayout.CENTER);
		jf.getContentPane().add(jb, BorderLayout.SOUTH);
		jf.validate();
		ChooseImage(jf, jp);
		jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				ChooseImage(jf, jp);
			}
		});
	}	
	
	/**
	 * @author 89551 This is the handler commu with clients
	 *
	 */
	class Handler implements Runnable {	
		Socket sock;
		DataInputStream in;
		DataOutputStream out;
		String clientName;
		public Handler(Socket cs){
			sock = cs;
		}
		public void run(){
			try{	
				in = new DataInputStream(sock.getInputStream());
				out = new DataOutputStream(sock.getOutputStream());
				while(true){
					out.writeUTF("USERNAME");
					out.flush();
					String name = in.readUTF();
					int portNum = in.readInt();
					if(name.length() == 0 || name == null){
						continue;
					}
					if(!names.contains(name)){	
						out.writeUTF("PEER");
						ObjectOutputStream oos = new ObjectOutputStream(out);
						oos.writeObject(getPeer());
						names.add(name);
						addData(name, portNum, in, out);	
						out.writeUTF("UPDATE");
						out.flush();
						clientName = name;
						break;
					}
				}
				String line;
				int num;
				while(true){
					line = in.readUTF();
					num = in.readInt();
					peerList.add(new Peer(line, num));
					System.out.println(line+": Port number: "+num+" is online.");
				}
			}catch(Exception ex){}		
		}

		private synchronized void addData(String name, int portNum, DataInputStream in, DataOutputStream out) {
			peerList.add(new Peer(name, portNum));
			names.add(name);
			inPeer.add(new PeerCon(in, out, portNum));
		}
	}

	/**
	 * @author 89551 Check whether clients are active
	 *
	 */
	class CheckActive implements Runnable {
		public void run(){
			while(true){
				try{
					for(PeerCon pc: inPeer){
						pc.out.writeUTF("CHECK");
					}
					Thread.sleep(6000);
				}catch(Exception ex){
					System.out.println("Fail to sleep!");
				}
			}	
		}
	}
	
	ArrayList<Peer> getPeer(){
		ArrayList<Peer> res = new ArrayList<Peer>();
		int total = peerList.size();
		int cnt = returnNum;
		if(cnt > total){
			cnt = total;
		}
		Random rand = new Random();
		while(cnt>0){
			int tmp = rand.nextInt(total);
			if(!res.contains(peerList.get(tmp))){
				cnt--;
				res.add(peerList.get(tmp));
			}
		}
		return res;
	}
	
	/**
	 * @author 89551 This is the main server
	 *
	 */
	class clientServer implements Runnable {
		String clientName;
		Socket sock;
		DataInputStream in; 
		DataOutputStream out;
		int[][] hasImg = new int[20][20];
		public clientServer(Socket sock){
			this.sock = sock;
			for(int i=0;i<20;i++)for(int j=0;j<20;j++)hasImg[i][j]=1;
		}
		public void run(){
			int blNum;
			try{
				in = new DataInputStream(sock.getInputStream());
				out = new DataOutputStream(sock.getOutputStream());
				inPeer.add(new PeerCon(in, out, sock.getPort()));
				int num=0;
				while(true){
					blNum = in.readInt();
					if(hasImg[blNum/20][blNum%20] == 0){
						out.writeInt(-1);
					}else{
						BufferedImage toSent = imagePieces[blNum/20][blNum%20];
						ByteArrayOutputStream ops = new ByteArrayOutputStream();  
						ImageIO.write(toSent, "JPG", ops);
						byte[] b = ops.toByteArray();
						int size = b.length;
						out.writeInt(blNum);
						out.writeInt(size);
						out.write(b);
						out.flush();
						ops.close();	
					}
				}
			}catch(Exception ex){}
		}
	}
	

	
}

/**
 * @author 89551 Shows current picture
 *
 */
class ServerPanel extends JPanel{
	BufferedImage[][] pic;
	public ServerPanel(BufferedImage[][] pic){
		this.pic = pic;
	}
	public void paintComponent(Graphics g){
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 600, 600);
		for(int i=0;i<20;i++){
			for(int j=0;j<20;j++){
				if(pic[i][j] != null){
					g.drawImage(pic[i][j], i*30, j*30, 30, 30, this);
				}
				
			}
		}
	
	}
}


