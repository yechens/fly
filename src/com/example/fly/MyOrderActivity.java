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
	// 订单显示列表
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

		// 判断用户点击哪一个子项
		_listOrder.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				FlightInfo flightInfo = _data.get(position);
				flight_num = flightInfo.getFlight_num(); // 获取当前点击子项的航班号
				order_num = flightInfo.getOrder_num(); // 获取当前子项的订单号
				refund = flightInfo.getRefund(); // 获取当前订单是否被用户取消

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

		// 返回按钮
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
			XmlPullParser parse = factory.newPullParser(); // 生成解析器
			parse.setInput(new StringReader(xmlData)); // 添加xml数据
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);

			int imageId = R.drawable.fei; // 图片资源固定
			String jp = "机票";
			String ycp = "";
			String go = ""; // 箭头显示也是固定
			String jiantou1 = "→";
			String get = "";

			// int total_price = -1;
			String total_price = "";
			String date = "";
			String depart_time = "";
			String jiantou2 = "→";
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

					// 从数据库读取9个参数
					if ("order_num".equals(nodeName)) {
						String order_num_Str = parse.nextText();
						result += "订单号为：" + order_num_Str + ", ";
						order_num = order_num_Str;
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "起点为：" + goStr + ", ";
						go = goStr;

					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "终点为" + getStr + ", ";
						get = getStr;
					} else if ("total_price".equals(nodeName)) {
						String total_price_str = parse.nextText();
						result += "票价为" + total_price_str + ", ";
						total_price = total_price_str;
						// total_price = Integer.parseInt(total_price_str);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "出发日期为" + dateStr + ", ";
						date = dateStr;
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "起飞时间为" + depart_timeStr + ", ";
						depart_time = depart_timeStr;
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "到达时间为" + land_timeStr + ", ";
						land_time = land_timeStr;
					} else if ("flight_num".equals(nodeName)) {
						String flight_numStr = parse.nextText();
						result += "航班号为：" + flight_numStr + ", ";
						flight_num = flight_numStr;
					} else if ("refund".equals(nodeName)) {
						String refund_Str = parse.nextText();
						result += "是否取消订单：" + refund_Str + ", ";
						refund = refund_Str;
						if (refund.equals("0"))
							ycp = "已出票";
						else if (refund.equals("1"))
							ycp = "订单取消中..";
						else if (refund.equals("2"))
							ycp = "订单已取消";
						else if (refund.equals("3"))
							ycp = "退票失败！";
					}
					break;

				case XmlPullParser.END_TAG:
					result += " \n ";
					Log.d("end_tag", "节点结束");
					// 添加数据
					FlightInfo info = new FlightInfo( // 12个参数
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

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					String account2 = URLEncoder.encode(account, "UTF-8"); // 中文转译！

					ip iip = new ip();
					// 打开链接
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/getMyOrder.jsp?account=" + account2);

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
