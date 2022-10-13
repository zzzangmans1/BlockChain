package blockchain.midterm;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.util.*;
import java.io.*;



public class Rsa {
	
	/**
     * java.security 임포트 해서 사용
     * 1024비트 RSA 키쌍을 생성합니다.
	 * @throws NoSuchProviderException 
     */
    public static KeyPair genRSAKeyPair() 
    		throws NoSuchAlgorithmException, NoSuchProviderException {
    	
    	//Security.addProvider(new BouncyCastleProvider());					// "BC" Provider 애플리케이션 실행시 BouncyCalstelProvider를 추가한다.
        SecureRandom secureRandom = new SecureRandom();						// 난수 생성
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA"); 	// RSA 객체 인스턴스화 BC는 바운드 캐슬 약자
        gen.initialize(1024, secureRandom);									// 1024비트 난수로 키 페어 제네레이터 초기화
        KeyPair keyPair = gen.genKeyPair();									// 키 페어를 생성
        return keyPair;
    }
 
    /**
     * Public Key로 RSA 암호화를 수행합니다.
     * @param plainText 암호화할 평문입니다.
     * @param publicKey 공개키 입니다.
     * @return
     
    public static String encryptRSA(String plainText, PublicKey publicKey)
           throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                  BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA");							// Chipher 객체를 RSA로 인스턴스화
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);						// ENCRYPT_MODE: cipher 객체를 암호화 모드로 초기화하고 공개키로 암호화.
        byte[] bytePlain = cipher.doFinal(plainText.getBytes());			// RSA 암호화
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);	// base64로 바이트 배열을 스트링 배열로 암호화해준다.
        
        return encrypted;
    }
    */
    
 // 공개키, 개인키로 암호화 가능
    public static String AllencryptRSA(String plainText, Key Key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                   BadPaddingException, IllegalBlockSizeException {

         Cipher cipher = Cipher.getInstance("RSA");							// Chipher 객체를 RSA로 인스턴스화
         cipher.init(Cipher.ENCRYPT_MODE, Key);								// ENCRYPT_MODE: cipher 객체를 암호화 모드로 초기화하고 공개키로 암호화.
         byte[] bytePlain = cipher.doFinal(plainText.getBytes());			// RSA 암호화
         String encrypted = Base64.getEncoder().encodeToString(bytePlain);	// base64로 바이트 배열을 스트링 배열로 암호화해준다.
         
         return encrypted;
     }
    
 	// 공개키, 개인키로 복호화 가능
    public static String AlldecryptRSA(String encrypted, Key Key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

         Cipher cipher = Cipher.getInstance("RSA");									// Chipher 객체를 RSA로 인스턴스화
         byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());	// base64로 스트링 배열로 바이트배열로 복호화해준다.
         cipher.init(Cipher.DECRYPT_MODE, Key);										// DECRYPT_MODE : cipher 객체를 복호화 모드로 초기화하고, 개인키로 복호화하겠다.
         byte[] bytePlain = cipher.doFinal(byteEncrypted);							// RSA 복호화
         String decrypted = new String(bytePlain, "utf-8");							// 바이트 배열을 String 배열로 변경
         
         return decrypted;
     }
    
    /**
     * Private Key로 RAS 복호화를 수행합니다.
     *
     * @param encrypted 암호화된 이진데이터를 base64 인코딩한 문자열 입니다.
     * @param privateKey 복호화를 위한 개인키 입니다.
     * @return
     * @throws Exception
     
    public static String decryptRSA(String encrypted, PrivateKey privateKey)
           throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
           BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance("RSA");									// Chipher 객체를 RSA로 인스턴스화
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());	// base64로 스트링 배열로 바이트배열로 복호화해준다.
        cipher.init(Cipher.DECRYPT_MODE, privateKey);								// DECRYPT_MODE : cipher 객체를 복호화 모드로 초기화하고, 개인키로 복호화하겠다.
        byte[] bytePlain = cipher.doFinal(byteEncrypted);							// RSA 복호화
        String decrypted = new String(bytePlain, "utf-8");							// 바이트 배열을 String 배열로 변경
        
        return decrypted;
    }
*/
   
    // Pem 오브젝트 세팅하고 파일로 저장
    private static void writePemFile(Key key, String description, String filename) 
    		throws FileNotFoundException, IOException
    {
    	Pem pemFile = new Pem(key, description);
    	pemFile.write(filename);
    	System.out.println(String.format("%s에 암호키를 저장하였습니다.",filename));
    }
    
    // Pem 파일 읽고 문자열 반환
    private static String readPemFile(String filename)
    		throws FileNotFoundException, IOException {
    	String pem = "";
    	BufferedReader br = new BufferedReader(new FileReader(filename));
    	
    	String line = "";
    	while((line = br.readLine()) != null) pem += line + "\n";
    	
    	br.close();
    	return pem;
    }
    
 // 문자열 형태의 인증서에서 개인키를 추출하는 함수
    public static PrivateKey readPrivateKeyFromPemFile(String privateKeyName) 
            throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException{
        
        String data = readPemFile(privateKeyName); 
        System.out.println("RSA 개인키를 " + privateKeyName + "로부터 불러왔습니다.");
        //System.out.println(data);
        
        // 불필요한 설명 구문 제거
        data = data.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "");
        data = data.replaceAll("-----END RSA PRIVATE KEY-----", "");
        data = data.replaceAll("\n", "");
        //System.out.println(data);

        // PEM 파일은 Base64로 인코딩 되어있으므로 디코딩
        byte[] decoded = Base64.getDecoder().decode(data);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(spec);
        return privateKey;
    }
    
    // 문자열 형태의 인증서에서 공키를 추출하는 함수
    public static PublicKey readPublicKeyFromPemFile(String publicKeyName) 
            throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalArgumentException{
        
        String data = readPemFile(publicKeyName); 
        System.out.println("RSA 공개키를 " + publicKeyName + "로부터 불러왔습니다.");
        //System.out.println(data);
        
        // 불필요한 설명 구문 제거
        data = data.replaceAll("-----BEGIN RSA PUBLIC KEY-----", "");
        data = data.replaceAll("-----END RSA PUBLIC KEY-----", "");
        data = data.replaceAll("\n", "");							// 파일에 저장될 때 띄어쓰기가 있는데 그게 있으면 base64 암복호화가 안돼 지워준다.
        
        
        // PEM 파일은 Base64로 인코딩 되어있으므로 디코딩
        byte[] decoded = Base64.getDecoder().decode(data);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(spec);
        return publicKey;
    }
    
    public static void main(String[] args) throws Exception {
    	
    	// RSA 키 쌍 생성
    	
    	KeyPair keypair = genRSAKeyPair();
    	System.out.println("RSA 키 쌍 생성하였습니다.");
    	
    	PublicKey publickey = keypair.getPublic();
    	PrivateKey privatekey = keypair.getPrivate();

    	//System.out.println("저장하기 전 키 쌍");
    	//System.out.println("PUBLICK KEY : " + publickey);
    	//System.out.println("PRIVATE KEY : " + privatekey);
    	
    	String stringPublicKey = Base64.getEncoder().encodeToString(publickey.getEncoded());			// PrivateKey Base64 암호화 후 String으로 반환
        String stringPrivateKey = Base64.getEncoder().encodeToString(privatekey.getEncoded());		// PublicKey Base64 암호화 후 String으로 반환

//        System.out.println("저장하기 전 암호화 한 키 쌍");
//        System.out.println("PUBLICK KEY ENC : "+ stringPublicKey);
//        System.out.println("PRIVATE KEY ENC : "+ stringPrivateKey);
        
        writePemFile(publickey, "RSA PUBLIC KEY", "publickey.pem");
        writePemFile(privatekey, "RSA PRIVATE KEY", "privatekey.pem");
       
    	String plaintext = "암호화할 문자열입니다.";
    	
    	String enc = AllencryptRSA(plaintext, publickey);
    	
    	System.out.println("암호화 된 문자열 : " +enc);
    	
    	String dec = AlldecryptRSA(enc, privatekey);
    	
    	System.out.println("복호화 된 문자열 : " + dec);
    	
    	//System.out.println("\n\n");
    	
    	privatekey = readPrivateKeyFromPemFile("privatekey.pem");
    	publickey = readPublicKeyFromPemFile("publickey.pem");
    	
    	// System.out.println("PUBLICK KEY : " + publickey);
    	// System.out.println("PRIVATE KEY : " + privatekey);
    	
    	Sha256 s = new Sha256();
    	String encSHA = s.encrypt(plaintext);
    	
    	System.out.println("SHA 256 암호화 : "+ encSHA);
    }
}