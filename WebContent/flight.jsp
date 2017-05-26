
<%@page import="pc.*"%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=utf-8" %> 

<%
	FlightBean bean = new FlightBean();
	ArrayList< FlightInfo > data = new ArrayList< FlightInfo >();
	boolean result = bean.getAll(data);	//查询所有航线
	
	Iterator< FlightInfo > iter = data.iterator();
    iter = data.listIterator();
	while( iter.hasNext() ){
		System.out.printf("%s\n", iter.next().toString());
	}
	
	
	iter = data.iterator();
    while( iter.hasNext() ){
    	
    	FlightInfo info = iter.next();
    	out.println( "<data>" );		//<data>标志不能1省略！！！
    	out.println( "<flight_num>" + info.getFlight_num() + "</flight_num>" );

    	out.println( "<go>" + info.getGo() + "</go>" );

        out.println( "<get>" + info.getGet() + "</get>" );

	    out.println( "<date>" + info.getDate() + "</date>" );

	    out.println( "<depart_time>" + info.getDepart_time() + "</depart_time>" );

	    out.println( "<fly_time>" + info.getFly_time() + "</fly_time>" );

	    out.println( "<land_time>" + info.getLand_time() + "</land_time>" );
	
	    out.println( "<depart_airport>" + info.getDepart_airport()  + "</depart_airport>" );

	    out.println( "<land_airport>" + info.getLand_airport() + "</land_airport>" );
    	out.println( "</data>" );	   
    }
%>