package client;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class Cryption {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private SecureRandom sr;
	private KeyPairGenerator kpg;
	private KeyPair kp;
	
	public Cryption() {
		try {
			SecureRandom sr = new SecureRandom();
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024, sr);			
			// Initial Key
			kp = kpg.genKeyPair();			
			// Public Key
			publicKey = kp.getPublic();			
			// Private Key
			privateKey = kp.getPrivate();
		}catch(Exception e) {
			System.out.println("Err: Fail to generate key");
		}
	}
	
	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	public String Decryption(String s, PrivateKey priKey) {
		String strDecry = null;
		System.out.println("decryption");
		try {
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.DECRYPT_MODE, priKey);
			byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(s));
			System.out.println("Dữ liệu sau khi giải mã: " + new String(decryptOut));
		}catch(Exception e) {
			System.out.println("Error for decode string: " + e);
		}
		System.out.println("decryption" + strDecry);
		return strDecry;
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
	
	public static void main(String[] args) {
		Cryption a = new Cryption();
		System.out.println("=====================================");
		Cryption b = new Cryption();
//		String msg = a.Encryption("Helloworld", b.getPublicKey());
//		b.Decryption(msg, b.getPrivateKey());
//		System.out.println(b.publicKey);
		String pub = Base64.getEncoder().encodeToString(b.publicKey.getEncoded());
		System.out.println(b.publicKey.getEncoded().toString().getBytes());
		byte[] z = Base64.getDecoder().decode(pub);
		
		try {
			System.out.println(z);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(z);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = factory.generatePublic(spec);
			
			String msg = a.Encryption("Helloworld", pubKey);
			b.Decryption(msg, b.getPrivateKey());
		}catch(Exception e) {
			System.out.println("Error: " + e);
		}
		
		

	}

	

}