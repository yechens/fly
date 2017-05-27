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

	static int N = 4; // ���·���ݶ����5������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		Intent intent = getIntent();
		account = intent.getStringExtra("account");
		TextView act = (TextView) findViewById(R.id.act); // ��ʾ��¼�˺���
		act.setText(account);

		listRoute = (ListView) findViewById(R.id.listRoute); // ��ʾ���·�ߵ��б�
		qi = (EditText) findViewById(R.id.qi); // ����յ�༭��
		zhong = (EditText) findViewById(R.id.zhong);
		load(); // �����������·��

		// �����ʷ�� ��Ϣ�ϴ�����ѯ��
		listRoute.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				FlightInfo info = routeList.get(position); // �ж��û������һ������
				qi.setText(info.getGo());
				zhong.setText(info.getGet());
			}
		});

		if (account.equals("�����ο�")) { // �Ϻ����¼����ؼۻ�Ʊ 328Ԫ
			qi.setText("�Ϻ�");
			zhong.setText("�¼���");
		}

		// ����·�߰�ť
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				qidian = qi.getText().toString();
				zhongdian = zhong.getText().toString();

				// �������㡢�յ㲻��Ϊ��
				if (TextUtils.isEmpty(qidian) || TextUtils.isEmpty(zhongdian)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(SearchActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("��ѯ����㡢�յ㲻��Ϊ��Ŷ");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				} else {
					String str = String.format("%s������%s", qidian, zhongdian);

					int index = dataList.indexOf(str);
					if (index == -1) {
						// dataList.add(0,str); //ԭListView������

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

					SharedPreferences.Editor editor = pref.edit(); // д������
					// Ҫ�Ͷ�ȡ���ݵļ���ȫһ�� �����ȡʧ��
					editor.putString("Qi" + n, qidian);
					editor.putString("Zhong" + n, zhongdian);
					editor.putInt("point", (n + 1) % 4); // ����4������
					editor.commit();
					my_adapter = new RouteAdapter(SearchActivity.this, R.layout.route_item, routeList);
					listRoute.setAdapter(my_adapter);
					if (routeList.size() > 4) {
						routeList.remove(routeList.size() - 1);
					}

					Intent intent = new Intent(SearchActivity.this, FlightActivity.class);
					intent.putExtra("account", account); // ���Ʊҳ�洫���˺š���㡢�յ㡢���
					intent.putExtra("qidian", qidian);
					intent.putExtra("zhongdian", zhongdian);
					intent.putExtra("yuE", yuE);

					// ��ǩʱ����һ����Ϣ
					intent.putExtra("flight_num", "");
					intent.putExtra("yuanpiaojia", "");
					intent.putExtra("gaiqian", "no");
					intent.putExtra("clas_gaiqian", "");
					intent.putExtra("order_num", "");

					startActivity(intent);
					Toast.makeText(getApplicationContext(), "������Ҫ��˵ĺ��࣡", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});

		// ��ѯ�ҵĶ�����ť
		Button btnMyorder = (Button) findViewById(R.id.btnMyorder);
		btnMyorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchActivity.this, MyOrderActivity.class);
				// �����û��������
				intent.putExtra("account", account);
				intent.putExtra("yuE", yuE);
				startActivity(intent);
				finish();
			}
		});

		getYuE(account); // �Ȼ�ȡ����
		// ��ѯ��ť
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
		// ת����㡢�յ�ͼƬ
		image = (ImageView) findViewById(R.id.change); // ת���ص�ͼ��
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

		// �û����µ�¼
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

	public void load() { // ��������

		SharedPreferences pref = getSharedPreferences("route", MODE_PRIVATE);
		int point = pref.getInt("point", 0); // ???
		String q, z;
		for (int i = 0, n = point; i < N; i++) {
			q = pref.getString("Qi" + n, null);
			z = pref.getString("Zhong" + n, null);

			if (q != null && z != null) {
				String str = String.format("%s������%s", q, z);
				dataList.add(0, str); // ԭListView������
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
					if ("money".equals(nodeName)) {
						String money = parse.nextText();
						yuE = money; // ��ȡ�û������
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

	// ��ѯ��ȡ�û������
	private void getYuE(String account2) {
		final String account = account2;

		new Thread(new Runnable() { // �������߳�
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					ip iip = new ip();

					String account2 = URLEncoder.encode(account, "UTF-8"); // ����ת�룡
					// ������
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/getYuE.jsp?account=" + account2);

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
