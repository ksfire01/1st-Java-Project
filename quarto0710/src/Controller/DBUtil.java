package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
 //mysql ����Ÿ���̽� ����̹���
 static final String driver = "com.mysql.jdbc.Driver";
 
 //������ġ�� �����Ҽ��ֵ��� ��θ�� ���õ� DB ���
 static final String url = "jdbc:mysql://localhost/member_db";

 public static Connection getConnection() throws Exception {
  
  Class.forName(driver);
  //��θ�� ���̵�� �н����尡 ���ٿ�û�� �Ѵ�. 
  Connection con = DriverManager.getConnection(url, "root", "123456");
  System.out.println("DB ���� ����");
  return con;
 }
} 
