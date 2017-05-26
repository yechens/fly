package com.example.fly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ChangeIpActivity extends Activity {

	EditText change_ip;
	String str_ip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		//隐藏标题
		setContentView(R.layout.activity_change_ip);
		
		change_ip = (EditText)findViewById(R.id.change_id);
		ip iip = new ip();
		str_ip = iip.getMy_ip();
		change_ip.setText(str_ip);		//未修改前的ip地址
		
		Button confirm = (Button)findViewById(R.id.confirm);
		confirm.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//修改ip地址的类
				str_ip = change_ip.getText().toString();	//写在onClick外部无法获得！！！
				ip iip = new ip();
				iip.setMy_ip(str_ip);
				Intent intent = new Intent(ChangeIpActivity.this,EnterActivity.class);
				startActivity(intent);
			}
		});	
	}
}
