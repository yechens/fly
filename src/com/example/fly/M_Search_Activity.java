package com.example.fly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class M_Search_Activity extends Activity {

	TextView qi, zhong;
	String qidian, zhongdian, account = "andy", yuE = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_m__search_);

		qi = (TextView) findViewById(R.id.m_qi);
		zhong = (TextView) findViewById(R.id.m_zhong);

		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(M_Search_Activity.this, AddActivity.class);
				startActivity(intent);
				finish();
			}
		});

		Button ask = (Button) findViewById(R.id.ask);
		ask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(M_Search_Activity.this, RefundActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// 管理员查询路线
		Button m_search = (Button) findViewById(R.id.m_search);
		m_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// 输入的起点、终点不能为空
				if (TextUtils.isEmpty(qi.getText().toString()) || TextUtils.isEmpty(zhong.getText().toString())) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(M_Search_Activity.this);
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
					Intent intent = new Intent(M_Search_Activity.this, FlightActivity.class);
					qidian = qi.getText().toString();
					zhongdian = zhong.getText().toString();
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
					finish();
				}
			}
		});
	}
}
