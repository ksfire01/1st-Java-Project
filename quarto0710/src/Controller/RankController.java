package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class RankController implements Initializable{
	int count=0;
	Socket socket;
	Stage mainStage;
	WaitingController waitingroom;
	String data;
	int killReceive;
	
	@FXML Button btnConfirm;
	@FXML Button btnExit;
	@FXML Label rankView;
	@FXML Label rankView1;
	@FXML Label label;
	@FXML TextField txtId;
	ArrayList<NNN> list;
	
	public void setStage(Stage mainStage, Socket socket){
		this.socket=socket;
		this.mainStage=mainStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		list = new ArrayList<NNN>();

		
		
		label.setOnMouseEntered(e->{

			Thread thread = new Thread(){
				@Override
				public void run(){
					receive();
				}
			};
			thread.start();
			send("&");
			label.setDisable(true);
			label.setVisible(false);
//			rankView.getColumns().addAll(colNick, colTotal, colWin, colLose);
//			Collections.sort(list);
//			System.out.println(list);
//			for(Member member : list){
//				System.out.println(member.toString());
//			}
//			rankView.setItems(list);
		});
		btnExit.setOnAction(e->handleBtnExit());
		btnConfirm.setOnAction(e->handleBtnConfirm());
		
		
		
	}

	private void handleBtnConfirm() {
		boolean isThereId=false;
		for(int i=0; i<list.size() ; i++){
			if(list.get(i).nickName.equals(txtId.getText())){
				rankView.setText((i+1)+".  " + list.get(i).nickName + "     \n" );
				rankView1.setText(list.get(i).total + "전     " + list.get(i).win + "승     "+list.get(i).lose + "패     \n");
				isThereId=true;
			}
		}
		if(isThereId==false){
			rankView.setText(" ");
			rankView1.setText("찾으시는 닉네임이 없습니다. 다시 입력해주세요.");
		}
	}

	private void handleBtnExit() {
		FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/waiting.fxml"));
		try {
			Parent root;
			root = loader.load();
			waitingroom = loader.getController();
			waitingroom.mainStage=mainStage;
			waitingroom.socket=socket;
			Scene scene = new Scene(root);
			
			mainStage.setTitle("WaitingRoom");
			
			mainStage.setScene(scene);
			mainStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void stopClient() {
		try {
//			Platform.runLater(()->{
//				displayText("[연결 끊음]");
//				btnConn.setText("start");
//				btnSend.setDisable(true);
//			});
			if(socket!=null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {}
	}
	
	void send(String data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {		
					byte[] byteArr = data.getBytes("UTF-8");
					OutputStream outputStream = socket.getOutputStream();
					outputStream.write(byteArr);
					outputStream.flush();
					//Platform.runLater(()->displayText("[보내기 완료]"));
				} catch(Exception e) {
					//Platform.runLater(()->displayText("[서버 통신 안됨]"));
					stopClient();
				}				
			}
		};
		thread.start();
	}

	void receive() {
		while(true){
			try {
				int size=0;
				
				byte[] byteArr = new byte[100];
				InputStream inputStream = socket.getInputStream();
				//서버가 비정상적으로 종료했을 경우 IOException 발생
				int readByteCount = inputStream.read(byteArr);
				//서버가 정상적으로 Socket의 close()를 호출했을 경우
				if(readByteCount == -1) { throw new IOException(); }
				data = new String(byteArr, 0, readByteCount, "UTF-8");
				System.out.println(count+"[Rank창 받기 완료] "  + data);
				
				if(data.charAt(0)=='&'){
					StringTokenizer token = new StringTokenizer(data, "&");
					String[] a = new String[4];
					for(int i=0; token.hasMoreTokens();i++){
						a[i]=token.nextToken();
					}
				
					list.add(new NNN(a[0], (int)Integer.parseInt(a[1]), (int)Integer.parseInt(a[2]), (int)Integer.parseInt(a[3])));
					for(NNN member : list){
						System.out.println(member.toString());
					}
				}
				if(data.charAt(0)=='*'){
					StringBuffer s = new StringBuffer();
					StringBuffer s1 = new StringBuffer();
					
					Collections.sort(list);
					for(int i=0; i<list.size(); i++){
						s.append((i+1)+".  " + list.get(i).nickName + "     \n" );
						s1.append(list.get(i).total + "전     " + list.get(i).win + "승     "+list.get(i).lose + "패     \n");
					}
					System.out.println(s.toString());
					Platform.runLater(()->{
						
						rankView.setText(s.toString());
						rankView1.setText(s1.toString());
					});
					break;
				}
				

				

			
			} catch (Exception e) {
				System.out.println("[서버통신안됨]");
				e.printStackTrace();
				//Platform.runLater(()->displayText("[서버 통신 안됨]"));
				stopClient();
				break;
				
			}
		}
	
	}
	
	class NNN implements Comparable<NNN>{
		String nickName;
		int total;
		int win;
		int lose;
		public NNN(String nickName, int total, int win, int lose) {
			super();
			this.nickName = nickName;
			this.total = total;
			this.win = win;
			this.lose = lose;
		}
		@Override
		public String toString() {
			return "Member [nickName=" + nickName + ", total=" + total + ", win=" + win + ", lose=" + lose + "]";
		}
		
		@Override
		public int compareTo(NNN o) {
			if(win>o.win)
				return -1;
			else if(win<o.win)
				return 1;
			else
				return 0;
		}
		
	}
	

}
