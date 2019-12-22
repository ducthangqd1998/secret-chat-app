package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import tags.Decode;
import tags.Tags;
import client.Client;

public class ClientServer {

	private String username = "";
	private ServerSocket serverPeer;
	private int port;
	private boolean isStop = false;
	private PublicKey myPublicKey = null;
	private Cryption myKey = null;

//	public void stopServerPeer() {
//		isStop = true;
//	}
//
//	public boolean getStop() {
//		return isStop;
//	}

	public ClientServer(String name) throws Exception {
		username = name;
		port = Client.getPort();
		myPublicKey = Client.getPublicKey();
		myKey = Client.getCryption();
		serverPeer = new ServerSocket(port);
		(new WaitPeerConnect()).start();
	}
	
	public void exit() throws IOException {
		isStop = true;
		serverPeer.close();
	}

	class WaitPeerConnect extends Thread {

		Socket connection;
		ObjectInputStream getRequest;

		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					connection = serverPeer.accept();
					getRequest = new ObjectInputStream(connection.getInputStream());
					String msg = (String) getRequest.readObject();
					List<String> name = Decode.getNameRequestChat(msg);
					int res = MainGui.request("Account: " + name + " want to connect with you !", true);
					ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
					if (res == 1) {
						send.writeObject(Tags.CHAT_DENY_TAG);
					} else if (res == 0) {
						String publicKey = Base64.getEncoder().encodeToString(myPublicKey.getEncoded());
						byte[] z = Base64.getDecoder().decode(name.get(1));
						try {
							System.out.println(z);
							X509EncodedKeySpec spec = new X509EncodedKeySpec(z);
							KeyFactory factory = KeyFactory.getInstance("RSA");
							PublicKey pubKey = factory.generatePublic(spec);
							send.writeObject(Tags.CHAT_ACCEPT_TAG + publicKey);
							new ChatGui(username, name.get(0), connection, port, pubKey, myKey);
						}catch(Exception e) {}
					}
					send.flush();
				} catch (Exception e) {
					break;
				}
			}
			try {
				serverPeer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
