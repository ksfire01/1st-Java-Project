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
		
		//Ŭ���̾�Ʈ�� ������ ���Ḧ ���� ��� IOException �߻�
		int readByteCount = inputStream.read(byteArr);
		
		//Ŭ���̾�Ʈ�� ���������� Socket�� close()�� ȣ������ ���
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
				handlePopupOpen("���̵� Ȥ�� ��й�ȣ�� �ùٸ��� �ʽ��ϴ�.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	 }
	private void dfdf() throws IOException {
//		 Stage customStage = new Stage(StageStyle.UTILITY);// �� ���� ȭ��â�� �����ϴ±�� ��񺸿����پ������°�
//		 customStage.initModality(Modality.WINDOW_MODAL);// ����â�� ��޷� ����
//		 customStage.initOwner(btnJoin.getScene().getWindow());// ���ι����� stage����
//		 
//		 FXMLLoader loader=new FXMLLoader();
//		 loader.setLocation(getClass().getResource("/View/popup.fxml"));
//		 Parent parent =loader.load();
//
//		 
//		 Scene scene = new Scene(parent);
//		 customStage.setScene(scene);
//		 customStage.setResizable(false); // â ũ�⸦ �������� ���ϵ��� ����
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
					System.out.println("[���� ��� �ȵ�]");
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
//				displayText("[���� ����]");
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
						//Platform.runLater(()->displayText("[������ �Ϸ�]"));
					} catch(Exception e) {
						//Platform.runLater(()->displayText("[���� ��� �ȵ�]"));
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









