package client;

import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.*;

public class Icon {
	JButton btnSmile, btnSmileBig, btnSmileCry, btnCrying, btnHeartEye, btnScared, btnSad;
	JPanel panel;
	
	public void GUI() {
		new Panel(new GridLayout(4, 4));
	}
	public Icon() {
		super();
		GUI();
	}
	public static void main(String args[]) {
		new Icon();
	}
}
