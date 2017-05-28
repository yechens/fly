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
	// ������ϸ��Ϣ������Ҫ��ʾ��TextView
	private TextView flight_num_txt, go_txt, get_txt, date_txt, depart_time_txt, fly_time_txt, land_time_txt,
			depart_airport_txt, land_airport_txt, toudeng_yupiao, toudeng_piaojia, jingji_yupiao, jingji_piaojia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ���ر���

		// buy_tou = (Button)findViewById(R.id.buy_tou);
		// buy_jing = (Button)findViewById(R.id.buy_jing);

		Intent intent = getIntent(); // ���մ��ݹ������˺š�����š����
		account = intent.getStringExtra("account");

		// �жϺ�����Ʊ�Ƿ���ʱ�ر�
		condition = intent.getStringExtra("condition");

		if (account.equals("andy")) { // ���ǹ���Ա��ѯ·��
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

		gaiqian = intent.getStringExtra("gaiqian"); // �жϵ�ǰ�Ƿ�Ϊ��ǩ״̬
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

		readSpecificFlight(flight_num); // ���ݺ���Ŷ�ȡ������ϸ��Ϣ

		if (account.equals("andy")) { // ���ֱ�������Button stop ��������������
			// ����Ա���ñ��κ���
			Button stop = (Button) findViewById(R.id.stop);

			if (condition.equals("1")) {
				i = "0";
				stop.setText("����ָ���Ʊ");
			} else if (condition.equals("0")) {
				i = "1";
				stop.setText("������ͣ��Ʊ");
			}

			stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					stopFlight(flight_num, i);
				}
			});
		} else {
			// ��Ʊͷ�Ȳգ�
			Button buy_tou = (Button) findViewById(R.id.buy_tou);
			buy_tou.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (condition.equals("1")) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("�Բ��𣬱��κ�����ͣ��Ʊ��");
						dialog.setCancelable(false);
						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						dialog.show();
					}
					// ��Ʊ������0 �����ܹ�Ʊ
					else if (Integer.parseInt(toudeng_yu_int) > 0) {
						// BuyTicketTouActivityΪ����ͷ�Ȳա����òչ��û
						Intent intent = new Intent(Specific_Activity.this, BuyTicketTouActivity.class);

						// ��Ʊ���洫�� ���˻������ڡ���㡢�յ㡢����š�Ʊ�ۡ���Ʊ������λ�ȼ�...
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

						String ad = "����ͷ�Ȳ�����        	      20kg�����";
						intent.putExtra("ad", ad);
						String title = "ͷ�Ȳչ�Ʊר��";
						intent.putExtra("title", title);

						String clas = "ͷ�Ȳ�";
						intent.putExtra("clas", clas);

						intent.putExtra("gaiqian", gaiqian);
						if (gaiqian.equals("yes")) {
							intent.putExtra("flight_num2", flight_num2);
							intent.putExtra("yuanpiaojia", yuanpiaojia);
							intent.putExtra("clas_gaiqian", clas_gaiqian);
							intent.putExtra("order_num", order_num); // ԭ��Ʊ������
						}
						startActivity(intent);
						finish();
					} else {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("�Բ���������ĺ�����������");
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

			// ��Ʊ���òգ�
			Button buy_jing = (Button) findViewById(R.id.buy_jing);
			buy_jing.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (condition.equals("1")) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("�Բ��𣬱��κ�����ͣ��Ʊ��");
						dialog.setCancelable(false);
						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
						dialog.show();
					} else if (Integer.parseInt(jingji_yu_int) > 0) {
						// BuyTicketTouActivityΪ����ͷ�Ȳա����òչ��û
						Intent intent = new Intent(Specific_Activity.this, BuyTicketTouActivity.class);

						// ��Ʊ���洫���˻������ڡ���㡢�յ㡢����š�Ʊ�ۡ���Ʊ������λ�ȼ�...
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

						String ad = "������7kg��������";
						intent.putExtra("ad", ad);
						String title = "���òչ�Ʊ��";
						intent.putExtra("title", title);

						String clas = "���ò�";
						intent.putExtra("clas", clas);

						intent.putExtra("gaiqian", gaiqian);
						if (gaiqian.equals("yes")) {
							intent.putExtra("flight_num2", flight_num2);
							intent.putExtra("yuanpiaojia", yuanpiaojia);
							intent.putExtra("clas_gaiqian", clas_gaiqian);
							intent.putExtra("order_num", order_num); // ԭ��Ʊ������
						}

						startActivity(intent);
						finish();
					} else {
						AlertDialog.Builder dialog = new AlertDialog.Builder(Specific_Activity.this);
						dialog.setTitle("This is a warnining!");
						dialog.setMessage("�Բ���������ĺ�����������");
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

			// ȡ�����ذ�ť
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

	// �����Ʊ��ϸ��Ϣ��ʾ����Ϣ
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
					dialog.setMessage("�ú�������ͣ��Ʊ��");
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
					dialog.setMessage("�ú����ѻָ���Ʊ��");
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
					dialog.setMessage("����ʧ�ܣ�");
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

	// �޸ĺ���Ϊ��ʱֹͣ��Ʊ
	private String parserXml2(String xmlData) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser(); // ���ɽ�����
			parse.setInput(new StringReader(xmlData)); // ���xml����
			int eventType = parse.getEventType();
			String str = String.format(" type = %d, str = %s\n", eventType, parse.getName());
			Log.d("xmlStr", str);

			while (eventType != XmlPullParser.END_DOCUMENT) {

				String nodeName = parse.getName();
				switch (eventType) {

				case XmlPullParser.START_TAG:
					// �����ݿ��ȡ15������
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

					// �����ݿ��ȡ9������
					if ("flight_num".equals(nodeName)) {
						String flight_numStr = parse.nextText();
						result += "�����Ϊ��" + flight_numStr + ", ";
						flight_num_txt.setText(flight_numStr);
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "���Ϊ" + goStr + ", ";
						go_txt.setText(goStr);
					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "�յ�Ϊ" + getStr + ", ";
						get_txt.setText(getStr);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "��������Ϊ" + dateStr + ", ";
						date_txt.setText(dateStr);
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "���ʱ��Ϊ" + depart_timeStr + ", ";
						depart_time_txt.setText(depart_timeStr);
					} else if ("fly_time".equals(nodeName)) {
						String fly_timeStr = parse.nextText();
						result += "����ʱ��Ϊ" + fly_timeStr + ", ";
						fly_time_txt.setText(fly_timeStr);
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "����ʱ��Ϊ" + land_timeStr + ", ";
						land_time_txt.setText(land_timeStr);
					} else if ("depart_airport".equals(nodeName)) {
						String depart_airportStr = parse.nextText();
						result += "��ɻ���Ϊ" + depart_airportStr + ", ";
						depart_airport_txt.setText(depart_airportStr);
					} else if ("land_airport".equals(nodeName)) {
						String land_airportStr = parse.nextText();
						result += "�������Ϊ" + land_airportStr + ", ";
						land_airport_txt.setText(land_airportStr);
					}

					else if ("toudeng_yupiao".equals(nodeName)) {
						String toudeng_yu = parse.nextText();
						toudeng_yu_int = toudeng_yu; // ����int���� ������󴫵ݡ�ʶ��
						toudeng_yu = String.format("%s��", toudeng_yu);
						toudeng_yupiao.setText(toudeng_yu);
						result += "ͷ�Ȳ���Ʊ��Ϊ" + toudeng_yu + ", ";
					} else if ("toudeng_jiage".equals(nodeName)) {
						String toudeng_jia = parse.nextText();
						toudeng_jia_int = toudeng_jia;
						toudeng_jia = String.format("%sԪ", toudeng_jia);
						toudeng_piaojia.setText(toudeng_jia);
						result += "ͷ�Ȳ�Ʊ��Ϊ" + toudeng_jia + ", ";
					} else if ("jingji_yupiao".equals(nodeName)) {
						String jingji_yu = parse.nextText();
						jingji_yu_int = jingji_yu;
						jingji_yu = String.format("%s��", jingji_yu);
						jingji_yupiao.setText(jingji_yu);
						result += "���ò���Ʊ��Ϊ" + jingji_yu + ", ";
					} else if ("jingji_jiage".equals(nodeName)) {
						String jingji_jia = parse.nextText();
						jingji_jia_int = jingji_jia;
						jingji_jia = String.format("%sԪ", jingji_jia);
						jingji_piaojia.setText(jingji_jia);
						result += "���ò�Ʊ��Ϊ" + jingji_jia + ", ";
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
		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// ������
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8");
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/speceficFlight.jsp?flight_num=" + flight_num2);

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

	private void stopFlight(String f, String ii) {
		final String flight_num = f;
		final String i = ii;

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// ������
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8");
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/stopFlight.jsp?flight_num=" + flight_num2
							+ "&pre=" + i);

					Log.d("what", flight_num2 + " ???" + i);
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
