<%@page import="pc.*"%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	ArrayList< FlightInfo > data = new ArrayList< FlightInfo >();	
	boolean result2;

	FlightBean bean = new FlightBean();
	//存放退票订单
	ArrayList< String > orderList = new ArrayList< String >();
	boolean result = bean.getRefundOrder_Num( orderList );
	
	Iterator< String > iter2 = orderList.iterator();
    iter2 = orderList.listIterator();
    
	while( iter2.hasNext() ){
		String temp = iter2.next().toString();
		System.out.printf("%s\n", temp );
		
		//由退单号获得退单的详细信息
		result2 = bean.getRefundOrder( temp, data );
	}

	Iterator< FlightInfo > iter = data.iterator();
	iter = data.iterator();
	
	while( iter.hasNext() ){   	
    	FlightInfo info = iter.next();
    	out.println( "<data>" );		//<data>标志不能1省略！！！
	    out.println( "<order_num>" + info.getOrder_num() + "</order_num>" );

    	out.println( "<go>" + info.getGo() + "</go>" );

        out.println( "<get>" + info.getGet() + "</get>" );

	    out.println( "<date>" + info.getDate() + "</date>" );
	    
    	out.println( "<flight_num>" + info.getFlight_num() + "</flight_num>" );
    	out.println( "</data>" );	   
    }
%>