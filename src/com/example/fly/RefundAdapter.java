package com.example.fly;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RefundAdapter extends ArrayAdapter<FlightInfo> {
	private int resourceId;

	public RefundAdapter(Context context, int textViewResourceId, List<FlightInfo> objects) {
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
		// 共得到7个控件
		TextView flight_num_txt = (TextView) view.findViewById(R.id.tvFlight_num);
		TextView go_txt = (TextView) view.findViewById(R.id.tvGo);
		TextView jiantou_txt = (TextView) view.findViewById(R.id.tvJian);
		TextView get_txt = (TextView) view.findViewById(R.id.tvGet);
		TextView ddh_txt = (TextView) view.findViewById(R.id.tvDingdan);
		TextView date_txt = (TextView) view.findViewById(R.id.tvDate);
		TextView order_txt = (TextView) view.findViewById(R.id.tvOrder);

		// 共设置7个参数
		flight_num_txt.setText(flightInfo.getFlight_num());
		go_txt.setText(flightInfo.getGo());
		jiantou_txt.setText("");
		get_txt.setText(flightInfo.getGet());
		date_txt.setText(flightInfo.getDate());
		ddh_txt.setText("订单号");
		order_txt.setText(flightInfo.getOrder_num());

		return view;
	}
}
