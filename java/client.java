package blockchain.midterm;

import java.io.*;
import java.net.*;	// socket 클래스
import java.util.Scanner;

public class client {

	public static void main(String[] args) {
		
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
				System.out.print("보내기>>"); 			// 프롬포트
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
