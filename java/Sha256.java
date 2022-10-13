package blockchain.midterm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class sha256 {
	
	protected String encrypt(String plaintext) 
	throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(plaintext.getBytes());
		
		return bytesToHex(md.digest());
	}
	
	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		
		for(byte b : bytes) sb.append(String.format("%02X", b));
		
		return sb.toString();
	}
}
