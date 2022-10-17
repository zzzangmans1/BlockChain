package blockchain.midterm;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sha256 {
	
	protected  String encrypt(String plaintext) 
	throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(plaintext.getBytes());
		
		return bytesToHex(md.digest());
	}
	
	private  String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		
		for(byte b : bytes) sb.append(String.format("%02X", b));
		
		return sb.toString();
	}
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String now = sdf.format(time);
		return now;
	}
	
	public static boolean isExists(String filename) {
		// Create a file object
				File file = new File(filename);
				
				// 1. check if the file exists or not
				boolean isExists = file.exists();
				return isExists;
	}
}
