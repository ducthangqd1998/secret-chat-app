package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

import data.Peer;
import sun.security.util.Length;
import tags.Decode;
import tags.Encode;
import tags.Tags;

public class Client {
	
	public static ArrayList<Peer> clientarray = null;
	private ClientServer server;
	private static Cryption key;
	private InetAddress IPserver;
	private int portServer = 8080;
	private String username = "";
	private boolean isStop = false;
	private static int portClient = 10000;
	private static PublicKey myPublicKey = null;
	private int timeOut = 10000;  //time to each request is 10 seconds.
	private Socket socketClient;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;

	public Client(String arg, int arg1, String name, String dataUser, Cryption key) throws Exception {
		IPserver = InetAddress.getByName(arg); // arg là IP address của server 
		username = name; // username của user
		portClient = arg1; // port của client 
		clientarray = Decode.getAllUser(dataUser); // get các user đang online
		this.key = key;
		this.myPublicKey = key.getPublicKey();
		// Có hai cách để tạo thread trong java
		// + implements runnable interfaces trong java: Ta cần override run method, tạo object Thread với params
		//   là 1 new Runnable và gọi start method
		// + extends thread class trong java: Ta cần override run method và tạo object Thread và gọi start methos 
		new Thread(new Runnable(){
			@Override
			public void run() {
				updateFriend(); // Tiến hành cập nhật dang sách user đang online 
			}
		}).start();
		
		// Tạo đối tượng server client, tạo thread Request nhằm tạo nhiều request lên server, 
		server = new ClientServer(username);
		// Thực hiện tạo thread bằng cách extends thread class bằng cách tạo class Request 
		(new Request()).start();
	}

	public static int getPort() {
		return portClient;
	}
	
	public static PublicKey getPublicKey() {
		return myPublicKey;
	}
	
	public static Cryption getCryption() {
		return key;
	}

	public void request() throws Exception {
		// Tạo 1 socket 
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		// Tạo 1 kết nối tới server
		socketClient.connect(addressServer);
		String msg = Encode.sendRequest(username);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		// Gửi yêu cầu tới server
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverInputStream = new ObjectInputStream(socketClient.getInputStream());
		msg = (String) serverInputStream.readObject();
//		System.out.println(msg);
		serverInputStream.close();
		//		just for test
//		System.out.println("toantoan" + msg); //test server return to user
		clientarray = Decode.getAllUser(msg);
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateFriend();
			}
		}).start();
	}

	public class Request extends Thread {
		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					// Cứ 10s 1 lần, thực hiện chạy hàm request 
					Thread.sleep(timeOut);
					request();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void intialNewChat(String IP, int host, String guest) throws Exception {
		final Socket connclient = new Socket(InetAddress.getByName(IP), host);
		ObjectOutputStream sendrequestChat = new ObjectOutputStream(connclient.getOutputStream());
		String key = Base64.getEncoder().encodeToString(this.key.getPublicKey().getEncoded());
		sendrequestChat.writeObject(Encode.sendRequestChat(username, key));
		sendrequestChat.flush();
		ObjectInputStream receivedChat = new ObjectInputStream(connclient.getInputStream());
		String msg = (String) receivedChat.readObject();
		if (msg.equals(Tags.CHAT_DENY_TAG)) {
			MainGui.request("Your friend denied connect with you!", false);
			connclient.close();
			return;
		}
		String pubkey = msg.substring(14, msg.length() - 0);
		byte[] z = Base64.getDecoder().decode(pubkey);
		try {
//			System.out.println(z);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(z);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = factory.generatePublic(spec);
//			System.out.println("guest" + guest + " " + connclient);
			new ChatGui(username, guest, connclient, portClient, pubKey, this.key);
		}catch(Exception e) {
			System.out.println("Error for create chatroom");
		}
		
		
	}

	public void exit() throws IOException, ClassNotFoundException {
		isStop = true;
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(IPserver, portServer);
		socketClient.connect(addressServer);
		String msg = Encode.exit(username);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverOutputStream.close();
		server.exit();
	}

	public void updateFriend(){
		int size = clientarray.size();
		MainGui.resetList();
		//while loop
		int i = 0;
		while (i < size) {
			if (!clientarray.get(i).getName().equals(username))
				// Gửi từng client đang online 
				MainGui.updateFriendMainGui(clientarray.get(i).getName());
			i++;
		}
	}
}