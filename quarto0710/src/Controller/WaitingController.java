package Controller;

import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class WaitingController implements Initializable {
	@FXML Polygon btnStart;
	@FXML Polygon btnRank;
	@FXML Polygon btnExit;
	@FXML Label startTxt;
	@FXML Label rankTxt;
	@FXML Label exitTxt;
	public Stage mainStage;
	public RootController gameController;
	public RankController rankController;
	public Socket socket;
	public String data;
	public Thread thread;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startTxt.setDisable(true);
		rankTxt.setDisable(true);
		exitTxt.setDisable(true);

		btnRank.setOnMouseEntered(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				btnRank.setEffect(new Glow(0.8));
				
			}
		});
		btnRank.setOnMouseExited(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				btnRank.setEffect(new Glow(0));
				
			}
			
		});
		btnRank.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event){
					try{
						FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/rank.fxml"));
						Parent root = loader.load();
						rankController = loader.getController();
						rankController.setStage(mainStage, socket);

						System.out.println("Socket4:" + socket.toString());
						Scene scene = new Scene(root);
						
						
						
						Platform.runLater(()->{
							mainStage.setTitle("Rank");
							
							mainStage.setScene(scene);
							mainStage.show();
							
						});
						}catch(IOException error){
							error.printStackTrace();
						}
				
			}
		});
		btnExit.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				btnExit.setEffect(new Glow(0.8));
				
			}
		});
		btnExit.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				btnExit.setEffect(new Glow(0));
				
			}
			
		});
		btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				Platform.exit();
				
			}
		});
		btnStart.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				btnStart.setEffect(new Glow(0.8));
				
			}
		});
		btnStart.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				btnStart.setEffect(new Glow(0));
				
			}
			
		});
		btnStart.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println("Socket3:"+socket.toString());
				startTxt.setText("Wait!");
				send("!start");
				

				btnStart.setDisable(true);
				try{
				byte[] byteArr = new byte[100];
				InputStream inputStream = socket.getInputStream();
				
				//서버가 비정상적으로 종료했을 경우 IOException 발생
				int readByteCount = inputStream.read(byteArr);
				
				//서버가 정상적으로 Socket의 close()를 호출했을 경우
				if(readByteCount == -1) { throw new IOException(); }
				
				data = new String(byteArr, 0, readByteCount, "UTF-8");
				}catch (Exception e) {
					System.out.println("[서버통신안됨]");
					e.printStackTrace();
					//Platform.runLater(()->displayText("[서버 통신 안됨]"));
					stopClient();
					
				}
				System.out.println("[Waiting Room 받기 완료] "  + data);
				if(data.equals("!start")){
					try{
					FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/root.fxml"));
					Parent root = loader.load();
					gameController = loader.getController();
					gameController.setStage(mainStage, socket);

					System.out.println("Socket4:" + socket.toString());
					Scene scene = new Scene(root);
					
					
					
					Platform.runLater(()->{
						mainStage.setTitle("GameRoom");
						
						mainStage.setScene(scene);
						mainStage.show();
						
					});
					}catch(IOException error){
						error.printStackTrace();
					}
				

				}
				
			}
		});
		
		
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
				byte[] byteArr = new byte[100];
				InputStream inputStream = socket.getInputStream();
				
				//서버가 비정상적으로 종료했을 경우 IOException 발생
				int readByteCount = inputStream.read(byteArr);
				
				//서버가 정상적으로 Socket의 close()를 호출했을 경우
				if(readByteCount == -1) { throw new IOException(); }
				
				data = new String(byteArr, 0, readByteCount, "UTF-8");

				
				System.out.println("[Waiting Room 받기 완료] "  + data);

			
			} catch (Exception e) {
				System.out.println("[서버통신안됨]");
				e.printStackTrace();
				//Platform.runLater(()->displayText("[서버 통신 안됨]"));
				stopClient();
				break;
				
			}
		}
	
	}
	
	

}
