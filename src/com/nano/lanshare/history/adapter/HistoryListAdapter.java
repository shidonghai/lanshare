
package com.nano.lanshare.history.adapter;

import java.util.ArrayList;
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
import android.widget.ImageView;
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
    private List<HistoryInfo> mSelectList = new ArrayList<HistoryInfo>();
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
            return getHistoryItems(convertView, position);
        }
    }
    

    public View getHistoryItems(View convertView, int position) {
        ViewHolder holder = null;
        if (convertView == null) {
            int type = getItemViewType(getItem(position));
            int typeLayout = HISTORY_RECV == type ? R.layout.history_item_recv
                    : R.layout.history_item_send;
            convertView = mInflater.inflate(typeLayout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(mHistoryInfoList.get(position));
        return convertView;
    }

    public void setChecked(View item, int position) {
        ViewHolder holder = (ViewHolder) item.getTag();
        holder.setChecked(position);
        notifyDataSetChanged();
    }

    public final class ViewHolder {
        public ImageView avater;
        public TextView name;
        public ImageView fileType;
        public ImageView fileIcon;
        public TextView fileName;
        public TextView fileSize;
        public TextView fileOpeate;
        public ImageView fileChecked;
        public ImageView fileUncheck;

        public ViewHolder(View view) {
        }

        public void setData(HistoryInfo info) {
            
            //setChecked(info);
        }

        private boolean isChecked() {
            return fileChecked.getVisibility() == View.VISIBLE;
        }

        private void setChecked(boolean falg) {
            if (falg) {
                fileChecked.setVisibility(View.VISIBLE);
                fileUncheck.setVisibility(View.GONE);
            } else {
                fileChecked.setVisibility(View.GONE);
                fileUncheck.setVisibility(View.VISIBLE);
            }
        }

        private void setChecked(HistoryInfo info) {
            if (info == null) {
                return;
            }
            if (mSelectList.contains(info)) {
                setChecked(false);
                mSelectList.remove(info);
            } else {
                setChecked(true);
                mSelectList.add(info);
            }
        }

        public void setChecked(int position) {
            //setChecked(getItem(position));
        }
    }

    private View getHeaderView(View convertView) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.history_list_header, null);

        }
        String totalSpace = StorageManager.getTotalSpace();
        String freeSpace = StorageManager.getTotalFreeSpace();
        ((TextView) convertView.findViewById(R.id.storaget_info)).setText(mContext
                .getString(R.string.dm_memory_state, totalSpace, freeSpace));

        Button flowBtn = (Button) convertView.findViewById(R.id.show_flow);
        flowBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,
                        TrafficInformationFragment.class));
            }
        });
        return convertView;
    }
}
