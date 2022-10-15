package blockchain.midterm;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

// Block으로 하나의 블록에 대한 정보를 담고 처리하는 역할 클래스
public class Block {
	 
    private int blockID;
    private String previousBlockHash;
    private int nonce;
    private ArrayList<Transaction> transactionList;
    
    public int getBlockID() {
        return blockID;
    }
    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }
    public int getNonce() {
        return nonce;
    }
    public void setNonce(int nonce) {
        this.nonce = nonce;
    }
    public String getTransaction() throws NoSuchAlgorithmException {
        String transactionInformations = "";
        
        for(int i=0;i<transactionList.size();i++) {
            transactionInformations += transactionList.get(i).getInformation();
        }
        
        return transactionInformations;
    }
    
    public Block(int blockID, String previousBlockHash, int nonce, ArrayList<Transaction> transactionList) {
        this.blockID = blockID;
        this.previousBlockHash = previousBlockHash;
        this.nonce = nonce;
        this.transactionList = transactionList;
    }
    
    public String getBlockHash() throws NoSuchAlgorithmException {
    	Sha256 s = new Sha256();
        return s.encrypt(nonce + getTransaction() + previousBlockHash);
    }
    
    // 정상적인 트랜잭션만 블록에 추가
    public void addTransaction(Transaction transaction) throws Exception {
    	Verification v = new Verification();
        if(v.verification(transaction.getSignature() , transaction.getSender())) {
            System.out.println("정상적인 트랜잭션을 발견했습니다.");
            transactionList.add(transaction);
        } else {
            System.out.println("트랜잭션이 바르게 인증되지 않았습니다.");
        }
    }
    public void showInformation() throws NoSuchAlgorithmException {    
        System.out.println("---------------------------");
        System.out.println("블록 번호: " + getBlockID());
        System.out.println("이전 해시: " + getPreviousBlockHash());
        System.out.println("채굴 변수 값: " + getNonce());
        System.out.println("블록 데이터: ");
        for(int i=0;i<transactionList.size();i++) System.out.println(transactionList.get(i).getInformation());
        System.out.println("블록 해시: " + getBlockHash());
        System.out.println("---------------------------");
    }
    
    public void mine() throws NoSuchAlgorithmException {
        while(true) {
            if(getBlockHash().substring(0, 4).equals("0000")) {
                System.out.println(blockID + "번째 블록의 채굴에 성공했습니다.");
                break;
            }
            nonce++;
        }
    }
}

