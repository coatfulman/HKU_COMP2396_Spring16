import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author 89551 This is the client code
 *
 */
public class ImageClient {

	/**
	 *  Server port number as indicated
	 */
	int serverPort = 8000;
	/**
	 * This is server port number for this client
	 */
	int localPort;
	/**
	 * GUI
	 */
	JFrame jf = new JFrame("Client Peer");
	/**
	 *  A sock instance
	 */
	Socket sock;
	/**
	 * A server sock instance
	 */
	ServerSocket serverSock;
	/**
	 *  This is client name
	 */
	String userName;
	/**
	 *  Default IP Address
	 */
	String SA = "127.0.0.1";
	/**
	 *  Peer list, where peer is as client
	 */
	ArrayList<PeerCon> inPeer = new ArrayList<PeerCon>();
	/**
	 * Peer list, where peer is as server
	 */
	ArrayList<Peer> peerList = new ArrayList<Peer>();
	/**
	 *  Data IO for server peer
	 */
	ArrayList<PeerCon> outPeer = new ArrayList<PeerCon>();
	/**
	 *  Aid to fill the picture
	 */
	ArrayList<Integer> cnt = new ArrayList<Integer>();
	/**
	 *  Indicate whether the client has the block
	 */
	int[][] hasImg = new int[20][20];
	/**
	 *  This is the picture
	 */
	BufferedImage[][] pic = new BufferedImage[20][20];

	public static void main(String[] args) throws Exception{
		ImageClient client = new ImageClient();
		client.jf.setSize(600, 600);
		client.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.jf.setVisible(true);
		client.go();
	}
	
	/**
	 *  Initialize the client side
	 */
	public void go(){
		try{	
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.shutdown();
			for(int i=0;i<20;i++)
				for(int j=0;j<20;j++)
					hasImg[i][j] = 0;
			sock = new Socket(SA, serverPort);
			new Thread(new AnserServer(sock)).start();
			serverSock = new ServerSocket(0);
			localPort = serverSock.getLocalPort();
			new Thread(new Ps(serverSock)).start();
			
		}catch(Exception ex){}
	}
	
	/**
	 * @author 89551 Help other peer client connect to this server
	 *
	 */
	class Ps implements Runnable {
		ServerSocket ss;
		public Ps(ServerSocket ss){
			this.ss = ss;
		}
		public void run(){
			while(true){
				try{
					Socket sock = ss.accept();
					new Thread(new clientServer(sock)).start();
				}catch(Exception e){
					return;
				}
			}
		}
	}
	
	/**
	 * @return Open dialogue for user to key in username and password
	 */
	public String getName(){
		String res;
		res = JOptionPane.showInputDialog(jf, "Username", "Input", JOptionPane.QUESTION_MESSAGE);
		userName = res;
		String lazy = res = JOptionPane.showInputDialog(jf, "Password", "Input", JOptionPane.QUESTION_MESSAGE);
		return res;
	}
	
	/**
	 * @author 89551 This is client server
	 *
	 */
	class clientServer implements Runnable {
		String clientName;
		Socket sock;
		DataInputStream in; 
		DataOutputStream out;
		public clientServer(Socket sock){
			this.sock = sock;
		}
		public void run(){
			int blNum;
			try{
				in = new DataInputStream(sock.getInputStream());
				out = new DataOutputStream(sock.getOutputStream());
				inPeer.add(new PeerCon(in, out, sock.getPort()));
				
				while(true){

					blNum = in.readInt();
					if(hasImg[blNum/20][blNum%20] == 0){
						out.writeInt(-1);
					}else{
						BufferedImage toSent = pic[blNum/20][blNum%20];
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
			}catch(Exception ex){
				return;
			}
		}
	}

	
	/**
	 * @author 89551 To receive picture from other peers
	 *
	 */
	class PeerRec implements Runnable {
		DataInputStream in;
		public PeerRec(DataInputStream in){
			this.in = in;
		}
		public void run(){
			try{
				int tr=1;
			while(true){
				int blk = in.readInt();
				if(blk>=400||blk<0){
					continue;
				}				
				int size = in.readInt();

				if(size == -1){
					continue;
				}
				
				byte[] b = new byte[size];
				in.read(b);
				ByteArrayInputStream bin = new ByteArrayInputStream(b);
				BufferedImage bi = ImageIO.read(bin);
				
				if(hasImg[blk/20][blk%20] == 0){
					hasImg[blk/20][blk%20] = 1;
					pic[blk/20][blk%20] = bi;
					freshGui();
					for(int i=0;i<cnt.size();i++){
						if(cnt.get(i) == blk){
							cnt.remove(i);
							break;
						}
					}	
				}
			}	
			}catch(Exception ex){}
		}
	}
	
	/**
	 * @author 89551 Do the commu with main server
	 *
	 */
	class AnserServer implements Runnable {
		Socket sock;
		DataInputStream in;
		DataOutputStream out;
		
		public AnserServer(Socket sock){
			this.sock = sock;
		}

		public void run(){
			try{
				in = new  DataInputStream(sock.getInputStream());
				out = new DataOutputStream(sock.getOutputStream());
				String line;
				while(true){
					line = in.readUTF();
					if(line.startsWith("UPDATE")){
						for(int i=0;i<20;i++)for(int j=0;j<20;j++)hasImg[i][j] = 0;
						cnt.clear();
						for(int i=0;i<400;i++) cnt.add(i);
						Collections.shuffle(cnt);
						freshGui();
						new Thread(new GetPeerBlk()).start();
					}
					if(line.startsWith("USERNAME")){
						String name = getName();
						userName = name;
						out.writeUTF(name);
						out.writeInt(localPort);
						out.flush();			
						jf.setTitle(name);
					}
					if(line.startsWith("PEER")){
						ObjectInputStream ois = new ObjectInputStream(in);
						peerList = (ArrayList<Peer>)ois.readObject();
						outPeer.clear();
						for(Peer peer: peerList){   // should update outPeer
							Socket so = new Socket(SA, peer.portNum);
							outPeer.add(new PeerCon(new DataInputStream(so.getInputStream()), new DataOutputStream(so.getOutputStream()),peer.portNum));
						}
						for(PeerCon pc: outPeer){
							new Thread(new PeerRec(pc.in)).start();
						}
					}	
					if(line.startsWith("CHE")){
						out.writeUTF(userName);
						out.writeInt(localPort);
					}
				}
			}catch(Exception ex){}
		}
	}

	/**
	 * @author 89551 Request block from peers
	 *
	 */
	class GetPeerBlk implements Runnable {
		DataOutputStream out;
		public synchronized void run(){	
			Random rand = new Random();
			
			while(cnt.size()>0){		
				for(PeerCon pc: outPeer){
					out = pc.out;
					if(cnt.size() == 0)
						break;
					int bln = cnt.get(0);
					try{	
						out.writeInt(bln);
						Thread.sleep(10);		
					}catch(Exception ex){}
				}			
			}
		}
	}
	

	/**
	 * Refresh the Gui
	 */
	public void freshGui(){
		ClientPanel cp = new ClientPanel(pic);
		cp.setPreferredSize(new Dimension(600,600));	
		jf.getContentPane().add(cp);
		jf.validate();
	}
	

}

/**
 * @author 89551 Get panel that shows the picture
 *
 */
class ClientPanel extends JPanel{
	BufferedImage[][] pic;
	public ClientPanel(BufferedImage[][] pic){
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

