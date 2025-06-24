package application;
	
import java.util.concurrent.ExecutorService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

//hand coding
public class MainClass extends Application {
	public static ExecutorService threadPool;
	public static Stage CurrentScreen;
	public static Scene scene;
	public static Parent root;
	public static String USERNAME;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.CurrentScreen = primaryStage;
			Font.loadFont(getClass().getResourceAsStream("/resources/ImcreSoojin.ttf"),14);
			System.setProperty("prism.lcdtext", "false"); //안티밸리어싱
			//System.out.println(~여기에 폰트파일~.getFamily()); 패밀리얻기용 코드
			root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			scene = new Scene(root);
			//폰트 css를 씬에 입히기
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			String css = this.getClass().getResource("application.css").toExternalForm(); 
			scene.getStylesheets().add(css);
			
			CurrentScreen.setTitle("DrawingQuiz");
			CurrentScreen.setScene(scene);
			CurrentScreen.show();
			CurrentScreen.setResizable(false);
			/*
        	primaryStage.setTitle("DrawingQuiz");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		//Client client = new Client();
		launch(args);
	}
}
