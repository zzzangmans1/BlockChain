package blockchain.midterm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;

import org.bouncycastle.util.io.pem.*;

/*
 * Pem 클래스 : Privacy Enhanced Mail의 약자로 '인증서를 Base64 인코딩하여 쉽게 읽을 수 있도록 처리한 것'이라는 의미를 가지고 있습니다. 
 * 하나의 Pem 객체를 생성해 파일을 처리할 수 있도록 넘겨줍니다.
 */

public class Pem {
	
	private PemObject pemObject;
	
	// Pem 객체 인스턴스화
	public Pem(Key key, String description) {
		this.pemObject = new PemObject(description, key.getEncoded());
	}
	
	// Pem 파일로 저장
	public void write(String filename) throws FileNotFoundException, IOException{
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		try {
			pemWriter.writeObject(this.pemObject);
		} finally {
			pemWriter.close();
		}
	}
}
