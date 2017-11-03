package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import Server.ServerExample.Client;
import javafx.application.Platform;

public class Gamer {
	public Socket player1;
	public Socket player2;
	ExecutorService executorService;
	
	public Gamer(Socket one, Socket two, ExecutorService executorService){
		this.player1=one;
		this.player2=two;
		this.executorService = executorService;
		receive();
		send1("#start");
		send2("#start");
		send1("#1");
		send2("#2");
	}
	
	void receive() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						byte[] byteArr1 = new byte[100];
						byte[] byteArr2 = new byte[100];
						InputStream inputStream1 = player1.getInputStream();
						InputStream inputStream2 = player2.getInputStream();
						
						//클라이언트가 비저상 종료를 했을 경우 IOException 발생
						int readByteCount1 = inputStream1.read(byteArr1);
						int readByteCount2 = inputStream2.read(byteArr2);
						
						//클라이언트가 정상적으로 Socket의 close()를 호출했을 경우
						if(readByteCount1 == -1||readByteCount2 == -1) 
						{  throw new IOException(); }
						
						String data1 = new String(byteArr1, 0, readByteCount1, "UTF-8");
						String data2 = new String(byteArr2, 0, readByteCount2, "UTF-8");
						
						send1(data2);
						send2(data1);
						
					}
				} catch(Exception e) {
					try {
						player1.close();
						player2.close();
					} catch (IOException e2) {}
				}
			}
		};
		executorService.submit(runnable);
	}

	void send1(String data) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					byte[] byteArr = data.getBytes("UTF-8");
					OutputStream outputStream = player1.getOutputStream();
					outputStream.write(byteArr);
					outputStream.flush();
				} catch(Exception e) {
					try {
						System.out.println("플레이어1 연결안됨");
						player1.close();
					} catch (IOException e2) {}
				}
			}
		};
		executorService.submit(runnable);
		
	}
	void send2(String data) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					byte[] byteArr = data.getBytes("UTF-8");
					OutputStream outputStream = player2.getOutputStream();
					outputStream.write(byteArr);
					outputStream.flush();
				} catch(Exception e) {
					try {
						System.out.println("플레이어2 연결안됨");
						player2.close();
					} catch (IOException e2) {}
				}
			}
		};
		executorService.submit(runnable);
		
	}
	
	
	

}
