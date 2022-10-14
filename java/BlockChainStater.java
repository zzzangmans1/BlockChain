package blockchain.midterm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BlockChainStater {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, FileNotFoundException, IOException, InvalidKeySpecException, IllegalArgumentException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
		
		Rsa rsa = new Rsa();
		
		// 키 생성 후 파일로 저장
		rsa.genRSAKeyPair("privatekey1.pem", "publickey1.pem");
		rsa.genRSAKeyPair("privatekey2.pem", "publickey2.pem");
		
		// 파일에서 키로 읽음
		PrivateKey privatekey1 = rsa.readPrivateKeyFromPemFile("privatekey1.pem");
		PublicKey publickey1 = rsa.readPublicKeyFromPemFile("publickey1.pem");

		PrivateKey privatekey2 = rsa.readPrivateKeyFromPemFile("privatekey2.pem");
		PublicKey publickey2 = rsa.readPublicKeyFromPemFile("publickey2.pem");
		
		String message = "A가 B에게 10000개의 코인을 보냈습니다.";
		
		String signMessage = Verification.signature(message, privatekey1);
		
		boolean b = Verification.verfication(signMessage, publickey1);
		
		System.out.println("신원 검증 : " + b);
		
		InetAddress local = InetAddress.getLocalHost();
		System.out.println("My PC IP :" + local.getHostAddress());
		
		//		Block block1 = new Block(1, null, 0, new ArrayList());
//        block1.mine();
//        block1.showInformation();
//        
//        Block block2 = new Block(2, block1.getBlockHash(), 0, new ArrayList());
//        block2.addTransaction(new Transaction("A", "B", 1.5));
//        block2.addTransaction(new Transaction("C", "B", 0.7));
//        block2.mine();
//        block2.showInformation();
//        
//        Block block3 = new Block(3, block2.getBlockHash(), 0, new ArrayList());
//        block3.addTransaction(new Transaction("D", "E", 8.2));
//        block3.addTransaction(new Transaction("B", "A", 0.4));
//        block3.mine();
//        block3.showInformation();
//        
//        Block block4 = new Block(4, block3.getBlockHash(), 0, new ArrayList());
//        block4.addTransaction(new Transaction("E", "D", 0.1));
//        block4.mine();
//        block4.showInformation();
	}

}
