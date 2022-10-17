package blockchain.midterm;

import java.io.*;
import java.net.*;	// socket 클래스
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Client {
	
	public static Sha256 s = new Sha256();
    public static Rsa rsa = new Rsa();
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
	        System.out.println("-------------------------------------------------");
	        return rsa.readPublicKeyFromPemFile(Keyname);
	    }
	 /**
	  * 트랜잭션 보내는 클라이언트
	  */
	 public static void dataSendClient() {
		 	Socket socket = null;
		 	BufferedWriter bw = null;
		 	
			try {
				socket = new Socket("localhost", 9999);
				
				bw = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );
				
				bw.write("데이터 \n");
				bw.flush();
				
				System.out.println( "Connection success" );
			
			} catch (UnknownHostException e) {
				System.out.println( "[에러] : " + e.getMessage() );
			} catch (IOException e) {
				System.out.println( "[에러] : " + e.getMessage() );
			} finally {
				if ( socket != null ) try { socket.close(); } catch(IOException e) {}
			}
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
		PublicKey ServerPublicKey = null;

		if(s.isExists("ClientPublicKey.pem") == false)
		{
			System.out.println("키교환을 하겠습니다.");
			CreateWallet(wallet);
			ServerPublicKey = keyExchangeclient();
			System.out.println("Client입니다. 서버에게 받은 키 :" +ServerPublicKey);
		}
		else {
			System.out.println("키교환을 이미 하였습니다.");
			wallet.setFromFile("ClientPrivateKey.pem", "ClientPublicKey.pem");
			ServerPublicKey = rsa.readPublicKeyFromPemFile("ServerPublicKey.pem");
		}
		Thread.sleep(5000);
		dataSendClient();
	}

}
