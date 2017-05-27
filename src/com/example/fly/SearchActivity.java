package com.example.fly;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {

	String account, yuE;
	String qidian, zhongdian;
	EditText qi, zhong;
	ImageView image;

	ListView listRoute;
	private List<String> dataList = new ArrayList<String>();
	private List<FlightInfo> routeList = new ArrayList<FlightInfo>();
	RouteAdapter my_adapter;

	static int N = 4; // 最近路线暂定最多5条数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		Intent intent = getIntent();
		account = intent.getStringExtra("account");
		TextView act = (TextView) findViewById(R.id.act); // 显示登录账号名
		act.setText(account);

		listRoute = (ListView) findViewById(R.id.listRoute); // 显示最近路线的列表
		qi = (EditText) findViewById(R.id.qi); // 起点终点编辑框
		zhong = (EditText) findViewById(R.id.zhong);
		load(); // 读入最近查找路线

		// 点击历史项 信息上传至查询框
		listRoute.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				FlightInfo info = routeList.get(position); // 判断用户点击哪一个子项
				qi.setText(info.getGo());
				zhong.setText(info.getGet());
			}
		});

		if (account.equals("匿名游客")) { // 上海到新加坡特价机票 328元
			qi.setText("上海");
			zhong.setText("新加坡");
		}

		// 查找路线按钮
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				qidian = qi.getText().toString();
				zhongdian = zhong.getText().toString();

				// 输入的起点、终点不能为空
				if (TextUtils.isEmpty(qidian) || TextUtils.isEmpty(zhongdian)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(SearchActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("查询的起点、终点不能为空哦");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				} else {
					String str = String.format("%s———%s", qidian, zhongdian);

					int index = dataList.indexOf(str);
					if (index == -1) {
						// dataList.add(0,str); //原ListView适配器

						FlightInfo info = new FlightInfo(qidian, zhongdian);
						routeList.add(0, info);
						dataList.add(0, str);

					} else {
						dataList.remove(index);
						routeList.remove(index);
						FlightInfo info = new FlightInfo(qidian, zhongdian);
						routeList.add(0, info);
						dataList.add(0, str);
					}
					SharedPreferences pref = getSharedPreferences("route", MODE_PRIVATE);
					int n = pref.getInt("point", 0);

					SharedPreferences.Editor editor = pref.edit(); // 写入数据
					// 要和读取数据的键完全一致 否则读取失败
					editor.putString("Qi" + n, qidian);
					editor.putString("Zhong" + n, zhongdian);
					editor.putInt("point", (n + 1) % 4); // 最多放4条代码
					editor.commit();
					my_adapter = new RouteAdapter(SearchActivity.this, R.layout.route_item, routeList);
					listRoute.setAdapter(my_adapter);
					if (routeList.size() > 4) {
						routeList.remove(routeList.size() - 1);
					}

					Intent intent = new Intent(SearchActivity.this, FlightActivity.class);
					intent.putExtra("account", account); // 向机票页面传递账号、起点、终点、余额
					intent.putExtra("qidian", qidian);
					intent.putExtra("zhongdian", zhongdian);
					intent.putExtra("yuE", yuE);

					// 改签时传递一下信息
					intent.putExtra("flight_num", "");
					intent.putExtra("yuanpiaojia", "");
					intent.putExtra("gaiqian", "no");
					intent.putExtra("clas_gaiqian", "");
					intent.putExtra("order_num", "");

					startActivity(intent);
					Toast.makeText(getApplicationContext(), "请点击您要搭乘的航班！", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});

		// 查询我的订单按钮
		Button btnMyorder = (Button) findViewById(R.id.btnMyorder);
		btnMyorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchActivity.this, MyOrderActivity.class);
				// 传递用户名、余额
				intent.putExtra("account", account);
				intent.putExtra("yuE", yuE);
				startActivity(intent);
				finish();
			}
		});

		getYuE(account); // 先获取到余额。
		// 查询余额按钮
		Button btnMyaccount = (Button) findViewById(R.id.btnMyaccount);
		btnMyaccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchActivity.this, MyAccountActivity.class);
				intent.putExtra("yuE", yuE);
				intent.putExtra("account", account);
				startActivity(intent);
				finish();
			}
		});
		// 转换起点、终点图片
		image = (ImageView) findViewById(R.id.change); // 转换地点图标
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ti = qi.getText().toString();
				String t2 = zhong.getText().toString();
				qi.setText(t2);
				zhong.setText(ti);
			}
		});

		// 用户重新登录
		Button tuichu = (Button) findViewById(R.id.back7);
		tuichu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchActivity.this, EnterActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public void load() { // 读入数据

		SharedPreferences pref = getSharedPreferences("route", MODE_PRIVATE);
		int point = pref.getInt("point", 0); // ???
		String q, z;
		for (int i = 0, n = point; i < N; i++) {
			q = pref.getString("Qi" + n, null);
			z = pref.getString("Zhong" + n, null);

			if (q != null && z != null) {
				String str = String.format("%s———%s", q, z);
				dataList.add(0, str); // 原ListView适配器
				FlightInfo info = new FlightInfo(q, z);
				routeList.add(info);
			}
			n = n > 0 ? (--n) : (--n + N) % 4; // ???????
		}
		my_adapter = new RouteAdapter(SearchActivity.this, R.layout.route_item, routeList);
		listRoute.setAdapter(my_adapter);
	}

	private Handler _handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String response = (String) msg.obj;
				parserXml(response);
				break;
			default:
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

			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();
				result += nodeName;
				result += ", ";

				switch (eventType) {

				case XmlPullParser.START_TAG:
					if ("money".equals(nodeName)) {
						String money = parse.nextText();
						yuE = money; // 获取用户的余额
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
	}

	// 查询获取用户的余额
	private void getYuE(String account2) {
		final String account = account2;

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();

					String account2 = URLEncoder.encode(account, "UTF-8"); // 中文转译！
					// 打开链接
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/getYuE.jsp?account=" + account2);

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
