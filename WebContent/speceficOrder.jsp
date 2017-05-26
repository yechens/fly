<%@page import="pc.*"%>
<%@page import ="java.util.ArrayList" %>
<%@page import="java.util.Iterator"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();
	ArrayList< FlightInfo > data = new ArrayList< FlightInfo >();
	
	String account = request.getParameter("account");
	String flight_num = request.getParameter("flight_num");
	boolean result = bean.getspecificOrder( data, account, flight_num );
	
	Iterator< FlightInfo > iter = data.iterator();
	iter = data.iterator();
	while( iter.hasNext() ){
		
		FlightInfo info = iter.next();
		out.println( "<data>" );		//<data>标志不能1省略！！！
		out.println( "<order_num>" + info.getOrder_num() + "</order_num>" );

		out.println( "<date>" + info.getDate() + "</date>" );
	
		out.println( "<go>" + info.getGo() + "</go>" );
	
	    out.println( "<get>" + info.getGet() + "</get>" );
	
	    out.println( "<depart_time>" + info.getDepart_time() + "</depart_time>" );
	
	    out.println( "<fly_time>" + info.getFly_time() + "</fly_time>" );
	
	    out.println( "<land_time>" + info.getLand_time() + "</land_time>" );
	
	    out.println( "<depart_airport>" + info.getDepart_airport()  + "</depart_airport>" );
	
	    out.println( "<land_airport>" + info.getLand_airport() + "</land_airport>" );
	    
	    out.println( "<clas>" + info.getClas() + "</clas>" );
	    
	    out.println( "<flight_num>" + info.getFlight_num() + "</flight_num>" );
	    
	    out.println( "<passenger>" + info.getPassenger() + "</passenger>" );
	    
	    out.println( "<passport>" + info.getPassport() + "</passport>" );
	    
	    out.println( "<nation>" + info.getNation() + "</nation>" );
	    
	    out.println( "<money>" + info.getTotal_price() + "</money>" );
	    
	    out.println( "<other>" + info.getBaoxian() + "</other>" );
		out.println( "</data>" );	   
	}
%>