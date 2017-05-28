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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Specific_Activity extends Activity {

	String yuE, account, flight_num, condition;
	String i = "0";
	String toudeng_jia_int, toudeng_yu_int, jingji_jia_int, jingji_yu_int;
	String gaiqian, flight_num2, yuanpiaojia, clas_gaiqian, order_num;
	// Button buy_tou, buy_jing;
	// 航班详细信息界面需要显示的TextView
	private TextView flight_num_txt, go_txt, get_txt, date_txt, depart_time_txt, fly_time_txt, land_time_txt,
			depart_airport_txt, land_airport_txt, toudeng_yupiao, toudeng_piaojia, jingji_yupiao, jingji_piaojia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题

		// buy_tou = (Button)findViewById(R.id.buy_tou);
		// buy_jing = (Button)findViewById(R.id.buy_jing);

		Intent intent = getIntent(); // 接收传递过来的账号、航班号、余额
		account = intent.getStringExtra("account");

		// 判断航班售票是否暂时关闭
		condition = intent.getStringExtra("condition");

		if (account.equals("andy")) { // 若是管理员查询路线
			setContentView(R.layout.m_activity_specific);
		} else {
			setContentView(R.layout.activity_specific_);
		}

		yuE = intent.getStringExtra("yuE");
		flight_num = intent.getStringExtra("flight_num");

		Button buy_tou2 = (Button) findViewById(R.id.buy_tou);
		Button buy_jing2 = (Button) findViewById(R.id.buy_jing);
		if (condition.equals("1")) {
			buy_tou2.setBackgroundColor(Color.rgb(168, 168, 168));
			buy_jing2.setBackgroundColor(Color.rgb(168, 168, 168));
		}

		gaiqian = intent.getStringExtra("gaiqian"); // 判断当前是否为改签状态
		if (gaiqian.equals("yes")) {
			flight_num2 = intent.getStringExtra("flight_num2");
			yuanpiaojia = intent.getStringExtra("yuanpiaojia");
			clas_gaiqian = intent.getStringExtra("clas_gaiqian");
			order_num = intent.getStringExtra("order_num");
		}

		flight_num_txt = (TextView) findViewById(R.id.flight_num_txt);
		go_txt = (TextView) findViewById(R.id.go_txt);
		get_txt = (TextView) findViewById(R.id.get_txt);
		date_txt = (TextView) findViewById(R.id.date_txt);
		depart_time_txt = (TextView) findViewById(R.id.depart_time_txt);
		fly_time_txt = (TextView) findViewById(R.id.fly_time_txt);
		land_time_txt = (TextView) findViewById(R.id.land_time_txt);

		depart_airport_txt = (TextView) findViewById(R.id.depart_airport_txt);
		land_airport_txt = (TextView) findViewById(R.id.land_airport_txt);

		toudeng_yupiao = (TextView) findViewById(R.id.toudeng_yupiao);
		toudeng_piaojia = (TextView) findViewById(R.id.toudeng_piaojia);
		jingji_yupiao = (TextView) findViewById(R.id.jingji_yupiao);
		jingji_piaojia = (TextView) findViewById(R.id.jingji_piaojia);

		readSpecificFlight(flight_num); // 根据航班号读取航班详细信息

		if (account.equals("andy")) { // 如果直接设变量Button stop 程序会崩溃！！！
			// 管理员禁用本次航班
			Button stop = (Button) findViewById(R.id.stop);

			if (condition.equals("1")) {
				i = "0";
				stop.setText("航班恢复售票");
			} else if (condition.equals("0")) {
				i = "1";
				stop.setText("航班暂停售票");
			}

			stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					stopFlight(flight_num, i);
				}
			});
		} else {
			// 购票头等舱！
			Button buy_tou = (Button) findViewById(R.id.buy_tou);
			buy_tou.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (condition.equals("1")) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("对不起，本次航班暂停购票！");
						dialog.setCancelable(false);
						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						dialog.show();
					}
					// 余票数大于0 ，才能购票
					else if (Integer.parseInt(toudeng_yu_int) > 0) {
						// BuyTicketTouActivity为购买头等舱、经济舱公用活动
						Intent intent = new Intent(Specific_Activity.this, BuyTicketTouActivity.class);

						// 向购票界面传递 余额、账户、日期、起点、终点、航班号、票价、余票数、舱位等级...
						intent.putExtra("yuE", yuE);
						intent.putExtra("account", account);
						intent.putExtra("flight_num", flight_num);

						String go = go_txt.getText().toString();
						intent.putExtra("go", go);
						String get = get_txt.getText().toString();
						intent.putExtra("get", get);

						String fly_time = fly_time_txt.getText().toString();
						intent.putExtra("fly_time", fly_time);
						String date = date_txt.getText().toString();
						intent.putExtra("date", date);

						String depart_airport = depart_airport_txt.getText().toString();
						intent.putExtra("depart_airport", depart_airport);
						String land_airport = land_airport_txt.getText().toString();
						intent.putExtra("land_airport", land_airport);

						String depart_time = depart_time_txt.getText().toString();
						intent.putExtra("depart_time", depart_time);
						String land_time = land_time_txt.getText().toString();
						intent.putExtra("land_time", land_time);

						intent.putExtra("piaojia", toudeng_jia_int);
						intent.putExtra("yupiao", toudeng_yu_int);

						String ad = "尊享头等舱礼遇        	      20kg行李额";
						intent.putExtra("ad", ad);
						String title = "头等舱购票专区";
						intent.putExtra("title", title);

						String clas = "头等舱";
						intent.putExtra("clas", clas);

						intent.putExtra("gaiqian", gaiqian);
						if (gaiqian.equals("yes")) {
							intent.putExtra("flight_num2", flight_num2);
							intent.putExtra("yuanpiaojia", yuanpiaojia);
							intent.putExtra("clas_gaiqian", clas_gaiqian);
							intent.putExtra("order_num", order_num); // 原机票订单号
						}
						startActivity(intent);
						finish();
					} else {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("对不起，您购买的航线已售罄！");
						dialog.setCancelable(false);
						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						dialog.show();
					}
				}
			});

			// 购票经济舱！
			Button buy_jing = (Button) findViewById(R.id.buy_jing);
			buy_jing.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (condition.equals("1")) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("对不起，本次航班暂停购票！");
						dialog.setCancelable(false);
						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						dialog.show();
					} else if (Integer.parseInt(jingji_yu_int) > 0) {
						// BuyTicketTouActivity为购买头等舱、经济舱公用活动
						Intent intent = new Intent(Specific_Activity.this, BuyTicketTouActivity.class);

						// 向购票界面传递账户、日期、起点、终点、航班号、票价、余票数、舱位等级...
						intent.putExtra("yuE", yuE);
						intent.putExtra("account", account);
						intent.putExtra("flight_num", flight_num);

						String go = go_txt.getText().toString();
						intent.putExtra("go", go);
						String get = get_txt.getText().toString();
						intent.putExtra("get", get);

						String fly_time = fly_time_txt.getText().toString();
						intent.putExtra("fly_time", fly_time);
						String date = date_txt.getText().toString();
						intent.putExtra("date", date);

						String depart_airport = depart_airport_txt.getText().toString();
						intent.putExtra("depart_airport", depart_airport);
						String land_airport = land_airport_txt.getText().toString();
						intent.putExtra("land_airport", land_airport);

						String depart_time = depart_time_txt.getText().toString();
						intent.putExtra("depart_time", depart_time);
						String land_time = land_time_txt.getText().toString();
						intent.putExtra("land_time", land_time);

						intent.putExtra("piaojia", jingji_jia_int);
						intent.putExtra("yupiao", jingji_yu_int);

						String ad = "您享有7kg免费行李额";
						intent.putExtra("ad", ad);
						String title = "经济舱购票区";
						intent.putExtra("title", title);

						String clas = "经济舱";
						intent.putExtra("clas", clas);

						intent.putExtra("gaiqian", gaiqian);
						if (gaiqian.equals("yes")) {
							intent.putExtra("flight_num2", flight_num2);
							intent.putExtra("yuanpiaojia", yuanpiaojia);
							intent.putExtra("clas_gaiqian", clas_gaiqian);
							intent.putExtra("order_num", order_num); // 原机票订单号
						}

						startActivity(intent);
						finish();
					} else {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("对不起，您购买的航线已售罄！");
						dialog.setCancelable(false);
						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						dialog.show();
					}
				}
			});

			// 取消返回按钮
			Button cancel = (Button) findViewById(R.id.cancel);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Specific_Activity.this, SearchActivity.class);
					intent.putExtra("account", account);
					startActivity(intent);
					finish();
				}
			});
		}

	}

	// 处理机票详细信息显示的消息
	private Handler _handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String response = (String) msg.obj;
				parserXml(response);
				break;
			case 2:
				String response2 = (String) msg.obj;
				if (parserXml2(response2).equals("succeessful1")) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("该航班已暂停售票！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Specific_Activity.this, M_Search_Activity.class);
							startActivity(intent);
							finish();
						}
					});
					dialog.show();
				} else if (parserXml2(response2).equals("succeessful2")) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("该航班已恢复售票！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Specific_Activity.this, M_Search_Activity.class);
							startActivity(intent);
							finish();
						}
					});
					dialog.show();

				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
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
			default:
				break;
			}
		}
	};

	// 修改航班为暂时停止售票
	private String parserXml2(String xmlData) {
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
						if (result.equals("succeessful1"))
							return "succeessful1";
						else if (result.equals("succeessful2"))
							return "succeessful2";
						else if (result.equals("failed"))
							return "failed";
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
		return "";
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

					// 从数据库读取9个参数
					if ("flight_num".equals(nodeName)) {
						String flight_numStr = parse.nextText();
						result += "航班号为：" + flight_numStr + ", ";
						flight_num_txt.setText(flight_numStr);
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "起点为" + goStr + ", ";
						go_txt.setText(goStr);
					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "终点为" + getStr + ", ";
						get_txt.setText(getStr);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "出发日期为" + dateStr + ", ";
						date_txt.setText(dateStr);
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "起飞时间为" + depart_timeStr + ", ";
						depart_time_txt.setText(depart_timeStr);
					} else if ("fly_time".equals(nodeName)) {
						String fly_timeStr = parse.nextText();
						result += "飞行时间为" + fly_timeStr + ", ";
						fly_time_txt.setText(fly_timeStr);
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "到达时间为" + land_timeStr + ", ";
						land_time_txt.setText(land_timeStr);
					} else if ("depart_airport".equals(nodeName)) {
						String depart_airportStr = parse.nextText();
						result += "起飞机场为" + depart_airportStr + ", ";
						depart_airport_txt.setText(depart_airportStr);
					} else if ("land_airport".equals(nodeName)) {
						String land_airportStr = parse.nextText();
						result += "降落机场为" + land_airportStr + ", ";
						land_airport_txt.setText(land_airportStr);
					}

					else if ("toudeng_yupiao".equals(nodeName)) {
						String toudeng_yu = parse.nextText();
						toudeng_yu_int = toudeng_yu; // 保留int数据 便于向后传递、识别。
						toudeng_yu = String.format("%s张", toudeng_yu);
						toudeng_yupiao.setText(toudeng_yu);
						result += "头等舱余票数为" + toudeng_yu + ", ";
					} else if ("toudeng_jiage".equals(nodeName)) {
						String toudeng_jia = parse.nextText();
						toudeng_jia_int = toudeng_jia;
						toudeng_jia = String.format("%s元", toudeng_jia);
						toudeng_piaojia.setText(toudeng_jia);
						result += "头等舱票价为" + toudeng_jia + ", ";
					} else if ("jingji_yupiao".equals(nodeName)) {
						String jingji_yu = parse.nextText();
						jingji_yu_int = jingji_yu;
						jingji_yu = String.format("%s张", jingji_yu);
						jingji_yupiao.setText(jingji_yu);
						result += "经济舱余票数为" + jingji_yu + ", ";
					} else if ("jingji_jiage".equals(nodeName)) {
						String jingji_jia = parse.nextText();
						jingji_jia_int = jingji_jia;
						jingji_jia = String.format("%s元", jingji_jia);
						jingji_piaojia.setText(jingji_jia);
						result += "经济舱票价为" + jingji_jia + ", ";
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
		// Log.d("resultStr", result);
	}

	private void readSpecificFlight(String flight_num2) {
		final String flight_num = flight_num2;
		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// 打开链接
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8");
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/speceficFlight.jsp?flight_num=" + flight_num2);

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

	private void stopFlight(String f, String ii) {
		final String flight_num = f;
		final String i = ii;

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// 打开链接
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8");
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/stopFlight.jsp?flight_num=" + flight_num2
							+ "&pre=" + i);

					Log.d("what", flight_num2 + " ???" + i);
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
