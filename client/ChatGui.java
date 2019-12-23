package client;

import java.awt.EventQueue;

import javax.crypto.Cipher;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.event.*;
import java.awt.Label;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.security.PublicKey;
import java.util.*;
import java.util.List;

import data.DataFile;
import tags.Decode;
import tags.Encode;
import tags.Tags;
import java.awt.*;



public class ChatGui {

	private static String URL_DIR = System.getProperty("user.dir");
	private static String TEMP = "/temp/";
	private Cryption myKey = null;
	private ChatRoom chat;
	private Socket socketChat;
	private String username = "", nameGuest = "", nameFile = "";
	private PublicKey Pubkey;
	boolean isSendImage = false;
	private JFrame frameChatGui;
	private JTextField textName;
	private JPanel panelMessage;
	private JTextPane txtDisplayChat;
	private Label textState, lblReceive;
	private JButton btnDisConnect, btnSend, btnChoose;
	public boolean isStop = false, isSendFile = false, isReceiveFile = false;
	private String pathImage = "";
	private JProgressBar progressSendFile;
	private JTextField txtPath;
	private int portServer = 0;
	private JDialog dialogIcon, dialogSticker;
	private JTextField txtMessage;
	private JScrollPane scrollPane;
	private String pathIcon;
	private List<JButton> Emotions = new ArrayList<JButton>();
	private List<JButton> Stickers = new ArrayList<JButton>();
	private JButton button;
	private List<String> iconPath = new ArrayList<String>();
	private String s = "";
	private JScrollPane scrollPanel;
	private JPanel panel;
	private File folder = null;


	public ChatGui(String user, String guest, Socket socket, int port, PublicKey key, Cryption mykey) {
		username = user;
		nameGuest = guest;
		socketChat = socket;
		Pubkey = key;
		this.portServer = port;
		myKey = mykey;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatGui window = new ChatGui(username, nameGuest, socketChat, portServer, 0, Pubkey, mykey);
					window.frameChatGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setPathIcon(String pathIcon) {
		this.pathIcon = pathIcon;
	}
	
	public String getPathIcon() {
		return this.pathIcon;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatGui window = new ChatGui();
					window.frameChatGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void updateChat_receive(String msg) {
		appendToPane(txtDisplayChat, "<div class='left' style='width: 40%; background-color: #f1f0f0; "
				 + "margin: 2px; border-radius: 10px; height: 30px; display: flex;"
				 + "align-items: center;'>"+ msg +"</div>");
	}
	
	

	public void updateChat_send(String msg) {
		appendToPane(txtDisplayChat, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%; '></td>"
				+ "<td style='width: 40%; background-color: #0084ff;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}
	
	public void updateChat_notify(String msg) {
		appendToPane(txtDisplayChat, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%; '></td>"
				+ "<td style='width: 40%; background-color: #f1c40f;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}

	
	public void updateChat_send_Symbol(String msg) {
		appendToPane(txtDisplayChat, "<table style='width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 100%;'></td>"
				+ "<td style='width: 100%;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}
	
	public void updateChat_receive_Symbol(String msg) {
		appendToPane(txtDisplayChat, "<div class='left' style='width: 40%;"
				 + "margin: 2px; border-radius: 10px; height: 30px; display: flex;'>" + msg +"</div>");
	}
	
	public ChatGui() {
		initialize();
	}

	public ChatGui(String user, String guest, Socket socket, int port, int a, PublicKey key, Cryption myKey)
			throws Exception {
		username = user;
		nameGuest = guest;
		socketChat = socket;
		this.Pubkey = key;
		this.portServer = port;
		initialize();
		chat = new ChatRoom(socketChat, username, nameGuest, Pubkey, myKey);
		chat.start();
	}

	private void initialize() {
		File fileTemp = new File(URL_DIR + "/temp");
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		
		// Create Frame Private Chat
		frameChatGui = new JFrame();
		frameChatGui.setTitle("Private Chat");
		frameChatGui.setResizable(false);
		frameChatGui.setBounds(200, 200, 673, 645);
		frameChatGui.getContentPane().setLayout(null);
		frameChatGui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// Icon user 
		JLabel lblClientIP = new JLabel("");
		lblClientIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblClientIP.setBounds(30, 6, 41, 40);
		lblClientIP.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/user_chat.png")));
		frameChatGui.getContentPane().add(lblClientIP);

		// username
		textName = new JTextField(username);
		textName.setForeground(Color.RED);
		textName.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		textName.setEditable(false);
		textName.setBounds(70, 6, 148, 40);
		frameChatGui.getContentPane().add(textName);
		textName.setText(nameGuest);
		textName.setColumns(10);
		
		// panel Create Message
		panelMessage = new JPanel();
		// setBounds(x, y, width, height)
		panelMessage.setBounds(6, 363, 649, 201);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
		frameChatGui.getContentPane().add(panelMessage);
		panelMessage.setLayout(null);

		txtMessage = new JTextField("fdfdfd");
		txtMessage.setBounds(10, 21, 479, 62);
		panelMessage.add(txtMessage);
		txtMessage.setColumns(10);

		btnSend = new JButton("");
		btnSend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnSend.setBounds(551, 33, 65, 39);
		btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSend.setContentAreaFilled(false);
		panelMessage.add(btnSend);
		btnSend.setIcon(new ImageIcon(ChatGui.class.getResource("/image/send.png")));
		
		btnSend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if (isSendFile)
					try {
						chat.sendMessage(Encode.sendFile(nameFile));
//						chat.sendFile(nameFile);
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				if (isStop) {
					updateChat_send(txtMessage.getText().toString());
					txtMessage.setText(""); //reset text Send
					return;
				}
				String msg = txtMessage.getText();
				if (msg.equals(""))
					return;
				try {
					chat.sendMessage(Encode.sendMessage(msg));
					updateChat_send(msg);
					txtMessage.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// Button send file for guest 
		btnChoose = new JButton("");
		btnChoose.setBounds(551, 152, 50, 36);
		panelMessage.add(btnChoose);
		btnChoose.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnChoose.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/attachment.png")));
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frameChatGui);
				if (result == JFileChooser.APPROVE_OPTION) {
					isSendFile = true;
					isSendImage = false;
					String path_send = (fileChooser.getSelectedFile()
							.getAbsolutePath()) ;
					System.out.println(path_send);
					File file = new File(path_send);
					try {
						String mimetype = Files.probeContentType(file.toPath());
						if(mimetype != null && mimetype.split("/")[0].equals("image")) {
							pathImage = path_send;
							isSendImage = true;
							isSendFile = false;
						}
					}catch(Exception e) {
						System.out.println("Error is define image: " + e);
					}
					nameFile = fileChooser.getSelectedFile().getName();
					txtPath.setText(path_send);
				}
			}
		});
		btnChoose.setBorder(BorderFactory.createEmptyBorder());
		btnChoose.setContentAreaFilled(false);
		
		txtPath = new JTextField("");
		txtPath.setBounds(76, 163, 433, 25);
		panelMessage.add(txtPath);
		txtPath.setEditable(false);
		txtPath.setColumns(10);
				
				
		Label label = new Label("Path");
		label.setBounds(10, 166, 39, 22);
		panelMessage.add(label);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JButton btnSendLike = new JButton("");
		btnSendLike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + ChatGui.class.getResource("/image/like.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendIcon(msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnSendLike.setBackground(new Color(240, 240, 240));
		btnSendLike.setBounds(501, 31, 50, 43);
		btnSendLike.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/like.png")));
		//transparent button
		btnSendLike.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSendLike.setContentAreaFilled(false);


		panelMessage.add(btnSendLike);
		
		JButton btnIcon = new JButton("");
		btnIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogIcon = new JDialog(frameChatGui, "Icon");
				
				try {
					s = Icon.class.getResource("/image/icon/").toString().split(":", 2)[1];
					folder = new File(s);
				}catch(Exception e) {}
				System.out.println(s);
				File[] listOfFiles = folder.listFiles();
				int i = 0; 
				for (File file : listOfFiles) {
				    if (file.isFile()) {
				        button = new JButton();
				        button.setBorder(new EmptyBorder(0, 0, 0, 0));
				        button.setContentAreaFilled(false);
				        button.setIcon(new ImageIcon(s + file.getName()));
				        button.setBackground(Color.WHITE);
				        iconPath.add(s + file.getName());
				        button.addActionListener( new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println(ChatGui.class.getResource("/image/like.png"));
//								String msg = "<img src='" + "file:" + s + file.getName() + "'></img>";
								String msg = "/image/icon/" + file.getName();
								System.out.println(msg);
								try {
									updateChat_send_Symbol(new String("<img src='" + "file:" + s + file.getName() + "'></img>"));
									chat.sendMessage(Encode.sendIcon(msg));
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						});
				        Emotions.add(button);
				        i++;
				    }
				}
				
				panel = new JPanel(new GridLayout(8, 4, 0, 10));
				
				for (i = 0; i < Emotions.size(); i++) {
				    panel.add(Emotions.get(i));
				}
				panel.setBackground(Color.WHITE);
				scrollPanel = new JScrollPane();
				scrollPanel.setViewportView(panel);
				scrollPanel.setForeground(Color.WHITE);
				dialogIcon.add(scrollPanel);
				dialogIcon.setSize(250, 250);
				dialogIcon.setVisible(true);
			}
		});
		btnIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnIcon.setContentAreaFilled(false);
		btnIcon.setBounds(62, 96, 50, 36);
		btnIcon.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/smile.png")));
		
		JButton btnSticker = new JButton();
		btnSticker.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSticker.setContentAreaFilled(false);
		btnSticker.setBounds(150, 96, 50, 36);
		btnSticker.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/qoobee.png")));
		btnSticker.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialogSticker = new JDialog(frameChatGui, "Icon");
				Stickers = null;
				try {
					s = Icon.class.getResource("/image/stickers/").toString().split(":", 2)[1];
					folder = new File(s);
				}catch(Exception e) {}
				System.out.println(s);
				File[] listOfFiles = folder.listFiles();
				int i = 0; 
				for (File file : listOfFiles) {
				    if (file.isFile()) {
				        button = new JButton();
				        button.setBorder(new EmptyBorder(0, 0, 0, 0));
				        button.setContentAreaFilled(false);
				        button.setIcon(new ImageIcon(s + file.getName()));
				        button.setBackground(Color.WHITE);
				        iconPath.add(s + file.getName());
				        button.addActionListener( new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String msg = "/image/stickers/" + file.getName();
								System.out.println(msg);
								try {
									updateChat_send_Symbol(new String("<img src='" + "file:" + s + file.getName() + "'></img>"));
									chat.sendMessage(Encode.sendIcon(msg));
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						});
				        Stickers.add(button);
				        i++;
				    }
				}
				
				panel = new JPanel(new GridLayout(17, 3, 0, 10));
				
				for (i = 0; i < Stickers.size(); i++) {
				    panel.add(Stickers.get(i));
				}
				panel.setBackground(Color.WHITE);
				scrollPanel = new JScrollPane();
				scrollPanel.setViewportView(panel);
				scrollPanel.setForeground(Color.WHITE);
				dialogSticker.add(scrollPanel);
				dialogSticker.setSize(400, 400);
				dialogSticker.setVisible(true);
			}
		});
		
		panelMessage.add(btnIcon);
		panelMessage.add(btnSticker);
		txtMessage.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = txtMessage.getText();
					if (isStop) {
						updateChat_send(txtMessage.getText().toString());
						txtMessage.setText("");
						return;
					}
					if (msg.equals("")) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
						return;
					}
					try {
						chat.sendMessage(Encode.sendMessage(msg));
						updateChat_send(msg);
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
					} catch (Exception e) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
					}
				}
			}
		});

		btnDisConnect = new JButton("");
		btnDisConnect.setIcon(new ImageIcon(MainGui.class.getResource("/image/leave.png")));
		btnDisConnect.setBorderPainted(false);
		btnDisConnect.setFocusPainted(false);
		btnDisConnect.setContentAreaFilled(false);
		btnDisConnect.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnDisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Tags.show(frameChatGui, "Are you sure to close chat with account: "
						+ nameGuest, true);
				if (result == 0) {
					try {
						isStop = true;
						frameChatGui.dispose();
						chat.sendMessage(Tags.CHAT_CLOSE_TAG);
						chat.stopChat();
						System.gc();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		btnDisConnect.setBounds(540, 6, 113, 40);
		frameChatGui.getContentPane().add(btnDisConnect);

		progressSendFile = new JProgressBar(0, 100);
		progressSendFile.setBounds(170, 577, 388, 14);
		progressSendFile.setStringPainted(true);
		frameChatGui.getContentPane().add(progressSendFile);
		progressSendFile.setVisible(false);

		textState = new Label("");
		textState.setBounds(6, 570, 158, 22);
		textState.setVisible(false);
		frameChatGui.getContentPane().add(textState);

		lblReceive = new Label("Receiving ...");
		lblReceive.setBounds(564, 577, 83, 14);
		lblReceive.setVisible(false);
		frameChatGui.getContentPane().add(lblReceive);
		
		txtDisplayChat = new JTextPane();
		txtDisplayChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDisplayChat.setEditable(false);
		txtDisplayChat.setContentType( "text/html" );
		txtDisplayChat.setMargin(new Insets(6, 6, 6, 6));
		txtDisplayChat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		txtDisplayChat.setBounds(6, 59, 670, 291);
		appendToPane(txtDisplayChat, "<div class='clear' style='background-color:white'></div>"); //set default background
			    
		frameChatGui.getContentPane().add(txtDisplayChat);
	
		scrollPane = new JScrollPane(txtDisplayChat);
		scrollPane.setBounds(6, 59, 649, 291);
		frameChatGui.getContentPane().add(scrollPane);					
	}

	public class ChatRoom extends Thread {
		private PublicKey PublicKey;
		private Cryption myKey = null;
		private Socket connect;
		// Self
		private ObjectOutputStream outPeer;
		// Guest 
		private ObjectInputStream inPeer;
		private boolean continueSendFile = true, finishReceive = false;
		private boolean sendImage = false;
		private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0,
				sizeReceive = 0;
		private String nameFileReceive = "";
		private InputStream inFileSend;
		private DataFile dataFile;

		public ChatRoom(Socket connection, String name, String guest, PublicKey key, Cryption mykey)
				throws Exception {
			connect = new Socket();
			// user socket 
			connect = connection;
			PublicKey = key;
			myKey = mykey;
			nameGuest = guest;
		}

		@Override
		public void run() {
			super.run();
			OutputStream out = null;
			while (!isStop) {
				try {
					inPeer = new ObjectInputStream(connect.getInputStream());
					Object obj = inPeer.readObject();
					System.out.println("objobj " + obj);
					if (obj instanceof String) {
						String msgObj = obj.toString();
						byte message[] = null;
						try {
							System.out.println("okokoko" + myKey.getPrivateKey());
							Cipher c = Cipher.getInstance("RSA");
							c.init(Cipher.DECRYPT_MODE, myKey.getPrivateKey());
							message = c.doFinal(Base64.getDecoder().decode(msgObj));
							System.out.println("Dữ liệu sau khi giải mã: " + new String(message));
						}catch(Exception e) {
							System.out.println("Error for decode string: " + e);
						}
						msgObj = new String(message);
						System.out.println("============" + msgObj + "============");
						// User bên kia có ý định dừng chat
						if (msgObj.equals(Tags.CHAT_CLOSE_TAG)) {
							System.out.println(1);
							isStop = true;
							Tags.show(frameChatGui, nameGuest 
									+ " closed chat with you! This windows will also be closed.", false);
							try {	
								isStop = true;
								frameChatGui.dispose();
								chat.sendMessage(Tags.CHAT_CLOSE_TAG);
								chat.stopChat();
								System.gc();
							} catch (Exception e) {
								e.printStackTrace();
							}
							connect.close();
							break;
						}
						if (Decode.checkFile(msgObj)) {
							System.out.println(2);
							isReceiveFile = true;
							nameFileReceive = msgObj.substring(10,
									msgObj.length() - 11);
							int result = Tags.show(frameChatGui, nameGuest
									+ " send file " + nameFileReceive
									+ " for you", true);
							if (result == 0) {
								File fileReceive = new File(URL_DIR + TEMP
										+ "/" + nameFileReceive);
								if (!fileReceive.exists()) {
									fileReceive.createNewFile();
								}
								String msg = Tags.FILE_REQ_ACK_OPEN_TAG
										+ Integer.toBinaryString(portServer)
										+ Tags.FILE_REQ_ACK_CLOSE_TAG;
								sendMessage(msg);
							} else {
								sendMessage(Tags.FILE_REQ_NOACK_TAG);
							}
						} else if (Decode.checkFeedBack(msgObj)) {
							btnChoose.setEnabled(false);
							System.out.println(3);
							new Thread(new Runnable() {
								public void run() {
									try {
										sendMessage(Tags.FILE_DATA_BEGIN_TAG);
										updateChat_notify("You are sending file: " + nameFile);
										isSendFile = false;
										sendFile(txtPath.getText());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						} 
						else if (msgObj.equals(Tags.FILE_REQ_NOACK_TAG)) {
							System.out.println(4);
							Tags.show(frameChatGui, nameGuest
									+ " don't want receive file", false);
						} else if (msgObj.equals(Tags.FILE_DATA_BEGIN_TAG)) {
							System.out.println(5);
							finishReceive = false;
							lblReceive.setVisible(true);
							out = new FileOutputStream(URL_DIR + TEMP
									+ nameFileReceive);
						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG)) {
							System.out.println("TT6");
							updateChat_receive("You receive file: " + nameFileReceive + " with size " + sizeReceive + " KB");
							sizeReceive = 0;
							out.flush();
							out.close();
							lblReceive.setVisible(false);
							new Thread(new Runnable() {

								@Override
								public void run() {
									showSaveFile();
								}
							}).start();
							finishReceive = true;
//						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG) && isFileLarge == true) {
//							updateChat_receive("File " + nameFileReceive + " too large to receive");
//							sizeReceive = 0;
//							out.flush();
//							out.close();
//							lblReceive.setVisible(false);
//							finishReceive = true;
						}
						else if(Decode.checkIcon(msgObj)) {
							String path = Decode.getPathIcon(msgObj);
							s = ChatGui.class.getResource(path).toString().split(":", 2)[1];
//							folder = new File(s);
							String msg = "<img src='" + "file:" + s + "'></img>";
							updateChat_receive_Symbol(new String(msg));
						}
						else {
							System.out.println(7);
							updateChat_receive( new String(msgObj));
						}
					} else if (obj instanceof DataFile) {
						DataFile data = (DataFile) obj;
						++sizeReceive;
						out.write(data.data);
					}
				} catch (Exception e) {
					File fileTemp = new File(URL_DIR + TEMP + nameFileReceive);
					if (fileTemp.exists() && !finishReceive) {
						fileTemp.delete();
					}
				}
			}
		}

		
		private void getData(String path) throws Exception {
			File fileData = new File(path);
			if (fileData.exists()) {
				sizeOfSend = 0;
				dataFile = new DataFile();
				sizeFile = (int) fileData.length();
				sizeOfData = sizeFile % 1024 == 0 ? (int) (fileData.length() / 1024)
						: (int) (fileData.length() / 1024) + 1;
				inFileSend = new FileInputStream(fileData);
			}
		}
		
		public String Encryption(String s, PublicKey pKey) {
			String strEncrypt = null;
			try {			
				Cipher c = Cipher.getInstance("RSA");
				c.init(Cipher.ENCRYPT_MODE, pKey);
				byte encryptOut[] = c.doFinal(s.getBytes());
				strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
				System.out.println("Chuỗi sau khi mã hoá: " + strEncrypt);
			}catch(Exception e) {
				System.out.println("Error for encode string: " + e);
			}
			return strEncrypt;
		}

		public void sendFile(String path) throws Exception {
			getData(path);
			textState.setVisible(true);
			if (sizeOfData > Tags.MAX_MSG_SIZE/1024) {
				textState.setText("File is too large...");
				inFileSend.close();
//				isFileLarge = true;
//				sendMessage(Tags.FILE_DATA_CLOSE_TAG);
				txtPath.setText("");
				btnChoose.setEnabled(true);
				isSendFile = false;
				inFileSend.close();
				return;
			}
			
			progressSendFile.setVisible(true);
			progressSendFile.setValue(0);
			
			textState.setText("Sending ...");
			do {
				System.out.println("sizeOfSend : " + sizeOfSend);
				if (continueSendFile) {
					continueSendFile = false;
//					updateChat_notify("If duoc thuc thi: " + String.valueOf(continueSendFile));
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								inFileSend.read(dataFile.data);
								sendMessage(dataFile);
								sizeOfSend++;
								if (sizeOfSend == sizeOfData - 1) {
									int size = sizeFile - sizeOfSend * 1024;
									dataFile = new DataFile(size);
								}
								progressSendFile
										.setValue((int) (sizeOfSend * 100 / sizeOfData));
								if (sizeOfSend >= sizeOfData) {
									inFileSend.close();
									isSendFile = true;
									sendMessage(Tags.FILE_DATA_CLOSE_TAG);
									progressSendFile.setVisible(false);
									textState.setVisible(false);
									isSendFile = false;
									txtPath.setText("");
									btnChoose.setEnabled(true);
									updateChat_notify("File sent complete");
									inFileSend.close();
								}
								continueSendFile = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			} while (sizeOfSend < sizeOfData);
		}

		private void showSaveFile() {
			while (true) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showSaveDialog(frameChatGui);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile()
							.getAbsolutePath() + "/" + nameFileReceive );
					if (!file.exists()) {
						try {
							file.createNewFile();
							Thread.sleep(1000);
							InputStream input = new FileInputStream(URL_DIR
									+ TEMP + nameFileReceive);
							OutputStream output = new FileOutputStream(
									file.getAbsolutePath());
							copyFileReceive(input, output, URL_DIR + TEMP
									+ nameFileReceive);
						} catch (Exception e) {
							Tags.show(frameChatGui, "Your file receive has error!!!",
									false);
						}
						break;
					} else {
						int resultContinue = Tags.show(frameChatGui,
								"File is exists. You want save file?", true);
						if (resultContinue == 0)
							continue;
						else
							break;
					}
				}
			}
		}
		
		//void send Message
		// synchronized: Đồng bộ hóa dữ liệu, chia sẻ tài nguyên cho nhiều thread truy cập cùng 1 file
		public synchronized void sendMessage(Object obj) throws Exception {
			outPeer = new ObjectOutputStream(connect.getOutputStream());
			// only send text
			if (obj instanceof String) {
				String message = obj.toString();
				System.out.println("message: " + message);
				String encodeString = Encryption(message, PublicKey);
				outPeer.writeObject(encodeString);
				outPeer.flush();
				if (isReceiveFile)
					isReceiveFile = false;
			} 
			// send attach file
			else if (obj instanceof DataFile) {
				System.out.println("Send file");
				outPeer.writeObject(obj);
				outPeer.flush();
			}
		}

		public void stopChat() {
			try {
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void copyFileReceive(InputStream inputStr, OutputStream outputStr,
			String path) throws IOException {
		byte[] buffer = new byte[1024];
		int lenght;
		while ((lenght = inputStr.read(buffer)) > 0) {
			outputStr.write(buffer, 0, lenght);
		}
		inputStr.close();
		outputStr.close();
		File fileTemp = new File(path);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
	}
	
	// send html to pane
	  private void appendToPane(JTextPane tp, String msg){
	    HTMLDocument doc = (HTMLDocument)tp.getDocument();
	    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
	    try {
	    	
	      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
	      tp.setCaretPosition(doc.getLength());
	      
	    } catch(Exception e){
	      e.printStackTrace();
	    }
	  }
}
