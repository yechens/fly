package com.example.fly;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BuyTicketTouActivity extends Activity {

	String yuE, account, date, go, get, fly_time, depart_airport, land_airport, flight_num, depart_time, land_time;
	String piaojia, yupiao, ad, title;
	String passenger, sex, birthday, nation, idcard, baoxian, clas;
	int zonge;static int chajia;
	long startTime = 0;
	private TextView title_txt, date_txt, go_txt, get_txt, fly_time_txt, 
		depart_airport_txt, land_airport_txt, account_txt,
		serve_txt, total_price_txt ;
	private EditText passenger_edit, birthday_edit, nation_edit, idcard_edit;
	private CheckBox yanwu,yiwai;
	String yuanpiaojia, flight_num2, gaiqian, clas_gaiqian, order_num ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		//隐藏标题
		setContentView(R.layout.activity_buy_ticket_tou);
		
		Intent intent = getIntent();	//接收传递过来的余额、账号、航班号...
		yuE = intent.getStringExtra("yuE");
		account = intent.getStringExtra("account");
		date = intent.getStringExtra("date");
		go = intent.getStringExtra("go");
		get = intent.getStringExtra("get");
		fly_time = intent.getStringExtra("fly_time");
		
		depart_airport = intent.getStringExtra("depart_airport");
		land_airport = intent.getStringExtra("land_airport");
		depart_time = intent.getStringExtra("depart_time").trim();
		land_time = intent.getStringExtra("land_time").trim();
		flight_num = intent.getStringExtra("flight_num");
		
		clas = intent.getStringExtra("clas");
		
		piaojia = intent.getStringExtra("piaojia");
		zonge = Integer.parseInt(piaojia);		//接收到的机票总额转换为int类型
		
		chajia = zonge;
		
		//接收余票数量，但不显示。提交订单后，对数据库操作。
		yupiao = intent.getStringExtra("yupiao");
		ad = intent.getStringExtra("ad");
		title = intent.getStringExtra("title");
		
		gaiqian = intent.getStringExtra("gaiqian");	
		//若是改签状态，接收原机票价格和原航班号
		if(gaiqian.equals("yes")){
			flight_num2 = intent.getStringExtra("flight_num2");
			yuanpiaojia = intent.getStringExtra("yuanpiaojia");
			clas_gaiqian = intent.getStringExtra("clas_gaiqian");
			order_num = intent.getStringExtra("order_num");
		}
		
		//获得控件、设置内容
		title_txt = (TextView)findViewById(R.id.title_txt);
		title_txt.setText(title);
		serve_txt = (TextView)findViewById(R.id.serve_txt);
		serve_txt.setText(ad);
		
		date_txt = (TextView)findViewById(R.id.date_txt);	//设置日期、起点、终点、飞行时间
		date_txt.setText(date);
		go_txt = (TextView)findViewById(R.id.go_txt);
		go_txt.setText(go);
		get_txt = (TextView)findViewById(R.id.get_txt);
		get_txt.setText(get);
		fly_time_txt = (TextView)findViewById(R.id.fly_time_txt);
		fly_time_txt.setText(fly_time);
		
		depart_airport_txt = (TextView)findViewById(R.id.depart_airport_txt);
		depart_airport_txt.setText(depart_airport);
		land_airport_txt = (TextView)findViewById(R.id.land_airport_txt);
		land_airport_txt.setText(land_airport);
		
		account_txt = (TextView)findViewById(R.id.account_txt);
		account_txt.setText(account);
		
		total_price_txt = (TextView)findViewById(R.id.total_price_txt);	//设置票价  piaojiao仍为整数
		String price = String.format("￥  %s", piaojia);
		total_price_txt.setText(price);
		
		//获得其余控件 
		passenger_edit = (EditText)findViewById(R.id.passenger_edit);
		birthday_edit = (EditText)findViewById(R.id.birthday_edit);
		nation_edit = (EditText)findViewById(R.id.nation_edit);
		idcard_edit = (EditText)findViewById(R.id.idcard_edit);
		
		
		//购买保险
		yanwu = (CheckBox)findViewById(R.id.yanwu);
		yanwu.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ){
					zonge += 20;
					String price = String.format("￥  %d", zonge);
					total_price_txt.setText(price);
				}
				else{
					zonge -= 20;
					String price = String.format("￥  %d", zonge);
					total_price_txt.setText(price);
				}
			}
		});
		
		yiwai = (CheckBox)findViewById(R.id.yiwai);
		yiwai.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ){
					zonge += 30;
					String price = String.format("￥  %d", zonge);
					total_price_txt.setText(price);
				}
				else{
					zonge -= 30;
					String price = String.format("￥  %d", zonge);
					total_price_txt.setText(price);
				}
			}
		});
		
		//提交订单。所有信息要再传递一次！
		Button submit = (Button)findViewById(R.id.submit);
		submit.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BuyTicketTouActivity.this,PaymentActivity.class);
			
				intent.putExtra("account", account);		//向下一个界面传递信息
				passenger = passenger_edit.getText().toString();
				intent.putExtra("passenger",passenger);
				
				sex = "男";
				intent.putExtra("sex",sex);
				
				birthday = birthday_edit.getText().toString();
				intent.putExtra("birthday",birthday);
				
				nation = nation_edit.getText().toString();
				intent.putExtra("nation",nation);
				
				idcard = idcard_edit.getText().toString();
				intent.putExtra("idcard",idcard);
				
				if(zonge-chajia==30)	baoxian = "您已购买意外险";
				else if(zonge-chajia==20)	baoxian = "您已购买延误险";
				else if(zonge-chajia==50)	baoxian = "您已购买延误险、意外险";
				else  baoxian = "您没有购买保险";
				
				intent.putExtra("clas", clas);
				intent.putExtra("zonge", zonge);

				intent.putExtra("date", date);
				intent.putExtra("go", go);
				intent.putExtra("get", get);
				intent.putExtra("fly_time", fly_time);
				intent.putExtra("depart_time", depart_time);
				intent.putExtra("land_time", land_time);
				
				intent.putExtra("depart_airport", depart_airport);
				intent.putExtra("land_airport", land_airport);
				intent.putExtra("flight_num", flight_num);
				intent.putExtra("baoxian", baoxian);

				intent.putExtra("yuE", yuE);
				intent.putExtra("yupiao", yupiao);
				
				intent.putExtra("gaiqian", gaiqian);	
				if(gaiqian.equals("yes")){
					intent.putExtra("flight_num2", flight_num2);
					intent.putExtra("yuanpiaojia", yuanpiaojia);	
					intent.putExtra("clas_gaiqian", clas_gaiqian);
					intent.putExtra("order_num", order_num);			//原机票订单号
				}
				
				//乘客等信息不能为空！！！
				if( TextUtils.isEmpty( passenger ) || TextUtils.isEmpty( birthday)
						|| TextUtils.isEmpty( nation ) || TextUtils.isEmpty( idcard ) ){
					AlertDialog.Builder dialog = new AlertDialog.Builder(BuyTicketTouActivity.this);
		    		dialog.setTitle("This is a warnining!");
		    		dialog.setMessage("您还有信息没有输入！");
		    		dialog.setCancelable(false);
		    		dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
		    			@Override
		    			public void onClick(DialogInterface dialog,int which){           				
		    			}
		    		});          		
		    		dialog.show();
				}
				else{
					startActivity(intent);	
					finish();
				}
			}
		});
	}
	
	public void onBackPressed(){
		Intent intent = new Intent(BuyTicketTouActivity.this,SearchActivity.class);
		intent.putExtra("account", account);
		startActivity(intent);	
		finish();
	}
}
