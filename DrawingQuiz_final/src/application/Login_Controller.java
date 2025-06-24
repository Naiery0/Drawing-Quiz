package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Login_Controller implements Initializable{
	
	@FXML
	private AnchorPane wrap;// 가장 큰 화면
	//로그인
	@FXML
    private Button btnMembership;//회원가입
    @FXML
    private Button btnCancel;//닫기
    @FXML
    private Button btnLogin;//로그인
    @FXML
    private PasswordField txtPassword;//비밀번호
    @FXML
    private TextField txtUserName;//아이디
    @FXML
    private Label loginMessageLabel;//아이디 혹은 비밀번호 틀렸을때
    @FXML
    private Label NewMessage;//회원가입 칸이 비어있을때 보내는 메세지
    
    @FXML
    private Button btnFind;//아이디/비밀번호 찾기
    @FXML
    private TextField txtEmail;//아이디/비밀번호 찾기에서 이메일
    
    
    //로그인 후 
    @FXML
    private Button btnLogout;//로그아웃
    
    

    //회원가입
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField NEWMAIL;
    @FXML
    private TextField NEWNAME;
    @FXML
    private PasswordField NEWPASSWORD;
    @FXML
    private TextField NEWUSERNAME;
    

    @FXML
    private Label FINDLABEL;//이메일이 존재하지 않을경우 경고문
    @FXML
    private Pane FINDPANE;//아이디/비밀번호 찾기 페인
    @FXML
    private Button test;
    @FXML
    private Pane LPANE;//로그인 페인
    @FXML
    private Pane MEMBERPANE;//회원가입 페인
    @FXML
    private Button btnBefore;//회원가입페이지에서 로그인페이지로 이전
    @FXML
    private Button btnFINDENTER;//아이디 비번 찾기에서 확인 버튼
    @FXML
    private Button btnFINDBEFORE;
    

    @FXML
    private Label FOUNDID;

    @FXML
    private Pane FOUNDPANE;

    @FXML
    private Pane roomWrap;
    
    @FXML
    private Label FOUNDPS;

    @FXML
    private Circle c1;
    @FXML
    private Pane CirclePlay;
    
    UserDAO userDAO = new UserDAO();
    int Screen = 0;
    
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
		System.out.println("Load Success!");
		
    	btnLogin.setOnMouseReleased(e->{
        	clickSound();
    	});
    	btnCancel.setOnMouseReleased(e->{
        	clickSound();
    	});
    	btnMembership.setOnMouseReleased(e->{
        	clickSound();
    	});
    	btnFind.setOnMouseReleased(e->{
        	clickSound();
    	});
    	btnConfirm.setOnMouseReleased(e->{
        	clickSound();
    	});
    	btnBefore.setOnMouseReleased(e->{
        	clickSound();
    	});
    	btnFINDENTER.setOnMouseReleased(e->{
        	clickSound();
    	});
    	btnFINDBEFORE.setOnMouseReleased(e->{
        	clickSound();
    	});
    
	}
    
    //로그인
    public void login() throws IOException, InterruptedException {
    	
    	
    	String UserName = txtUserName.getText();
        String PassWord = txtPassword.getText();

        int result = userDAO.login(UserName, PassWord);

        if(result == 1) {
        	CirclePlay.setVisible(true);
            MEMBERPANE.setVisible(false);
            LPANE.setVisible(false);
            FINDPANE.setVisible(false);
            FOUNDPANE.setVisible(false);
            
            setRotate(c1,true,360,10);
        }
        if(UserName.equals("")) {
            loginMessageLabel.setText("아이디를 입력하시오.");// 아이디 입력을 안했을 때
        }
        else if(PassWord.equals("")) {
            loginMessageLabel.setText("비밀번호를 입력하시오.");// 비밀번호 입력을 안했을 때
        }
        else {
            loginMessageLabel.setText("일치하는 계정을 찾을 수 없습니다.");//아이디 또는 비밀번호가 틀렸을 때
        }
        txtPassword.setText("");//로그인 실패시 txtPassword칸에 다시 빈칸으로 수정
    }
    public void Login2(KeyEvent event) throws IOException, InterruptedException {
        if(event.getCode() == KeyCode.ENTER) {
            login();
        }
    }
    //CLOSE버튼
    public void cancelLogin() {
		Platform.exit();
	}
    
    //로그아웃
    public void logout() throws IOException {
    	btnLogout.getScene().getWindow().hide();//로그인창 숨키기
    	AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root,1084,732);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage primaryStage = new Stage(); 
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);//사이즈변경불가
		primaryStage.setTitle("로그인");
		primaryStage.show();
		
    	
    }
    
    
    //회원가입
    @FXML
    public void Membership() throws IOException {
    	MEMBERPANE.setVisible(true);
    	LPANE.setVisible(false);
    	
        FINDPANE.setVisible(false);
    }
    
    
    //회원가입 확인버튼
    @FXML
    public void MembershipEnter() throws IOException {
    	
        String NewUserName = NEWUSERNAME.getText();
        String NewPassWord = NEWPASSWORD.getText();
        String NewName = NEWNAME.getText();
        String NewMail = NEWMAIL.getText();
        int result = userDAO.join(NewUserName, NewPassWord, NewName, NewMail);

        if(NewUserName.equals("")||NewPassWord.equals("")||NewName.equals("")||NewMail.equals(""))
        {
            NewMessage.setText("모두 입력하시오.");
        }

        else if(result == -1) {
            NewMessage.setText("아이디가 중복되었습니다.");
        }
        else if(result == 3) {
            NewMessage.setText("이메일이 중복되었습니다.");
        }
        else if(result == 4) {
            NewMessage.setText("아이디와 이메일이 중복되었습니다.");
        }
        else {
            userDAO.join(NewUserName, NewPassWord, NewName, NewMail);
            NEWUSERNAME.setText("");
            NEWPASSWORD.setText("");
            NEWNAME.setText("");
            NEWMAIL.setText("");
            MEMBERPANE.setVisible(false);
            LPANE.setVisible(true);

            FINDPANE.setVisible(false);
            NewMessage.setText("");
            loginMessageLabel.setText("");
        }
    	
    }
    
    @FXML
    void MembershipBefore(ActionEvent event) throws IOException {
    	
    	MEMBERPANE.setVisible(false);
    	LPANE.setVisible(true);
        FINDPANE.setVisible(false);
    }
	


    //아이디 비밀번호 찾기 눌렀을때 페이지 이동
    @FXML
    public void FindMemBer() throws IOException {
    	LPANE.setVisible(false);
    	
        FINDPANE.setVisible(true);
        
        
    	
    }
    
    //아이디 비번 찾기에서 이전키
    @FXML
    void FINDBEFORE(ActionEvent event) {
    	txtEmail.setText("");
    	MEMBERPANE.setVisible(false);
    	LPANE.setVisible(true);
        FINDPANE.setVisible(false);
        FOUNDPANE.setVisible(false);
    }

    
    //아이디 비번 찾기에서 확인 버튼 눌렀을 때
    @FXML
    void FindEnter(ActionEvent event) {
    	String Email = txtEmail.getText();
    	String ID = userDAO.FindID(Email);
    	String PW = userDAO.FindPassword(Email);
    	
    	if(ID == null || PW == null) {
    		FINDLABEL.setText("이메일이 존재하지 않습니다.");
    	}
    	else {
    		FINDLABEL.setText("");
    		txtEmail.setText("");
        	MEMBERPANE.setVisible(false);
        	LPANE.setVisible(false);
            FINDPANE.setVisible(false);
            FOUNDPANE.setVisible(true);
            FOUNDID.setText(ID);
            FOUNDPS.setText(PW);
    	}
    	
    }
    @FXML
    void FOUNDCORRECT(ActionEvent event) {
    	MEMBERPANE.setVisible(false);
    	LPANE.setVisible(true);
        FINDPANE.setVisible(false);
        FOUNDPANE.setVisible(false);
    }
    
    public void setRotate(Circle c, boolean reverse, int angle, int duration) throws InterruptedException {
        RotateTransition rt = new RotateTransition(Duration.seconds(duration),c);

        rt.setAutoReverse(false);

        rt.setByAngle(angle);
        rt.setDelay(Duration.seconds(0));
        rt.setRate(7);
        rt.setCycleCount(1);
        rt.setInterpolator(Interpolator.LINEAR);
        
        rt.setOnFinished(new EventHandler<ActionEvent>() {//원이 다 돌고난 뒤 실행할 함수
            @Override
            public void handle(ActionEvent t) {
                AnchorPane root;
                try {
                    root = (AnchorPane)FXMLLoader.load(getClass().getResource("Login.fxml"));
                    Scene scene = new Scene(root,631,563);
                    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                    Stage primaryStage = new Stage();

                    
                    // 방 만들기 , 방 들어가기 화면으로 전환하면서 클라이언트에 유저 정보 등록 //
                    CirclePlay.setVisible(false);
                    roomWrap.setVisible(true);
                    
                  	String ID = txtUserName.getText();
                	String PW = txtPassword.getText();
                	
                	System.out.println("입력된 아이디 : "+ID);
                	System.out.println("입력된 비밀번호 : "+PW);
                	
                	String nickname;
                	
                	// 입력된 아이디와 비밀번호를 데이터베이스에서 비교해 해당 유저의 닉네임을 가져옴
                	nickname = userDAO.SetClientNickname(ID, PW);
                	System.out.println("가져온 닉네임 : "+nickname);
                	// 가져온 닉네임을 매개변수로 넣어 메인클래스의 클라이언트에 닉네임을 설정해줌
                	MainClass.USERNAME = nickname;
                	LPANE.setVisible(false);
                    roomWrap.setVisible(true);
                    ////////////////////////////////////////////////////////
                    
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        
        rt.play();

    }	
    @FXML
    public void ConvertScreen(ActionEvent event) {
    	
    	// 화면 전환
        try {
        	System.out.println("게임 화면으로 이동합니다.");
			MainClass.root = FXMLLoader.load(getClass().getResource("Hand.fxml"));
			MainClass.scene = new Scene(MainClass.root);
			MainClass.CurrentScreen.setScene(MainClass.scene);
			MainClass.CurrentScreen.show();	
		} catch (IOException e) {
			System.out.println("쵸 비 상");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	/////////
    }
    public void clickSound() {
    	try {
    		AudioInputStream ais = AudioSystem.getAudioInputStream(new File("@../../sound/click1.wav"));
    		Clip clip = AudioSystem.getClip();
    		clip.stop();
    		clip.open(ais);
    		clip.start();
    	} catch(Exception e) {
    		System.out.println("사운드 파일을 찾을 수 없음");
    	}
    }

}