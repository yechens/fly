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
		requestWindowFeature(Window.FEATURE_NO_TITLE);		//���ر���
		setContentView(R.layout.activity_change_ip);
		
		change_ip = (EditText)findViewById(R.id.change_id);
		ip iip = new ip();
		str_ip = iip.getMy_ip();
		change_ip.setText(str_ip);		//δ�޸�ǰ��ip��ַ
		
		Button confirm = (Button)findViewById(R.id.confirm);
		confirm.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�޸�ip��ַ����
				str_ip = change_ip.getText().toString();	//д��onClick�ⲿ�޷���ã�����
				ip iip = new ip();
				iip.setMy_ip(str_ip);
				Intent intent = new Intent(ChangeIpActivity.this,EnterActivity.class);
				startActivity(intent);
			}
		});	
	}
}
