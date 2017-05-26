
<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();
	String account = request.getParameter("account");
	int money = bean.getYuE(account);
	if(money>=0){
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<money>" + money + "</money>" );
		out.println( "</data>" );
	}
%>
