package com.example.fly;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlightAdapter extends ArrayAdapter<FlightInfo> {
	private int resourceId;

	public FlightAdapter( Context context,int textViewResourceId,List<FlightInfo> objects ){
		super(context,textViewResourceId,objects);
		resourceId = textViewResourceId;	
	}
	@Override
	public View getView( int position,View convertView,ViewGroup parent ){
		FlightInfo flightInfo = getItem(position);
		View view;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId,null);
		}
		else{
			view = convertView;
		}
		//共得到11个控件
		ImageView fei = (ImageView)view.findViewById(R.id.fei);
		TextView flight_num_txt = (TextView)view.findViewById(R.id.flight_num_txt);
		TextView go_txt = (TextView)view.findViewById(R.id.go_txt);
		TextView jiantou = (TextView)view.findViewById(R.id.jiantou);
		TextView get_txt = (TextView)view.findViewById(R.id.get_txt);
		
		TextView date_txt = (TextView)view.findViewById(R.id.date_txt);
		TextView depart_time_txt = (TextView)view.findViewById(R.id.depart_time_txt);
		TextView fly_time_txt = (TextView)view.findViewById(R.id.fly_time_txt);
		TextView land_time_txt = (TextView)view.findViewById(R.id.land_time_txt);
		TextView depart_airport_txt = (TextView)view.findViewById(R.id.depart_airport_txt);
		TextView land_airport_txt = (TextView)view.findViewById(R.id.land_airport_txt);
		
		//共设置11个参数
		fei.setImageResource(flightInfo.getImageId());
		flight_num_txt.setText(flightInfo.getFlight_num());
		go_txt.setText(flightInfo.getGo());
		jiantou.setText(flightInfo.getJiantou());
		get_txt.setText(flightInfo.getGet());
		
		String condition = flightInfo.getCondition();
		if( condition.equals("1") ){
			date_txt.setText("停止售票");
			date_txt.setTextColor(Color.GRAY);
		}
		else if( condition.equals("0") ){
			date_txt.setText(flightInfo.getDate());
		}
		depart_time_txt.setText(flightInfo.getDepart_time());
		fly_time_txt.setText(flightInfo.getFly_time());
		land_time_txt.setText(flightInfo.getLand_time());
		depart_airport_txt.setText(flightInfo.getDepart_airport());
		land_airport_txt.setText(flightInfo.getLand_airport());
		
		return view;
	}
}
