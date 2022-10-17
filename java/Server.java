package blockchain.midterm;

import java.io.*;
import java.net.*;	// socket 클래스
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class Server {
	/**
	 * 클라이언트와 키교환 하는 메소드
	 * @return PublicKey
	 * @throws FileNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IllegalArgumentException
	 * @throws NoSuchProviderException
	 * @throws IOException
	 */
	public static PublicKey keyExchangeServer() throws FileNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException, NoSuchProviderException, IOException {
		System.out.println("----------------ServerKeyExchange-------------------");
        ServerSocket mServerSocket = null;
        Socket mSocket = null;

        BufferedReader mIn = null;    // 들어오는 통로
        PrintWriter mOut = null;  // 나가는 통로
        
        String keyname = "";
        try {
            mServerSocket = new ServerSocket(9090);
            System.out.println("");
            // 스레드가 멈춰 있고

            // 연결 요청이 들어오면 연결
            mSocket = mServerSocket.accept();
            System.out.println("클라이언트와 연결 됨");

            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream()));

            mOut = new PrintWriter(mSocket.getOutputStream());
            
            keyname = mIn.readLine();
            // 클라이언트에서 보낸 문자열 출력
            System.out.println("클라이언트에서 보낸 : ");
            System.out.println(keyname);

            // 클라이언트에 문자열 전송
            mOut.println("ServerPublicKey.pem");
            mOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 소켓 닫기 (연결 끊기)
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Rsa rsa = new Rsa();
        System.out.println("------------------------------------------------------");
        return rsa.readPublicKeyFromPemFile(keyname);
    }
	/**
	 * 지갑 생성하는 메소드
	 * @throws Exception 
	 */
	public static void CreateWallet(Wallet wallet) throws Exception
	{
		System.out.println("----------------ServerWalletCreate--------------\n");
		Rsa.genRSAKeyPair("ServerPrivateKey.pem", "ServerPublicKey.pem");		// 키생성 후 파일에 저장
		wallet.setFromFile("ServerPrivateKey.pem", "ServerPublicKey.pem");		// 키파일 읽어와 지갑에 저장
		System.out.println("------------------------------------------------\n");
	}
	
	/**
	 * 트랜스액션 생성하는 메소드
	 * @param sender
	 * @param publicKey
	 * @param data
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchProviderException
	 */
	public static void CreateTransaction(Wallet sender, PublicKey publicKey, String data, Transaction transaction) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
		Sha256 util = new Sha256();
		transaction = new Transaction(sender, publicKey, data, util.getDate());
	}
	
	public static void CreateBlock(int blockNum, String prevBlockHash, Transaction transaction) throws Exception {
		 Block block = new Block(blockNum, prevBlockHash, 0, new ArrayList());
		 block.mine();
		 block.addTransaction(transaction);
		 block.showInformation(); 
	}
	
	public static void main(String[] args) throws Exception {

    	Security.addProvider(new BouncyCastleProvider());	
		Wallet wallet = new Wallet();
		CreateWallet(wallet);
		PublicKey ClientpublicKey = null;
		ClientpublicKey = keyExchangeServer();
		
		System.out.println("서버입니다. 클라이언트에게 받은 키 : " + ClientpublicKey);
		
		ServerSocket ss = null;		
		Socket sock = null;
		
		BufferedReader in = null;							// 데이터 주고 받을 입출력 스트림 생성
		BufferedWriter out = null;
		
		String inputMessage = "";
		String outputMessage = ""; 
		
		Scanner scanner = new Scanner(System.in);			// 키보드에서 읽을 scanner 객체 생성
		
		try {												// try catch 는 ServerSocket 오류 때문에 
			ss = new ServerSocket(9999); 					// 서버 소켓 포트 9999번 생성
			
			
			System.out.println("연결을 기다리고 있습니다.");	
			
			sock = ss.accept();								// 클라이언트로부터 접속 대기
			System.out.println("연결되었습니다.");
						
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));		// accept() 메소드로부터 얻은 socket 객체의 getInputStream, getOutputStream 메소드를 이용하여 얻어낸다.
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
						
			while(true) {
				inputMessage = in.readLine();				// 클라이언트로부터 한 행 읽기
				System.out.println(sock.getInetAddress() + ":" + sock.getLocalPort() + "에서 연결되었습니다.");
				if(inputMessage.equalsIgnoreCase("exit")) {
					System.out.println(ss.getInetAddress() +":"+ ss.getLocalPort() +"에서 클라이언트에서 종료하였습니다");
					break;
				}
				System.out.println("클라이언트: " + inputMessage);
				System.out.print("Server>>");						
				outputMessage = scanner.nextLine();			
				out.write(outputMessage + "\n");			// 키보드에서 읽은 문자열 전송
				out.flush();								// out의 스트림 버퍼에 있는 모든 문자열 전송
			}
		} catch (IOException e) {
			System.out.println (e.toString());
		} finally {
			try {
				sock.close();								// 통신용 소켓 닫기
				ss.close();									// 서버 소켓 닫기
			} catch(IOException e) {
				System.out.println("통신중 오류가 발생했습니다");
			}
		}
	}
}
