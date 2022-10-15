package blockchain.midterm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Verification {

	/**
	 *  검증 클래스
	 *  @Param 서명 
	 *  @Param 복호화 할 키
	 *  @Retun 검증 성공 여부
	 */
	public boolean verification(String signMessage, Key key) 
			throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchProviderException {
		Sha256 s = new Sha256();
		String[] str = signMessage.split("\n");
		//System.out.println("Verfication.verfication Func : signMessage : " + signMessage);
		//System.out.println("Verfication.verfication Func : str[0] : " + str[0] );				// 온전한 메시지
		//System.out.println("Verfication.verfication Func : str[1] : " + str[1] );				// 키로 암호화 된 메시지
		
		String decSignMessage = Rsa.decryptRSA(str[1], key);									// 해시값 만 남음
		//System.out.println("Verfication.verfication Func : decSignMessage : " + decSignMessage);
		String checkHash = s.encrypt(str[0]);
		//System.out.println("Verfication.verfication Func : checkHash : " + checkHash);
		
		boolean b = checkHash.equals(decSignMessage);
		//System.out.println("Verfication.verfication Func : bool " + b);
		return b;
	}
	
	/**
	 *  서명 클래스
	 *  @Param 보낼 메시지
	 *  @Param 암호화할 키
	 *  @Return 서명
	 */
	public String signature(String message, Key key) 
			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
		Sha256 s = new Sha256();
		//System.out.println("Verfication.signature Func : message : " + message);
		String messageHash = s.encrypt(message);
		//System.out.println("Verfication.signature Func : messageHash : " + messageHash);
		String signMessage = message +"\n" + Rsa.encryptRSA(messageHash, key);
		//System.out.println("Verfication.signature Func : signMessage : "+ signMessage);
		return signMessage;
	}
}
