package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import tags.Tags;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import java.awt.Color;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.PublicKey;

public class MainGui {

	private Client clientNode;
	private Cryption key;
	private static String IPClient = "", username = "", dataUser = "";
	private static int portClient = 0;
	private JFrame frameMainGui;
	private JTextField txtNameFriend;
	private JButton btnChat, btnExit;
	private JLabel lblLogo;
	private JLabel lblActiveNow;
	private static JList<String> listActive;	
	static DefaultListModel<String> model = new DefaultListModel<>();
	private JLabel lblUsername;

//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainGui window = new MainGui(key);
//					window.frameMainGui.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public MainGui(String arg, int arg1, String name, String msg, Cryption pubkey) throws Exception {
		IPClient = arg; // IP server 
		portClient = arg1; // port client 
		System.out.println("Port client: " + pubkey);
		username = name;
		dataUser = msg;
		key = pubkey;
		System.out.println("Key: +++" + pubkey);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui(key);
					window.frameMainGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainGui(Cryption key) throws Exception {
		initialize();
		// Tạo một client 
		System.out.println("Key: " + key);
		clientNode = new Client(IPClient, portClient, username, dataUser, key);
	}

	public static void updateFriendMainGui(String msg) {
		model.addElement(msg);
	}

	public static void resetList() {
		model.clear();
	} 
	
	private void initialize() {
		frameMainGui = new JFrame();
		frameMainGui.setTitle("View");
		frameMainGui.setResizable(false);
		frameMainGui.setBounds(100, 100, 500, 560);
		frameMainGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMainGui.getContentPane().setLayout(null);

		// Name Friend
		JLabel lblFriendsName = new JLabel("Friend: ");
		lblFriendsName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblFriendsName.setBounds(12, 425, 110, 16);
		frameMainGui.getContentPane().add(lblFriendsName);
		
		txtNameFriend = new JTextField("");
		txtNameFriend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtNameFriend.setColumns(10);
		txtNameFriend.setBounds(100, 419, 384, 28);
		frameMainGui.getContentPane().add(txtNameFriend);

		btnChat = new JButton("");
		btnChat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnChat.setFocusPainted(false);
		btnChat.setContentAreaFilled(false);
		btnChat.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String name = txtNameFriend.getText();
				if (name.equals("") || Client.clientarray == null) {
					Tags.show(frameMainGui, "Invaild username", false);
					return;
				}
				if (name.equals(username)) {
					Tags.show(frameMainGui, "This software doesn't support chat yourself function", false);
					return;
				}
				int size = Client.clientarray.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Client.clientarray.get(i).getName())) {
						try {
							clientNode.intialNewChat(Client.clientarray.get(i).getHost(),Client.clientarray.get(i).getPort(), name);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				Tags.show(frameMainGui, "Friend is not found. Please wait to update your list friend", false);
			}
		});
		btnChat.setBounds(10, 465, 129, 44);
		frameMainGui.getContentPane().add(btnChat);
		btnChat.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/chat1.png")));
		btnExit = new JButton("");
		btnExit.setFocusPainted(false);
		btnExit.setContentAreaFilled(false);
		btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Tags.show(frameMainGui, "Are you sure ?", true);
				if (result == 0) {
					try {
						clientNode.exit();
						frameMainGui.dispose();
					} catch (Exception e) {
						frameMainGui.dispose();
					}
				}
			}
		});
		btnExit.setBounds(353, 465, 129, 44);
		btnExit.setIcon(new ImageIcon(MainGui.class.getResource("/image/exit.png")));
		frameMainGui.getContentPane().add(btnExit);
		
		lblLogo = new JLabel("SECRET CHAT APP");
		lblLogo.setForeground(new Color(0, 0, 205));
		lblLogo.setIcon(new ImageIcon(MainGui.class.getResource("/image/logo.png")));
		lblLogo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblLogo.setBounds(10, 13, 413, 38);
		frameMainGui.getContentPane().add(lblLogo);
		
		lblActiveNow = new JLabel("Users Online");
		lblActiveNow.setForeground(new Color(100, 149, 237));
		lblActiveNow.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblActiveNow.setBounds(10, 70, 80, 16);
		frameMainGui.getContentPane().add(lblActiveNow);
		
		listActive = new JList<>(model);
		listActive.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		listActive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String value = (String)listActive.getModel().getElementAt(listActive.locationToIndex(arg0.getPoint()));
				txtNameFriend.setText(value);
			}
		});
		listActive.setBounds(12, 100, 472, 290);
		frameMainGui.getContentPane().add(listActive);
		
		lblUsername = new JLabel(username);
		lblUsername.setForeground(Color.RED);
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblUsername.setBounds(95, 70, 80, 28);
		frameMainGui.getContentPane().add(lblUsername);

	}
		
	public static int request(String msg, boolean type) {
		JFrame frameMessage = new JFrame();
		return Tags.show(frameMessage, msg, type);
	}
}
