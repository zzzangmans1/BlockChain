package blockchain.midterm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

// Transaction 클래스는 A라는 사람이 B라는 사람에게 코인을 얼마나 전송했는지에 대한 하나의 코인 거래 정보를 저장하는 역할 클래스
public class Transaction {
	
	String signature;
	PublicKey sender;
	PublicKey receiver;
	double amount;
	Timestamp timestamp;
	
	public String getSignature() {
		return signature;
	}
	public void setSignnature(String signature) {
		this.signature = signature;
	}
	public PublicKey getSender() {
		return sender;
	}
	public void setSender(PublicKey sender) {
		this.sender = sender;
	}
	public PublicKey getReceiver() {
		return receiver;
	}
	public void setReceiver(PublicKey receiver) {
		this.receiver = receiver;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public Transaction(Wallet wallet, PublicKey recevier, double amount, String timestamp) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException
	{
		
		this.sender = wallet.getPublicKey();
		this.receiver = recevier;
		this.amount = amount;
		this.timestamp = Timestamp.valueOf(timestamp);
		this.signature = wallet.sign(getData());
	}
	
	
	
	// 서명 값을 포함한 단순 트랜잭션 정보를 반환
	public String getInformation() throws NoSuchAlgorithmException {
		Sha256 s = new Sha256();
		return "<" + signature + ">\n" + s.encrypt(sender.toString()) + " -> " + s.encrypt(receiver.toString()) + " : " +amount + " 개 " + timestamp;
	}
	
	// 서명 값을 제외한 단순 트랜젝션 정보를 반환
	public String getData() throws NoSuchAlgorithmException {
		Sha256 s = new Sha256();
		return s.encrypt(sender.toString()) + " -> " + s.encrypt(receiver.toString()) + " : " +amount + " 개 " + timestamp;
	}
}
