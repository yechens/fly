<%@page import="pc.*"%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();
	ArrayList< FlightInfo > data = new ArrayList< FlightInfo >();
	
	String account = request.getParameter("account");
	
	boolean result = bean.getMyOrder( data, account );	//根据用户名查找订单
	
	Iterator< FlightInfo > iter = data.iterator();
	iter = data.listIterator();
	while( iter.hasNext() ){
		System.out.printf("%s\n", iter.next().toString());
	}
	
	iter = data.iterator();
	while( iter.hasNext() ){
		
		FlightInfo info = iter.next();
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<order_num>" + info.getOrder_num() + "</order_num>" );
		
		out.println( "<go>" + info.getGo() + "</go>" );
	
		out.println( "<get>" + info.getGet() + "</get>" );
		
		out.println( "<total_price>" + info.getTotal_price() + "</total_price>" );
	
	    out.println( "<date>" + info.getDate() + "</date>" );
	
	    out.println( "<depart_time>" + info.getDepart_time() + "</depart_time>" );
	
	    out.println( "<land_time>" + info.getLand_time() + "</land_time>" );
	
	    out.println( "<flight_num>" + info.getFlight_num()  + "</flight_num>" );
	    
	    out.println( "<refund>" + info.getRefund()  + "</refund>" );
		out.println( "</data>" );	   
	}
%>