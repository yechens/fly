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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class EnterActivity extends Activity{
	int type = 1;	//登录人员类型
	String act,pasd;
	private EditText account, password;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enter);
        
        account = (EditText)findViewById(R.id.account); 
    	password = (EditText)findViewById(R.id.password); 
        
        RadioGroup rg = (RadioGroup)findViewById(R.id.type);		//获得单选按钮组
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int RadioBtnId = group.getCheckedRadioButtonId();
				switch(RadioBtnId){
					case R.id.client:
						type = 1;
						break;
					case R.id.manager:
						type = 2;
						break;
				}
			}
		});
        
      //注册新用户。管理员不能注册
        Button register = (Button)findViewById(R.id.register);      
        register.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(type==2){
					AlertDialog.Builder dialog = new AlertDialog.Builder(EnterActivity.this);
            		dialog.setTitle("This is a warnining!");
            		dialog.setMessage("对不起，您不能注册成为管理员哦!");
            		dialog.setCancelable(false);
            		dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            			@Override
            			public void onClick(DialogInterface dialog,int which){           				
            			}
            		});          		
            		dialog.show();
				}
				else{
					Intent intent = new Intent(EnterActivity.this,RegisterActivity.class);
					Toast.makeText(getApplicationContext(), "欢迎您注册加入去哪儿！",
							Toast.LENGTH_LONG).show();
					startActivity(intent);
					finish();
				}
			}
		});
        		
        //用户/管理员登录
        Button login = (Button)findViewById(R.id.login);      
        login.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(type==1){
					act = account.getText().toString();
					pasd = password.getText().toString();
					if(TextUtils.isEmpty( act ) || TextUtils.isEmpty( pasd )){
						AlertDialog.Builder dialog = new AlertDialog.Builder(EnterActivity.this);
	            		dialog.setTitle("This is a warnining!");
	            		dialog.setMessage("您的账号/密码不能为空哦");
	            		dialog.setCancelable(false);
	            		dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
	            			@Override
	            			public void onClick(DialogInterface dialog,int which){           				
	            			}
	            		});          		
	            		dialog.show();
					}
					else
						whetherRegister(act,pasd); 			
				}
			
				else if(type==2){
					act = account.getText().toString();
					pasd = password.getText().toString();
					if(TextUtils.isEmpty( act ) || TextUtils.isEmpty( pasd )){
						AlertDialog.Builder dialog = new AlertDialog.Builder(EnterActivity.this);
	            		dialog.setTitle("This is a warnining!");
	            		dialog.setMessage("您的账号/密码不能为空哦");
	            		dialog.setCancelable(false);
	            		dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
	            			@Override
	            			public void onClick(DialogInterface dialog,int which){           				
	            			}
	            		});          		
	            		dialog.show();
					}
					else if( !act.equals("andy") || !pasd.equals("1") ){
						AlertDialog.Builder dialog = new AlertDialog.Builder(EnterActivity.this);
	            		dialog.setTitle("This is a warnining!");
	            		dialog.setMessage("管理员登录账号/密码错误");
	            		dialog.setCancelable(false);
	            		dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
	            			@Override
	            			public void onClick(DialogInterface dialog,int which){           				
	            			}
	            		});          		
	            		dialog.show();
					}
					else{
						Toast.makeText(getApplicationContext(), "欢迎您Andy！", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(EnterActivity.this,M_Search_Activity.class);
						startActivity(intent);
						finish();
					}
				}
				
			}
		});    
        
//        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
//        detector = new GestureDetector(this);
//        
//        //��viewFlipper���View
//        viewFlipper.addView(getImageView(R.drawable.ad3));
//        viewFlipper.addView(getImageView(R.drawable.ad4));
//        viewFlipper.addView(getImageView(R.drawable.ad5));
//        viewFlipper.addView(getImageView(R.drawable.ad6));
//        //����Ч��
//    	leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
//		leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
//		rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
//		rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
    }
//    private ImageView getImageView(int id){
//    	ImageView imageView = new ImageView(this);
//    	imageView.setImageResource(id);
//    	return imageView;
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {     
//    	//touch�¼��������ƴ���
//    	return this.detector.onTouchEvent(event); 
//    }

//	@Override
//	public boolean onDown(MotionEvent e) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//
//	@Override
//	public void onShowPress(MotionEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//
//	@Override
//	public void onLongPress(MotionEvent e) {
//		// TODO Auto-generated method stub
//		
//	}


//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
//		// TODO Auto-generated method stub	
//		if(e1.getX()-e2.getX()>120){
//			viewFlipper.setInAnimation(leftInAnimation);
//			viewFlipper.setOutAnimation(leftOutAnimation);
//		    viewFlipper.showNext();//���һ���
//		    return true;
//		}else if(e1.getX()-e2.getY()<-120){
//			viewFlipper.setInAnimation(rightInAnimation);
//			viewFlipper.setOutAnimation(rightOutAnimation);
//			viewFlipper.showPrevious();//���󻬶�
//			return true;
//		}
//		return false;
//	}
	
	private Handler _handler = new Handler(){		
		public void handleMessage( Message msg ){
				switch ( msg.what ) {
				case 1:
					String response = ( String )msg.obj;
					if ( parserXml(response) ){
						Toast.makeText(getApplicationContext(), 
								"尊敬的用户，欢迎您常来!", Toast.LENGTH_SHORT).show();	
							Intent intent = new Intent(EnterActivity.this,SearchActivity.class);
							act = account.getText().toString();
							intent.putExtra("account", act);
							startActivity(intent);
							finish();
					}
					else{
						AlertDialog.Builder dialog = new AlertDialog.Builder(EnterActivity.this);
			    		dialog.setTitle("This is a warnining!");
			    		dialog.setMessage("对不起，您的账户或密码有错!");
			    		dialog.setCancelable(false);
			    		dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
			    			@Override
			    			public void onClick(DialogInterface dialog,int which){           				
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
	
	private boolean parserXml( String xmlData ){		
		try{
			XmlPullParserFactory factory = 
					XmlPullParserFactory.newInstance();
			XmlPullParser parse = factory.newPullParser();	//生成解析器
			parse.setInput( new StringReader( xmlData ) );	//添加xml数据
			int eventType = parse.getEventType();	
			
			String result = "";			
			while( eventType != XmlPullParser.END_DOCUMENT ){
				
				String nodeName = parse.getName();
				
				switch (eventType) {			
					case XmlPullParser.START_TAG:

						if( "result".equals( nodeName ) ){
							result = parse.nextText();
						}
						if( result.equals("succeessful") )
							return true;
						else if( result.equals("failed") )
							return false;
						break;					
				}
				eventType = parse.next();
			}			
		}catch( Exception ex ){
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	private void whetherRegister(String account2,String password2){
		final String account = account2;			//进程中不能传入变量 一定要为常量final
		final String password = password2;		
	
		new Thread( new Runnable() {	//开启子线程		
			@Override
			public void run() {
				
				HttpURLConnection connection = null;
				try{
				//  打开链接
					ip iip = new ip();
					
					String account2 = URLEncoder.encode(account, "UTF-8");	//中文转译！
					URL url = new URL(
						"http://"+iip.getMy_ip()+":8080/pc/whetherRegister.jsp?account=" 
					+ account2 + "&password=" + password );
					
					connection = ( HttpURLConnection )url.openConnection();
					
					// 设置属性
					connection.setRequestMethod("GET");
					// Post 1)容量没有限制  2） 安全
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					// 读取数据
					// 1)获取位流
					InputStream in = connection.getInputStream(); 
					// 二进制-->BufferedReader
					BufferedReader reader = new BufferedReader( 
							new InputStreamReader(in));
					// 2) 读取
					StringBuilder response = new StringBuilder();
					String line;
					while( ( line = reader.readLine() ) != null ){
						response.append( line );
					}				
					
					// 发送消息
					Message msg = new Message();
					msg.what = 1;
					msg.obj = response.toString();
					_handler.sendMessage(msg);
					// Handler
				}
				catch( Exception ex ){
					ex.printStackTrace();
				}
				finally{
					if( connection != null ){
						connection.disconnect(); // 断开链接
					}
				}
			}
		}).start();	
	}
	
//	@Override  
//	public void onBackPressed() {  	  
//	    long currentTime = System.currentTimeMillis();  
//	    if ((currentTime - startTime) >= 2000) {  
//	        Toast.makeText(EnterActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();  
//	        startTime = currentTime;  
//	        finish();
//	    }
//	}

}
