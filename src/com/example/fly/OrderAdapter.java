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

public class OrderAdapter extends ArrayAdapter<FlightInfo> {

	private int resourceId;

	public OrderAdapter(Context context, int textViewResourceId, List<FlightInfo> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FlightInfo flightInfo = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			view = convertView;
		}
		// 共得到13个控件
		ImageView fei = (ImageView) view.findViewById(R.id.fei);
		TextView jp = (TextView) view.findViewById(R.id.jp);
		TextView ycq = (TextView) view.findViewById(R.id.ycq);
		TextView tv_go = (TextView) view.findViewById(R.id.tv_go);
		TextView xian1 = (TextView) view.findViewById(R.id.xian1);
		TextView tv_get = (TextView) view.findViewById(R.id.tv_get);

		TextView tv_piaojia = (TextView) view.findViewById(R.id.tv_piaojia);
		TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
		TextView tv_depart_time = (TextView) view.findViewById(R.id.tv_depart_time);
		TextView xian2 = (TextView) view.findViewById(R.id.xian2);
		TextView tv_land_time = (TextView) view.findViewById(R.id.tv_land_time);
		TextView tv_flight_num = (TextView) view.findViewById(R.id.tv_flight_num);

		TextView tv_order_num = (TextView) view.findViewById(R.id.tv_order_num);

		// 共设置13个参数
		fei.setImageResource(flightInfo.getImageId());
		jp.setText(flightInfo.getJp());
		String temp = flightInfo.getYcp();
		ycq.setText(temp);
		if (temp.equals("订单已取消")) {
			ycq.setTextColor(Color.rgb(112, 128, 144));
		} else if (temp.equals("订单正在取消")) {
			ycq.setTextColor(Color.rgb(0, 0, 0));
		}
		tv_go.setText(flightInfo.getGo());
		xian1.setText(flightInfo.getJiantou());
		tv_get.setText(flightInfo.getGet());

		// setText 一定要为String类型，否则程序会崩溃
		String temp2 = "￥" + flightInfo.getTotal_price();
		tv_piaojia.setText(temp2);
		tv_piaojia.setTextColor(Color.rgb(255, 127, 0));
		tv_piaojia.setText(flightInfo.getTotal_price());
		tv_date.setText(flightInfo.getDate());
		tv_depart_time.setText(flightInfo.getDepart_time());
		xian2.setText(flightInfo.getJiantou2());
		tv_land_time.setText(flightInfo.getLand_time());
		tv_flight_num.setText(flightInfo.getFlight_num());

		tv_order_num.setText(flightInfo.getOrder_num());

		return view;
	}

}
