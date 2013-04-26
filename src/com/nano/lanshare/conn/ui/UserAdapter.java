package com.nano.lanshare.conn.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.socket.moudle.Stranger;

public class UserAdapter extends BaseAdapter {
	private ArrayList<Stranger> mUserList;
	private Context mContext;
	private LayoutInflater mInflater;

	public UserAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mUserList = new ArrayList<Stranger>();
	}

	@Override
	public int getCount() {
		return mUserList == null ? 0 : mUserList.size();
	}

	@Override
	public Stranger getItem(int position) {
		return mUserList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.friend_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView) convertView
					.findViewById(R.id.user_avatar);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.user_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.setVal(getItem(position));
		return convertView;
	}

	public void addUsers(List<Stranger> list) {
		mUserList.clear();
		mUserList.addAll(list);
		notifyDataSetChanged();
	}

	static class ViewHolder {
		ImageView avatar;
		TextView name;

		void setVal(Stranger user) {
			if (user.getUserPhoto() != null) {
				avatar.setImageBitmap(BitmapFactory.decodeByteArray(
						user.getUserPhoto(), 0, 0));
			}
			name.setText(user.getName());
		}
	}

}
