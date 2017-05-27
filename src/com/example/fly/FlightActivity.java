package com.example.fly;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FlightActivity extends Activity {
	
	// 机票显示列表
	ArrayList< FlightInfo > _data;
	ListView _listFlight;
	String account, qidian, zhongdian, yuE;
	String flight_num, condition;
	//改签时接收
	String flight_num2, gaiqian, yuanpiaojia, clas_gaiqian, order_num;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flight);
		
		Intent intent = getIntent();	//接收传递过来的账号、起点、终点、余额
		
		yuE = intent.getStringExtra("yuE");
		
		account = intent.getStringExtra("account");
		qidian = intent.getStringExtra("qidian");
		zhongdian = intent.getStringExtra("zhongdian");
		
		//改签时接收一下信息
		flight_num2 = intent.getStringExtra("flight_num");
		gaiqian = intent.getStringExtra("gaiqian");
		yuanpiaojia = intent.getStringExtra("yuanpiaojia");
		clas_gaiqian = intent.getStringExtra("clas_gaiqian");
		order_num = intent.getStringExtra("order_num");
		
		_data = new ArrayList< FlightInfo >();
		_listFlight = (ListView)findViewById(R.id.listFlight);
//		readAll();										//显示所有航线
		readByClient(qidian,zhongdian);					//按照用户输入的起点终点查找航线
		
		//判断用户点击哪一个子项
		_listFlight.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				FlightInfo flightInfo = _data.get(position);		
				flight_num = flightInfo.getFlight_num();	//获取当前点击子项的航班号
				condition = flightInfo.getCondition();
				
				Intent intent = new Intent(FlightActivity.this,Specific_Activity.class);
				

				intent.putExtra("account", account);		//向下一个界面传递信息
				intent.putExtra("flight_num", flight_num);	//传递余额、用户名、航班号。航班号是主键 唯一值				
				intent.putExtra("yuE", yuE);
				intent.putExtra("condition", condition);
				
				intent.putExtra("gaiqian", gaiqian);
				if(gaiqian.equals("yes")){
					intent.putExtra("flight_num2", flight_num2);		//原机票航班号
					intent.putExtra("yuanpiaojia", yuanpiaojia);		//原机票票价	
					intent.putExtra("clas_gaiqian", clas_gaiqian);		//原机票舱位等级
					intent.putExtra("order_num", order_num);			//原机票订单号
				}
				startActivity(intent);		
				finish();
			}       	
        });
		//重新查找按钮
		Button back = (Button)findViewById(R.id.back4);
		back.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( account.equals("andy") ){
					Intent intent = new Intent(FlightActivity.this,M_Search_Activity.class);
					startActivity(intent);	
					finish();
				}
				else{
					Intent intent = new Intent(FlightActivity.this,SearchActivity.class);
					intent.putExtra("account", account);
					startActivity(intent);	
					finish();
				}
			}
		});
	}
	
	private Handler _handler = new Handler(){
		
		public void handleMessage( Message msg ){
				switch ( msg.what ) {
				case 1:
					String response = ( String )msg.obj;
					_data.clear();
					parserXml( response );
					Toast.makeText(
							getApplicationContext(), 
							String.format("All 共有%d条记录", _data.size()), 
							Toast.LENGTH_SHORT).show();
					FlightAdapter adapter = new FlightAdapter(
							FlightActivity.this,
							R.layout.flight_item, 
							_data);
					_listFlight.setAdapter(adapter);
					break;		
				case 2:
					String response2 = ( String )msg.obj;
					_data.clear();
					parserXml( response2 );
					FlightAdapter adapter2 = new FlightAdapter(
							FlightActivity.this,
							R.layout.flight_item, 
							_data);
					_listFlight.setAdapter(adapter2);
					break;
				default:
					break;	
					
				}
		}
	};
	
	private void parserXml( String xmlData ){
		String result = "";
		try{
			XmlPullParserFactory factory = 
					XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser();	//生成解析器
			parse.setInput( new StringReader( xmlData ) );	//添加xml数据
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);			
			
			int imageId = R.drawable.fei;	//图片资源固定
			String flight_num = "";
			String go = "";
			String jiantou = "——";		//箭头显示也是固定
			String get = "";
			
			String date = "";
			String depart_time = "";
			String fly_time = "";
			String land_time = "";
			String depart_airport = "";
			String land_airport = "";
			String condition = "";
			
			while( eventType != XmlPullParser.END_DOCUMENT ){
				
				String nodeName = parse.getName();
				result += nodeName;
				result += ", ";
				
				switch (eventType) {
				
					case XmlPullParser.START_TAG:
						
						//从数据库读取9个参数
						if( "flight_num".equals( nodeName ) ){
							String flight_numStr = parse.nextText();
							result += "航班号为：" + flight_numStr + ", ";
							flight_num = flight_numStr;
							
						}
						else if( "go".equals( nodeName )  ){
							String goStr = parse.nextText();
							result += "起点为" + goStr + ", ";
							go = goStr;
						}
						else if( "get".equals( nodeName )  ){
							String getStr = parse.nextText();
							result += "终点为" + getStr + ", ";
							get = getStr;
						}
						else if( "date".equals( nodeName )  ){
							String dateStr = parse.nextText();
							result += "出发日期为" + dateStr + ", ";
							date = dateStr;
						}
						else if( "depart_time".equals( nodeName )  ){
							String depart_timeStr = parse.nextText();
							result += "起飞时间为" + depart_timeStr + ", ";
							depart_time = depart_timeStr;
						}
						else if( "fly_time".equals( nodeName )  ){
							String fly_timeStr = parse.nextText();
							result += "飞行时间为" + fly_timeStr + ", ";
							fly_time = fly_timeStr;
						}
						else if( "land_time".equals( nodeName )  ){
							String land_timeStr = parse.nextText();
							result += "到达时间为" + land_timeStr + ", ";
							land_time = land_timeStr;
						}
						else if( "depart_airport".equals( nodeName )  ){
							String depart_airportStr = parse.nextText();
							result += "起飞机场为" + depart_airportStr + ", ";
							depart_airport = depart_airportStr;
						}					
						else if( "land_airport".equals( nodeName )  ){
							String land_airportStr = parse.nextText();
							result += "降落机场为" + land_airportStr + ", ";
							land_airport = land_airportStr;
						}
						else if( "condition".equals( nodeName )  ){
							String conditionStr = parse.nextText();
							result += "是否正常售票" + conditionStr + ", ";
							condition = conditionStr;
						}
						break;
					case XmlPullParser.END_TAG:
						result += " \n ";
						Log.d( "end_tag", "节点结束" );
						// 添加数据
						FlightInfo info = new FlightInfo(	//11个参数
								imageId, flight_num, go, jiantou, get,
								date, depart_time, fly_time, land_time, depart_airport, land_airport, condition);
						_data.add(info);
						break;
					default:
						break;
				}
				eventType = parse.next();
			}			
		}catch( Exception ex ){
			ex.printStackTrace();
		}
		Log.d("resultStr", result);
	}

	private void readByClient(String qi1, String zhong1){
		final String qi = qi1;			//进程中不能传入变量 一定要为常量final
		final String zhong = zhong1;
		
		new Thread( new Runnable() {	//开启子线程		
			@Override
			public void run() {
				
				HttpURLConnection connection = null;
				try{
					String qi2 = URLEncoder.encode(qi, "UTF-8");	//中文转译！
					String zhong2 = URLEncoder.encode(zhong, "UTF-8");
					
					ip iip = new ip();
				//  打开链接
					URL url = new URL(
							"http://"+iip.getMy_ip()+":8080/pc/getByClient.jsp?go=" + qi2 + "&get=" + zhong2);
					
					connection = ( HttpURLConnection )url.openConnection();
					
					// 设置属性
					connection.setRequestMethod("GET");
					// Post 1)容量没有限制  2） 安全
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// 读取数据
					// 1)获取位流
					InputStream in = connection.getInputStream(); 
					// 二进制-->BufferedReader
					BufferedReader reader = new BufferedReader( 
							new InputStreamReader(in));
					// 2) 读取
					StringBuilder response = new StringBuilder();
					String line;
					while( ( line = reader.readLine() ) != null ){
						response.append( line );
					}
					
					
					// 发送消息
					Message msg = new Message();
					msg.what = 2;
					msg.obj = response.toString();
					_handler.sendMessage(msg);
					// Handler
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
				finally{
					if( connection != null ){
						connection.disconnect(); // 断开链接
					}
				}
			}
		}).start();
	}
	
	private void readAll(){
		new Thread( new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// Http链接
				HttpURLConnection connection = null;
				try{
					//  打开链接
//					URL url = new URL("http://10.22.24.108:8080/pc/flight.jsp");
					URL url = new URL("http://10.22.22.69:8080/pc/flight.jsp");
					connection = ( HttpURLConnection )url.
							openConnection();
					// 设置属性
					connection.setRequestMethod("GET");
					// Post 1)容量没有限制  2） 安全
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// 读取数据
					// 1)获取位流
					InputStream in = connection.getInputStream(); 
					// 二进制-->BufferedReader
					BufferedReader reader = new BufferedReader( 
							new InputStreamReader(in));
					// 2) 读取
					StringBuilder response = new StringBuilder();
					String line;
					while( ( line = reader.readLine() ) != null ){
						response.append( line );
					}
					
					
					// 发送消息
					Message msg = new Message();
					msg.what = 1;
					msg.obj = response.toString();
					_handler.sendMessage(msg);
					// Handler
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
				finally{
					if( connection != null ){
						connection.disconnect(); // 断开链接
					}
				}				
			}
		}).start();
		// 2）解析数据：xml-->ArrayList
	}
}
