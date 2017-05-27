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

		new_act_edit = (EditText) findViewById(R.id.new_act); // 新注册账户
		new_psd1_edit = (EditText) findViewById(R.id.new_psd1); // 新密码
		new_psd2_edit = (EditText) findViewById(R.id.new_psd2); // 确认密码
		phone_edit = (EditText) findViewById(R.id.phone); // 手机号码

		// 跳转至飞新加坡仅328元rmb的特价航线 用户名为“匿名” 不能购票 仅查看功能
		Button tejia = (Button) findViewById(R.id.tejia);
		tejia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String account = "匿名游客";
				Intent intent = new Intent(RegisterActivity.this, SearchActivity.class);
				intent.putExtra("account", account);
				startActivity(intent);
			}
		});

		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new_act = new_act_edit.getText().toString(); // 获取各个控件的值
				new_psd2 = new_psd2_edit.getText().toString();
				new_psd1 = new_psd1_edit.getText().toString();
				phone = phone_edit.getText().toString();
				RadioGroup rg2 = (RadioGroup) findViewById(R.id.type2); // 获得单选按钮组
				rg2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						int RadioBtnId = group.getCheckedRadioButtonId();
						switch (RadioBtnId) {
						case R.id.man:
							sex = "Gentleman"; // 获得性别
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

				// 账户不能为空
				if (TextUtils.isEmpty(new_act)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("注册账户不能为空哦!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				// 密码不能为空
				else if (TextUtils.isEmpty(new_psd1) || TextUtils.isEmpty(new_psd2)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("密码不能为空哦!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				// 两次输入的密码不一致
				else if (!new_psd1.equals(new_psd2)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("您两次输入的密码不一致哦!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog.show();
				}
				// 手机号码不能为空
				else if (TextUtils.isEmpty(phone)) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("手机号码不能为空哦!");
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

		Button back = (Button) findViewById(R.id.back); // 取消注册 直接返回
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// 修改ip地址的按钮
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
					dialog.setMessage("恭喜您注册成功!");
					dialog.setCancelable(false);
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 注册成功后 跳转至登录界面
							Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
							startActivity(intent);
						}
					});
					dialog.show();
				} else {
					AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
					dialog.setTitle("This is a warnining!");
					dialog.setMessage("对不起，您注册的账户名已存在!");
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
		final String a = aa; // 进程中不能传入变量 一定要为常量final
		final String p = pp;
		final String s = ss;
		final String cell = celll;

		new Thread(new Runnable() { // 开启子线程
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					String a2 = URLEncoder.encode(a, "UTF-8"); // 中文转译！

					ip iip = new ip();
					// 打开链接
					URL url = new URL("http://" + iip.getMy_ip() + ":8080/pc/addClient.jsp?account=" + a2 + "&password="
							+ p + "&sex=" + s + "&phone=" + cell);

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
