package blockchain.midterm;

import java.security.NoSuchAlgorithmException;

// Block으로 하나의 블록에 대한 정보를 담고 처리하는 역할
public class Block {
	
	private int blockID;
	private int nonce;
	private String data;
	private String previousBlockHash;
	
	public int getBlockID() {
		return blockID;
	}
	public void setBlockID(int blockID) {
		this.blockID = blockID;
	}
	public int getNonce() {
		return nonce;
	}
	public void setNonce(int nonce) {
		this.nonce = nonce;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getPreviousBlockHash(){
		return previousBlockHash;
	}
	public void setPreviousBlockHash(String previousBlockHash)
	{
		this.previousBlockHash = previousBlockHash;
	}
	public Block(int blockID, String previousBlockHash, int nonce, String data)
	{
		this.blockID = blockID;
		this.previousBlockHash = previousBlockHash;
		this.nonce = nonce;
		this.data = data;
	}
	
	public String getBlockHash() throws NoSuchAlgorithmException 
	{
		Sha256 sha = new Sha256();
		return sha.encrypt(nonce + data + previousBlockHash);
	}
	
	 public void getInformation() throws NoSuchAlgorithmException {
	        System.out.println("---------------------------");
	        System.out.println("블록 번호: " + getBlockID());
	        System.out.println("이전 해시: " + getPreviousBlockHash());
	        System.out.println("채굴 변수 값: " + getNonce());
	        System.out.println("블록 데이터: " + getData());
	        System.out.println("블록 해시: " + getBlockHash());
	        System.out.println("---------------------------");
	    }
	
	public void mine() {
	    while(true) {
	        try {
				if(getBlockHash().substring(0, 4).equals("0000")) {
				    System.out.println(blockID + "번째 블록의 채굴에 성공했습니다.");
				    break;
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
	        nonce++;
	    }
	}

}
