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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RefundActivity extends Activity {

	// 退票订单列表显示
	ArrayList<FlightInfo> _data;
	ListView _listRefund;

	String order_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_refund);

		_data = new ArrayList<FlightInfo>();
		_listRefund = (ListView) findViewById(R.id.listRefund);

		readByOrder(); // 读取申请退款订单

		// 判断用户点击哪一个子项
		_listRefund.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				FlightInfo flightInfo = _data.get(position);
				order_num = flightInfo.getOrder_num(); // 获取当前点击子项的航班号

				Intent intent = new Intent(RefundActivity.this, Refund_Specific_Activity.class);
				intent.putExtra("order_num", order_num); // 向下一个界面传递信息

				startActivity(intent);
				finish();
			}
		});

		// 返回按钮
		Button back = (Button) findViewById(R.id.back2);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RefundActivity.this, M_Search_Activity.class);
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
				Toast.makeText(getApplicationContext(), String.format("ByOrder 共有%d条记录", _data.size()),
						Toast.LENGTH_LONG).show();
				RefundAdapter adapter = new RefundAdapter(RefundActivity.this, R.layout.refund_item, _data);
				_listRefund.setAdapter(adapter);
				break;
			}
		}
	};

	private void parserXml(String xmlData) {
		String result = "";
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser(); // 生成解析器
			parse.setInput(new StringReader(xmlData)); // 添加xml数据
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);

			String flight_num = "";
			String go = "";
			String jiantou = ""; // 箭头显示也是固定
			String get = "";
			String date = "";
			String ddh = "订单号";
			String order_num = "";

			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();
				result += nodeName;
				result += ", ";

				switch (eventType) {

				case XmlPullParser.START_TAG:

					// 从数据库读取9个参数
					if ("order_num".equals(nodeName)) {
						String order_num_Str = parse.nextText();
						result += "订单号为：" + order_num_Str + ", ";
						order_num = order_num_Str;

					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "起点为" + goStr + ", ";
						go = goStr;
					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "终点为" + getStr + ", ";
						get = getStr;
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "出发日期为" + dateStr + ", ";
						date = dateStr;
					} else if ("flight_num".equals(nodeName)) {
						String flight_num_Str = parse.nextText();
						result += "航班号为" + flight_num_Str + ", ";
						flight_num = flight_num_Str;
					}
					break;
				case XmlPullParser.END_TAG:
					result += " \n ";
					Log.d("end_tag", "节点结束");
					// 添加数据
					FlightInfo info = new FlightInfo( // 11个参数
							ddh, order_num, go, jiantou, get, date, flight_num);
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
		// Log.d("resultStr", result);
	}

	private void readByOrder() {

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// 打开链接
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/getByOrder.jsp");

					connection = (HttpURLConnection) url.openConnection();
					// 设置属性
					connection.setRequestMethod("GET");
					// Post 1)容量没有限制 2） 安全
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// 读取数据
					// 1)获取位流
					InputStream in = connection.getInputStream();
					// 二进制-->BufferedReader
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					// 2) 读取
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					// 发送消息
					Message msg = new Message();
					msg.what = 1;
					msg.obj = response.toString();
					_handler.sendMessage(msg);
					// Handler
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect(); // 断开链接
					}
				}
			}
		}).start();
	}
}
