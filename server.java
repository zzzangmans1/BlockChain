import java.io.*;
import java.net.*;	// socket 클래스
import java.util.Scanner;

public class server {
	public static void main(String[] args) {
		
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
				
				if(inputMessage.equalsIgnoreCase("exit")) {
					System.out.println("클라이언트에서 종료하였습니다");
					break;
				}
				System.out.println("클라이언트: " + inputMessage);
				System.out.print("보내기>>");						
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
