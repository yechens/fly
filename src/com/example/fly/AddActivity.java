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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends Activity {

	// 13个参数
	EditText flight_add, go_add, get_add, date_add, depart_time_add, fly_time_add, land_time_add, depart_airport_add,
			land_airport_add, toudeng_yupiao, toudeng_piaojia, jingji_yupiao, jingji_piaojia;
	String fli, go, get, dat, dep1, fly, lan1, dep2, lan2, tou1, tou2, jin1, jin2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add2);

		flight_add = (EditText) findViewById(R.id.flight_add);
		go_add = (EditText) findViewById(R.id.go_add);
		get_add = (EditText) findViewById(R.id.get_add);
		date_add = (EditText) findViewById(R.id.date_add);
		depart_time_add = (EditText) findViewById(R.id.depart_time_add);
		fly_time_add = (EditText) findViewById(R.id.fly_time_add);
		land_time_add = (EditText) findViewById(R.id.land_time_add);

		depart_airport_add = (EditText) findViewById(R.id.depart_airport_add);
		land_airport_add = (EditText) findViewById(R.id.land_airport_add);

		toudeng_yupiao = (EditText) findViewById(R.id.toudeng_yupiao);
		toudeng_piaojia = (EditText) findViewById(R.id.toudeng_piaojia);
		jingji_yupiao = (EditText) findViewById(R.id.jingji_yupiao);
		jingji_piaojia = (EditText) findViewById(R.id.jingji_piaojia);

		Button btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fli = flight_add.getText().toString();
				go = go_add.getText().toString();
				get = get_add.getText().toString();
				dat = date_add.getText().toString();
				dep1 = depart_time_add.getText().toString();
				fly = fly_time_add.getText().toString();
				lan1 = land_time_add.getText().toString();
				dep2 = depart_airport_add.getText().toString();
				lan2 = land_airport_add.getText().toString();

				tou1 = toudeng_yupiao.getText().toString();
				tou2 = toudeng_piaojia.getText().toString();
				jin1 = jingji_yupiao.getText().toString();
				jin2 = jingji_piaojia.getText().toString();

				addFlight(fli, go, get, dat, dep1, fly, lan1, dep2, lan2, tou1, tou2, jin1, jin2);
				// Log.d("lose", fli+" "+go+" "+get+" "+dat+" "+dep1+" "+fly+"
				// "+lan1+" "+dep2+" "+
				// lan2+" "+tou1+" "+tou2+" "+jin1+" "+jin2);
			}
		});

		// 取消按钮
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddActivity.this, M_Search_Activity.class);
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
				if (parserXml(response)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("航线新增成功!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 订航线新增成功后 跳转至查询航线界面
							Intent intent = new Intent(AddActivity.this, M_Search_Activity.class);
							startActivity(intent);
							finish();
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("对不起，系统出错了！");
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

	private boolean parserXml(String xmlData) {

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
					// 简单的判断订单是否创建成功即可！
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

	private void addFlight(String a, String b, String c, String d, String e, String f, String g, String h, String i,
			String j, String k, String l, String m) {
		final String fli = a;
		final String g1 = b;
		final String g2 = c;
		final String dat = d;
		final String dep1 = e;
		final String fly = f;
		final String lan1 = g;
		final String dep2 = h;
		final String lan2 = i;
		final String toudeng_yupiao = j;
		final String toudeng_jiage = k;
		final String jingji_yupiao = l;
		final String jingji_jiage = m;

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					String flight_num = URLEncoder.encode(fli, "UTF-8");
					String go = URLEncoder.encode(g1, "UTF-8");
					String get = URLEncoder.encode(g2, "UTF-8");
					String date = URLEncoder.encode(dat, "UTF-8");
					String depart_time = URLEncoder.encode(dep1, "UTF-8");
					String fly_time = URLEncoder.encode(fly, "UTF-8");
					String land_time = URLEncoder.encode(lan1, "UTF-8");
					String depart_airport = URLEncoder.encode(dep2, "UTF-8");
					String land_airport = URLEncoder.encode(lan2, "UTF-8");

					ip iip = new ip();
					// 打开链接
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/addFlight.jsp?flight_num=" + flight_num
							+ "&go=" + go + "&get=" + get + "&date=" + date + "&depart_time=" + depart_time
							+ "&fly_time=" + fly_time + "&land_time=" + land_time + "&depart_airport=" + depart_airport
							+ "&land_airport=" + land_airport + "&toudeng_yupiao=" + toudeng_yupiao + "&toudeng_jiage="
							+ toudeng_jiage + "&jingji_yupiao=" + jingji_yupiao + "&jingji_jiage=" + jingji_jiage);

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
