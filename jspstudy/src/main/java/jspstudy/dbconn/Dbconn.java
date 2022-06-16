package jspstudy.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconn {
	
		String url="jdbc:mysql://127.0.0.1:3306/opentutorials?serverTimezone=UTC&characterEncoding=UTF-8";
		String user="root";
		String password = "dlfjsdlfjs9";		
		
		public Connection getConnection() {
			Connection conn = null;
			try {
			//
			Class.forName("com.mysql.cj.jdbc.Driver");
			//
			conn = DriverManager.getConnection(url, user, password);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return conn;
		}
}
