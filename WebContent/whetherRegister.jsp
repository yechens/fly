<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();
	String account = request.getParameter("account").trim();
	String password = request.getParameter("password").trim();
	boolean result = bean.whetherRegister(account, password);	//判断密码和后台的是否一致
	if(result == true){
		System.out.printf("密码成功匹配!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "succeessful" + "</result>" );
		out.println( "</data>" );	   
	}
	else{
		System.out.printf("密码匹配失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<result>" + "failed" + "</result>" );
		out.println( "</data>" );	
	}
%>