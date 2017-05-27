package com.example.fly;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Order_Specific_Activity extends Activity {

	String flight_num, account, yuE, money = "0", order_num, refund;
	TextView chupiao, date, qi, zhong, time1, time2, time3, airport1, airport2, clas, f_n, psg, psp, nat, mon, oth,
			ordr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order_specific2);
		Intent intent = getIntent();
		account = intent.getStringExtra("account");
		flight_num = intent.getStringExtra("flight_num");
		yuE = intent.getStringExtra("yuE");
		order_num = intent.getStringExtra("order_num");
		refund = intent.getStringExtra("refund");

		// 17个文本框
		chupiao = (TextView) findViewById(R.id.chupiao);
		if (refund.equals("0") || refund.equals("3"))
			chupiao.setText("成功出票");
		else if (refund.equals("1")) {
			chupiao.setText("订单取消中..");
			chupiao.setTextColor(Color.rgb(0, 0, 0));
		} else if (refund.equals("2")) {
			chupiao.setText("订单已取消");
			chupiao.setTextColor(Color.rgb(112, 128, 144));
		}

		date = (TextView) findViewById(R.id.date);
		qi = (TextView) findViewById(R.id.qi);
		zhong = (TextView) findViewById(R.id.zhong);
		time1 = (TextView) findViewById(R.id.time1);
		time2 = (TextView) findViewById(R.id.time2);
		time3 = (TextView) findViewById(R.id.time3);
		airport1 = (TextView) findViewById(R.id.airport1);
		airport2 = (TextView) findViewById(R.id.airport2);
		clas = (TextView) findViewById(R.id.clas);
		f_n = (TextView) findViewById(R.id.f_n);
		psg = (TextView) findViewById(R.id.psg);
		psp = (TextView) findViewById(R.id.psp);
		nat = (TextView) findViewById(R.id.nat);
		mon = (TextView) findViewById(R.id.mon);
		oth = (TextView) findViewById(R.id.oth);
		ordr = (TextView) findViewById(R.id.order_num);

		getSepcificOrder(account, flight_num);

		Button gaiqian = (Button) findViewById(R.id.gaiqian);
		gaiqian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (chupiao.getText().toString().equals("订单取消中..") || chupiao.getText().toString().equals("订单已取消")) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Order_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("您的订单正在取消或已退票!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Order_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("当前机票将作废！您确定要改签吗？");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							// 确定改签.传递用户名、原航班号、原票价
							Intent intent = new Intent(Order_Specific_Activity.this, FlightActivity.class);

							intent.putExtra("qidian", qi.getText().toString());
							intent.putExtra("zhongdian", zhong.getText().toString());
							intent.putExtra("account", account);
							intent.putExtra("yuE", yuE);

							intent.putExtra("flight_num", flight_num);
							intent.putExtra("yuanpiaojia", money);
							intent.putExtra("gaiqian", "yes");
							intent.putExtra("clas_gaiqian", clas.getText().toString());
							intent.putExtra("order_num", order_num);

							Toast.makeText(getApplicationContext(), "请重新选择航班", Toast.LENGTH_LONG);
							startActivity(intent);
							finish();
						}
					});
					dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
			}
		});
		// 申请退票
		Button tuipiao = (Button) findViewById(R.id.tuipiao);
		tuipiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (chupiao.getText().toString().equals("订单取消中..") || chupiao.getText().toString().equals("订单已取消")) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Order_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("您的订单正在取消或已退票!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Order_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("当前机票将作废！您确定要退票吗？");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							// 确定取消订单，向管理员提交申请
							RefundRequest(order_num);
						}
					});
					dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
			}
		});

		Button fanhui = (Button) findViewById(R.id.fanhui);
		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Order_Specific_Activity.this, SearchActivity.class);
				intent.putExtra("account", account);
				startActivity(intent);
				finish();
			}
		});
	}

	// 处理订单详细信息显示的消息
	private Handler _handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 申请改签
				String response = (String) msg.obj;
				parserXml(response);
				break;
			case 2: // 申请退票
				String response2 = (String) msg.obj;
				if (parserXml2(response2)) {
					Intent intent = new Intent(Order_Specific_Activity.this, MyOrderActivity.class);
					intent.putExtra("account", account);
					intent.putExtra("yuE", yuE);

					Toast.makeText(getApplicationContext(), "您的申请已提交，请耐心等待！", Toast.LENGTH_LONG);
					startActivity(intent);
					finish();
				}
				break;
			default:
				break;
			}
		}
	};

	// 确认订单是否成功提交申请
	private boolean parserXml2(String xmlData) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser(); // 生成解析器
			parse.setInput(new StringReader(xmlData)); // 添加xml数据
			int eventType = parse.getEventType();

			String result = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();

				switch (eventType) {
				case XmlPullParser.START_TAG:

					if ("result".equals(nodeName)) {
						result = parse.nextText();
					}
					if (result.equals("succeessful"))
						return true;
					else if (result.equals("failed"))
						return false;
					break;
				}
				eventType = parse.next();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}

	// 解析出机票详细信息显示的消息
	private void parserXml(String xmlData) {
		String result = "";
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser(); // 生成解析器
			parse.setInput(new StringReader(xmlData)); // 添加xml数据
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);

			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();
				result += nodeName;
				result += ", ";

				switch (eventType) {

				case XmlPullParser.START_TAG:

					// 从数据库读取16个参数
					if ("order_num".equals(nodeName)) {
						String order_num_Str = parse.nextText();
						result += "订单号为" + order_num_Str + ", ";
						ordr.setText(order_num_Str);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "出发日期为" + dateStr + ", ";
						date.setText(dateStr);
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "起点为" + goStr + ", ";
						qi.setText(goStr);
					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "终点为" + getStr + ", ";
						zhong.setText(getStr);
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "起飞时间为" + depart_timeStr + ", ";
						time1.setText(depart_timeStr);
					} else if ("fly_time".equals(nodeName)) {
						String fly_timeStr = parse.nextText();
						result += "飞行时间为" + fly_timeStr + ", ";
						time2.setText(fly_timeStr);
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "到达时间为" + land_timeStr + ", ";
						time3.setText(land_timeStr);
					} else if ("depart_airport".equals(nodeName)) {
						String depart_airportStr = parse.nextText();
						result += "起飞机场为" + depart_airportStr + ", ";
						airport1.setText(depart_airportStr);
					} else if ("land_airport".equals(nodeName)) {
						String land_airportStr = parse.nextText();
						result += "降落机场为" + land_airportStr + ", ";
						airport2.setText(land_airportStr);
					} else if ("clas".equals(nodeName)) {
						String classs = parse.nextText();
						result += "舱位等级为" + classs + ", ";
						clas.setText(classs);
					} else if ("flight_num".equals(nodeName)) {
						String flight_num = parse.nextText();
						result += "航班号为" + flight_num + ", ";
						f_n.setText(flight_num);
					} else if ("passenger".equals(nodeName)) {
						String passenger = parse.nextText();
						result += "乘机人为" + passenger + ", ";
						psg.setText(passenger);
					} else if ("passport".equals(nodeName)) {
						String passport = parse.nextText();
						result += "航班号为" + passport + ", ";
						psp.setText(passport);
					} else if ("nation".equals(nodeName)) {
						String nation = parse.nextText();
						result += "国籍为" + nation + ", ";
						nat.setText(nation);
					} else if ("money".equals(nodeName)) {
						money = parse.nextText();
						String money2 = money;
						money2 = String.format("￥%s", money2);
						mon.setText(money2);
						result += "订单总价为" + money2 + ", ";
					} else if ("other".equals(nodeName)) {
						String other = parse.nextText();
						result += "保险为" + other + ", ";
						oth.setText(other);
					}

					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
				eventType = parse.next();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.d("resultStr", result);
	}

	private void getSepcificOrder(String aa, String ff) {
		final String account = aa;
		final String flight_num = ff;

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {

					ip iip = new ip();
					// 打开链接
					String accunt2 = URLEncoder.encode(account, "UTF-8");
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8");
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/speceficOrder.jsp?flight_num="
							+ flight_num2 + "&account=" + accunt2);

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

	private void RefundRequest(String o) {
		final String order_num = o;

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// 打开链接
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/refundRequest.jsp?order_num=" + order_num);

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
					msg.what = 2;
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
