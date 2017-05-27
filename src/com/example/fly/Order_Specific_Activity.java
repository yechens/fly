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

		// 17���ı���
		chupiao = (TextView) findViewById(R.id.chupiao);
		if (refund.equals("0") || refund.equals("3"))
			chupiao.setText("�ɹ���Ʊ");
		else if (refund.equals("1")) {
			chupiao.setText("����ȡ����..");
			chupiao.setTextColor(Color.rgb(0, 0, 0));
		} else if (refund.equals("2")) {
			chupiao.setText("������ȡ��");
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
				if (chupiao.getText().toString().equals("����ȡ����..") || chupiao.getText().toString().equals("������ȡ��")) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Order_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("���Ķ�������ȡ��������Ʊ!");
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
					dialog.setMessage("��ǰ��Ʊ�����ϣ���ȷ��Ҫ��ǩ��");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							// ȷ����ǩ.�����û�����ԭ����š�ԭƱ��
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

							Toast.makeText(getApplicationContext(), "������ѡ�񺽰�", Toast.LENGTH_LONG);
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
		// ������Ʊ
		Button tuipiao = (Button) findViewById(R.id.tuipiao);
		tuipiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (chupiao.getText().toString().equals("����ȡ����..") || chupiao.getText().toString().equals("������ȡ��")) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Order_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("���Ķ�������ȡ��������Ʊ!");
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
					dialog.setMessage("��ǰ��Ʊ�����ϣ���ȷ��Ҫ��Ʊ��");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							// ȷ��ȡ�������������Ա�ύ����
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

	// ��������ϸ��Ϣ��ʾ����Ϣ
	private Handler _handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // �����ǩ
				String response = (String) msg.obj;
				parserXml(response);
				break;
			case 2: // ������Ʊ
				String response2 = (String) msg.obj;
				if (parserXml2(response2)) {
					Intent intent = new Intent(Order_Specific_Activity.this, MyOrderActivity.class);
					intent.putExtra("account", account);
					intent.putExtra("yuE", yuE);

					Toast.makeText(getApplicationContext(), "�����������ύ�������ĵȴ���", Toast.LENGTH_LONG);
					startActivity(intent);
					finish();
				}
				break;
			default:
				break;
			}
		}
	};

	// ȷ�϶����Ƿ�ɹ��ύ����
	private boolean parserXml2(String xmlData) {

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

	// ��������Ʊ��ϸ��Ϣ��ʾ����Ϣ
	private void parserXml(String xmlData) {
		String result = "";
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser(); // ���ɽ�����
			parse.setInput(new StringReader(xmlData)); // ���xml����
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);

			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();
				result += nodeName;
				result += ", ";

				switch (eventType) {

				case XmlPullParser.START_TAG:

					// �����ݿ��ȡ16������
					if ("order_num".equals(nodeName)) {
						String order_num_Str = parse.nextText();
						result += "������Ϊ" + order_num_Str + ", ";
						ordr.setText(order_num_Str);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "��������Ϊ" + dateStr + ", ";
						date.setText(dateStr);
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "���Ϊ" + goStr + ", ";
						qi.setText(goStr);
					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "�յ�Ϊ" + getStr + ", ";
						zhong.setText(getStr);
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "���ʱ��Ϊ" + depart_timeStr + ", ";
						time1.setText(depart_timeStr);
					} else if ("fly_time".equals(nodeName)) {
						String fly_timeStr = parse.nextText();
						result += "����ʱ��Ϊ" + fly_timeStr + ", ";
						time2.setText(fly_timeStr);
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "����ʱ��Ϊ" + land_timeStr + ", ";
						time3.setText(land_timeStr);
					} else if ("depart_airport".equals(nodeName)) {
						String depart_airportStr = parse.nextText();
						result += "��ɻ���Ϊ" + depart_airportStr + ", ";
						airport1.setText(depart_airportStr);
					} else if ("land_airport".equals(nodeName)) {
						String land_airportStr = parse.nextText();
						result += "�������Ϊ" + land_airportStr + ", ";
						airport2.setText(land_airportStr);
					} else if ("clas".equals(nodeName)) {
						String classs = parse.nextText();
						result += "��λ�ȼ�Ϊ" + classs + ", ";
						clas.setText(classs);
					} else if ("flight_num".equals(nodeName)) {
						String flight_num = parse.nextText();
						result += "�����Ϊ" + flight_num + ", ";
						f_n.setText(flight_num);
					} else if ("passenger".equals(nodeName)) {
						String passenger = parse.nextText();
						result += "�˻���Ϊ" + passenger + ", ";
						psg.setText(passenger);
					} else if ("passport".equals(nodeName)) {
						String passport = parse.nextText();
						result += "�����Ϊ" + passport + ", ";
						psp.setText(passport);
					} else if ("nation".equals(nodeName)) {
						String nation = parse.nextText();
						result += "����Ϊ" + nation + ", ";
						nat.setText(nation);
					} else if ("money".equals(nodeName)) {
						money = parse.nextText();
						String money2 = money;
						money2 = String.format("��%s", money2);
						mon.setText(money2);
						result += "�����ܼ�Ϊ" + money2 + ", ";
					} else if ("other".equals(nodeName)) {
						String other = parse.nextText();
						result += "����Ϊ" + other + ", ";
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

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {

					ip iip = new ip();
					// ������
					String accunt2 = URLEncoder.encode(account, "UTF-8");
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8");
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/speceficOrder.jsp?flight_num="
							+ flight_num2 + "&account=" + accunt2);

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

	private void RefundRequest(String o) {
		final String order_num = o;

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// ������
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/refundRequest.jsp?order_num=" + order_num);

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
					msg.what = 2;
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
