package Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class CreateController implements Initializable{
	
	Socket socket;
	 @FXML TextField txtId;
	 @FXML TextField txtName;
	 @FXML TextField txtNick;
	 @FXML PasswordField txtPass;
	
	 
	 @FXML Button btnOk1;
	 @FXML Button btnExit;
	 @FXML Button sname;
	LoginController login;
	Stage mainStage;
	 
	 @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnOk1.setOnAction((event)->handleBtnOk());
		btnExit.setOnAction((event)->handleBtnExit());
	}
	 
	 
	 private void handleBtnExit() {
		 FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/login.fxml"));
			Parent root;
			try {
				root = loader.load();
				login = loader.getController();
				login.socket=socket;
				login.mainStage=mainStage;
				Scene scene = new Scene(root);
				mainStage.setTitle("WaitingRoom");
				mainStage.setScene(scene);
				mainStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


	public void handleBtnOk(){
		 if(txtId.getText().equals("")||txtName.getText().equals("")||txtNick.getText().equals("")||txtPass.getText().equals("")){
			 try {
				handlePopupOpen("빠짐없이 입력해주세요.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 send("@"+txtId.getText()+"@"+txtName.getText()+"@"+txtNick.getText()+"@"+txtPass.getText());
		 //send("@n"+txtName.getText());
		 //send("@s"+txtNick.getText());
		 //send("@p"+txtPass.getText());
		FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/login.fxml"));
		Parent root;
		try {
			root = loader.load();
			login = loader.getController();
			login.socket=socket;
			login.mainStage=mainStage;
			Scene scene = new Scene(root);
			mainStage.setTitle("WaitingRoom");
			mainStage.setScene(scene);
			mainStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
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
	 
		void stopClient() {
			try {
//				Platform.runLater(()->{
//					displayText("[연결 끊음]");
//					btnConn.setText("start");
//					btnSend.setDisable(true);
//				});
				if(socket!=null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {}
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
	 

