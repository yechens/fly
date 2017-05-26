<%@page import="pc.*"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();
	String account = request.getParameter("account");
	String password = request.getParameter("password");
	String sex = request.getParameter("sex");
	String phone = request.getParameter("phone");
	boolean result = bean.addClient(account, password, sex, phone, 2000);	//新账户默认赠送2000元
	if(result == true){
		System.out.printf("新增账户成功!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
    	out.println( "<result>" + "succeessful" + "</result>" );
    	out.println( "</data>" );	   
	}
	else{
		System.out.printf("新增账户失败!\n");
		out.println( "<data>" );		//<data>标志不能1省略！！！
    	out.println( "<result>" + "failed" + "</result>" );
    	out.println( "</data>" );	
	}
%>