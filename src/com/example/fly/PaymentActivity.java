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

	int zonge, type; // �ܶ�+֧������
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

		Intent intent = getIntent(); // ���մ��ݹ��������˺š������... ��Ʊ����������18+2������

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
		money = String.format("�� %d", zonge);
		tvmoney.setText(money);

		String order = String.format("��Ʊ��%s �� %s", go, get);
		tvorder = (TextView) findViewById(R.id.tvorder);
		tvorder.setText(order);

		gaiqian = intent.getStringExtra("gaiqian");
		// ���Ǹ�ǩ״̬������ԭ��Ʊ�۸��ԭ����š���λ�ȼ�
		if (gaiqian.equals("yes")) {
			flight_num2 = intent.getStringExtra("flight_num2");
			yuanpiaojia = intent.getStringExtra("yuanpiaojia");
			clas_gaiqian = intent.getStringExtra("clas_gaiqian");
			order_num = intent.getStringExtra("order_num");
		}
		// Log.d("gaiqian", flight_num2+"???"+yuanpiaojia+"???"+clas_gaiqian);

		// RadioGroup rg = (RadioGroup)findViewById(R.id.payment); //��õ�ѡ��ť��
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
					dialog.setMessage("�Բ������˻������㣬���ȳ�ֵ!");
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
					// һ��jspҳ��ͬʱ��ɻ�Ʊ�����������˻��ۿ��Ʊ��Ʊ�����������
					addOrder(account_name, passenger, sex, birthday, nation, idcard, baoxian, clas, zonge, date, go,
							get, fly_time, depart_airport, land_airport, flight_num, yuE, yupiao, depart_time,
							land_time, order_num, gaiqian, yuanpiaojia, flight_num2, clas_gaiqian);
				}
			}
		});
		// ȡ�����ذ�ť
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
					dialog.setMessage("��ϲ����Ʊ�ɹ���ף����;���!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// �����ɹ��� ��ת����ѯ���߽���
							Intent intent = new Intent(PaymentActivity.this, SearchActivity.class);
							// SearchActivity��Ҫ����һ���û�������
							// ����ᷢ����ָ������³��������
							intent.putExtra("account", account_name);
							startActivity(intent);
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("�Բ���ϵͳ��æ�����Ժ�����һ�ԣ�");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ע��ɹ��� ��ת����ѯ���߽���
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
			XmlPullParser parse = factory.newPullParser(); // ���ɽ�����
			parse.setInput(new StringReader(xmlData)); // ���xml����
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
					// �򵥵��ж϶����Ƿ񴴽��ɹ����ɣ�
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
			String yyp, String ddep, String lla, String oor, String ggai, String yyuan, String ff2, String cc2) { // 18�������еĲ���+���+��Ʊ
																													// =
																													// 18������

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

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					// �����֤/���ա������ܶ�û�����Ʊ���������ġ��š�����Ϊ���ģ� ���඼��ҪUTF-8ת��
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

					// ������ϲ�����Ϊ�� ���޷�ִ������Ĵ��룬�Զ��ж�
					// Log.d("what", "????");

					// ����ҪΪString����
					String price = Integer.toString(total_price);

					ip iip = new ip();
					// ������
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
