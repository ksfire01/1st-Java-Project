package Server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Controller.MemberDAO;
import Model.MemberVO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ServerExample extends Application {
	int count;
	ExecutorService executorService;
	ServerSocket serverSocket;
	List<Client> connections = new Vector<Client>();
	List<Client> waitingList = new Vector<Client>();
	
	
	static int number=100;
	
	void startServer() {
		executorService = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors()
	    );
		
		try {
			serverSocket = new ServerSocket();		
			serverSocket.bind(new InetSocketAddress("192.168.1.102", 5001));
		} catch(Exception e) {
			if(!serverSocket.isClosed()) { stopServer(); }
			return;
		}
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Platform.runLater(()->{
					displayText("[서버 시작]");
					btnStartStop.setText("stop");
				});		
				while(true) {
					try {
						Socket socket = serverSocket.accept();
						String message = "[연결 수락: " + socket.getRemoteSocketAddress()  + ": " + Thread.currentThread().getName() + "]";
						Platform.runLater(()->displayText(message));
		
						Client client = new Client(socket);
						connections.add(client);
//						client.send(String.valueOf(connections.indexOf(client)));
						Platform.runLater(()->displayText("[연결 개수: " + connections.size() + "]"));
					} catch (Exception e) {
						if(!serverSocket.isClosed()) { stopServer(); }
						break;
					}
				}
			}
		};
		executorService.submit(runnable);
	}
	
	void stopServer() {
		try {
			Iterator<Client> iterator = connections.iterator();
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			if(serverSocket!=null && !serverSocket.isClosed()) { 
				serverSocket.close(); 
			}
			if(executorService!=null && !executorService.isShutdown()) { 
				executorService.shutdown(); 
			}
			Platform.runLater(()->{
				displayText("[서버 멈춤]");
				btnStartStop.setText("start");
			});
		} catch (Exception e) { }
	}	
	
	class Client {
		Socket socket;
		int clientNumber;
		int player;
		int isPair;
		
		String clientId;
		String clientNick;
		int clientWin;
		int clientLose;
		int clientTotal;
		
		String opponentId;
		String opponentNick;
		int opponentTotal;
		int opponentWin;
		int opponentLose;
		
		
		Client(Socket socket) {
			this.socket = socket;
			receive();
		}
		
		void receive() {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							byte[] byteArr = new byte[100];
							InputStream inputStream = socket.getInputStream();
							
							//클라이언트가 비저상 종료를 했을 경우 IOException 발생
							int readByteCount = inputStream.read(byteArr);
							
							//클라이언트가 정상적으로 Socket의 close()를 호출했을 경우
							if(readByteCount == -1) {  throw new IOException(); }
							
							String message = "[요청 처리: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							Platform.runLater(()->displayText(message));
							
							String data = new String(byteArr, 0, readByteCount, "UTF-8");
							Platform.runLater(()->displayText(data));
//							for(Client client : connections) {
//								if(client.socket.equals(socket)==true)
//									{client.send(data); Platform.runLater(()->displayText("보내기 성공!"));}
////								client.send(data);
//							}
							//로그인 버튼을 눌렀을때
							if(data.charAt(0)=='$'){
								StringTokenizer token = new StringTokenizer(data, "$");
								String[] a = new String[2];
								for(int i=0;token.hasMoreTokens();i++){
									a[i]=token.nextToken();
									System.out.println(a[i]);
								}
								MemberDAO mDao = new MemberDAO();
								MemberVO mVo = new MemberVO();
								boolean isMember = false;
								List<MemberVO> memberList = new Vector<MemberVO>();
								memberList = mDao.getMemberList();
								for(MemberVO member : memberList){
									if(member.getTxtId().equals(a[0])&&member.getTxtPass().equals(a[1])){
										isMember=true;
										clientId=member.getTxtId();
										clientNick=member.getNick();
										clientWin=member.getWin();
										clientLose=member.getLose();
										clientTotal=member.getTotalGame();
									}
								}
								if(isMember==true){
									send("s"+data);
								}else{
									send("f"+data);
								}
								
							}else if(data.equals("&")){
								MemberDAO mDao = new MemberDAO();
								MemberVO mVo = new MemberVO();
								List<MemberVO> memberList = new ArrayList<MemberVO>();
								memberList = mDao.getMemberList();
								for(int i=0; i<memberList.size();i++){
									System.out.println(memberList.get(i).toString());
								}
								
								
								
								for(int i=0; i<memberList.size();i++){
									String s = new String("&"+memberList.get(i).getTxtId()+"&"+memberList.get(i).getTotalGame()+"&"
								+memberList.get(i).getWin()+"&"+memberList.get(i).getLose());
									
									System.out.println(s);
									Thread.sleep(100);
									send(s);
								
								}
								Thread.sleep(100);
								send("*");

								
								
							}else if(data.equals("!start")){
								waitingList.add(Client.this);
								System.out.println(waitingList.size());
								if(waitingList.size()==2){
									waitingList.get(0).isPair=2;
									waitingList.get(0).clientNumber=number;
									waitingList.get(0).player=1;
									
									waitingList.get(1).isPair=2;
									waitingList.get(1).clientNumber=number;
									waitingList.get(1).player=2;
									
									waitingList.get(0).opponentId=clientId;
									waitingList.get(0).opponentNick=clientNick;
									waitingList.get(0).opponentTotal=clientTotal;
									waitingList.get(0).opponentWin=clientWin;
									waitingList.get(0).opponentLose=clientLose;
									
									opponentId=waitingList.get(0).clientId;
									opponentNick=waitingList.get(0).clientNick;
									opponentTotal=waitingList.get(0).clientTotal;
									opponentWin=waitingList.get(0).clientWin;
									opponentLose=waitingList.get(0).clientLose;
									
									number++;
									for(Client client : waitingList) {
											System.out.println("클라이언트번호:" + client.clientNumber);
											System.out.println("isPair:" + client.isPair);
											//!start큐를 보냄
											client.send(data);
											Platform.runLater(()->displayText(data+"보내기 성공!"));
										
//										
									}
									for(Client client : waitingList) {
										

										client.send("#"+client.player);
										Platform.runLater(()->displayText("#client.player보내기 성공!"));
									
//									
									}
									waitingList.removeAll(waitingList);
									System.out.println("WaitingList:"+waitingList.toString()+", Size:" + waitingList.size());
									
								}
									
						
							}else if(data.charAt(0)=='%'){
								send("%"+opponentNick+"%"+opponentTotal+"%"+opponentWin+"%"+opponentLose);
								Platform.runLater(()->displayText(data+"보내기 성공!"));
							}else if(data.charAt(0)=='@'){
								MemberDAO mDao = new MemberDAO();
								MemberVO mVo = new MemberVO();
								String[] string = data.split("@");
								List<MemberVO> members = mDao.getMemberList();
								mVo.setTxtId(string[1]);
								mVo.setName(string[2]);
								mVo.setNick(string[3]);
								mVo.setTxtPass(string[4]);
								
								mDao.getMemberRegister(mVo);
								
							}else if(data.charAt(0)=='^'){
								send(data);
								if(data.equals("^WIN!")){
									clientTotal++;
									clientWin++;
									MemberDAO mDAO = new MemberDAO();
									MemberVO mVo = new MemberVO();
									mVo.setTxtId(clientId);
									mVo.setWin(clientWin);
									mVo.setLose(clientLose);
									mVo.setTotalGame(clientTotal);
									mDAO.updateMemberResult(mVo);
									clientNumber=0;
									isPair=0;
								}else if(data.equals("^LOSE!")){
									clientTotal++;
									clientLose++;
									MemberDAO mDAO = new MemberDAO();
									MemberVO mVo = new MemberVO();
									mVo.setTxtId(clientId);
									mVo.setWin(clientWin);
									mVo.setLose(clientLose);
									mVo.setTotalGame(clientTotal);
									mDAO.updateMemberResult(mVo);
									clientNumber=0;
									isPair=0;
								}
								
							}
							
							
	
								if((clientNumber>=100&&isPair==2)&&(data.equals("!start")==false)&&(data.charAt(0)!='%')){
									for(Client client : connections) {
										if(client.clientNumber==clientNumber&&client.socket.equals(socket)==false){
								
											System.out.println("보내는 클라이언트의 소켓어드레스 : " + client.socket.toString());
											client.send(data); Platform.runLater(()->displayText(data+"보내기 성공!"));
											
										}
		//										
									}
								}
							
							
							
							
						}
					} catch(Exception e) {
						try {
							e.printStackTrace();
							for(Client client : connections) {
								if(client.clientNumber==clientNumber&&client.socket.equals(socket)==false){
						
									System.out.println("보내는 클라이언트의 소켓어드레스 : " + client.socket.toString());
									client.send("+"); Platform.runLater(()->displayText("+보내기 성공!"));
									
								}
//										
							}
							connections.remove(Client.this);
							String message = "[클라이언트 통신 안됨: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							Platform.runLater(()->displayText(message));
							socket.close();
						} catch (IOException e2) {}
					}
				}
			};
			executorService.submit(runnable);
		}
	
		void send(String data) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						byte[] byteArr = data.getBytes("UTF-8");
						OutputStream outputStream = socket.getOutputStream();
						count++;
						System.out.println(count+"[보내는 data] :" + data);
						outputStream.write(byteArr);
						outputStream.flush();
					} catch(Exception e) {
						try {
							String message = "[클라이언트 통신 안됨: " + socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName() + "]";
							Platform.runLater(()->displayText(message));
							connections.remove(Client.this);
							socket.close();
						} catch (IOException e2) {}
					}
				}
			};
			executorService.submit(runnable);
		}
	}
	
	//////////////////////////////////////////////////////
	TextArea txtDisplay;
	Button btnStartStop;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(500, 300);
		
		txtDisplay = new TextArea();
		txtDisplay.setEditable(false);
		BorderPane.setMargin(txtDisplay, new Insets(0,0,2,0));
		root.setCenter(txtDisplay);
		
		btnStartStop = new Button("start");
		btnStartStop.setPrefHeight(30);
		btnStartStop.setMaxWidth(Double.MAX_VALUE);
		btnStartStop.setOnAction(e->{
			if(btnStartStop.getText().equals("start")) {
				startServer();
			} else if(btnStartStop.getText().equals("stop")){
				stopServer();
			}
		});
		root.setBottom(btnStartStop);
		
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("app.css").toString());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Server");
		primaryStage.setOnCloseRequest(event->stopServer());
		primaryStage.show();
	}
	
	void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}	
	
	public static void main(String[] args) {
		launch(args);
	}
}