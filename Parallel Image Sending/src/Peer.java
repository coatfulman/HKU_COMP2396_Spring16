import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
/**
 * @author 89551 Stores peer information
 *
 */
public class Peer implements Serializable{
	String userName;
	int portNum;
	public Peer(String un, int pn){
		userName = un;
		portNum = pn;
	
	}
}