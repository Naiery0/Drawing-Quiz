package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.paint.Color;

//한명의 클라이언트와 통신하게 해주는 클래스 입니다.
public class Client {
		//소켓이 있어야지 클라이언트와 네트워크상에서 통신할 수 있음.
		Random rd = new Random();
		Socket socket;
		//생성자 생성
		public Client(Socket socket) {
			this.socket = socket;
			//반복적으로 클라이언트로부터 메시지를 전달받을 수 있도록 receive()함수를 만듬.
			receive();
		}
		//클라이언트로부터 메시지를 전달받는 메소드.
		public void receive() {
			//작업 생성은 Runnable 인터페이스 or Callable 인터페이스를 구현한 클래스로 작업요청할 코드를 삽입해 작업을 만들 수 있습니다.
			//둘의 차이점은 Runnable의 run() 메서드는 리턴값이 없고, Callable의 call() 메서드는 리턴 값이 있습니다.
			Runnable thread = new Runnable() {
				@Override
				public void run() {
					try {
						//반복적으로 클라이언드에게 내용을 받을 수 있도록  while문 생성
						while(true) {
						    DataInputStream in=new DataInputStream(socket.getInputStream());
			            	String message = in.readUTF();
			            	String[] tokens= message.split("`");//0,2,4,6번이 인덱스임
			            	String inMemberIndex="";
							//서버에 접속을 한 클라이언트의 주소정보 출력, 스레드의 이름값을 출력, 
			            	if(tokens[0].equals("Draw")) {System.out.print("~");}
			            	else {
			            		System.out.println("[메시지 수신 성공]"+message
									+ socket.getRemoteSocketAddress()
									+ ":" + Thread.currentThread().getName());}
							
			            	if(tokens[0].equals("USER")) {
			            		ServerMain.USERINDEX++;
				            	String[] memberIndex=ServerMain.RESULT.split("`");//0,2,4,6 index
			            		if(ServerMain.USERINDEX==1)
			            			ServerMain.RESULT=ServerMain.USERINDEX+"`"+tokens[1];
			            		else {
			            			boolean flag=false;
			            			int index=1;
			            			while(!flag) {
			            				if(flag==true)break;
			            				inMemberIndex=String.valueOf(index);
			            				for(int j=0;j<memberIndex.length;j=j+2) {
			            					System.out.println("안에있는 친구들의 인덱스:"+memberIndex[j]
			            							+"\n초기 부여받은 인덱스:"+inMemberIndex);
			            					if(memberIndex[j].equals(inMemberIndex)) {
			            						System.out.println("중복이 났어요");
			            						index++;
				            					break;
			            					}
			            					if(j==memberIndex.length-2&&!(memberIndex[memberIndex.length-2].equals(inMemberIndex))){
			            						flag=true;
			            						System.out.println("중복이없군요");
			            					}
			            				}
			            			}
			            			ServerMain.RESULT=ServerMain.RESULT+"`"+inMemberIndex+"`"+tokens[1];	
			            		}
			            	}
			            	else if(tokens[0].equals("LEAVE")) {
			            		String leaveName=tokens[1];//나가는 사람을
			            		System.out.println("나가기 전"+ServerMain.USERINDEX);
			            		ServerMain.USERINDEX--;//인원수 하나 빼고
			            		System.out.println("나가기 후"+ServerMain.USERINDEX);
			            		ServerMain.RESULT= ServerMain.RESULT.replace(leaveName, "나간녀석");
			            		ServerMain.RESULT= ServerMain.RESULT.replace("1`나간녀석","`");
			            		ServerMain.RESULT= ServerMain.RESULT.replace("2`나간녀석","`");
			            		ServerMain.RESULT= ServerMain.RESULT.replace("3`나간녀석","`");
			            		ServerMain.RESULT= ServerMain.RESULT.replace("4`나간녀석","`");
			            		ServerMain.RESULT= ServerMain.RESULT.replace("```","`");
			            		ServerMain.RESULT= ServerMain.RESULT.replace("``","");
			            		String[] memberInfo=ServerMain.RESULT.split("`");
			            		System.out.println("번호 정렬 전 : "+ ServerMain.RESULT);
			            		ServerMain.RESULT="";
			            		for(int i=0;i<memberInfo.length;i++) {
			            			if(i==0&&!(memberInfo[i].equals("1"))) { //누가 나가면 번호 바꿔주는녀석
			            				memberInfo[0]="1";				
			            			}										
			            			if(i==2&&!(memberInfo[i].equals("2"))) {
			            				memberInfo[0]="2";
			            			}
			            			if(i==4&&!(memberInfo[i].equals("3"))) {
			            				memberInfo[0]="3";
			            			}
			            			if(i==6&&!(memberInfo[i].equals("4"))) {
			            				memberInfo[0]="4";
			            			}
			            			ServerMain.RESULT=ServerMain.RESULT+"`"+memberInfo[i];
			            		}
			            		ServerMain.RESULT=ServerMain.RESULT.substring(1, ServerMain.RESULT.length());
			            		System.out.println("번호 정렬 후 : "+ServerMain.RESULT);
							}
		            		///////////타이머///////////
			            	/*else if(tokens[0].equals("TIMEON")||tokens[0].equals("TIMEOFF")) {
			            		if(tokens[0].equals("TIMEON")) {
            						System.out.println("타이머시작 하나요?");
			            			Timer timer=new Timer();
			            			TimerTask task=new TimerTask(){
			            			    @Override
			            			    public void run() {
			            			    //TODO Auto-generated method stub
			            					if(ServerMain.COUNT >=1){ //count값이
			            						System.out.println("[카운트다운 : "+ServerMain.COUNT+"]");
			            						ServerMain.COUNT--; //카운트다운 
			            						send("nowTime`"+ServerMain.COUNT);
			            					}
			            					if(tokens[0].equals("TIMEOFF`")){
			            						send("Draw`"+0+"`"+0+"`"+Color.AQUA+"`"+41);//폭탄투척
			            						ServerMain.COUNT=90;
			            						timer.cancel(); //타이머 종료
			            						send("timeOut`");
			            					}
			            					else if (ServerMain.COUNT<1){
			            						send("Draw`"+0+"`"+0+"`"+Color.AQUA+"`"+41);//폭탄투척
			            						ServerMain.COUNT=90;
			            						timer.cancel(); //타이머 종료
			            						send("timeOut`");
			            					}
			            			    }	
			            			};
			            			timer.schedule(task, 1000L, 1000L);
			            		}
			            	}*/
			            	
			            	String send="userPut`"+ServerMain.USERINDEX+"`"+ServerMain.RESULT;
		            		send= send.replace("``","`");
							//전달받은 메시지를 다른 클라이언트들에게 보낼 수 있도록 만들어 줍니다.
							for(Client client : ServerMain.clients) {
				            	if(tokens[0].equals("USER")) {
				            		//for(int i = 0; i<userIndex; i++) {
				            			client.send(send);
				            		//}
				            	}
				            	else if(tokens[0].equals("LEAVE")) {
			            			client.send(send);
				            	}
				            	else client.send(message);
							}
						}
					}catch(Exception e){
						try {
							System.out.println(" [메시지 수신 오류]"
							+ socket.getRemoteSocketAddress()
							+ " : " + Thread.currentThread().getName());
						}catch(Exception e2){
							e2.printStackTrace();
						}
					}
				}
			};
			//메인함수에 있는 스레드풀에 섭밋을 해줍니다.
			//즉 스레드풀에 만들어진 하나의 스레드를 등록
			ServerMain.threadPool.submit(thread);
		}
		//클라이언트에게 메시지를 전송하는 메소드.
		public void send(String message) {
			Runnable thread = new Runnable() {
				@Override
				public void run() {
					try {
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF(message);
					}catch(Exception e){
						try {
							System.out.println("[메시지 송수신 오류]" +message
									+ socket.getRemoteSocketAddress()
									+ " :" + Thread.currentThread().getName());
								ServerMain.clients.remove(Client.this);
								socket.close();
						}catch(Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			};
			ServerMain.threadPool.submit(thread);
		}
}