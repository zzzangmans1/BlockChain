package blockchain.midterm;

import java.io.*;
import java.net.*;	// socket 클래스
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class Server {

	public static Block[] block = new Block[20];
	public static Transaction[] t = new Transaction[20];
	public static Sha256 s = new Sha256();
	public static Rsa rsa = new Rsa();
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
	 * 트랜잭션 받을 서버
	 * @throws Exception 
	 */
	public static void dataReceiveServer(Wallet w, PublicKey key) throws Exception {
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader br = null;
		
		int transNum = 0;
		String prevH = null;
		int blockNum = 0;
		
		try {
			serverSocket = new ServerSocket( 9999 );
			
			//항시 대기 서버
			while(true) {
				//CreateBlock(blockNum, prevH, new ArrayList());
				try {
					System.out.println( serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort() +"\tData Receive Server Ready" );
					socket = serverSocket.accept();
					System.out.println( socket.getLocalAddress() + ":" + socket.getLocalPort() + "\tClient connect");
					br = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
					String data = br.readLine();
					System.out.println("암호화 된 데이터 : " + data);
					data = rsa.decryptRSA(data, key);
					System.out.println( "복호화 된 데이터 : " + data );
					
					block[blockNum] = new Block(blockNum, prevH, 0, new ArrayList());
					block[blockNum].mine();
					t[transNum] = new Transaction(w, key, data, s.getDate());
					block[blockNum].addTransaction(t[transNum++]);
					block[blockNum].showInformation();
					prevH = block[blockNum++].getBlockHash();
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if ( socket != null ) try { socket.close(); } catch(IOException e) {}
				}
			}
		} catch (IOException e) {
			System.out.println( "[에러] : " + e.getMessage() );
		} finally {
			if ( serverSocket != null ) try { serverSocket.close(); } catch(IOException e) {}
		}
	}
	/**
	 * 지갑 생성하는 메소드
	 * @throws Exception 
	 */
	public static void CreateWallet(Wallet wallet) throws Exception
	{
		System.out.println("----------------ServerWalletCreate--------------\n");
		rsa.genRSAKeyPair("ServerPrivateKey.pem", "ServerPublicKey.pem");		// 키생성 후 파일에 저장
		wallet.setFromFile("ServerPrivateKey.pem", "ServerPublicKey.pem");		// 키파일 읽어와 지갑에 저장
		System.out.println("------------------------------------------------\n");
	}
	
	public static void main(String[] args) throws Exception {

		PublicKey ClientpublicKey = null;
		Wallet wallet = new Wallet();
		
    	Security.addProvider(new BouncyCastleProvider());	
    	if(s.isExists("ClientPublicKey.pem") == false)								// 키교환을 받았었는지
    	{
    		System.out.println("키교환을 하겠습니다.");
    		CreateWallet(wallet);
    		ClientpublicKey = keyExchangeServer();
    		System.out.println("서버입니다. 클라이언트에게 받은 키 : " + ClientpublicKey);
    	}
    	else {
    		System.out.println("키교환을 이미 하였습니다.");
    		wallet.setFromFile("ServerPrivateKey.pem", "ServerPublicKey.pem");		// 키파일 읽어와 지갑에 저장
    		ClientpublicKey = rsa.readPublicKeyFromPemFile("ClientPublicKey.pem");	
    	}
		dataReceiveServer(wallet, ClientpublicKey);
	}
}
