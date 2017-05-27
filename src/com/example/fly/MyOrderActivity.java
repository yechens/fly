package com.example.fly;

import java.io.BufferedReader;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyOrderActivity extends Activity {

	String account, yuE, flight_num, order_num, refund;
	// ������ʾ�б�
	ArrayList<FlightInfo> _data;
	ListView _listOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_order);

		Intent intent = getIntent();
		account = intent.getStringExtra("account");
		yuE = intent.getStringExtra("yuE");

		_data = new ArrayList<FlightInfo>();
		_listOrder = (ListView) findViewById(R.id.listOrder);

		getMyOrder(account);

		// �ж��û������һ������
		_listOrder.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				FlightInfo flightInfo = _data.get(position);
				flight_num = flightInfo.getFlight_num(); // ��ȡ��ǰ�������ĺ����
				order_num = flightInfo.getOrder_num(); // ��ȡ��ǰ����Ķ�����
				refund = flightInfo.getRefund(); // ��ȡ��ǰ�����Ƿ��û�ȡ��

				Intent intent = new Intent(MyOrderActivity.this, Order_Specific_Activity.class);
				intent.putExtra("account", account);
				intent.putExtra("flight_num", flight_num);
				intent.putExtra("yuE", yuE);
				intent.putExtra("order_num", order_num);
				intent.putExtra("refund", refund);

				startActivity(intent);
				finish();
			}
		});

		// ���ذ�ť
		Button back = (Button) findViewById(R.id.back1);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyOrderActivity.this, SearchActivity.class);
				intent.putExtra("account", account);
				startActivity(intent);
				finish();
			}
		});
	}

	private Handler _handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String response = (String) msg.obj;
				_data.clear();
				parserXml(response);
				OrderAdapter adapter = new OrderAdapter(MyOrderActivity.this, R.layout.order_item, _data);
				_listOrder.setAdapter(adapter);
				break;
			}
		}
	};

	private void parserXml(String xmlData) {

		String result = "";
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser(); // ���ɽ�����
			parse.setInput(new StringReader(xmlData)); // ���xml����
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);

			int imageId = R.drawable.fei; // ͼƬ��Դ�̶�
			String jp = "��Ʊ";
			String ycp = "";
			String go = ""; // ��ͷ��ʾҲ�ǹ̶�
			String jiantou1 = "��";
			String get = "";

			// int total_price = -1;
			String total_price = "";
			String date = "";
			String depart_time = "";
			String jiantou2 = "��";
			String land_time = "";
			String flight_num = "";
			String order_num = "";
			String refund = "";

			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();
				result += nodeName;
				result += ", ";

				switch (eventType) {

				case XmlPullParser.START_TAG:

					// �����ݿ��ȡ9������
					if ("order_num".equals(nodeName)) {
						String order_num_Str = parse.nextText();
						result += "������Ϊ��" + order_num_Str + ", ";
						order_num = order_num_Str;
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "���Ϊ��" + goStr + ", ";
						go = goStr;

					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "�յ�Ϊ" + getStr + ", ";
						get = getStr;
					} else if ("total_price".equals(nodeName)) {
						String total_price_str = parse.nextText();
						result += "Ʊ��Ϊ" + total_price_str + ", ";
						total_price = total_price_str;
						// total_price = Integer.parseInt(total_price_str);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "��������Ϊ" + dateStr + ", ";
						date = dateStr;
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "���ʱ��Ϊ" + depart_timeStr + ", ";
						depart_time = depart_timeStr;
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "����ʱ��Ϊ" + land_timeStr + ", ";
						land_time = land_timeStr;
					} else if ("flight_num".equals(nodeName)) {
						String flight_numStr = parse.nextText();
						result += "�����Ϊ��" + flight_numStr + ", ";
						flight_num = flight_numStr;
					} else if ("refund".equals(nodeName)) {
						String refund_Str = parse.nextText();
						result += "�Ƿ�ȡ��������" + refund_Str + ", ";
						refund = refund_Str;
						if (refund.equals("0"))
							ycp = "�ѳ�Ʊ";
						else if (refund.equals("1"))
							ycp = "����ȡ����..";
						else if (refund.equals("2"))
							ycp = "������ȡ��";
						else if (refund.equals("3"))
							ycp = "��Ʊʧ�ܣ�";
					}
					break;

				case XmlPullParser.END_TAG:
					result += " \n ";
					Log.d("end_tag", "�ڵ����");
					// �������
					FlightInfo info = new FlightInfo( // 12������
							order_num, imageId, jp, ycp, go, jiantou1, get, total_price, date, depart_time, jiantou2,
							land_time, flight_num, refund);
					_data.add(info);
					break;
				default:
					break;
				}
				eventType = parse.next();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.d("result", result);
	}

	private void getMyOrder(String a) {

		final String account = a;

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					String account2 = URLEncoder.encode(account, "UTF-8"); // ����ת�룡

					ip iip = new ip();
					// ������
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/getMyOrder.jsp?account=" + account2);

					connection = (HttpURLConnection) url.openConnection();

					// ��������
					connection.setRequestMethod("GET");
					// Post 1)����û������ 2�� ��ȫ
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// ��ȡ����
					// 1)��ȡλ��
					InputStream in = connection.getInputStream();
					// ������-->BufferedReader
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					// 2) ��ȡ
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					// ������Ϣ
					Message msg = new Message();
					msg.what = 1;
					msg.obj = response.toString();
					_handler.sendMessage(msg);
					// Handler
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect(); // �Ͽ�����
					}
				}
			}
		}).start();
	}
}
