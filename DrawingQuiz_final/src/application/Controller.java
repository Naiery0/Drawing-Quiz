package application;

import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class Controller implements Initializable{ 
	//그림판
	private @FXML Canvas canvas;	//그림장
    private @FXML ColorPicker cp;//색상 선택기
    private @FXML Slider slider;//두께 조절기
    
    //private @FXML Label penStick;//폐기물
    private @FXML Button skipBtn;//넘기기 버튼
    private @FXML ImageView boomBtn;//폭탄 이미지
    private @FXML ImageView esrBtn;//지우개 이미지
    private @FXML ImageView penBtn;//펜 이미지
    boolean esrOrPen=false;//true 시 지우개
    
    private @FXML Pane pane; //canvas가 올라가 있는 판
    private @FXML Pane toolbar;
    private @FXML Pane wordbar;
    
    //방 정보 (폐기물)
    /*
    private @FXML Label roomField;
    private @FXML Label roomTitle;
    */
    private @FXML Pane user1;
    private @FXML Pane user2;
    private @FXML Pane user3;
    private @FXML Pane user4;
    //방 멤버
    private @FXML Label member1;
    private @FXML Label member2;
    private @FXML Label member3;
    private @FXML Label member4;
    private @FXML Button exitBtn;
    
    //멤버 점수
    private @FXML Label mScore1;
    private @FXML Label mScore2;
    private @FXML Label mScore3;
    private @FXML Label mScore4;
    
    //멤버 이미지
    private @FXML ImageView userIMG1;
    private @FXML ImageView userIMG2;
    private @FXML ImageView userIMG3;
    private @FXML ImageView userIMG4;
    
    //정답 맞췄는지
    /*
    private @FXML Label nice1;
    private @FXML Label nice2;
    private @FXML Label nice3;
    private @FXML Label nice4;
    */ //폐기물
    
    //제시어 시간
    private @FXML Label drawWord;//제시어
    private @FXML ProgressBar time;//타임바
    
    //채팅
    private @FXML TextFlow chatLog;//채팅로그
    private @FXML TextField chatIn;//채팅쓰는 필드
    private @FXML Button sendBtn;//채팅보내기
    private @FXML ScrollPane scroll;
    public String user;//유저 닉네임
    
    GraphicsContext gc;//그림도구 객체
    String chatText = "";    

    //게임
    public boolean artist = false;//그림그리는 사람인가 아닌가    
    private int artist_index = -1;
    private int USERINDEX = 0;	// 유저 수
    private String USERLIST = "";
    private boolean isStart = false;
    //
    private @FXML Pane scoreBoard;
    private @FXML ImageView closeScore;
    private @FXML Label first;
    private @FXML Label second;
    private @FXML Label third;
    private @FXML Label fourth;

    //
    
    int RightClick = 0;	// 0 - 우클릭으로 지우기를 시도하지 않음 , 1 - 우클릭으로 지우기를 시도함
    
    //////////////////////////////////////////추가////////////////////////////////////////////////////////////
    int myNum = 0;
    int num = 0;
    int score1 = 0;
    int score2 = 0;
    int score3 = 0;
    int score4 = 0;
    String word = null;
    int textNum = 1;
    int turn = 0;
    int FINALROUND = 15;
    int nextArtist = 0;
    
    private String path = "@../../sound/game.mp3";
    private String path2 = "@../../sound/O.mp3";
    private String path3 = "@../../sound/myturn.mp3";
    @FXML
    private ImageView ClickSound;
    @FXML
    private ImageView ClickStop;
    

    Media media = new Media(new File(path).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    
    Media media2 = new Media(new File(path2).toURI().toString());
    MediaPlayer mediaPlayer2 = new MediaPlayer(media2);
    
    Media media3 = new Media(new File(path3).toURI().toString());
    MediaPlayer mediaPlayer3 = new MediaPlayer(media3);
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		/////////////////////// 서버 관련 ///////////////////////////
		startClient("211.247.48.218", 8080);
		
		// 주재오 IP : 116.37.177.52  // port : 8000
		// 김준태 IP : 211.247.48.218 // port : 8080
		// 나희진 IP : 222.118.75.28  // port : 8765
		System.out.println("[서버 접속 시도 중 ..]");		
		//////////////////////////////////////////////////////////
		mediaPlayer.setVolume(0.1);
		mediaPlayer.setCycleCount(100);
    	mediaPlayer.play();
    	ClickSound.setVisible(false);
    	ClickStop.setVisible(true);
		//receiveWord();
		//drawWord.setText(word);
		
		cp.getStyleClass().add("button");//cp버튼화
		// TODO Auto-generated method stub
		//슬라이드 밸류 조정
        slider.setMin(1);//slider value
        slider.setMax(40);
        //그림도구객체 정의 , 초기 색, 굵기
	    gc = canvas.getGraphicsContext2D();
	    cp.setValue(Color.BLACK);
        gc.setLineWidth(3);//굵기
    	penBtn.setEffect(new InnerShadow(15, Color.BLACK));//초기실행 시 펜 상태 ui
        drawGame();
	}
	
	//마우스 그림 기능 구현
	//지우개는 캔버스와 같은 색으로 출력하기로
	//StackPane위에있는 Canvas에 그리는 것이므로 pane.마우스~~ 쓰는 것임
	void drawGame() {
        //(1) 마우스를 누를 때 
	    	pane.setOnMousePressed (e->{
	    		// 내가 그림 그리기 권한을 받았을 때만 실행 가능
	    		if(artist==true) {
	    			// 지우개가 활성화 되어있는지 확인
	    			if(esrOrPen==true) {
	    				gc.setStroke(Color.WHITE);
	    				esrBtn.setEffect(new InnerShadow(15, Color.BLACK));
	    				penBtn.setEffect(null);
	    				
	    				sendDraw(e.getSceneX()-8,e.getSceneY()-12,Color.WHITE,slider.getValue());
	    			}
	    			// 지우개가 활성화 되어있지 않음
	    			else {
	    				// 	마우스 오른쪽 클릭으로 그리기를 시도했다면 지우기
	    				if(e.getButton() == MouseButton.SECONDARY) {
	    					RightClick = 1;	// 우클릭 되었음
	    			
	    					gc.setStroke(Color.WHITE);
	    					esrBtn.setEffect(new InnerShadow(15, Color.BLACK));
	    					penBtn.setEffect(null);
	    					
	    					sendDraw(e.getSceneX()-8,e.getSceneY()-12,Color.WHITE,slider.getValue());
	    				}
	    				// 	마우스 왼쪽 클릭으로 그리기를 시도했다면 그리기
	    				else { 				
	    					gc.setStroke(cp.getValue());
	    					penBtn.setEffect(new InnerShadow(15, Color.BLACK));
	    					esrBtn.setEffect(null);
	    					
	    					sendDraw(e.getSceneX()-8,e.getSceneY()-12,cp.getValue(),slider.getValue());
	    				}	    				    				
	    			}   			
	    		}
	        });
	        //(2) 마우스를 드래그 할 때
	        pane.setOnMouseDragged(e->{
	        	// 내가 그림 그리기 권한을 받았을 때만 실행 가능
	        	if(artist==true) {
	        		// 지우개가 활성화 되있는지 여부 확인
	        		if(esrOrPen==true) {
	        			gc.setStroke(Color.WHITE);
	        			esrBtn.setEffect(new InnerShadow(15, Color.BLACK));
	        			penBtn.setEffect(null);
	        			
	        			sendDraw(e.getSceneX()-8,e.getSceneY()-12,Color.WHITE,slider.getValue());
	        		}
	        		// 지우개가 활성화 되어있지 않음
	        		else {
	        			// 마우스 오른쪽 클릭으로 그리기를 시도했다면 지우기
	        			if(e.getButton() == MouseButton.SECONDARY) {
	        				RightClick = 1;	// 우클릭 되었음
	        		
	        				gc.setStroke(Color.WHITE);
	        				esrBtn.setEffect(new InnerShadow(15, Color.BLACK));
	        				penBtn.setEffect(null);
	        				
	        				sendDraw(e.getSceneX()-8,e.getSceneY()-12,Color.WHITE,slider.getValue());
	        			}
	        			// 마우스 왼쪽 클릭으로 그리기를 시도했다면 그리기
	        			else {    				
	        				gc.setStroke(cp.getValue());
	        				penBtn.setEffect(new InnerShadow(15, Color.BLACK));
	        				esrBtn.setEffect(null);
	        				
	        				sendDraw(e.getSceneX()-8,e.getSceneY()-12,cp.getValue(),slider.getValue());
	        			}	        				  
	        		}
	        	
	        			//gc.lineTo(e.getSceneX()-8, e.getSceneY()-11); //이동한 좌표마다
	        			//gc.stroke(); //계속해서 점을 찍음
	        	}
	        });
	        //연필 색 변경
	        cp.setOnAction(e->{ 
	        	gc.setStroke(cp.getValue());//연필 색을 컬러픽에 맞춰 바꾸는 것
            });
	        //연필 굵기 변경
	        slider.valueProperty().addListener(e->{
	        	double value = slider.getValue(); 
            	gc.setLineWidth(value);
	        	
            });
	       //지우개이미지 클릭 시 펜 하얀색으로 
	       esrBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	System.out.println("지우개 활성화");
	            	esrOrPen=true;
	            }
	        });
	       	// 마우스 뗐을때
	        pane.setOnMouseReleased(e->{
				//gc.beginPath();
	        	// 우클릭 눌렀을때
	        	if(RightClick == 1) {
	        		//System.out.println("자동으로 펜으로 변환되었음");
	        		penBtn.setEffect(new InnerShadow(15, Color.BLACK));
	        		esrBtn.setEffect(null);
	        		
	        		RightClick = 0;
	        	}
	        	else {
	        		//System.out.println("변환되지 않음");
	        	}
	       });
	       //버튼에 마우스를 댔다 때면 이펙트(inner)가 사라지는 현상이 있어 추가
	       //마우스가 버튼에서 벗어날 때 액션임
	       esrBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
	            new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent e) {        
	            	if(esrOrPen==true) {
	                esrBtn.setEffect(new InnerShadow(15, Color.BLACK));
	            	penBtn.setEffect(null);
	                }
	                else {
		            penBtn.setEffect(new InnerShadow(15, Color.BLACK));
		            esrBtn.setEffect(null);
	                }
	            }
	          });
	       //펜이미지 클릭 시 원래 펜 색으로
	       penBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	esrOrPen=false;
	            	if(esrOrPen==false) {
	            	gc.setStroke(cp.getValue());
		        	penBtn.setEffect(new InnerShadow(15, Color.BLACK));
		        	esrBtn.setEffect(null);
		        	}
	            }
	        });
	       //버튼에 마우스를 댔다 때면 이펙트(inner)가 사라지는 현상이 있어 추가
	       penBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
	                new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent e) {        
	            	if(esrOrPen==false) {
	            	penBtn.setEffect(new InnerShadow(15, Color.BLACK));
	            	esrBtn.setEffect(null);
	                }
	                else {
	                esrBtn.setEffect(new InnerShadow(15, Color.BLACK));
	            	penBtn.setEffect(null);
	                }
	            }
	          });
	       
	}
	void DrawScreen(double curX, double curY, Color setColor, double setSize) {
		//System.out.println("X 좌표 : "+curX+" , Y 좌표 : "+curY+" , 선택된 색깔 : "+setColor+" , 굵기 : "+setSize);
		if(setSize==41) {
			boom();
		}
		else {
		Platform.runLater(() -> {
				gc.beginPath();
				gc.setStroke(setColor);
				gc.setLineWidth(setSize);
				gc.lineTo(curX,curY); // 좌표로 이동하여
				gc.stroke(); //계속해서 점을 찍음
				//gc.closePath();
			/*pane.setOnMousePressed (e->{
	            gc.beginPath(); 
	            gc.lineTo(curX,curY);
	                gc.stroke();
	        });
	        pane.setOnMouseDragged(e->{
	        	gc.lineTo(curX,curY);
	        	gc.stroke();
	        });*/
			});
		}
	}
	//폭탄 버튼, timer()에 의한 모두 지우기
	void boom() {
		gc.clearRect(0, 0, 1280, 800);
	}
	
	@FXML
	void send_boom() {
		sendDraw(0,0,Color.BLACK,41);
	}
	
	//2분 타이머(240, minutes(4))
	void timer() {
	       IntegerProperty seconds = new SimpleIntegerProperty();
	       seconds.bind(time.progressProperty().divide(0));
	       Timeline timeline = new Timeline(
	                new KeyFrame(Duration.ZERO, new KeyValue(seconds,0)),
	                new KeyFrame(Duration.minutes(2), e-> {
	                    //시간 끝나면
	                	receiveWord();
	                	sendClient("Chat`[SYSTEM] `시간초과! 정답은 ["+word+"]였습니다4");
	            		send_boom();
	                }, new KeyValue(time.progressProperty(), -0.001))    
	            );	
	       timeline.setCycleCount(Animation.INDEFINITE);
	       timeline.play();
	}
	
	//채팅 보내기
	@FXML
	void sendChatEnter(KeyEvent event) {
		if( event.getCode() == KeyCode.ENTER) {
			sendChat();
		}
	}
	
	@FXML
	void sendChat() {
		if(chatIn.getText().length()>0) {
			sendClient("Chat`"+MainClass.USERNAME + " : " +"`"+ chatIn.getText());
		}
		chatIn.clear();
	}
	
	@FXML
	void leaveGame() {
        sendClient("LEAVE`"+MainClass.USERNAME);
        Platform.exit();
    }
	
	 @FXML
	 void PlaySound(MouseEvent event) {
		 mediaPlayer.setVolume(0.1);
		 mediaPlayer.setCycleCount(100);
		 mediaPlayer.play();
		 ClickSound.setVisible(false);
		 ClickStop.setVisible(true);
	}

	@FXML
	void stopSOUND(MouseEvent event) {
		mediaPlayer.stop();
		ClickSound.setVisible(true);
		ClickStop.setVisible(false);
	}
	@FXML
	void closeScoreBoard() {
		scoreBoard.setVisible(false);
	}
	public void correctSound() {
		/*
    	try {
    		AudioInputStream ais = AudioSystem.getAudioInputStream(new File("@../../sound/O.wav"));
    		Clip clip = AudioSystem.getClip();
    		clip.stop();
    		clip.open(ais);
    		clip.start();
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println("사운드 파일을 찾을 수 없음");
    	}
    	*/
		mediaPlayer2.stop();
		mediaPlayer2.setVolume(0.2);
		mediaPlayer2.setCycleCount(1);
    	mediaPlayer2.play();
    }
	public void myturnSound() {
		/*
    	try {
    		AudioInputStream ais = AudioSystem.getAudioInputStream(new File("@../../sound/O.wav"));
    		Clip clip = AudioSystem.getClip();
    		clip.stop();
    		clip.open(ais);
    		clip.start();
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println("사운드 파일을 찾을 수 없음");
    	}
    	*/
		mediaPlayer3.stop();
		mediaPlayer3.setVolume(0.2);
		mediaPlayer3.setCycleCount(1);
    	mediaPlayer3.play();
    }
	
	Socket socket;
	
	// 클라이언트 프로그램 동작 메소드 (어떤 IP로 , 어떤 port로 접속할지 정해줌)
	public void startClient(String IP, int port) {
		// 스레드 객체 생성!
		Thread thread = new Thread() {
			public void run() {
				try {
					// socket 초기화
					socket = new Socket(IP, port);
					receiveClient();
				} catch (Exception e) {
					System.out.println("[서버 접속 실패]");
					// 오류가 생긴다면
					if (!socket.isClosed()) {
						stopClient();
						Platform.exit();
					}		
				}
			}
		};
		thread.start();
	}

	// 클라이언트 프로그램 종료 메소드
	public void stopClient() {
		try {
		    	if(socket != null && !socket.isClosed()) {
		    		sendClient("LEAVE`"+MainClass.USERNAME);
		    		socket.close(); 
			}
		}catch(Exception e){
			System.out.println("클라이언트가 오류에 의해 종료되었음");
			//e.printStackTrace();
		}
	}
	// 서버로부터 메시지를 전달받는 메소드
	public void receiveClient() {
		// 서버 프로그램으로부터 메시지를 계속 전달 받을 수 있도록
		System.out.println("[서버 접속 완료]");
		sendClient("Chat`[SYSTEM] "+MainClass.USERNAME+"`"+"님이 게임에 입장하셨습니다2");
		sendClient("USER`"+MainClass.USERNAME);
		while (true) {
			try {
				
				// 서버로부터 메시지를 전달 받을 수 있도록
			    DataInputStream in=new DataInputStream(this.socket.getInputStream());
            	String DataInfo = in.readUTF();
            	//System.out.println("Data : "+in.readUTF());
            	
            	String[] tokens = DataInfo.split("`");
				//StringTokenizer st = new StringTokenizer(DataInfo);
				//String InfoType = st.nextToken("`");
				
				// 가져온 데이터가 그림에 관한 데이터면 실행
            	if(tokens[0].equals("Draw")) {
					
					// 각각의 데이터들을 토큰을 통해 나누어 실제 그림 그리는 메소드에 넘겨서 호출해줌
					double curX = Double.valueOf(tokens[1]);
					double curY = Double.valueOf(tokens[2]);
					Color setColor = Color.valueOf(tokens[3]);
					double setSize = Double.valueOf(tokens[4]);
					
					//System.out.println("2) "+curX+curY+setColor+setSize);
					DrawScreen(curX,curY,setColor,setSize);
					//System.out.println("서버로 부터 받은 좌표 : "+DataInfo);
				}
				// 가져온 데이터가 채팅에 관한 데이터면 실행
				else if(tokens[0].equals("Chat")) {
					//System.out.println("서버로 부터 받은 메세지 : "+DataInfo);
					
					Platform.runLater(() -> {
							
						Text newChat = new Text();
						//String result = st.nextToken("`");
						
						// 이 부분 수정할 것
						String result = tokens[1]+tokens[2];
						if (tokens[2].equals(word)) {
							
							if(artist_index==myNum) 
								sendClient("Chat`[SYSTEM] `"+tokens[1].substring(0,tokens[1].length()-3)+"님이 ["+word+"] 정답을 맞췄습니다!2");
							
							String[] temp = USERLIST.split("`");
							
							//System.out.println("유저 목록 : "+USERLIST);
							//System.out.println("찾는 유저 : "+tokens[1].substring(0,tokens[1].length()-3));
							int i = 0;
							int flag = 0;
							for(i=0;i<USERINDEX;i++) {
								//System.out.println("i : "+i+" , temp[i] : "+temp[i]+" , target : "+tokens[1].substring(0,tokens[1].length()-3));
								if(temp[i].equals(tokens[1].substring(0,tokens[1].length()-3))) {
									flag = 1;
									break;
								}
							}
							
							if(flag==1) {
								//System.out.println("유저 찾기 성공!");
								//System.out.println("찾은 유저 인덱스 : "+(i+1));
							}
							else {
								//System.out.println("유저 찾기 실패");
							}
							Correct(i+1);
							//timer();
						}
						else if(tokens[2].charAt(0)=='/') {
							String command = tokens[2].substring(1,tokens[2].length()); 
							
							if(command.equals("game")&&isStart==false) {
								score1 = 0;
								score2 = 0;
								score3 = 0;
								score4 = 0;
								
								mScore1.setText("0점");
								mScore2.setText("0점");
								mScore3.setText("0점");
								mScore4.setText("0점");
								
								artist_index=1;
								nextArtist=0;
								
								turn = 0;
								
								gamePlay();
							}
							else if(command.substring(0,command.length()-1).equals("round")) {
								switch(command.charAt(command.length())){
									case 'a':
										FINALROUND = 5;
										break;
									case 'b':
										FINALROUND = 10;
										break;
									case 'c':
										FINALROUND = 15;
										break;
									case 'd':
										FINALROUND = 20;
										break;
								}
								sendClient("Chat`[SYSTEM] `총 라운드 수가 "+FINALROUND+"로 고정되었습니다.2");
							}
							
						}					
						switch(result.charAt(result.length()-1)) {
							case '1':
								newChat.setFill(Color.GREEN);
								result = result.substring(0, result.length() - 1);
								break;
							case '2':
								newChat.setFill(Color.GOLD);
								result = result.substring(0, result.length() - 1);
								break;
							case '3':
								newChat.setFill(Color.ORANGE);
								result = result.substring(0, result.length() - 1);
								break;
							case '4':
								newChat.setFill(Color.RED);
								result = result.substring(0, result.length() - 1);
								break;
							case '5':
								newChat.setFill(Color.GRAY);
								result = result.substring(0, result.length() - 1);
								break;
							case '6':
								newChat.setFill(Color.PINK);
								result = result.substring(0, result.length() - 1);
								break;
							case '7':
								newChat.setFill(Color.AQUA);
								result = result.substring(0, result.length() - 1);
								break;
							case '8':
								newChat.setFill(Color.AQUAMARINE);
								result = result.substring(0, result.length() - 1);
								break;
							case '9':
								newChat.setFill(Color.SKYBLUE);
								result = result.substring(0, result.length() - 1);
								break;
							default:
								newChat.setFill(Color.BLACK);
								break;
						}
						newChat.setText(result+"\n");
						chatLog.getChildren().add(newChat);
						scroll.vvalueProperty().bind(chatLog.heightProperty());
					});
				}
            	//가져온 데이터가 서버입장이면
				else if(tokens[0].equals("userPut")) {
            		System.out.println(DataInfo);
            		Platform.runLater(() -> {
            			if(tokens[1].equals("1")) {
            				member1.setText(tokens[3]);
            				member2.setText("");
            				member3.setText("");
            				member4.setText("");
            				
            				mScore1.setVisible(true);
            				mScore1.setText(String.valueOf(score1));
            				mScore2.setText("");
            				mScore3.setText("");
            				mScore4.setText("");
            				//System.out.println("1번주자 : "+tokens[3]);
            			
            				userIMG1.setVisible(true);	
            				userIMG2.setVisible(false);	
            				userIMG3.setVisible(false);	
            				userIMG4.setVisible(false);
            			}
            			else if(tokens[1].equals("2")) {
            				member1.setText(tokens[3]);
            				member2.setText(tokens[5]);
            				member3.setText("");
            				member4.setText("");
            				
            				mScore1.setVisible(true);
            				mScore2.setVisible(true);
            				mScore1.setText(String.valueOf(score1));
            				mScore2.setText(String.valueOf(score2));
            				mScore3.setText("");
            				mScore4.setText("");
            				//System.out.println("2번주자 : "+tokens[5]);
            			
            				userIMG1.setVisible(true);
            				userIMG2.setVisible(true);
            				userIMG3.setVisible(false);	
            				userIMG4.setVisible(false);	
            				USERLIST = tokens[3]+"`"+tokens[5];
            			}            			
            			else if(tokens[1].equals("3")) {
            				member1.setText(tokens[3]);
            				member2.setText(tokens[5]);
            				member3.setText(tokens[7]);
            				member4.setText("");
            				
            				mScore1.setVisible(true);
            				mScore2.setVisible(true);
            				mScore3.setVisible(true);
            				mScore1.setText(String.valueOf(score1));
            				mScore2.setText(String.valueOf(score2));
            				mScore3.setText(String.valueOf(score3));
            				mScore4.setText("");
            				//System.out.println("3번주자 : "+tokens[7]);
            			
            				userIMG1.setVisible(true);
            				userIMG2.setVisible(true);
            				userIMG3.setVisible(true);
            				userIMG4.setVisible(false);	
            				USERLIST = tokens[3]+"`"+tokens[5]+"`"+tokens[7];
            			}
            			else if(tokens[1].equals("4")) {
            				member1.setText(tokens[3]);
            				member2.setText(tokens[5]);
            				member3.setText(tokens[7]);
            				member4.setText(tokens[9]);
            				
            				mScore1.setVisible(true);
            				mScore2.setVisible(true);
            				mScore3.setVisible(true);
            				mScore4.setVisible(true);
            				mScore1.setText(String.valueOf(score1));
            				mScore2.setText(String.valueOf(score2));
            				mScore3.setText(String.valueOf(score3));
            				mScore4.setText(String.valueOf(score4));

            				//System.out.println("4번주자 : "+tokens[9]);
            				
            				userIMG1.setVisible(true);
            				userIMG2.setVisible(true);
            				userIMG3.setVisible(true);
            				userIMG4.setVisible(true);
            				USERLIST = tokens[3]+"`"+tokens[5]+"`"+tokens[7]+"`"+tokens[9];
            			}
            		});
            		/////////////////////////////////////////////(추가)번호를 받아오기//////////////////////////////////////////////////////            		
            		for(int i=3;i<Integer.parseInt(tokens[1])*2+2;i=i+2) {
            			if (MainClass.USERNAME.equals(tokens[i])) {
            				myNum = Integer.parseInt(tokens[i-1]);
            			}
            		}
            		USERINDEX = Integer.parseInt(tokens[1]);
            		
            	}
            	else if(tokens[0].equals("Artist")) {
            		artist_index = Integer.parseInt(tokens[1]);
            	}
            	else if(tokens[0].equals("Quiz")) {
            		word = tokens[1];
            	}
            	else if(tokens[0].equals("Score")) {
            		//System.out.println("받아온 점수 : "+DataInfo);
            		
            		Platform.runLater(() -> {
            			mScore1.setText(tokens[1]+"점");
            			mScore2.setText(tokens[2]+"점");
            			mScore3.setText(tokens[3]+"점");
            			mScore4.setText(tokens[4]+"점");
            		});
            	}
				else {
					System.out.println("그림을 수신하는 과정에서 오류가 발생함");
					System.out.println("수신 데이터는 다음과 같음 : "+in.readUTF());
					//stopClient();
				}

			} catch (Exception e) {
				System.out.println("받는 도중에 오류가 생기는 매우 큰일이 생겨버렸다는 뜻!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				//stopClient();
				break;
			}
		}
	}
	////////////////////////////////////////////////////추가/////////////////////////////////////////////////
	public void Correct(int index) {
		//System.out.println("정답을 맞춘 유저 인덱스 : "+index);
		correctSound();
		switch(index) {
			case 1:
				score1++;
				break;
			case 2:
				score2++;
				break;
			case 3:
				score3++;
				break;
			case 4:
				score4++;
				break;
		}
		
		try {
			//System.out.println("내 번호 : "+myNum+ " , 현재 출제자 번호 : "+artist_index);
			// 내가 그림 그리는 사람이라면
			if(myNum==artist_index) {
				sendClient("Score`"+score1+"`"+score2+"`"+score3+"`"+score4);
				Thread.sleep(100);
			}
			else Thread.sleep(100);
		}catch(Exception e) {
			System.out.println("점수 업로드에서 문제가 생겼습니다");
		}
		
		turn++;
		gamePlay();
	}
	UserDAO us = new UserDAO();

	@FXML
	public void receiveWord() {//단어 하니씩 db에서 받아오기
		word = us.receiveWord();
		sendClient("Quiz`"+word);
		drawWord.setText(word);
	}
	
	//그림 보내기
	void sendDraw(double curX, double curY, Color color, double penSize) {
		String DrawInfo = "Draw`"+curX+"`"+curY+"`"+color+"`"+penSize;
		try {
			DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
			out.writeUTF(DrawInfo);
		} catch (IOException e) {
			System.out.println("그림을 보내는 과정에서 오류가 발생함!");
			System.out.println("보낸 데이터는 다음과 같음 : "+DrawInfo);
			//e.printStackTrace();
		}
	}
	
	// 서버로 메시지를 전송하는 메소드
	public void sendClient(String data) {
		Thread thread = new Thread() {
			public void run() {
				try {
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF(data);
				} catch (Exception e) {
					System.out.println("메세지를 보내는 과정에서 오류가 발생함!");
					System.out.println("보낸 데이터는 다음과 같음 : "+data);
					stopClient();
				}
			}
		};
		thread.start();
	}
	
	void gamePlay() {	
		
		try {
			Thread.sleep(100);	
			// 첫 번째 턴이라면 방장이 문제를 뽑음 
			if(turn == 0) {
				if(myNum == 1) {				
					sendClient("Chat`[SYSTEM] `게임을 시작합니다2");		
					isStart = true;			
					
					String[] temp = USERLIST.split("`");
					sendClient("Chat`[SYSTEM] `"+temp[nextArtist]+"님이 그림을 그릴 차례입니다2");
					sendClient("Artist`"+(nextArtist+1));
					Thread.sleep(200);
				}
				else {
					Thread.sleep(200);
				}
				nextArtist = artist_index;
				System.out.println("USERINDEX : "+USERINDEX+" , nextArtist : "+(nextArtist+1));
			}
			// 모든 턴이 지났다면
			else if(turn-1>FINALROUND) {
				boom();
				artist=false;
				
				if(myNum==1) {
					sendClient("Chat`[SYSTEM] `게임이 종료되었습니다2");
					isStart = false;
				}
				scoreBoard.setVisible(true);
				
				String[] temp = USERLIST.split("`");
				
				if(!(temp[0].equals(null))) {
					first.setText(temp[0]+" 님의 점수 : "+score1);
				}
				if(!(temp[1].equals(null))) {
					second.setText(temp[1]+" 님의 점수 : "+score2);
				}
				if(!(temp[2].equals(null))) {
					third.setText(temp[2]+" 님의 점수 : "+score3);
				}
				if(!(temp[3].equals(null))) {
					fourth.setText(temp[3]+" 님의 점수 : "+score4);
				}
				
				//for(int i=0;i<USERINDEX;i++) {
					//sendClient("Chat`[SYSTEM] `게임 종료!!2");
					//Thread.sleep(50);
				//}
			}
			// 첫 번째 턴이 아니라면 이전 턴에 그림을 그렸던 사람이 문제를 뽑음
			else {
				if(nextArtist>USERINDEX-1) {
					nextArtist = 0;
					artist_index = 1;
				}
				if(myNum == artist_index) {
				
					String[] temp = USERLIST.split("`");
					sendClient("Chat`[SYSTEM] `"+temp[nextArtist]+"님이 그림을 그릴 차례입니다2");
					sendClient("Artist`"+(nextArtist+1));
	
					Thread.sleep(200);
				}
				else Thread.sleep(200);
				
				nextArtist = artist_index;		
				//System.out.println("다음 USERINDEX : "+USERINDEX+" , nextArtist : "+(nextArtist+1)+" , artist_index : "+artist_index);
			}
			//System.out.println("value : "+nextArtist);
			//sendClient("Artist`"+((nextArtist%USERINDEX)+1));
			
			Thread.sleep(100);
			boom();
		
			user1.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
			user2.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
			user3.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
			user4.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
			
			switch(artist_index) {
				case 1:
					user1.setBorder(new Border(new BorderStroke(Color.GOLD, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
					break;
				case 2:
					user2.setBorder(new Border(new BorderStroke(Color.GOLD, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
					break;
				case 3:
					user3.setBorder(new Border(new BorderStroke(Color.GOLD, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
					break;
				case 4:
					user4.setBorder(new Border(new BorderStroke(Color.GOLD, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
					break;
			}
			// 내가 그리는 사람일 경우
			if(artist_index==myNum) {
				myturnSound();
				System.out.println("당신이 그림을 그릴 차례입니다");
				
				artist = true; // 그림 그릴 권한 부여받음
				toolbar.setVisible(true); // 
				wordbar.setVisible(true);
				canvas.setCursor(Cursor.CROSSHAIR);
				
				receiveWord();
				
				System.out.println("뽑은 단어 : "+word);
				Thread.sleep(100);
				sendClient("Quiz`"+word);
				Thread.sleep(100);
			}
			// 내가 맞추는 사람일 경우
			else {
				artist = false;
				toolbar.setVisible(false);
				wordbar.setVisible(false);
				canvas.setCursor(Cursor.DEFAULT);
				
				Thread.sleep(200);
			}
			drawWord.setText(word);
			
		} catch(Exception e) {
			
		}
	}
}