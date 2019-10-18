package com.exam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

public class ChatServerTask implements Runnable {

	private Socket socket;
	private Map<String, BufferedWriter> map; //[스레드 간 공유객체]

	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	String userId;
	
	public ChatServerTask(Socket socket, Map<String, BufferedWriter> map) {
		this.socket = socket;
		this.map = map;
		
		System.out.println(socket.getInetAddress() + "로부터 연결요청 받음.");
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"));
			
			userId = reader.readLine();
			
			map.put(userId, writer);
			
			System.out.println("접속한 클라이언트의 아이디는 " +userId+ "입니다.");
			
//			broadcast(userId +"님이 접속하셨습니다.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//생성자
	



			@Override
			public void run() {
				// TODO Auto-generated method stub
				String line = "";
				
				while(true) {
				try {
					line= reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
				if(line == null || line.equals("/quit")) {
					break;
				
				}else if(line.contains("/to")) {
					//sendWhisper(line);
				}else {
					broadcast(userId + ":" + line);
				}
			}//while
				
				//"/quit"종료명령어 수행시
				map.remove(userId);
				System.out.println(userId + "님이 나가셨습니다.");
				broadcast(userId + "님이 나가셨습니다.");
				
				//입출력스트림과 소켓객체 닫기
				close();
				
	}//run
			
			//귓속말 보내기 형식 : /to 아이디 대화내용
			public void sendWhisper(String message) {
				//아이디 문자열 가져오기
				int beginIndex = message.indexOf(" ") + 1;
				int endIndex = message.indexOf("", beginIndex);
				
				if(endIndex > -1) {
					String toId= message.substring(beginIndex, endIndex);
					String content = message.substring(endIndex +1);
					
					BufferedWriter out = map.get(toId);
					
					if(out !=null) {
						try {
							out.write(userId + "님이" + toId +" 님께 귓속말을 보냈습니다: " + content + "\n");
							out.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}//sendWhisper method
			
			
			public void broadcast(String message) {
				for(BufferedWriter out: map.values()) {
					try {
						out.write(message+"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				}
				}
				
				
				private void close() {
					if (reader != null) {
						try {
							reader.close();
							System.out.println("reader closed.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					if (writer != null) {
						try {
							writer.close();
							System.out.println("writer closed.");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					if (socket != null && !socket.isClosed()) {
						try {
							socket.close();
							System.out.println("socket closed.");
						} catch (IOException e) {
							e.printStackTrace();
						
					}
				}

			}//broadcast method
			
}
