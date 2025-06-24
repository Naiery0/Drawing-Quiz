package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://116.37.177.52:3306/bbs";
			String dbID = "root";
			String dbPassword = "6973";
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			System.out.println("연결!");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) 
					return 1;//로그인 성공
				else
					return 0; // 비밀번호 불일치
			}
			return -1; //아이디가 없음
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -2;//데이터베이스 오류
	}
	public int join(String NewUserName, String NewPassWord, String NewName, String NewMail) {

        String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?)";
        try {
            pstmt = conn.prepareStatement("select userID from user "+"where userEmail = ?");
            pstmt.setString(1, NewMail);
            rs = pstmt.executeQuery();

            if( rs.next() ) {
                pstmt = conn.prepareStatement("select userID from user "+"where userID = ?");
                pstmt.setString(1, NewUserName);
                rs = pstmt.executeQuery();
                if(rs.next()) {
                    return 4;
                }
                return 3;
            }

            else {
                pstmt = conn.prepareStatement(SQL);
                pstmt.setString(1, NewUserName);
                pstmt.setString(2, NewPassWord);
                pstmt.setString(3, NewName);
                pstmt.setString(4, NewMail);
                
                return pstmt.executeUpdate();
            }

        }catch(Exception e) {
            //e.printStackTrace();
        }
        return -1;

    }
	public String FindID(String userEmail) {
		
		String SQL = "SELECT userID FROM user WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement("select userID from user "+"where userEmail = ?");
			pstmt.setString(1, userEmail);
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				String ID = rs.getString("userID");
				return ID;
			}
			
	        else
	        	return null;
			/*
			String ID = rs.getString(1);
			System.out.println(ID);
			return ID;
			*/
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public String FindPassword(String userEmail) {
		
		try {
			pstmt = conn.prepareStatement("select userPassword from user "+"where userEmail = ?");
			pstmt.setString(1, userEmail);
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				String PS = rs.getString("userPassword");
				return PS;
			}
			
	        else
	        	return null;
			/*
			String ID = rs.getString(1);
			System.out.println(ID);
			return ID;
			*/
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String SetClientNickname(String USERID, String USERPW) {
		
		String SQL = "SELECT userName FROM user WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement("select userName from user "+"where userID = ?");
			pstmt.setString(1, USERID);
			//pstmt.setString(2, USERPW);
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				String nickname = rs.getString("userName");
				System.out.println("가져온 닉네임 : "+nickname);
				return nickname;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
		//String result = null;
		//MainClass.client = new Client(result);
	}
	public String receiveWord() {
		String SQL = "SELECT * FROM word ORDER BY RAND() LIMIT 1";
		try {
			

			String word = null;
			pstmt = conn.prepareStatement("SELECT * FROM word ORDER BY RAND() LIMIT 1");
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				word =  rs.getString("sub");
			}
			/*
			for(int i = 0; i < 11; i++) {
				pstmt = conn.prepareStatement("SELECT * FROM word ORDER BY RAND() LIMIT 1");
				rs = pstmt.executeQuery();
				
				if( rs.next() ) {
					
//					System.out.println(i+" "+rs.getString("sub"));
					int flag = 0;
					for (int j = 0; j < i; j++) {
						
						
						if(word[j].equals(rs.getString("sub"))) {//word[j].equals(rs.getString("sub"))
//							System.out.println("중복발생");
							i--;
							flag = 1;
							break;
						}
						else {
							word[i] =  rs.getString("sub");
						}
						
					}
//					if (flag == 0) {
////						System.out.println("성공");
//						word[i] =  rs.getString("sub");
//						
//					}
				}
//				System.out.println(word[i]);
			}
			
			for (int i = 0; i < word.length; i++) {
				System.out.println(i+" "+word[i]);
			}
			*/
			return word;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}