package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable{
	
	@FXML public Button btnJoin;
	@FXML public Button btnOk;
	@FXML public Button btnDel;
	@FXML public Button sname;
	@FXML public TextField txtId;
	@FXML public PasswordField txtPass;
	
	public Stage mainStage;
	private WaitingController waitingroom;
	private CreateController create;
	Socket socket;
	
//	@FXML public Button txtName;
//	@FXML public Button txtId;
//	@FXML public Button txtNick;
//	@FXML public Button txtYY;
//	@FXML public Button txtDD;
//	@FXML public Button txtMM;
//	@FXML public Button txtPass;
	
	
//	 private static Stage primaryStage;
//	 public void setPrimaryStage(Stage primaryStage) {
//		this.primaryStage = primaryStage;
//	}
	public void setStage(Stage stage){
		this.mainStage=stage;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
			startClient();
			btnJoin.setOnAction(event->{
				try {
					dfdf();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			btnOk.setOnAction(event->{
				try {
					handleBtnOk();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			btnDel.setOnAction(event->{
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Platform.exit();
			});
		
	}
	 private void handleBtnOk() throws IOException {
		send("$"+txtId.getText()+"$"+txtPass.getText());
		byte[] byteArr = new byte[100];
		InputStream inputStream = socket.getInputStream();
		
		//클라이언트가 비저상 종료를 했을 경우 IOException 발생
		int readByteCount = inputStream.read(byteArr);
		
		//클라이언트가 정상적으로 Socket의 close()를 호출했을 경우
		if(readByteCount == -1) {  throw new IOException(); }
		String data = new String(byteArr, 0, readByteCount, "UTF-8");
		System.out.println(data);
		if(data.charAt(0)=='s'){
		FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/waiting.fxml"));
		Parent root = loader.load();
		waitingroom = loader.getController();
		waitingroom.mainStage=mainStage;
		System.out.println("Socket :" + socket.toString());
		waitingroom.socket=socket;
		System.out.println("Socket2 :" + waitingroom.socket.toString());
		
		Scene scene = new Scene(root);

		mainStage.setTitle("WaitingRoom");
		
		mainStage.setScene(scene);
		mainStage.show();
		}else if(data.charAt(0)=='f'){
			try {
				handlePopupOpen("아이디 혹은 비밀번호가 올바르지 않습니다.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	 }
	private void dfdf() throws IOException {
//		 Stage customStage = new Stage(StageStyle.UTILITY);// 부 무대 화면창을 설정하는기능 잠깐보여지다없어지는것
//		 customStage.initModality(Modality.WINDOW_MODAL);// 현재창을 모달로 선택
//		 customStage.initOwner(btnJoin.getScene().getWindow());// 주인무대인 stage설정
//		 
//		 FXMLLoader loader=new FXMLLoader();
//		 loader.setLocation(getClass().getResource("/View/popup.fxml"));
//		 Parent parent =loader.load();
//
//		 
//		 Scene scene = new Scene(parent);
//		 customStage.setScene(scene);
//		 customStage.setResizable(false); // 창 크기를 변경하지 못하도록 설정
//		 customStage.show();
		FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/popup.fxml"));
		Parent root = loader.load();
		create = loader.getController();
		create.socket=socket;
		create.mainStage=mainStage;
		Scene scene = new Scene(root);
		mainStage.setTitle("WaitingRoom");
		mainStage.setScene(scene);
		mainStage.show();
	}
	public void handleCustomOpen(ActionEvent event) throws IOException {
	      
	      
	      
	 }
	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress("192.168.1.102", 5001));
				} catch(Exception e) {
					System.out.println("[서버 통신 안됨]");
					if(!socket.isClosed()) { stopClient(); }
					return;
				}
			}
		};
		thread.start();
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
			
			popup.show(mainStage, mainStage.getX()+225, mainStage.getY()+20);
		}

	   


}









