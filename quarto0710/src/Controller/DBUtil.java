package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
 //mysql 데이타베이스 드라이버명
 static final String driver = "com.mysql.jdbc.Driver";
 
 //서버위치를 접근할수있도록 경로명과 선택된 DB 경로
 static final String url = "jdbc:mysql://localhost/member_db";

 public static Connection getConnection() throws Exception {
  
  Class.forName(driver);
  //경로명과 아이디와 패스워드가 접근요청을 한다. 
  Connection con = DriverManager.getConnection(url, "root", "123456");
  System.out.println("DB 연결 성공");
  return con;
 }
} 
