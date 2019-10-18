package com.exam;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//스레드 간 공유하는 map 객체
		//key는 아이디(채팅명), value는 출력문자스트림
		Map<String, BufferedWriter> map = new ConcurrentHashMap<String, BufferedWriter>();
		
		final int PORT = 6000;
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(PORT);
			
			System.out.println("***chatting server***");
			System.out.println("서버는 클라이언트 소켓의 접속요청을 대기중...");
			while (true) {
				//특정 클라이언트 하나가 접속되면 새로운 소켓객체를 리턴
				socket = serverSocket.accept();
				
				ChatServerTask task = new ChatServerTask(socket, map);
				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();
			}//while
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
					System.out.println("serverSocket closed.");
				}
			} catch (IOException e) {
				e.printStackTrace();
		}
	

	}

}
}