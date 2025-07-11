package application;
	
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class ServerMain extends Application {
	
	//스레드 풀 사용(한정된 자원으로 안정적이게 서버를 운용하기 위해서 threadPool 기법을 사용합니다.)
	public static ExecutorService threadPool;
	//접속한 클라이언트들을 관리 할수 있도록 만듬.
	public static Vector<Client> clients = new Vector<Client>();
	//서버 소켓 생성
	ServerSocket serverSocket;
	//서버를 구동시켜 클라이언트의 연결을 기다리는 메소드.
	public static String[] USERS = {};
	public static int USERINDEX = 0;
	public static String RESULT="";
	public static int COUNT=90;
	//자신의 로컬 서버
	String IP = "192.168.0.3";
	int port = 8080;
	
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			//특정한 ip번호와 port번호로 특정한 클라이언트에게 접속을 기다리게 해줌
			serverSocket.bind(new InetSocketAddress(IP, port));
		}catch(Exception e) {
			e.printStackTrace();
			//서버 소켓이 닫혀있는 경우가 아니라면 
			if(!serverSocket.isClosed()) {
				stopServer();//서버를 종료
			}
			return;
		}
		// 클라이언트가 접속할 때 까지 계속 기다리는 스레드.
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				//계속해서 새로운 클라이언트가 접속 할 수 있도록 해줌
				while(true) {
					try {
						if(USERINDEX<3) {//5번째 클라부터 접속 거부
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println(" [클라이언트 접속] "
								+ socket.getRemoteSocketAddress()
								+ " :" + Thread.currentThread().getName());
						}
						
					}catch(Exception e) {
						//   서버 소켓에 문제가 생긴거니 서버를 종료 시키고 break문을 활용해서 빠져나갑니다.
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
			
		};
		//스레드 풀을 초기화
		threadPool = Executors.newCachedThreadPool();
		//클라이언트에 접속을 원하는 스레드를 넣어줍니다. 
		threadPool.submit(thread);
	}
	//서버의 작동을 중지시켜주는 메소드
	public void stopServer() {
		try {
			USERINDEX=0;
			//현재 작업중인 모든 소켓 닫기
			Iterator<Client> iterator = clients.iterator();
			//한명 한명의 클라이언트에게 접근
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			//서버 소켓 객체 닫기
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
				
			}
			//스레드풀 종료
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//UI를 생성하고 , 실질적으로 프로그램을 동작시키는 메서드
	@Override
	public void start(Stage primaryStage) {
		//하나의 전체 디자인 틀을 담을 수 있는 하나의 펜을 생성 
		BorderPane root = new BorderPane();
		//내부 페이딩 5
		root.setPadding(new Insets(5));
		
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("나눔고딕", 15));
		root.setCenter(textArea);
		
		//토글 버튼은 스위치라고 생각하시면 됨.
		Button toggleButton = new Button("시작하기");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
		root.setBottom(toggleButton);
		
		
		toggleButton.setOnAction(event -> {
			if(toggleButton.getText().equals("시작하기")) {
				startServer(IP, port);
				//자바 fx같은 경우는 바로 textArea에 쓰면 안되고 runLator와 같은 함수를 이용하여 어떠한 gui요소를 출력할 수 있도록 해야함.
				Platform.runLater(() -> {
					String message = String.format("[서버 시작]\n", IP, port);
					textArea.appendText(message);
					toggleButton.setText("종료하기");
				});
			}else {
				stopServer();
				Platform.runLater(() -> {
					String message = String.format("[서버 종료]\n", IP, port);
					textArea.appendText(message);
					toggleButton.setText("시작하기");
				});
			}
		});
		//크기
		Scene scene = new Scene(root, 500, 500);
		primaryStage.setTitle("[ 채팅 서버 ]");
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	//프로그램의 메인 메서드
	public static void main(String[] args) {
		launch(args);
	}
}