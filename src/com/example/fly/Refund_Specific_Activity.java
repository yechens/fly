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

		Intent intent = getIntent(); // ���մ��ݹ������˺š���㡢�յ㡢���
		order_num = intent.getStringExtra("order_num");

		a1 = (TextView) findViewById(R.id.a1); // ������ֱ�����ã�
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

		// ͬ���û���Ʊ
		Button agree = (Button) findViewById(R.id.agree);
		agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
				dialog.setTitle("This is a warnining!");
				dialog.setMessage("��ȷ��ͬ����Ʊ��");
				dialog.setCancelable(false);
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ȷ��ȡ������,���ݹ�Ʊ�û����ܼۡ�����š�clas
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

		// �ܾ����û���Ʊ
		Button disagree = (Button) findViewById(R.id.disagree);
		disagree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
				dialog.setTitle("This is a warnining!");
				dialog.setMessage("��ȷ���ܾ���");
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
		// ȡ�����ذ�ť
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

	// ��Ʊ����
	private void AgreeRefund(String o, String a, String m, String c, String f) {
		final String order_num = o;
		final String account = a;
		final String money = m;
		final String clas = c;
		final String flight_num = f;

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// ������
					String account2 = URLEncoder.encode(account, "UTF-8"); // ����ת�룡
					String clas2 = URLEncoder.encode(clas, "UTF-8"); // ����ת�룡
					String flight_num2 = URLEncoder.encode(flight_num, "UTF-8"); // ����ת�룡
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/AgreeRefund.jsp?order_num=" + order_num + "&account="
									+ account2 + "&money=" + money + "&clas=" + clas2 + "&flight_num=" + flight_num2);

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
					dialog.setMessage("���ѳɹ�Ϊ���û���Ʊ��");
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
			case 3:
				String response3 = (String) msg.obj;
				if (parserXml2(response3)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(Refund_Specific_Activity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("���Ѿܾ����û���Ʊ��");
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
					dialog.setMessage("����ʧ�ܣ�����");
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
					// �����ݿ��ȡ15������
					if ("flight_num".equals(nodeName)) {
						String flight_numStr = parse.nextText();
						result += "�����Ϊ��" + flight_numStr + ", ";
						a11.setText(flight_numStr);
					} else if ("go".equals(nodeName)) {
						String goStr = parse.nextText();
						result += "���Ϊ" + goStr + ", ";
						a3.setText(goStr);
					} else if ("get".equals(nodeName)) {
						String getStr = parse.nextText();
						result += "�յ�Ϊ" + getStr + ", ";
						a4.setText(getStr);
					} else if ("date".equals(nodeName)) {
						String dateStr = parse.nextText();
						result += "��������Ϊ" + dateStr + ", ";
						a2.setText(dateStr);
					} else if ("depart_time".equals(nodeName)) {
						String depart_timeStr = parse.nextText();
						result += "���ʱ��Ϊ" + depart_timeStr + ", ";
						a5.setText(depart_timeStr);
					} else if ("fly_time".equals(nodeName)) {
						String fly_timeStr = parse.nextText();
						result += "����ʱ��Ϊ" + fly_timeStr + ", ";
						a6.setText(fly_timeStr);
					} else if ("land_time".equals(nodeName)) {
						String land_timeStr = parse.nextText();
						result += "����ʱ��Ϊ" + land_timeStr + ", ";
						a7.setText(land_timeStr);
					} else if ("depart_airport".equals(nodeName)) {
						String depart_airportStr = parse.nextText();
						result += "��ɻ���Ϊ" + depart_airportStr + ", ";
						a8.setText(depart_airportStr);
					} else if ("land_airport".equals(nodeName)) {
						String land_airportStr = parse.nextText();
						result += "�������Ϊ" + land_airportStr + ", ";
						a9.setText(land_airportStr);
					} else if ("clas".equals(nodeName)) {
						String clasStr = parse.nextText();
						result += "�ȼ�Ϊ" + clasStr + ", ";
						a10.setText(clasStr);
					} else if ("passenger".equals(nodeName)) {
						String passengerStr = parse.nextText();
						result += "�˿�Ϊ" + passengerStr + ", ";
						a12.setText(passengerStr);
					} else if ("passport".equals(nodeName)) {
						String passportStr = parse.nextText();
						result += "����Ϊ" + passportStr + ", ";
						a13.setText(passportStr);
					} else if ("nation".equals(nodeName)) {
						String nationStr = parse.nextText();
						result += "����Ϊ" + nationStr + ", ";
						a14.setText(nationStr);
					} else if ("money".equals(nodeName)) {
						String moneyStr = parse.nextText();
						money = moneyStr;
						result += "�����ܶ�Ϊ" + moneyStr + ", ";
						a15.setText("�� " + moneyStr);
					} else if ("other".equals(nodeName)) {
						String otherStr = parse.nextText();
						result += "����Ϊ" + otherStr + ", ";
						a16.setText(otherStr);
					} else if ("account_name".equals(nodeName)) {
						String account_name = parse.nextText();
						result += "��Ʊ�˻�Ϊ" + account_name + ", ";
						a17.setText(account_name);
					}
					break;
				case XmlPullParser.END_TAG:
					result += " \n ";
					Log.d("end_tag", "�ڵ����");
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
		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// ������
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/getOrderSpecific.jsp?order_num=" + order_num);

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

	// �ܾ��û���Ʊ����
	private void DisagreeRefund(String o) {
		final String order_num = o;
		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();
					// ������
					URL url = new URL(
							"http://" + iip.getMy_ip() + ":8080/pc/disagreeRefund.jsp?order_num=" + order_num);

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
					msg.what = 3;
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
