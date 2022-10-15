package blockchain.midterm;

import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Wallet {
	
	private PrivateKey privateKey;
    private PublicKey publicKey;
    
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
    
	public String sign(String data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException
	{
		Verification v = new Verification();
		return v.signature(data, privateKey);
	}

	// 특정한 파일로부터 개인키 및 공개키를 불러와 초기화
    public void setFromFile(String privateKeyName, String publicKeyName) throws Exception {
        this.privateKey = new Rsa().readPrivateKeyFromPemFile(privateKeyName);
        this.publicKey = new Rsa().readPublicKeyFromPemFile(publicKeyName);
    }
}
