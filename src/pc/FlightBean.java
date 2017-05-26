package pc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class FlightBean {		//封装对航线的所有操作	读/写
	private Connection _conn;
	
	public boolean getByClient( ArrayList< FlightInfo > data ,String qi ,String zhong){
		
		//按用户输入的起点终点查询航线
		data.clear();		
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{
			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select flight_num, go, `get`, date, depart_time, fly_time, " )
			    .append( " land_time, depart_airport, land_airport, `condition` " )
			    .append( " from ").append( FlightInfo._tableName )
			    .append( " where" ).append( " go=? and `get`=? " );
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				//指定参数1、2：go、get的赋值!!!!!
     		st.setString(i, qi);
     		i = 2;
     		st.setString(i, zhong);
     		
     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
	
			while( rs.next() ){
				FlightInfo info  = new FlightInfo();
				if( getDataFromResultSet(rs, info) ){
					data.add( info );
				}			
			}			
		}
		
		catch( SQLException e ){
			System.out.printf( "getByClient失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getByClient失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;			
	}
	
	
	public boolean getAll( ArrayList< FlightInfo > data ){		//查询所有航线
		data.clear();
		
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{
			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select flight_num, go, get, date, depart_time, fly_time, " )
			    .append( " land_time, depart_airport, land_airport, `condition` " )
			    .append( " from ").append( FlightInfo._tableName );
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
	
			while( rs.next() ){
				FlightInfo info  = new FlightInfo();
				if( getDataFromResultSet(rs, info) ){
					data.add( info );
				}				
			}			
		}
		catch( SQLException e ){
			System.out.printf( "getAll失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getAll失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if
			
		}// finally		
		return true;
	}
	
	//数据库结果集的数据表中  获取数据 显示在ListView的航班信息
	private  boolean getDataFromResultSet( ResultSet rs, FlightInfo info){		
		try{				
			String flight_num = rs.getString( "flight_num" );
			info.setFlight_num(flight_num);

			String go = rs.getString( "go" );
			info.setGo(go);

			String get = rs.getString( "get" );
			info.setGet(get);

			String date = rs.getString( "date" );
			info.setDate(date);

			String depart_time = rs.getString( "depart_time" );
			info.setDepart_time(depart_time);
	
			String fly_time = rs.getString( "fly_time" );
			info.setFly_time(fly_time);

			String land_time = rs.getString( "land_time" );
			info.setLand_time(land_time);

			String depart_airport = rs.getString( "depart_airport" );
			info.setDepart_airport(depart_airport);
	
			String land_airport = rs.getString( "land_airport" );
			info.setLand_airport(land_airport);
						
			String condition = rs.getString( "condition" );
			info.setCondition(condition);
			
		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet()出错!\n" + e.getMessage()  );
			return false;
		}		
		return true;				
	}
	
	public boolean addClient( String account ,String password ,
			String sex, String phone ,int money){
		
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{
			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " insert into ").append( FlightInfo._tableName2 )
			    .append( " values (?, ?, ?, ?, ?) " );
			
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				//指定参数1、2：go、get的赋值!!!!!
     		st.setString(i, account);
     		i = 2;
     		st.setString(i, password);
     		i = 3;
     		st.setString(i, sex);
     		i = 4;
     		st.setString(i, phone);
     		i = 5;
     		st.setInt(i, money);
     		
     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();
     		
		}
		catch( SQLException e ){
			System.out.printf( "addClient失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "addClient失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;		
	}	
	
	//判断用户输入的密码是否正确
	public boolean whetherRegister(String account, String password){
		
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{
			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select password from ").append( FlightInfo._tableName2 )
			    .append( " where account_name = ? " );
			
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;			
     		st.setString(i, account);
      		
     		System.out.printf( "sql = %s\n" , st.toString());   		
     		//执行sql语句
     		ResultSet rs = st.executeQuery();
     		//先让rs指向下一个
     		rs.next();	
     		
     		//判断密码是否和数据库中的一致
     		if( getDataFromResultSet2(rs,password) )
     			return true;	//若一致 可以成功登录
     		else
     			return false;
		}
		catch( SQLException e ){
			System.out.printf( "whetherRegister失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "whetherRegister失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally	
	}
	
	//判断查询得到的密码和用户输入的密码是否一致。一致说明是用户本人在登录
	private  boolean getDataFromResultSet2( ResultSet rs ,String password){		
		try{				
			String get_password = rs.getString( "password" );
			if( get_password.equals(password) )
				return true;
			else
				return false;					

		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet2()出错!\n" + e.getMessage()  );
			return false;
		}				
	}
	
	//由航班号查询得到本次航班的具体信息
	public boolean getspecificFlight( ArrayList< FlightInfo > data, String flight_num ){
	
		data.clear();
		_conn = DBConnection.getConnection();				// 连接数据库
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select flight_num, go, `get`, date, depart_time, fly_time, " )
			    .append( " land_time, depart_airport, land_airport, " )
			    .append( " toudeng_yupiao, toudeng_jiage, jingji_yupiao, jingji_jiage ")
			    .append( " from ").append( FlightInfo._tableName )
			    .append( " where flight_num = ? ");
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		int i = 1;			
     		st.setString(i, flight_num);
     		
     		System.out.printf( "sql = %s\n" , st.toString());  		 		
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
	
			while( rs.next() ){
				FlightInfo info  = new FlightInfo();
				if( getDataFromResultSet3(rs, info) ){
					data.add( info );
				}			
			}			
		}
		catch( SQLException e ){
			System.out.printf( "getspecificFlight失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getspecificFlight失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if
			
		}// finally		
		return true;		
	}
	//数据库结果集的数据表中  获取数据。 显示在航班的详细信息。
	private  boolean getDataFromResultSet3( ResultSet rs, FlightInfo info){		
		try{				
			String flight_num = rs.getString( "flight_num" );
			info.setFlight_num(flight_num);

			String go = rs.getString( "go" );
			info.setGo(go);

			String get = rs.getString( "get" );
			info.setGet(get);

			String date = rs.getString( "date" );
			info.setDate(date);

			String depart_time = rs.getString( "depart_time" );
			info.setDepart_time(depart_time);
	
			String fly_time = rs.getString( "fly_time" );
			info.setFly_time(fly_time);

			String land_time = rs.getString( "land_time" );
			info.setLand_time(land_time);

			String depart_airport = rs.getString( "depart_airport" );
			info.setDepart_airport(depart_airport);
	
			String land_airport = rs.getString( "land_airport" );
			info.setLand_airport(land_airport);
			//头等舱 经济舱详细信息。
			int toudeng_yupiao = rs.getInt( "toudeng_yupiao" );
			info.setToudeng_piao(toudeng_yupiao);
			
			int toudeng_jiage = rs.getInt( "toudeng_jiage" );
			info.setToudeng_jia(toudeng_jiage);
			
			int jingji_yupiao = rs.getInt( "jingji_yupiao" );
			info.setJingji_piao(jingji_yupiao);
			
			int jingji_jiage = rs.getInt( "jingji_jiage" );
			info.setJingji_jia(jingji_jiage);
			
		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet3()出错!\n" + e.getMessage()  );
			return false;
		}		
		return true;				
	}
	
	//获取用户的当前余额
	public int getYuE( String account ){
		
		_conn = DBConnection.getConnection();				// 连接数据库
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select money from ")
				.append( FlightInfo._tableName2 )
			    .append( " where account_name = ? ");
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		int i = 1;			
     		st.setString(i, account);
     		
     		System.out.printf( "sql = %s\n" , st.toString());  		 		
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
			rs.next();	    		
     		int money = getDataFromResultSet4(rs);	//获取到用户的余额并返回
     		return money;
		}
		catch( SQLException e ){
			System.out.printf( "getYuE失败!\n" + e.getMessage()  );
			return 0;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getYuE失败了!!!!!\n" + e.getMessage()  );
					return 0;
				}// try
			}// if
			
		}// finally				
	}
	
	//解析出用户当前的余额
	private  int getDataFromResultSet4( ResultSet rs ){		
		try{				
			int money = rs.getInt( "money" );
			return money;			
		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet4()出错!\n" + e.getMessage()  );
			return 0;
		}			
	}
	
	//新增机票订单 total_price要转为int
	public boolean addOrder( String order_num, String account_name, String passenger, String sex, String birthday, 
			String nation, String idcard, String baoxian, String clas, String total_price, 
			String date, String go,String get, String fly_time, 
			String depart_airport, String land_airport, String flight_num,
			String depart_time, String land_time){
		
		_conn = DBConnection.getConnection();				// 连接数据库
		
		int total = Integer.parseInt(total_price);
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " insert into ").append( FlightInfo._tableName3 )
			    .append( " values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " );
			
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				//指定参数1、2：go、get的赋值!!!!!
     		st.setString(i, order_num);
     		i = 2;
     		st.setString(i, account_name);
     		i = 3;
     		st.setString(i, passenger);
     		i = 4;
     		st.setString(i, sex);
     		i = 5;
     		st.setString(i, birthday);
     		i = 6;
     		st.setString(i, nation);
     		i = 7;
     		st.setString(i, idcard);
     		i = 8;
     		st.setString(i, baoxian);
     		i = 9;
     		st.setString(i, clas);
     		i = 10;
     		st.setInt(i, total);
     		i = 11;
     		st.setString(i, date);
     		i = 12;
     		st.setString(i, go);
     		i = 13;
     		st.setString(i, get);
     		i = 14;
     		st.setString(i, fly_time);
     		i = 15;
     		st.setString(i, depart_airport);
     		i = 16;
     		st.setString(i, land_airport);
     		i = 17;
     		st.setString(i, flight_num);   
     		i = 18;
     		st.setString(i, depart_time);  
     		i = 19;
     		st.setString(i, land_time); 
     		i = 20;
     		st.setString(i, "0"); 
     		
     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();
     		
		}
		catch( SQLException e ){
			System.out.printf( "addOrder失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "addOrder失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;	
	}
	
	//给账号余额扣款
	public boolean buyTicket( String account, String total_price, int yuE ){
		_conn = DBConnection.getConnection();				// 连接数据库
		
		int total = Integer.parseInt(total_price);
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " update ").append( FlightInfo._tableName2 )
			    .append( " set money = ? where account_name = ? " );
			
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				//指定参数1、2：go、get的赋值!!!!!
     		st.setInt(i, yuE-total);
     		i = 2;
     		st.setString(i, account);

     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();
     		
		}
		catch( SQLException e ){
			System.out.printf( "buyTicket失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "buyTicket失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;			
	}
	
	//更新航班剩余票数
	public boolean updateTicket( String flight_num, String yupiao, String clas ){
		_conn = DBConnection.getConnection();				// 连接数据库
		
		int yu = Integer.parseInt(yupiao);

		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			
			if( clas.equals("经济舱") ){
				sBuffer.append( " update ").append( FlightInfo._tableName )
			    .append( " set jingji_yupiao = ? where flight_num = ? " );
			}
			else if( clas.equals("头等舱") ){
				sBuffer.append( " update ").append( FlightInfo._tableName )
			    .append( " set toudeng_yupiao = ? where flight_num = ? " );
			}
		
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				//指定参数1、2：go、get的赋值!!!!!
     		st.setInt(i, --yu);
     		i = 2;
     		st.setString(i, flight_num);

     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();
     		
		}
		catch( SQLException e ){
			System.out.printf( "updateTicket失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "updateTicket失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;			
	}
	
	//改签后，原航班机票数+1
	public boolean updateTicket_old_flight( String flight_num2, String clas_gaiqian ){
		_conn = DBConnection.getConnection();				// 连接数据库
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			
			if( clas_gaiqian.equals("经济舱") ){
				sBuffer.append( " update ").append( FlightInfo._tableName )
			    .append( " set jingji_yupiao = jingji_yupiao+1 where flight_num = ? " );
			}
			else if( clas_gaiqian.equals("头等舱") ){
				sBuffer.append( " update ").append( FlightInfo._tableName )
			    .append( " set toudeng_yupiao = toudeng_yupiao+1 where flight_num = ? " );
			}
		
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				//指定参数1、2：go、get的赋值!!!!
     		st.setString(i, flight_num2);

     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();
     		
		}
		catch( SQLException e ){
			System.out.printf( "updateTicket_old_flight失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "updateTicket_old_flight失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;		
	}
	
	//根据用户名查找我的订单
	public boolean getMyOrder( ArrayList< FlightInfo > data, String account ){

		data.clear();		
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select go, `get`, total_price, date, depart_time, " )
			    .append( " land_time, flight_num, order_num, refund " )
			    .append( " from ").append( FlightInfo._tableName3 )
			    .append( " where" ).append( " account_name = ? " );
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				//指定参数1、2：go、get的赋值!!!!!
     		st.setString(i, account);
     		
     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
	
			while( rs.next() ){
				FlightInfo info  = new FlightInfo();
				if( getDataFromResultSet5(rs, info) ){
					data.add( info );
				}			
			}			
		}		
		catch( SQLException e ){
			System.out.printf( "getMyOrder失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getMyOrder失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;		
	}
	//数据库结果集的数据表中  获取数据 显示在ListView的订单信息
	private  boolean getDataFromResultSet5( ResultSet rs, FlightInfo info){		
		try{	
			String order_num = rs.getString( "order_num" );
			info.setOrder_num(order_num);;
			
			String go = rs.getString( "go" );
			info.setGo(go);;

			String get = rs.getString( "get" );
			info.setGet(get);

			int total_price = rs.getInt( "total_price" );
			info.setTotal_price(total_price);;

			String date = rs.getString( "date" );
			info.setDate(date);

			String depart_time = rs.getString( "depart_time" );
			info.setDepart_time(depart_time);

			String land_time = rs.getString( "land_time" );
			info.setLand_time(land_time);

			String flight_num = rs.getString( "flight_num" );
			info.setFlight_num(flight_num);
			
			String refund = rs.getString( "refund" );
			info.setRefund(refund);
			
		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet5()出错!\n" + e.getMessage()  );
			return false;
		}		
		return true;				
	}
	
	//根据航班号、用户名读取订单中的详细信息
	public boolean getspecificOrder( ArrayList< FlightInfo > data, 
			String account, String flight_num){
		
		System.out.printf( "account = %s, flight = %s\n" ,account,flight_num );  	
		
		data.clear();
		_conn = DBConnection.getConnection();				// 连接数据库
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select order_num, date, go, `get`, depart_time, fly_time, " )
			    .append( " land_time, depart_airport, land_airport, " )
			    .append( " class, flight_num, passenger, idcard, nation, ")
			    .append( " total_price, baoxian " )
			    .append( " from ").append( FlightInfo._tableName3 )
			    .append( " where" ).append( " account_name=? and flight_num=? " );
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		int i = 1;			
     		st.setString(i, account);
     		i = 2;
     		st.setString(i, flight_num);
     		
     		System.out.printf( "sql = %s\n" , st.toString());  		 		
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
	
			while( rs.next() ){
				FlightInfo info  = new FlightInfo();
				if( getDataFromResultSet6(rs, info) ){
					data.add( info );
				}			
			}			
		}
		catch( SQLException e ){
			System.out.printf( "getspecificOrder6失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getspecificOrder失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if
			
		}// finally		
		return true;
	}
	//数据库结果集的数据表中  获取数据。 显示在订单的详细信息。
	private  boolean getDataFromResultSet6( ResultSet rs, FlightInfo info){		
		try{			
			String order_num = rs.getString( "order_num" );
			info.setOrder_num(order_num);;
		
			String date = rs.getString( "date" );
			info.setDate(date);

			String go = rs.getString( "go" );
			info.setGo(go);

			String get = rs.getString( "get" );
			info.setGet(get);

			String depart_time = rs.getString( "depart_time" );
			info.setDepart_time(depart_time);
	
			String fly_time = rs.getString( "fly_time" );
			info.setFly_time(fly_time);

			String land_time = rs.getString( "land_time" );
			info.setLand_time(land_time);

			String depart_airport = rs.getString( "depart_airport" );
			info.setDepart_airport(depart_airport);
	
			String land_airport = rs.getString( "land_airport" );
			info.setLand_airport(land_airport);
			
			String clas = rs.getString( "class" );
			info.setClas(clas);
			
			String flight_num = rs.getString( "flight_num" );
			info.setFlight_num(flight_num);

			String passenger = rs.getString( "passenger" );
			info.setPassenger(passenger);
			
			String passport = rs.getString( "idcard" );
			info.setPassport(passport);
			
			String nation = rs.getString( "nation" );
			info.setNation(nation);
						
			int total_price = rs.getInt( "total_price" );
			info.setTotal_price(total_price);
			
			String baoxian = rs.getString( "baoxian" );
			info.setBaoxian(baoxian);
			
		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet6()出错!\n" + e.getMessage()  );
			return false;
		}		
		return true;				
	}
	
	//改签后，删除原订单机票
	public boolean delete_old_ticket( String order_num2){
		_conn = DBConnection.getConnection();				// 连接数据库
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
	
			sBuffer.append( " delete from ").append( FlightInfo._tableName3 )
			    .append( " where order_num = ?" );

		
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;				
     		st.setString(i, order_num2);

     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();
     		
		}
		catch( SQLException e ){
			System.out.printf( "delete_old_ticket失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "delete_old_ticket失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;
	}
	
	public boolean addFlight( String a, String b, String c, String d, 
			String ee, String f, String g, 
			String h, String ii, String j, String k ,String l, String m ){
		
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " insert into ").append( FlightInfo._tableName )
			    .append( " values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " );
			
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
			int i = 1;				//指定参数1、2：go、get的赋值!!!!!
     		st.setString(i, a);
     		i = 2;
     		st.setString(i, b);
     		i = 3;
     		st.setString(i, c);
     		i = 4;
     		st.setString(i, d);
     		i = 5;
     		st.setString(i, ee);
     		i = 6;
     		st.setString(i, f);
     		i = 7;
     		st.setString(i, g);
     		i = 8;
     		st.setString(i, h);
     		i = 9;
     		st.setString(i, ii);
     		i = 10;
     		st.setString(i, j);
     		i = 11;
     		st.setString(i, k);
     		i = 12;
     		st.setString(i, l);
     		i = 13;
     		st.setString(i, m);
     		
     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();
     		
		}
		catch( SQLException e ){
			System.out.printf( "addFlight失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "addFlight失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;		
	}
	
	//提交用户退票申请
	public boolean RefundRequest( String order_num ){
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " insert into ").append( FlightInfo._tableName4 )
			    .append( " values ( ? ) " );
			
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
			int i = 1;				
     		st.setString(i, order_num);
			
     		
			StringBuffer sBuffer2 = new StringBuffer();		//存放sql语句
			sBuffer2.append( " update ").append( FlightInfo._tableName3 )
			    .append( " set refund = '1' where order_num = ? " );
			
			PreparedStatement st2 = _conn.prepareStatement(  sBuffer2.toString() );    					
     		st2.setString(i, order_num);
 
     		
     		System.out.printf( "sql = %s\n" , st.toString());
     		System.out.printf( "sql = %s\n" , st2.toString());     		
     		//执行sql语句
     		st.execute();
     		st2.execute();    		
		}
		catch( SQLException e ){
			System.out.printf( "RefundRequest失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "RefundRequest失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;		
	}
	
	//获得申请退款的订单号
	public boolean getRefundOrder_Num( ArrayList< String > data ){
		data.clear();		
		_conn = DBConnection.getConnection();// 连接数据库
		
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select order_num from ").append( FlightInfo._tableName4 );
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
	
			while( rs.next() ){
				data.add( getDataFromResultSet7( rs ) );
			}			
		}
		catch( SQLException e ){
			System.out.printf( "getRefundOrder失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getRefundOrder失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if
			
		}// finally		
		return true;
	}
	//数据库结果集的数据表中  获取数据 显示在ListView的航班信息
	private  String getDataFromResultSet7( ResultSet rs ){		
		try{				
			String order_num = rs.getString( "order_num" );
			return order_num;
		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet7()出错!\n" + e.getMessage()  );
			return "";
		}					
	}
	
	//获取退票订单的信息
	public boolean getRefundOrder( String order_num, ArrayList< FlightInfo > data ){
		
		_conn = DBConnection.getConnection();				// 连接数据库
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select order_num, go, `get`, date, flight_num from ")
				.append( FlightInfo._tableName3 )
				.append( " where order_num = ?" );
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		int i = 1;			
     		st.setString(i, order_num);
     		
     		System.out.printf( "sql = %s\n" , st.toString());  		 		
     		   		
			//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );	
			
			while( rs.next() ){
				FlightInfo info  = new FlightInfo();
				if( getDataFromResultSet8(rs, info) ){
					data.add( info );
				}			
			}		
		}
		catch( SQLException e ){
			System.out.printf( "getRefundOrder失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getRefundOrder失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if
			
		}// finally	
		return true;
	}
	private  boolean getDataFromResultSet8( ResultSet rs, FlightInfo info){		
		try{				
			String order_num = rs.getString( "order_num" );
			info.setOrder_num(order_num);
			
			String go = rs.getString( "go" );
			info.setGo(go);

			String get = rs.getString( "get" );
			info.setGet(get);

			String date = rs.getString( "date" );
			info.setDate(date);

			String flight_num = rs.getString( "flight_num" );
			info.setFlight_num(flight_num);

		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet8()出错!\n" + e.getMessage()  );
			return false;
		}		
		return true;				
	}
	
	public FlightInfo getspecificRefund(String order_num){
		
		FlightInfo info  = new FlightInfo();
		_conn = DBConnection.getConnection();				// 连接数据库
		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句
			sBuffer.append( " select date, go, `get`, depart_time, fly_time, " )
			    .append( " land_time, depart_airport, land_airport, " )
			    .append( " class, flight_num, account_name, passenger, idcard, nation, ")
			    .append( " total_price, baoxian " )
			    .append( " from ").append( FlightInfo._tableName3 )
			    .append( " where" ).append( " order_num=? " );
			
			//PreparedStatement 实例包含已编译的 SQL 语句。这就是使语句“准备好”
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		int i = 1;			
     		st.setString(i, order_num);
     		
     		System.out.printf( "sql = %s\n" , st.toString());  		 		
     		
     		//数据库结果集的数据表，通常通过执行查询数据库的语句生成。
			ResultSet rs = st.executeQuery(  );
			rs.next();
	
			getDataFromResultSet9(rs, info);
		
		}
		catch( SQLException e ){
			System.out.printf( "getspecificRefund失败!\n" + e.getMessage()  );
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "getspecificRefund失败了!!!!!\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally		
		return info;
	}
	private  boolean getDataFromResultSet9( ResultSet rs, FlightInfo info){		
		try{
			String date = rs.getString( "date" );
			info.setDate(date);

			String go = rs.getString( "go" );
			info.setGo(go);

			String get = rs.getString( "get" );
			info.setGet(get);

			String depart_time = rs.getString( "depart_time" );
			info.setDepart_time(depart_time);
	
			String fly_time = rs.getString( "fly_time" );
			info.setFly_time(fly_time);

			String land_time = rs.getString( "land_time" );
			info.setLand_time(land_time);

			String depart_airport = rs.getString( "depart_airport" );
			info.setDepart_airport(depart_airport);
	
			String land_airport = rs.getString( "land_airport" );
			info.setLand_airport(land_airport);
			
			String clas = rs.getString( "class" );
			info.setClas(clas);
			
			String flight_num = rs.getString( "flight_num" );
			info.setFlight_num(flight_num);

			String account_name = rs.getString( "account_name" );
			info.setAccount_name(account_name);;
			
			String passenger = rs.getString( "passenger" );
			info.setPassenger(passenger);
			
			String passport = rs.getString( "idcard" );
			info.setPassport(passport);
			
			String nation = rs.getString( "nation" );
			info.setNation(nation);
						
			int total_price = rs.getInt( "total_price" );
			info.setTotal_price(total_price);
			
			String baoxian = rs.getString( "baoxian" );
			info.setBaoxian(baoxian);
			
		}catch( SQLException e ){
			System.out.printf( "getDataFromResultSet8()出错!\n" + e.getMessage()  );
			return false;
		}		
		return true;				
	}
	
	//一系列管理员取消订单的操作
	public boolean AgreeRefund( String order_num, String account, 
			String money, String clas, String flight_num){
		
		_conn = DBConnection.getConnection();				// 连接数据库
		try{			
			StringBuffer sBuffer = new StringBuffer();	
			StringBuffer sBuffer2 = new StringBuffer();	
			StringBuffer sBuffer3 = new StringBuffer();	
			StringBuffer sBuffer4 = new StringBuffer();	
			
			//退款给用户！
			sBuffer.append( " update " ).append( FlightInfo._tableName2 )
				.append( " set money = money + ? where account_name = ?" );
			
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		int i = 1;			
     		st.setString(i, money);
     		i = 2;
     		st.setString(i, account);
     		
     		System.out.printf( "sql = %s\n" , st.toString());  	
     		st.execute(  );
     		
     		i = 1;
     		
     		//原机票余票数+1
     		if( clas.equals("头等舱") ){
	     		sBuffer2.append( " update " )
				.append( FlightInfo._tableName )
				.append( " set toudeng_yupiao = toudeng_yupiao+1 where flight_num = ?" );
     		}
     		else if( clas.equals("经济舱") ){
     			sBuffer2.append( " update " )
				.append( FlightInfo._tableName )
				.append( " set jingji_yupiao = jingji_yupiao+1 where flight_num = ?" );
     		}
		
	 		PreparedStatement st2 = _conn.prepareStatement(  sBuffer2.toString() );	
	 		st2.setString(i, flight_num);
 		
	 		System.out.printf( "sql = %s\n" , st2.toString());  	
	 		st2.execute(  );
	 		
	 		//删除refund中的order_num
			sBuffer3.append( " delete from " ).append( FlightInfo._tableName4 )
				.append( " where order_num = ?" );
			
     		PreparedStatement st3 = _conn.prepareStatement(  sBuffer3.toString() );		
     		st3.setString(i, order_num);
     		
     		System.out.printf( "sql = %s\n" , st3.toString());  	
     		st3.execute(  );
	 		
     		//修改order中对应的refund信息
     		sBuffer4.append( " update " ).append( FlightInfo._tableName3 )
			.append( " set refund = '2' where order_num = ?" );
		
	 		PreparedStatement st4 = _conn.prepareStatement(  sBuffer4.toString() );		
	 		st4.setString(i, order_num);
	 		
	 		System.out.printf( "sql = %s\n" , st.toString());  	
	 		st4.execute(  );
 		
		}
		catch( SQLException e ){
			System.out.printf( "AgreeRefund失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "AgreeRefund失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if
			
		}// finally	
		return true;
	}
	
	//管理员暂停本次航班售票
	public boolean stopFlight( String flight_num, String present ){
		_conn = DBConnection.getConnection();				// 连接数据库

		try{			
			StringBuffer sBuffer = new StringBuffer();		//存放sql语句

			if( present.equals("1") ){
				sBuffer.append( " update ").append( FlightInfo._tableName )
				    .append( " set `condition` = '1' where `flight_num` = ? " );
			}
			else if( present.equals("0") ){
				sBuffer.append( " update ").append( FlightInfo._tableName )
				    .append( " set `condition` = '0' where `flight_num` = ? " );
			}
			
			PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		
     		int i = 1;			
     		st.setString(i, flight_num);

     		System.out.printf( "sql = %s\n" , st.toString());
     		
     		//执行sql语句
     		st.execute();	     		
		}
		catch( SQLException e ){
			System.out.printf( "stopFlight失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "stopFlight失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if			
		}// finally			
		return true;			
	}
	
	//拒绝用户退票请求
	public boolean disagreeRefund( String order_num ){
		_conn = DBConnection.getConnection();				// 连接数据库
		try{			
			StringBuffer sBuffer = new StringBuffer();	
			
			sBuffer.append( " update " ).append( FlightInfo._tableName3 )
				.append( " set refund = '3' where order_num = ?" );
			
     		PreparedStatement st = _conn.prepareStatement(  sBuffer.toString() );
     		int i = 1;			
     		st.setString(i, order_num);
     			
     		System.out.printf( "sql = %s\n" , st.toString());  	
     		st.execute(  ); 		
		}
		catch( SQLException e ){
			System.out.printf( "disagreeRefund失败!\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( _conn != null ){
				try{
					_conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "disagreeRefund失败了!!!!!\n" + e.getMessage()  );
					return false;
				}// try
			}// if
			
		}// finally	
		return true;
	}
}
