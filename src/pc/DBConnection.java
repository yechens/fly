package pc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static Connection getConnection(){
	Connection conn = null;
	try{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection( 
				"jdbc:mysql://localhost:3306/"
				+ "flight?useUnicode=true&characterEncoding=UTF-8",	//flight为数据库名
				"root", 
				"123456"  );
	}
	catch( Exception ex ){
		System.out.printf("数据库连接失败\n");
	}
	return conn;
	}
}
