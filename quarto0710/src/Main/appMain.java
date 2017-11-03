package Main;

import java.io.IOException;

import Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class appMain extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		
			FXMLLoader loader =new FXMLLoader(getClass().getResource("/View/login.fxml"));
			Parent root = loader.load();
			LoginController controller = loader.getController();
			controller.mainStage=primaryStage;
//			Parent root = FXMLLoader.load(getClass().getResource("/View/login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("login");
			primaryStage.setScene(scene);
			primaryStage.show();
//			controller.setStage(primaryStage);
	}
	public static void main(String[] args) {
		launch(args);
	}
}

//public class Main extends Application {
//
//	   @Override
//	   public void start(Stage primaryStage) throws Exception {
//	   
//	      FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login.fxml"));
//	      
//	      Parent root =loader.load();
//	      
//	      LoginController loginController= loader.getController();
//	      loginController.mainStage = primaryStage;
//
////	      root = FXMLLoader.load(getClass().getResource("/View/login.fxml"));
////	      root = FXMLLoader.load(getClass().getResource("/View/reservation.fxml"));
//	      Scene scene = new Scene(root);
//
//	      primaryStage.setTitle("항공권 예약");
//	      
//	      primaryStage.setScene(scene);
//	      primaryStage.show();
//	   }
//
//	   public static void main(String[] args) {
//	      launch(args);
//
//	   }
//
//
//	}