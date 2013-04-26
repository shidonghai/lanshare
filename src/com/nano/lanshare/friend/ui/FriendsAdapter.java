package com.nano.lanshare.friend.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.friend.ContactInfo;

public class FriendsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<ContactInfo> mInfos;

	public FriendsAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mInfos = new ArrayList<ContactInfo>();
	}

	@Override
	public int getCount() {
		return mInfos.size();
	}

	@Override
	public ContactInfo getItem(int arg0) {
		return mInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (null == view) {
			view = mInflater.inflate(R.layout.friends_item_layout, null);

			holder = new ViewHolder();
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.phone = (TextView) view.findViewById(R.id.phone);
			holder.select = (ImageView) view.findViewById(R.id.select);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ContactInfo info = mInfos.get(position);
		holder.name.setText(info.mContactName);
		holder.phone.setText(info.mPhone);

		holder.select
				.setImageResource(info.mSelected ? R.drawable.zapya_group_signal_checkbox_pressed
						: R.drawable.zapya_group_signal_checkbox_normal);

		return view;
	}

	public void updateList(List<ContactInfo> infosList) {
		if (infosList != null) {
			mInfos = infosList;
			notifyDataSetChanged();
		}
	}

	public List<ContactInfo> getContactList() {
		return mInfos;
	}

	private class ViewHolder {
		ImageView avatar;
		TextView name;
		TextView phone;
		ImageView select;
	}

}
