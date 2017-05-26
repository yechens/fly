<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>

<%
	FlightBean bean = new FlightBean();
	//接收订单表中共16个参数+余额+余票
	String account_name = request.getParameter("account_name");
	String passenger = request.getParameter("passenger");
	String sex = request.getParameter("sex");
	String birthday = request.getParameter("birthday");
	String nation = request.getParameter("nation");
	String idcard = request.getParameter("idcard");
	String baoxian = request.getParameter("baoxian");
	String clas = request.getParameter("clas");
	String total_price = request.getParameter("total_price");
	String date = request.getParameter("date");
	String go = request.getParameter("go");
	String get = request.getParameter("get");
	String fly_time = request.getParameter("fly_time");
	String depart_airport = request.getParameter("depart_airport");
	String land_airport = request.getParameter("land_airport");
	String flight_num = request.getParameter("flight_num");
	String yuE = request.getParameter("yuE");
	String yupiao = request.getParameter("yupiao");
	String depart_time = request.getParameter("depart_time");
	String land_time = request.getParameter("land_time");
	
	String gaiqian = request.getParameter("gaiqian");
	String yuanpiaojia = request.getParameter("yuanpiaojia");
	String flight_num2 = request.getParameter("flight_num_gai2");
	String clas_gaiqian = request.getParameter("clas_gaiqian");
	String order_num2 = request.getParameter("order_num");	//改签时的原机票订单
	
	//根据系统当前时间生成一个唯一的订单号
	Date now_date = new Date();
	SimpleDateFormat from = new SimpleDateFormat("yyyyMMddHHmmss");  //订单号共14位数
	String order_num = from.format(now_date);
	System.out.println(order_num);
	
	//新增机票订单
	boolean result = bean.addOrder(order_num, account_name, passenger, sex, birthday, 
			nation, idcard, baoxian, clas, total_price, date,go, get, 
			fly_time, depart_airport, land_airport, flight_num, depart_time, land_time);
	
	//成功订票，给用户余额扣款
	boolean result2 = bean.buyTicket( account_name, total_price, 
			Integer.parseInt(yuE) + Integer.parseInt(yuanpiaojia));
	
	//更新航班剩余机票数目
	boolean result3 = bean.updateTicket( flight_num, yupiao, clas);
	
	boolean result4 = false, result5 = false;
	if( gaiqian.equals("yes") ){
		//若改签，原航班机票数目修改，+1
		result4 = bean.updateTicket_old_flight(flight_num2, clas_gaiqian);
		
		//若改签，原机票将被删除
		result5 = bean.delete_old_ticket(order_num2);
	}
	
	if( result==true && result2==true && result3==true ){
		System.out.printf("新增机票订单成功!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	}
	else if( result==true && result2==true && result3==true && result4==true && result5==true ){
		System.out.printf("恭喜您改签成功!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	}
	
	else if(result==true && result2==true && result3==true && (result4==false || result5==false) ){
		System.out.printf("对不起，改签失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	} 
	
	else{
		System.out.printf("新增机票订单失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "failed" + "</result>" );
		out.println( "</data>" );	
	}
%>