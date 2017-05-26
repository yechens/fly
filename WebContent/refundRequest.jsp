<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();

	String order_num = request.getParameter("order_num");
	
	boolean result = bean.RefundRequest( order_num );
	if( result==true ){
		System.out.printf("删除订单申请成功!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	}
	else{
		System.out.printf("删除订单申请失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "falied" + "</result>" );
		out.println( "</data>" );
	}
	
%>