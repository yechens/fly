<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();

	String order_num = request.getParameter("order_num");
	String account = request.getParameter("account");
	String money = request.getParameter("money");
	String clas = request.getParameter("clas");
	String flight_num = request.getParameter("flight_num");
	
	//退款给用户、原机票数+1、删除refund中的order_num、修改order中对应的refund信息
	//对应4张表都需要修改！
	boolean result = bean.AgreeRefund(order_num, account, money, clas, flight_num);
	
	if( result==true ){
		System.out.printf("您已成功为该用户退票!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	}
	else{
		System.out.printf("对不起，退票失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "failed" + "</result>" );
		out.println( "</data>" );	   
	}
%>