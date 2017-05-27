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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends Activity {

	private String new_act, new_psd1, new_psd2, phone, sex = "Gentleman";
	EditText new_act_edit, new_psd1_edit, new_psd2_edit, phone_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		new_act_edit = (EditText) findViewById(R.id.new_act); // ��ע���˻�
		new_psd1_edit = (EditText) findViewById(R.id.new_psd1); // ������
		new_psd2_edit = (EditText) findViewById(R.id.new_psd2); // ȷ������
		phone_edit = (EditText) findViewById(R.id.phone); // �ֻ�����

		// ��ת�����¼��½�328Ԫrmb���ؼۺ��� �û���Ϊ�������� ���ܹ�Ʊ ���鿴����
		Button tejia = (Button) findViewById(R.id.tejia);
		tejia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String account = "�����ο�";
				Intent intent = new Intent(RegisterActivity.this, SearchActivity.class);
				intent.putExtra("account", account);
				startActivity(intent);
			}
		});

		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new_act = new_act_edit.getText().toString(); // ��ȡ�����ؼ���ֵ
				new_psd2 = new_psd2_edit.getText().toString();
				new_psd1 = new_psd1_edit.getText().toString();
				phone = phone_edit.getText().toString();
				RadioGroup rg2 = (RadioGroup) findViewById(R.id.type2); // ��õ�ѡ��ť��
				rg2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						int RadioBtnId = group.getCheckedRadioButtonId();
						switch (RadioBtnId) {
						case R.id.man:
							sex = "Gentleman"; // ����Ա�
							break;
						case R.id.woman:
							sex = "Lady";
							break;
						}
					}
				});
				// Toast.makeText(getApplicationContext(), new_act+"
				// "+new_psd1+" "+sex+" "+phone,
				// Toast.LENGTH_SHORT).show();

				// �˻�����Ϊ��
				if (TextUtils.isEmpty(new_act)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("ע���˻�����Ϊ��Ŷ!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				// ���벻��Ϊ��
				else if (TextUtils.isEmpty(new_psd1) || TextUtils.isEmpty(new_psd2)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("���벻��Ϊ��Ŷ!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				// ������������벻һ��
				else if (!new_psd1.equals(new_psd2)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("��������������벻һ��Ŷ!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				// �ֻ����벻��Ϊ��
				else if (TextUtils.isEmpty(phone)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("�ֻ����벻��Ϊ��Ŷ!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				} else
					addClient(new_act, new_psd1, sex, phone);
			}
		});

		Button back = (Button) findViewById(R.id.back); // ȡ��ע�� ֱ�ӷ���
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// �޸�ip��ַ�İ�ť
		Button ip = (Button) findViewById(R.id.ip);
		ip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, ChangeIpActivity.class);
				startActivity(intent);
			}
		});
	}

	private Handler _handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String response = (String) msg.obj;
				if (parserXml(response)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("��ϲ��ע��ɹ�!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ע��ɹ��� ��ת����¼����
							Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
							startActivity(intent);
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("�Բ�����ע����˻����Ѵ���!");
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

	private void addClient(String aa, String pp, String ss, String celll) {
		final String a = aa; // �����в��ܴ������ һ��ҪΪ����final
		final String p = pp;
		final String s = ss;
		final String cell = celll;

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					String a2 = URLEncoder.encode(a, "UTF-8"); // ����ת�룡

					ip iip = new ip();
					// ������
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/addClient.jsp?account=" + a2 + "&password="
							+ p + "&sex=" + s + "&phone=" + cell);

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
