package data;

import java.security.PublicKey;
import java.util.Base64;

public class Peer {

	private String namePeer = "";
	private String hostPeer = "";
	private PublicKey pubKey = null;
	private int portPeer = 0;

	public void setPeer(String name, String host, int port, PublicKey pubKey) {
		this.namePeer = name;
		this.hostPeer = host;
		this.portPeer = port;
		this.pubKey = pubKey;
	}

	public void setName(String name) {
		this.namePeer = name;
	}

	public void setHost(String host) {
		this.hostPeer = host;
	}

	public void setPort(int port) {
		this.portPeer = port;
	}
	
	public void setPublicKey(PublicKey pubKey) {
		this.pubKey = pubKey;
	}

	public String getName() {
		return namePeer;
	}

	public String getHost() {
		return hostPeer;
	}

	public int getPort() {
		return portPeer;
	}

	public String getPublicKey() {
		return Base64.getEncoder().encodeToString(pubKey.getEncoded());
	}

}

