<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();
	//接收新增航线的13个参数
	String a = request.getParameter("flight_num");
	String b = request.getParameter("go");
	String c = request.getParameter("get");
	String d = request.getParameter("date");
	String ee = request.getParameter("depart_time");
	String f = request.getParameter("fly_time");
	String g = request.getParameter("land_time");
	String h = request.getParameter("depart_airport");
	String ii = request.getParameter("land_airport");
	String j = request.getParameter("toudeng_yupiao");
	String k = request.getParameter("toudeng_jiage");
	String l = request.getParameter("jingji_yupiao");
	String m = request.getParameter("jingji_jiage");
	
	boolean result = bean.addFlight(a, b, c, d, ee, f, g, h, ii, j, k, l, m);
	if( result==true ){
		System.out.printf("新增航线成功!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	}
	else{
		System.out.printf("新增航线失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "falied" + "</result>" );
		out.println( "</data>" );
	}
%>