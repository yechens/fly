<%@page import="pc.*"%>
<%@page import ="java.util.ArrayList" %>
<%@page import="java.util.Iterator"%>
<%@ page contentType="text/html; charset=utf-8" %>

<%
	FlightBean bean = new FlightBean();
	ArrayList< FlightInfo > data = new ArrayList< FlightInfo >();
	
	String flight_num = request.getParameter("flight_num");
	boolean result = bean.getspecificFlight(data, flight_num);
	
	Iterator< FlightInfo > iter = data.iterator();
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
	    
	    out.println( "<toudeng_yupiao>" + info.getToudeng_piao() + "</toudeng_yupiao>" );
	    
	    out.println( "<toudeng_jiage>" + info.getToudeng_jia() + "</toudeng_jiage>" );
	    
	    out.println( "<jingji_yupiao>" + info.getJingji_piao() + "</jingji_yupiao>" );
	    
	    out.println( "<jingji_jiage>" + info.getJingji_jia() + "</jingji_jiage>" );
		out.println( "</data>" );	   
	}
%>