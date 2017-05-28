package com.example.fly;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RouteAdapter extends ArrayAdapter<FlightInfo> {
	private int resourceId;

	public RouteAdapter(Context context, int textViewResourceId, List<FlightInfo> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FlightInfo info = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		} else {
			view = convertView;
		}
		TextView Qi = (TextView) view.findViewById(R.id.zj_qi);
		TextView Zhong = (TextView) view.findViewById(R.id.zj_zhong);

		Qi.setText(info.getGo());
		Zhong.setText(info.getGet());

		return view;
	}
}
