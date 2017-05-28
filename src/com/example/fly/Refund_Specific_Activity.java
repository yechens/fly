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

public class Refund_Specific_Activity extends Activity {

	String order_num, money;
	TextView a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_refund__specific_);

		Intent intent = getIntent(); // 接收传递过来的账号、起点、终点、余额
		order_num = intent.getStringExtra("order_num");

		a1 = (TextView) findViewById(R.id.a1); // 订单号直接设置！
		a1.setText(order_num);

		a2 = (TextView) findViewById(R.id.a2);
		a3 = (TextView) findViewById(R.id.a3);
		a4 = (TextView) findViewById(R.id.a4);
		a5 = (TextView) findViewById(R.id.a5);
		a6 = (TextView) findViewById(R.id.a6);
		a7 = (TextView) findViewById(R.id.a7);
		a8 = (TextView) findViewById(R.id.a8);
		a9 = (TextView) findViewById(R.id.a9);
		a10 = (TextView) findViewById(R.id.a10);
		a11 = (TextView) findViewById(R.id.a11);
		a12 = (TextView) findViewById(R.id.a12);
		a13 = (TextView) findViewById(R.id.a13);
		a14 = (TextView) findViewById(R.id.a14);
		a15 = (TextView) findViewById(R.id.a15);
		a16 = (TextView) findViewById(R.id.a16);
		a17 = (TextView) findViewById(R.id.a17);

		getRefundSpecific(order_num);

		// 同意用户退票
		Button agree = (Button) findViewById(R.id.agree);
		agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
				dialog.setTitle("This is a warnining!");
				dialog.setMessage("您确定同意退票吗？");
				dialog.setCancelable(false);
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 确定取消订单,传递购票用户、总价、航班号、clas
						String account = a17.getText().toString();
						String clas = a10.getText().toString();
						String flight_num = a11.getText().toString();
						// Log.d("refund", "???"+order_num+" "+account+"
						// "+money+" "+clas+" "+flight_num);
						AgreeRefund(order_num, account, money, clas, flight_num);
					}
				});
				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				dialog.show();
			}
		});

		// 拒绝该用户退票
		Button disagree = (Button) findViewById(R.id.disagree);
		disagree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
				dialog.setTitle("This is a warnining!");
				dialog.setMessage("您确定拒绝吗？");
				dialog.setCancelable(false);
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DisagreeRefund(order_num);
					}
				});
				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				dialog.show();
			}
		});
		// 取消返回按钮
		Button cancel = (Button) findViewById(R.id.back6);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Refund_Specific_Activity.this, M_Search_Activity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	// 退票流程
	private void AgreeRefund(String o, String a, String m, String c, String f) {
		final String order_num = o;
		final String account = a;
		final String money = m;
		final String clas = c;
		final String flight_num = f;

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// 打开链接
					String account2 = URLEncoder.encode(account, "UTF-8"); // 中文转译！
					String clas2 = URLEncoder.encode(clas, "UTF-8"); // 中文转译！
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8"); // 中文转译！
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/AgreeRefund.jsp?order_num=" + order_num + "&account="
									+ account2 + "&money=" + money + "&clas=" + clas2 + "&flight_num=" + flight_num2);

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

	private Handler _handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String response = (String) msg.obj;
				parserXml(response);
				break;
			case 2:
				String response2 = (String) msg.obj;
				if (parserXml2(response2)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("您已成功为该用户退票！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Refund_Specific_Activity.this, RefundActivity.class);
							startActivity(intent);
							finish();
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("操作失败！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				break;
			case 3:
				String response3 = (String) msg.obj;
				if (parserXml2(response3)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("您已拒绝该用户退票！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Refund_Specific_Activity.this, RefundActivity.class);
							startActivity(intent);
							finish();
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("操作失败！！！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				break;
			}
		}
	};

	private boolean parserXml2(String xmlData) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser(); // 生成解析器
			parse.setInput(new StringReader(xmlData)); // 添加xml数据
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);

			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();
				switch (eventType) {

				case XmlPullParser.START_TAG:
					// 从数据库读取15个参数
					if ("result".equals(nodeName)) {
						String result = parse.nextText();
						if (result.equals("succeessful"))
							return true;
						else if (result.equals("failed"))
							return false;
					}

					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventType = parse.next();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

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
					// 从数据库读取15个参数
					if ("flight_num".equals(nodeName)) {
						String flight_numStr = parse.nextText();
						result += "航班号为：" + flight_numStr + ", ";
						a11.setText(flight_numStr);
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "起点为" + goStr + ", ";
						a3.setText(goStr);
					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "终点为" + getStr + ", ";
						a4.setText(getStr);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "出发日期为" + dateStr + ", ";
						a2.setText(dateStr);
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "起飞时间为" + depart_timeStr + ", ";
						a5.setText(depart_timeStr);
					} else if ("fly_time".equals(nodeName)) {
						String fly_timeStr = parse.nextText();
						result += "飞行时间为" + fly_timeStr + ", ";
						a6.setText(fly_timeStr);
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "到达时间为" + land_timeStr + ", ";
						a7.setText(land_timeStr);
					} else if ("depart_airport".equals(nodeName)) {
						String depart_airportStr = parse.nextText();
						result += "起飞机场为" + depart_airportStr + ", ";
						a8.setText(depart_airportStr);
					} else if ("land_airport".equals(nodeName)) {
						String land_airportStr = parse.nextText();
						result += "降落机场为" + land_airportStr + ", ";
						a9.setText(land_airportStr);
					} else if ("clas".equals(nodeName)) {
						String clasStr = parse.nextText();
						result += "等级为" + clasStr + ", ";
						a10.setText(clasStr);
					} else if ("passenger".equals(nodeName)) {
						String passengerStr = parse.nextText();
						result += "乘客为" + passengerStr + ", ";
						a12.setText(passengerStr);
					} else if ("passport".equals(nodeName)) {
						String passportStr = parse.nextText();
						result += "护照为" + passportStr + ", ";
						a13.setText(passportStr);
					} else if ("nation".equals(nodeName)) {
						String nationStr = parse.nextText();
						result += "国籍为" + nationStr + ", ";
						a14.setText(nationStr);
					} else if ("money".equals(nodeName)) {
						String moneyStr = parse.nextText();
						money = moneyStr;
						result += "订单总额为" + moneyStr + ", ";
						a15.setText("￥ " + moneyStr);
					} else if ("other".equals(nodeName)) {
						String otherStr = parse.nextText();
						result += "其他为" + otherStr + ", ";
						a16.setText(otherStr);
					} else if ("account_name".equals(nodeName)) {
						String account_name = parse.nextText();
						result += "订票账户为" + account_name + ", ";
						a17.setText(account_name);
					}
					break;
				case XmlPullParser.END_TAG:
					result += " \n ";
					Log.d("end_tag", "节点结束");
					break;
				default:
					break;
				}
				eventType = parse.next();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void getRefundSpecific(String o) {

		final String order_num = o;
		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// 打开链接
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/getOrderSpecific.jsp?order_num=" + order_num);

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

	// 拒绝用户退票请求
	private void DisagreeRefund(String o) {
		final String order_num = o;
		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// 打开链接
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/disagreeRefund.jsp?order_num=" + order_num);

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
					msg.what = 3;
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
