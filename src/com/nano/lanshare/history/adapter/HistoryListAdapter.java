package com.nano.lanshare.history.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.history.been.HistoryInfo;
import com.nano.lanshare.history.fragment.TrafficInformationFragment;
import com.nano.lanshare.utils.StorageManager;

public class HistoryListAdapter extends BaseAdapter {
	private List<HistoryInfo> mHistoryInfoList;
	public static final int HISTORY_RECV = 1;
	public static final int HISTORY_SEND = 2;
	private LayoutInflater mInflater;
	private Cursor mCursor;
	private Context mContext;

	public HistoryListAdapter(Context context, List<HistoryInfo> historyList,
			boolean autoRequery) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		setDate(historyList);
	}

	public void setDate(List<HistoryInfo> historyList) {
		mHistoryInfoList = historyList;
	}

	@Override
	public int getCount() {
		return mHistoryInfoList == null ? 1 : mHistoryInfoList.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	private int getItemViewType(HistoryInfo info) {
		return info.historyType;

	}

	@Override
	public int getViewTypeCount() {
		// Received and sent history
		return 2;
	}

	@Override
	public HistoryInfo getItem(int position) {
		if (position == 0) {
			return null;
		} else {
			return mHistoryInfoList.get(position - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == 0) {
			return getHeaderView(convertView);
		} else {
			int type = getItemViewType(getItem(position));
			int typeLayout = HISTORY_RECV == type ? R.layout.history_item_recv
					: R.layout.history_item_send;
			return mInflater.inflate(typeLayout, null);
		}
	}

	static class ViewHolder {

	}

	private View getHeaderView(View convertView) {
		View v = mInflater.inflate(R.layout.history_list_header, null);
		String totalSpace = StorageManager.getTotalSpace();
		String freeSpace = StorageManager.getTotalFreeSpace();
		((TextView) v.findViewById(R.id.storaget_info)).setText(mContext
				.getString(R.string.dm_memory_state, totalSpace, freeSpace));

		Button flowBtn = (Button) v.findViewById(R.id.show_flow);
		flowBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(mContext,
						TrafficInformationFragment.class));
			}
		});
		return v;
	}
}
