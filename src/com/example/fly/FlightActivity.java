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
	
	// ��Ʊ��ʾ�б�
	ArrayList< FlightInfo > _data;
	ListView _listFlight;
	String account, qidian, zhongdian, yuE;
	String flight_num, condition;
	//��ǩʱ����
	String flight_num2, gaiqian, yuanpiaojia, clas_gaiqian, order_num;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flight);
		
		Intent intent = getIntent();	//���մ��ݹ������˺š���㡢�յ㡢���
		
		yuE = intent.getStringExtra("yuE");
		
		account = intent.getStringExtra("account");
		qidian = intent.getStringExtra("qidian");
		zhongdian = intent.getStringExtra("zhongdian");
		
		//��ǩʱ����һ����Ϣ
		flight_num2 = intent.getStringExtra("flight_num");
		gaiqian = intent.getStringExtra("gaiqian");
		yuanpiaojia = intent.getStringExtra("yuanpiaojia");
		clas_gaiqian = intent.getStringExtra("clas_gaiqian");
		order_num = intent.getStringExtra("order_num");
		
		_data = new ArrayList< FlightInfo >();
		_listFlight = (ListView)findViewById(R.id.listFlight);
//		readAll();										//��ʾ���к���
		readByClient(qidian,zhongdian);					//�����û����������յ���Һ���
		
		//�ж��û������һ������
		_listFlight.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				FlightInfo flightInfo = _data.get(position);		
				flight_num = flightInfo.getFlight_num();	//��ȡ��ǰ�������ĺ����
				condition = flightInfo.getCondition();
				
				Intent intent = new Intent(FlightActivity.this,Specific_Activity.class);
				

				intent.putExtra("account", account);		//����һ�����洫����Ϣ
				intent.putExtra("flight_num", flight_num);	//�������û���������š������������ Ψһֵ				
				intent.putExtra("yuE", yuE);
				intent.putExtra("condition", condition);
				
				intent.putExtra("gaiqian", gaiqian);
				if(gaiqian.equals("yes")){
					intent.putExtra("flight_num2", flight_num2);		//ԭ��Ʊ�����
					intent.putExtra("yuanpiaojia", yuanpiaojia);		//ԭ��ƱƱ��	
					intent.putExtra("clas_gaiqian", clas_gaiqian);		//ԭ��Ʊ��λ�ȼ�
					intent.putExtra("order_num", order_num);			//ԭ��Ʊ������
				}
				startActivity(intent);		
				finish();
			}       	
        });
		//���²��Ұ�ť
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
							String.format("All ����%d����¼", _data.size()), 
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
			XmlPullParser parse = factory.newPullParser();	//���ɽ�����
			parse.setInput( new StringReader( xmlData ) );	//���xml����
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);			
			
			int imageId = R.drawable.fei;	//ͼƬ��Դ�̶�
			String flight_num = "";
			String go = "";
			String jiantou = "����";		//��ͷ��ʾҲ�ǹ̶�
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
						
						//�����ݿ��ȡ9������
						if( "flight_num".equals( nodeName ) ){
							String flight_numStr = parse.nextText();
							result += "�����Ϊ��" + flight_numStr + ", ";
							flight_num = flight_numStr;
							
						}
						else if( "go".equals( nodeName )  ){
							String goStr = parse.nextText();
							result += "���Ϊ" + goStr + ", ";
							go = goStr;
						}
						else if( "get".equals( nodeName )  ){
							String getStr = parse.nextText();
							result += "�յ�Ϊ" + getStr + ", ";
							get = getStr;
						}
						else if( "date".equals( nodeName )  ){
							String dateStr = parse.nextText();
							result += "��������Ϊ" + dateStr + ", ";
							date = dateStr;
						}
						else if( "depart_time".equals( nodeName )  ){
							String depart_timeStr = parse.nextText();
							result += "���ʱ��Ϊ" + depart_timeStr + ", ";
							depart_time = depart_timeStr;
						}
						else if( "fly_time".equals( nodeName )  ){
							String fly_timeStr = parse.nextText();
							result += "����ʱ��Ϊ" + fly_timeStr + ", ";
							fly_time = fly_timeStr;
						}
						else if( "land_time".equals( nodeName )  ){
							String land_timeStr = parse.nextText();
							result += "����ʱ��Ϊ" + land_timeStr + ", ";
							land_time = land_timeStr;
						}
						else if( "depart_airport".equals( nodeName )  ){
							String depart_airportStr = parse.nextText();
							result += "��ɻ���Ϊ" + depart_airportStr + ", ";
							depart_airport = depart_airportStr;
						}					
						else if( "land_airport".equals( nodeName )  ){
							String land_airportStr = parse.nextText();
							result += "�������Ϊ" + land_airportStr + ", ";
							land_airport = land_airportStr;
						}
						else if( "condition".equals( nodeName )  ){
							String conditionStr = parse.nextText();
							result += "�Ƿ�������Ʊ" + conditionStr + ", ";
							condition = conditionStr;
						}
						break;
					case XmlPullParser.END_TAG:
						result += " \n ";
						Log.d( "end_tag", "�ڵ����" );
						// �������
						FlightInfo info = new FlightInfo(	//11������
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
		final String qi = qi1;			//�����в��ܴ������ һ��ҪΪ����final
		final String zhong = zhong1;
		
		new Thread( new Runnable() {	//�������߳�		
			@Override
			public void run() {
				
				HttpURLConnection connection = null;
				try{
					String qi2 = URLEncoder.encode(qi, "UTF-8");	//����ת�룡
					String zhong2 = URLEncoder.encode(zhong, "UTF-8");
					
					ip iip = new ip();
				//  ������
					URL url = new URL(
							"http://"+iip.getMy_ip()+":8080/pc/getByClient.jsp?go=" + qi2 + "&get=" + zhong2);
					
					connection = ( HttpURLConnection )url.openConnection();
					
					// ��������
					connection.setRequestMethod("GET");
					// Post 1)����û������  2�� ��ȫ
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// ��ȡ����
					// 1)��ȡλ��
					InputStream in = connection.getInputStream(); 
					// ������-->BufferedReader
					BufferedReader reader = new BufferedReader( 
							new InputStreamReader(in));
					// 2) ��ȡ
					StringBuilder response = new StringBuilder();
					String line;
					while( ( line = reader.readLine() ) != null ){
						response.append( line );
					}
					
					
					// ������Ϣ
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
						connection.disconnect(); // �Ͽ�����
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
				// Http����
				HttpURLConnection connection = null;
				try{
					//  ������
//					URL url = new URL("http://10.22.24.108:8080/pc/flight.jsp");
					URL url = new URL("http://10.22.22.69:8080/pc/flight.jsp");
					connection = ( HttpURLConnection )url.
							openConnection();
					// ��������
					connection.setRequestMethod("GET");
					// Post 1)����û������  2�� ��ȫ
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// ��ȡ����
					// 1)��ȡλ��
					InputStream in = connection.getInputStream(); 
					// ������-->BufferedReader
					BufferedReader reader = new BufferedReader( 
							new InputStreamReader(in));
					// 2) ��ȡ
					StringBuilder response = new StringBuilder();
					String line;
					while( ( line = reader.readLine() ) != null ){
						response.append( line );
					}
					
					
					// ������Ϣ
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
						connection.disconnect(); // �Ͽ�����
					}
				}				
			}
		}).start();
		// 2���������ݣ�xml-->ArrayList
	}
}
