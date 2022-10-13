package blockchain.midterm;

import java.text.SimpleDateFormat;
import java.util.Date;

// Transaction 클래스는 A라는 사람이 B라는 사람에게 코인을 얼마나 전송했는지에 대한 하나의 코인 거래 정보를 저장하는 역할 클래스
public class Transaction {
	String sender;
	String receiver;
	double amount;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Transaction(String sender, String recevier, double amount)
	{
		this.sender = sender;
		this.receiver = recevier;
		this.amount = amount;
	}
	
	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Date time = new Date();
		String now = sdf.format(time);
		return now;
	}
	
	public String getInformation() {
		return "[" + getDate() + "]" + sender + "이(가) " +receiver + "에게" +amount + "개의 코인을 보냈습니다.";
	}
}
