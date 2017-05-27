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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PaymentActivity extends Activity {

	int zonge, type; // 总额+支付种类
	String yuE, money, order, yupiao;
	String account_name, passenger, sex, birthday, nation, idcard, baoxian, clas, date, go, get, fly_time,
			depart_airport, land_airport, flight_num, depart_time, land_time;
	TextView tvmoney, tvorder;
	String yuanpiaojia = "0", flight_num2 = "0", gaiqian = "no", clas_gaiqian = "0", order_num = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payment);

		Intent intent = getIntent(); // 接收传递过来的余额、账号、航班号... 机票订单表中有18+2个参数

		yuE = intent.getStringExtra("yuE");
		yupiao = intent.getStringExtra("yupiao");

		account_name = intent.getStringExtra("account");
		passenger = intent.getStringExtra("passenger");
		sex = intent.getStringExtra("sex");
		birthday = intent.getStringExtra("birthday");
		nation = intent.getStringExtra("nation");
		idcard = intent.getStringExtra("idcard");
		baoxian = intent.getStringExtra("baoxian");
		clas = intent.getStringExtra("clas");
		zonge = intent.getIntExtra("zonge", -1);
		date = intent.getStringExtra("date");
		go = intent.getStringExtra("go");
		get = intent.getStringExtra("get");
		fly_time = intent.getStringExtra("fly_time");
		depart_time = intent.getStringExtra("depart_time");
		land_time = intent.getStringExtra("land_time");

		depart_airport = intent.getStringExtra("depart_airport");
		land_airport = intent.getStringExtra("land_airport");
		flight_num = intent.getStringExtra("flight_num");

		tvmoney = (TextView) findViewById(R.id.tvmoney);
		money = String.format("￥ %d", zonge);
		tvmoney.setText(money);

		String order = String.format("机票：%s → %s", go, get);
		tvorder = (TextView) findViewById(R.id.tvorder);
		tvorder.setText(order);

		gaiqian = intent.getStringExtra("gaiqian");
		// 若是改签状态，接收原机票价格和原航班号、舱位等级
		if (gaiqian.equals("yes")) {
			flight_num2 = intent.getStringExtra("flight_num2");
			yuanpiaojia = intent.getStringExtra("yuanpiaojia");
			clas_gaiqian = intent.getStringExtra("clas_gaiqian");
			order_num = intent.getStringExtra("order_num");
		}
		// Log.d("gaiqian", flight_num2+"???"+yuanpiaojia+"???"+clas_gaiqian);

		// RadioGroup rg = (RadioGroup)findViewById(R.id.payment); //获得单选按钮组
		// rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// // TODO Auto-generated method stub
		// int RadioBtnId = group.getCheckedRadioButtonId();
		// switch(RadioBtnId){
		// case R.id.by_zfb:
		// type = 1;
		// break;
		// case R.id.by_wechat:
		// type = 2;
		// break;
		// case R.id.by_unionpay:
		// type = 3;
		// break;
		// case R.id.qianbao:
		// type = 4;
		// break;
		// }
		// }
		// });

		Button pay_btn = (Button) findViewById(R.id.pay_btn);
		pay_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int myaccount = Integer.parseInt(yuE);
				if (myaccount + Integer.parseInt(yuanpiaojia) < zonge) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("对不起，您账户的余额不足，请先充值!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(PaymentActivity.this, SearchActivity.class);
							intent.putExtra("account", account_name);
							startActivity(intent);
							finish();
						}
					});
					dialog.show();
				} else {
					// 一个jsp页面同时完成机票订单新增、账户扣款、机票余票更新三项操作
					addOrder(account_name, passenger, sex, birthday, nation, idcard, baoxian, clas, zonge, date, go,
							get, fly_time, depart_airport, land_airport, flight_num, yuE, yupiao, depart_time,
							land_time, order_num, gaiqian, yuanpiaojia, flight_num2, clas_gaiqian);
				}
			}
		});
		// 取消返回按钮
		Button cancel = (Button) findViewById(R.id.back5);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PaymentActivity.this, SearchActivity.class);
				intent.putExtra("account", account_name);
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
					AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("恭喜您订票成功，祝您旅途愉快!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 订单成功后 跳转至查询航线界面
							Intent intent = new Intent(PaymentActivity.this, SearchActivity.class);
							// SearchActivity需要接受一个用户名参数
							// 否则会发生空指针错误导致程序崩溃！
							intent.putExtra("account", account_name);
							startActivity(intent);
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("对不起，系统正忙请您稍后再试一试！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 注册成功后 跳转至查询航线界面
							Intent intent = new Intent(PaymentActivity.this, SearchActivity.class);
							startActivity(intent);
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
						Log.d("whether", result);
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

	private void addOrder(String aa, String pp, String ss, String bb, String nn, String ii, String bba, String cc,
			int zz, String dd, String ggo, String gge, String ff, String dde, String ll, String ffl, String yy,
			String yyp, String ddep, String lla, String oor, String ggai, String yyuan, String ff2, String cc2) { // 18个订单中的参数+余额+余票
																													// =
																													// 18个参数

		final String account_name = aa;
		final String passenger = pp;
		final String sex = ss;
		final String birthday = bb;
		final String nation = nn;
		final String idcard = ii;
		final String baoxian = bba;
		final String clas = cc;
		final int total_price = zz;
		final String date = dd;
		final String go = ggo;
		final String get = gge;
		final String fly_time = ff;
		final String depart_airport = dde;
		final String land_airport = ll;
		final String flight_num = ffl;
		final String yuE = yy;
		final String yupiao = yyp;
		final String depart_time = ddep;
		final String land_time = lla;

		final String order_num = oor;
		final String gaiqian = ggai;
		final String yuanpiaojia = yyuan;
		final String flight_num_gai = ff2;
		final String clas_gaiqian = cc2;

		Log.d("gaiqian",
				order_num + "???" + clas_gaiqian + "???" + flight_num_gai + "???" + yuanpiaojia + "???" + gaiqian);

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					// 除身份证/护照、订单总额、用户余额、余票（不含中文‘张’）不为中文， 其余都需要UTF-8转码
					String account_name2 = URLEncoder.encode(account_name, "UTF-8");
					String passenger2 = URLEncoder.encode(passenger, "UTF-8");
					String sex2 = URLEncoder.encode(sex, "UTF-8");
					String birthday2 = URLEncoder.encode(birthday, "UTF-8");
					String nation2 = URLEncoder.encode(nation, "UTF-8");
					String baoxian2 = URLEncoder.encode(baoxian, "UTF-8");
					String clas2 = URLEncoder.encode(clas, "UTF-8");
					String date2 = URLEncoder.encode(date, "UTF-8");
					String go2 = URLEncoder.encode(go, "UTF-8");
					String get2 = URLEncoder.encode(get, "UTF-8");
					String fly_time2 = URLEncoder.encode(fly_time, "UTF-8");
					String depart_airport2 = URLEncoder.encode(depart_airport, "UTF-8");
					String land_airport2 = URLEncoder.encode(land_airport, "UTF-8");
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8");
					String depart_time2 = URLEncoder.encode(depart_time, "UTF-8");
					String land_time2 = URLEncoder.encode(land_time, "UTF-8");
					String flight_num_gai2 = URLEncoder.encode(flight_num_gai, "UTF-8");
					String clas_gaiqian2 = URLEncoder.encode(clas_gaiqian, "UTF-8");

					// 如果以上参数有为空 则无法执行下面的代码，自动中断
					// Log.d("what", "????");

					// 传参要为String类型
					String price = Integer.toString(total_price);

					ip iip = new ip();
					// 打开链接
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/addOrder.jsp?account_name=" + account_name2
							+ "&passenger=" + passenger2 + "&sex=" + sex2 + "&birthday=" + birthday2 + "&nation="
							+ nation2 + "&idcard=" + idcard + "&baoxian=" + baoxian2 + "&clas=" + clas2
							+ "&total_price=" + price + "&date=" + date2 + "&go=" + go2 + "&get=" + get2 + "&fly_time="
							+ fly_time2 + "&depart_airport=" + depart_airport2 + "&land_airport=" + land_airport2
							+ "&flight_num=" + flight_num2 + "&yuE=" + yuE + "&yupiao=" + yupiao + "&depart_time="
							+ depart_time2 + "&land_time=" + land_time2 + "&order_num=" + order_num + "&gaiqian="
							+ gaiqian + "&yuanpiaojia=" + yuanpiaojia + "&flight_num_gai2=" + flight_num_gai2
							+ "&clas_gaiqian=" + clas_gaiqian2);

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
