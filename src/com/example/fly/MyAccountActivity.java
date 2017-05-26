package com.example.fly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MyAccountActivity extends Activity {

	String yuE, account,str;
	private TextView account_txt,money_txt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_account);
		
		Intent intent = getIntent();	//��ô��ݹ������û���������ֵ
		yuE = intent.getStringExtra("yuE");
		account = intent.getStringExtra("account");
		
		account_txt = (TextView)findViewById(R.id.account_txt);
		account = String.format("�𾴵�ȥ�Ķ��û� %s :", account);
		account_txt.setText( account );
		
		money_txt = (TextView)findViewById(R.id.money_txt);
		str = String.format("����ǰ�����Ϊ��%s Ԫ", yuE);
		money_txt.setText( str );
		
		//ȡ�����ذ�ť
		Button back = (Button)findViewById(R.id.backbtn);		
		back.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyAccountActivity.this,SearchActivity.class);
				intent.putExtra("account", account);
				startActivity(intent);	
				finish();		
			}
		});
	}
}
