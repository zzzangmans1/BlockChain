package blockchain.midterm;

import java.io.*;
import java.net.*;	// socket 클래스
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Client {
	/**
	 * 서버와 키교환 하는 메소드
	 * @return PublicKey
	 * @throws FileNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IllegalArgumentException
	 * @throws NoSuchProviderException
	 * @throws IOException
	 */
	 public static PublicKey keyExchangeclient() throws FileNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException, NoSuchProviderException, IOException {
		 	System.out.println("----------------ClientKeyExchange-------------------");
	    	Socket mSocket = null;

	        BufferedReader mIn = null;
	        PrintWriter mOut = null;
	        
	        String Keyname = "";
	        try {
	            // 서버에 요청 보내기
	            mSocket = new Socket("0.0.0.0", 9090);
	            System.out.println("0.0.0.0" + " 연결됨");

	            // 통로 뚫기
	            mIn = new BufferedReader(
	                    new InputStreamReader(mSocket.getInputStream()));
	            mOut = new PrintWriter(mSocket.getOutputStream());

	            // 메세지 전달
	            mOut.println("ClientPublicKey.pem");
	            mOut.flush();

	            // 응답 출력
	            Keyname = mIn.readLine();
	            System.out.println("서버에서 보낸 : ");
	            System.out.println(Keyname);

	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            // 소켓 닫기 (연결 끊기)
	            try {
	                mSocket.close();
	            } catch (IOException e) {
	                System.out.println(e.getMessage());
	            }
	        }
	        Rsa rsa = new Rsa();
	        System.out.println("-------------------------------------------------");
	        return rsa.readPublicKeyFromPemFile(Keyname);
	    }
	/**
	 * 지갑 생성하는 메소드
	 * @throws Exception
	 */
	public static void CreateWallet(Wallet wallet) throws Exception {
		System.out.println("----------------ClientWalletCreate--------------\n");
		Rsa.genRSAKeyPair("ClientPrivateKey.pem", "ClientPublicKey.pem");			// 키생성 후 파일에 저장
		wallet.setFromFile("ClientPrivateKey.pem", "ClientPublicKey.pem");
		System.out.println("-------------------------------------------------\n");
	}

	public static void main(String[] args) throws Exception {

    	Security.addProvider(new BouncyCastleProvider());	
    	Wallet wallet = new Wallet();
		CreateWallet(wallet);
		PublicKey ServerPublicKey = null;
		ServerPublicKey = keyExchangeclient();
		
		System.out.println("Client입니다. 서버에게 받은 키 :" +ServerPublicKey);
		
		BufferedReader in = null;
		BufferedWriter out = null;
		
		Socket sock = null;
		Scanner scanner = new Scanner(System.in);
	
		String outputMessage = "";
		String inputMessage	= "";
		
		try {
			sock = new Socket("0.0.0.0", 9999);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
							
			while(true) {
				System.out.print("Client>>"); 			// 프롬포트
				outputMessage = scanner.nextLine();		// 서버에게 보낼 메시지 입력
				if(outputMessage.equalsIgnoreCase("exit")) {
					out.write(outputMessage + "\n");
					out.flush();
					System.out.println("종료되었습니다");
					break;								// 서버에게 exit 보내면 실행 종료
				}
				out.write(outputMessage +"\n");
				out.flush();
				inputMessage = in.readLine();			// 서버로부터 한 행 수신
				System.out.println("서버 : "+inputMessage);
			}
		} catch (Exception e) {			
			System.out.println("에러" + e.getMessage());
		} finally {
			try {
				scanner.close();
				if(sock != null) sock.close();			// 클라이언트 소켓 닫기
			} catch (Exception e) {
				System.out.println("서버와 채팅 중 오류가 발생했습니다.");
			}
		}
	}

}
