<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();

    String order_num = request.getParameter("order_num");
	boolean result = bean.disagreeRefund( order_num );
	
	if( result==true ){
		System.out.printf("您已成功拒绝该用户退票!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	}
	else{
		System.out.printf("操作失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "failed" + "</result>" );
		out.println( "</data>" );	   
	}
%>