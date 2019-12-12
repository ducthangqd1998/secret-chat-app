package temp;

import java.net.URL;

public class testcode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testcode a = new testcode();
		String pathImage = "/E:/DUT/Java/jaa_chat/src/image/smile.png";
		Class cls = a.getClass();
		URL z = cls.getResource(pathImage);
		System.out.println("Value = " + z);

	}

}
