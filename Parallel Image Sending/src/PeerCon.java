import java.io.*;

/**
 * @author 89551 Stores connection tools
 *
 */
public class PeerCon {
	DataInputStream in;
	DataOutputStream out;
	int portNum;
	public PeerCon(DataInputStream in, DataOutputStream out, int portNum){
		this.in = in;
		this.out = out;

		this.portNum = portNum;
	}
}
