<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();

	String flight_num = request.getParameter("flight_num");
	String pre = request.getParameter("pre");
	System.out.println(flight_num);
	System.out.println(pre);
	
	boolean result = bean.stopFlight(flight_num, pre);
	
	if( result==true && pre.equals("1") ){
		System.out.printf("本航班已停止售票!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful1" + "</result>" );
		out.println( "</data>" );	   
	}
	else if( result==true && pre.equals("0") ){
		System.out.printf("本航班已恢复售票!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful2" + "</result>" );
		out.println( "</data>" );	   
	}
	else{
		System.out.printf("对不起，操作失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "failed" + "</result>" );
		out.println( "</data>" );	   
	}

%>	