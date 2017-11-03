package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.text.AbstractDocument.Content;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.sg.prism.NGNode;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class RootController implements Initializable{
	Socket socket;
	String data;
	String recievedLocation;
	String recievedHorse;
	public static Stage primaryStage;
	WaitingController waitingroom;
	boolean killReceive=true;
	
	public void setStage(Stage primaryStage, Socket socket){
		this.primaryStage = primaryStage;
		this.socket=socket;
		System.out.println("Socket5 :" + socket.toString());
	}
	
	//모양 구멍 색깔 테두리
	@FXML ImageView cfdt;
	@FXML ImageView chdt;
	@FXML ImageView cfdb;
	@FXML ImageView chdb;
	@FXML ImageView cfbt;
	@FXML ImageView chbt;
	@FXML ImageView cfbb;
	@FXML ImageView chbb;
	@FXML ImageView sfdt;
	@FXML ImageView shdt;
	@FXML ImageView sfdb;
	@FXML ImageView shdb;
	@FXML ImageView sfbt;
	@FXML ImageView shbt;
	@FXML ImageView sfbb;
	@FXML ImageView shbb;
	
	@FXML GridPane board;
	@FXML HBox hbox00;
	@FXML HBox hbox10;
	@FXML HBox hbox20;
	@FXML HBox hbox30;
	@FXML HBox hbox01;
	@FXML HBox hbox11;
	@FXML HBox hbox21;
	@FXML HBox hbox31;
	@FXML HBox hbox02;
	@FXML HBox hbox12;
	@FXML HBox hbox22;
	@FXML HBox hbox32;
	@FXML HBox hbox03;
	@FXML HBox hbox13;
	@FXML HBox hbox23;
	@FXML HBox hbox33;
	@FXML FlowPane anchorPane;
	
	@FXML Button btnInit;
	@FXML Button btnQuarto;
	@FXML Button btnTurnOver;
	@FXML Button btnSendHorse;
	@FXML Button btnResult;
	@FXML Label gameStart;
	@FXML Label yourInfo;
	
	
	
	@FXML HBox yourHorseBoard;
	@FXML VBox myHorseBoard;
	@FXML VBox mainVbox;
	private String horseId;
	private String hboxId;
	HBox[][] boardArray;
	List<ImageView> horseArray = new ArrayList<ImageView>();
	
	Thread rThread;
	
	
//	void startClient() {
//		Thread thread = new Thread() {
//			@Override
//			public void run() {
//				try {
//					socket = new Socket();
//					socket.connect(new InetSocketAddress("localhost", 5001));
//				} catch(Exception e) {
//					System.out.println("[서버 통신 안됨]");
//					if(!socket.isClosed()) { stopClient(); }
//					return;
//				}
//				receive();
//			}
//		};
//		thread.start();
//	}
	
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
	
	
	
	void receive() {
		while(killReceive) {
			try {
				byte[] byteArr = new byte[100];
				InputStream inputStream = socket.getInputStream();
				
				//서버가 비정상적으로 종료했을 경우 IOException 발생
				int readByteCount = inputStream.read(byteArr);
				
				//서버가 정상적으로 Socket의 close()를 호출했을 경우
				if(readByteCount == -1) { throw new IOException(); }
				
					data = new String(byteArr, 0, readByteCount, "UTF-8");
					System.out.println("[RootController 받기완료]" + data);
					
					
					if(data.equals("#1")){
						btnInit.setDisable(true);
						btnQuarto.setDisable(true);
						btnTurnOver.setDisable(true);
						btnSendHorse.setDisable(true);
						Platform.runLater(()->{
							try {
								handlePopupOpen("당신이 선입니다!\n상대방에게 넘길 말을\n선택해주세요!");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
						
					}else if(data.equals("#2")){
						cfdt.setDisable(true);
						cfdt.setDisable(true);
						chdt.setDisable(true);
						cfdb.setDisable(true);
						chdb.setDisable(true);
						cfbt.setDisable(true);
						chbt.setDisable(true);
						cfbb.setDisable(true);
						chbb.setDisable(true);
						sfdt.setDisable(true);
						shdt.setDisable(true);
						sfdb.setDisable(true);
						shdb.setDisable(true);
						sfbt.setDisable(true);
						shbt.setDisable(true);
						sfbb.setDisable(true);
						shbb.setDisable(true);
						
						btnInit.setDisable(true);
						btnQuarto.setDisable(true);
						btnTurnOver.setDisable(true);
						btnSendHorse.setDisable(true);
						Platform.runLater(()->{
						
							try {
								handlePopupOpen("상대방이 선입니다!\n상대방이 말을 고를때까지\n기다려주세요!");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
					}else if(data.charAt(0)=='^'){
						FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/waiting.fxml"));
						Parent root = loader.load();
						waitingroom = loader.getController();
						waitingroom.mainStage=primaryStage;
						waitingroom.socket=socket;
						Scene scene = new Scene(root);
						Platform.runLater(()->{
							
							primaryStage.setTitle("WaitingRoom");
							primaryStage.setScene(scene);
							primaryStage.show();
						});
					}else if(data.charAt(0)=='%'){
						StringTokenizer token = new StringTokenizer(data, "%");
						String[] a = new String[4];
						for(int i=0;token.hasMoreTokens();i++){
							a[i]=token.nextToken();
							System.out.println(a[i]);
						}
						Platform.runLater(()->{
							
							yourInfo.setText("닉네임 :" + a[0] + "\n게임수 :" + a[1] +"\n승리 :"+a[2]+"\n패배 :"+a[3]);
						});
					}else if(data.charAt(0)=='h'){
						//상대방이 말을 놓은 위치가 넘어왔을때
						recievedLocation = data;
						
						Platform.runLater(()->{
							
							HBox target = (HBox)board.lookup("#"+recievedLocation);
							ImageView image = (ImageView) yourHorseBoard.lookup("#"+horseId);
							target.getChildren().add(image);
							for(int i=0;i<horseArray.size();i++){
								horseArray.get(i).setDisable(true);
							}
							
						});
						}
					else if(data.charAt(0)=='c'||data.charAt(0)=='s'){
						//말이 넘어왔을때
						recievedHorse = data;
						Platform.runLater(()->{
							
							try {
								handlePopupOpen("말이 넘어왔습니다.\n놓을 위치를 선택해주세요!");
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ImageView image = (ImageView) anchorPane.lookup("#"+recievedHorse);
							myHorseBoard.getChildren().add(image);
							image.setDisable(true);
							
//							//
//							for(int i=0;i<horseArray.size();i++){
//								if(horseArray.get(i).getId().equals(recievedHorse))
//									horseArray.remove(i);
//							}
							
							
							
							for(int i=0;i<horseArray.size();i++){
									horseArray.get(i).setDisable(true);
							}
							
							btnInit.setDisable(true);
							btnQuarto.setDisable(true);
							btnTurnOver.setDisable(true);
							btnSendHorse.setDisable(true);
							//게임판 클릭가능하게 만들기
							for(int i=0; i<4; i++){
								for(int j=0; j<4; j++){
									boardArray[i][j].setDisable(false);
								}
							}
							
							board.setDisable(false);
							board.setOnMouseEntered(new EventHandler<MouseEvent>() {
								
								
								@Override
								public void handle(MouseEvent event) {
									hbox00.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox10.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox20.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox30.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox01.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox11.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox21.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox31.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox02.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox12.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox22.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox32.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox03.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox13.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox23.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									hbox33.setOnMouseClicked(e->handleHBox00MouseClicked(e));
									
									
								}
								
								
							});
							
						});
					}
					else if(data.equals("quarto")){
						Platform.runLater(()->{
							
							try {
								handlePopupOpen("상대방이 콰르토에 성공했습니다!");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							btnResult.setVisible(true);
							btnResult.setDisable(false);
							btnResult.setText("YOU LOSE!");
						});
					}
					else if(data.equals("quartofailed")){
						Platform.runLater(()->{
							
							try {
								handlePopupOpen("상대방이 콰르토에 실패했습니다!");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							btnResult.setVisible(true);
							btnResult.setDisable(false);
							btnResult.setText("YOU WIN!");
						});
					}
					else if(data.equals("+")){
						Platform.runLater(()->{
							
							try {
								handlePopupOpen("상대방이 나갔습니다.");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							btnResult.setVisible(true);
							btnResult.setDisable(false);
							btnResult.setText("YOU WIN!");
						});
					}
				
				//System.out.println("[받기 완료] "  + data);
			} catch (Exception e) {
				System.out.println("[서버통신안됨]");
				e.printStackTrace();
				//Platform.runLater(()->displayText("[서버 통신 안됨]"));
				stopClient();
				break;
			}
		}
		System.out.println("recieve가 끝났습니다.");
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
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		boardArray= new HBox[4][4];
		boardArray[0][0]=hbox00;
		boardArray[0][1]=hbox01;
		boardArray[0][2]=hbox02;
		boardArray[0][3]=hbox03;
		boardArray[1][0]=hbox10;
		boardArray[1][1]=hbox11;
		boardArray[1][2]=hbox12;
		boardArray[1][3]=hbox13;
		boardArray[2][0]=hbox20;
		boardArray[2][1]=hbox21;
		boardArray[2][2]=hbox22;
		boardArray[2][3]=hbox23;
		boardArray[3][0]=hbox30;
		boardArray[3][1]=hbox31;
		boardArray[3][2]=hbox32;
		boardArray[3][3]=hbox33;
		horseArray.add(cfdt);
		horseArray.add(chdt);
		horseArray.add(cfdb);
		horseArray.add(chdb);
		horseArray.add(cfbt);
		horseArray.add(chbt);
		horseArray.add(cfbb);
		horseArray.add(chbb);
		horseArray.add(sfdt);
		horseArray.add(shdt);
		horseArray.add(sfdb);
		horseArray.add(shdb);
		horseArray.add(sfbt);
		horseArray.add(shbt);
		horseArray.add(sfbb);
		horseArray.add(shbb);
		yourInfo.setText("상대방의 정보를 보려면\n여기를 클릭하세요!");
		
		btnResult.setVisible(false);
		btnResult.setDisable(true);
		
		btnResult.setOnAction(e->handleBtnResult());
		
		gameStart.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				rThread = new Thread(){
					@Override
					public void run() {
						receive();
					}
				};
				rThread.start();
				
			
				gameStart.setVisible(false);
				gameStart.setDisable(true);
				
			}
		});
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				boardArray[i][j].setDisable(true);
			}
		}
		
		yourInfo.setOnMouseClicked(e->{
			send("%");
		});
		
		cfdt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		cfdt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		chdt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		cfdb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		chdb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		cfbt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		chbt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		cfbb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		chbb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		sfdt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		shdt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		sfdb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		shdb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		sfbt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		shbt.setOnMouseClicked(e->handleCircleMouseClicked(e));
		sfbb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		shbb.setOnMouseClicked(e->handleCircleMouseClicked(e));
		btnInit.setDisable(true);
		btnQuarto.setOnAction(e->handleBtnQuarto());
		btnTurnOver.setOnAction(e->hanldeBtnTurnOver());
			
	}
		

		



	private void handleBtnResult() {
		System.out.println("btnResult가 눌렸습니다.");
		send("^"+btnResult.getText().substring(4));
//			rThread.interrupt();
		killReceive=false;
		
	}



	private void handleBtnQuarto() {
		int quarto = 0;
		int quartoflag=0;
		
		//열 검사
		for(int i=0; i<=3; i++){
			if(boardArray[i][0].getChildren().isEmpty())
				continue;
			else{
				for(int k=0; k<=3; k++){
					
					char feature = boardArray[i][0].getChildren().get(0).getId().charAt(k);
					
					for(int j=1; j<=3; j++){
						if(boardArray[i][j].getChildren().isEmpty()) 
							break;
						else{
							if(boardArray[i][j].getChildren().get(0).getId().charAt(k)==feature)
								quartoflag++;
						}
					}
					
					if(quartoflag==3){ quarto++; quartoflag=0;}
					else{ quartoflag=0;}
				}
			}
		}
		
		
		//행 검사
		for(int i=0; i<=3; i++){
			if(boardArray[0][i].getChildren().isEmpty())
				continue;
			else{
				for(int k=0; k<=3; k++){
					
					char feature = boardArray[0][i].getChildren().get(0).getId().charAt(k);
					
					for(int j=1; j<=3; j++){
						if(boardArray[j][i].getChildren().isEmpty()) 
							break;
						else{
							if(boardArray[j][i].getChildren().get(0).getId().charAt(k)==feature)
								quartoflag++;
						}
					}
					
					if(quartoflag==3){ quarto++; quartoflag=0;}
					else{ quartoflag=0;}
				}
			}
		}

		//정방향 대각선검사
		for(int k=0; k<=3; k++){
			if(boardArray[0][0].getChildren().isEmpty())
				break;
			char feature = boardArray[0][0].getChildren().get(0).getId().charAt(k);
			for(int i=1; i<=3; i++){
				if(boardArray[i][i].getChildren().isEmpty())
					break;
				else{
					if(boardArray[i][i].getChildren().get(0).getId().charAt(k)==feature){
						quartoflag++;
					}
				}
			}
			if(quartoflag==3){ quarto++; quartoflag=0;}
			else{ quartoflag=0;}
		}
		
		
		//역방향 대각선검사
		for(int k=0; k<=3; k++){
			if(boardArray[0][3].getChildren().isEmpty())
				break;
			char feature = boardArray[0][3].getChildren().get(0).getId().charAt(k);
			for(int i=1; i<=3; i++){
				if(boardArray[i][3-i].getChildren().isEmpty())
					break;
				else{
					if(boardArray[i][3-i].getChildren().get(0).getId().charAt(k)==feature){
						quartoflag++;
					}
				}
			}
			if(quartoflag==3){ quarto++; quartoflag=0;}
			else{ quartoflag=0;}
		}
	
		
		if(quarto>0){
			try {
				handlePopupOpen("콰르토에 성공했습니다!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			btnResult.setVisible(true);
			btnResult.setDisable(false);
			btnResult.setText("YOU WIN!");
			send("quarto");
			
			
		}
		else{
			try {
				handlePopupOpen("콰르토에 실패했습니다!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			btnResult.setVisible(true);
			btnResult.setDisable(false);
			btnResult.setText("YOU LOSE!");
			send("quartofailed");
		}
		
		
		
	}

	private void hanldeBtnTurnOver() {
		send(hboxId);
		for(int i=0;i<horseArray.size();i++){
			horseArray.get(i).setDisable(false);
		}
		
		btnInit.setDisable(true);
		btnQuarto.setDisable(false);
		btnTurnOver.setDisable(true);
		btnSendHorse.setDisable(true);
		try {
			handlePopupOpen("상대방에게 넘길 말을\n선택해주세요!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void handleBtnInit(ImageView image, HBox target) {
		target.getChildren().remove(image);
		myHorseBoard.getChildren().add(image);
		btnInit.setDisable(true);
		
	}

	public void handleHBox00MouseClicked(Event e) {
		ImageView image = (ImageView) myHorseBoard.lookup("#"+recievedHorse);
		HBox target = (HBox)e.getSource();
		target.getChildren().add(image);
//		hbox00.setEffect(new Glow(1));
//		hbox00.setB
//		hbox00.getChildren().add(circle);
		image.setEffect(new Glow(0));
		btnInit.setOnAction(event->handleBtnInit(image, target));
		horseId=null;
		hboxId=target.getId();
		btnInit.setDisable(false);
		btnQuarto.setDisable(true);
		btnTurnOver.setDisable(false);
		btnSendHorse.setDisable(true);
		try {
			handlePopupOpen("턴을 넘겨주세요!");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void handleCircleMouseClicked(MouseEvent e){
		System.out.println("이전id:"+horseId);
		if(horseId!=null){
			ImageView image = (ImageView) anchorPane.lookup("#"+horseId);
			image.setEffect(new Glow(0));
		}
		System.out.println("Image가 클릭되었습니다.");
		ImageView target = (ImageView) e.getSource();
		target.setEffect(new Glow(1));
		horseId=target.getId();
		System.out.println(horseId);
		btnSendHorse.setOnAction(event->handleBtnSendHorse(e));
		btnInit.setDisable(true);
		btnQuarto.setDisable(true);
		btnTurnOver.setDisable(true);
		btnSendHorse.setDisable(false);
	}
	
	private void handleBtnSendHorse(MouseEvent e) {
		send(horseId);
		ImageView image = (ImageView) anchorPane.lookup("#"+horseId);
		yourHorseBoard.getChildren().add(image);
		image.setEffect(new Glow(0));
		
		cfdt.setDisable(true);
		cfdt.setDisable(true);
		chdt.setDisable(true);
		cfdb.setDisable(true);
		chdb.setDisable(true);
		cfbt.setDisable(true);
		chbt.setDisable(true);
		cfbb.setDisable(true);
		chbb.setDisable(true);
		sfdt.setDisable(true);
		shdt.setDisable(true);
		sfdb.setDisable(true);
		shdb.setDisable(true);
		sfbt.setDisable(true);
		shbt.setDisable(true);
		sfbb.setDisable(true);
		shbb.setDisable(true);
		
		btnInit.setDisable(true);
		btnQuarto.setDisable(true);
		btnTurnOver.setDisable(true);
		btnSendHorse.setDisable(true);
		
		
		
		
	}
	
	private void handlePopupOpen(String content) throws Exception {
		Popup popup = new Popup();
		Parent parent = FXMLLoader.load(getClass().getResource("/View/sign.fxml"));
		ImageView imgMessage = (ImageView)parent.lookup("#imgMessage");
		//imgMessage.setImage(new Image(getClass().getResource("images/dialog-info.png").toString()));
		imgMessage.setOnMouseClicked(e->popup.hide());
		
		Label lblMessage = (Label)parent.lookup("#lblMessage");
		lblMessage.setText(content);
		
		popup.setAutoHide(true);
		popup.getContent().add(parent);
		
		popup.show(primaryStage, primaryStage.getX()+150, primaryStage.getY()+250);
	}
	
//		board.setOnMouseEntered(new EventHandler<MouseEvent>() {
//			
//			
//			@Override
//			public void handle(MouseEvent event) {
//				hbox00.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox10.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox20.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox30.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox01.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox11.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox21.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox31.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox02.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox12.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox22.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox32.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox03.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox13.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox23.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				hbox33.setOnMouseClicked(e->handleHBox00MouseClicked(e));
//				
//				
//			}
//			
//			
//		});

	
	
	
}



//프로토콜
// 
//게임시작(Player 1과 Player2가 모두 들어왔음을 가정)!
// 
//Player 1:  handleCircleMouseClicked(게임판의 말 클릭하는 메소드)->handleBtnSendHorse(player2에게 말정보가 넘어감)
//
//Player 2: receive(말정보가 넘어왔을때 받는 메소드)->handleHBox00MouseClicked(넘어온 말을 게임판에 위치시키는 메소드)
//			->hanldeBtnTurnOver(턴넘김버튼을 누르는 메소드 : player1에게 위치정보가 넘어감)
//			->handleCircleMouseClicked->handleBtnSendHorse(player1에게 말정보가 넘어감)
//			
//Player 1: receive->handleHBox00MouseClicked->hanldeBtnTurnOver(player2에게 위치정보가 넘어감)
//			->handleCircleMouseClicked->handleBtnSendHorse(player2에게 말정보가 넘어감)
 





