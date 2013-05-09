
package com.nano.lanshare.history.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.history.been.HistoryInfo;
import com.nano.lanshare.history.fragment.TrafficInformationFragment;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.thumbnail.util.ImageWorker.LoadMethod;
import com.nano.lanshare.utils.StorageManager;

public class HistoryListAdapter extends BaseAdapter {
    private List<HistoryInfo> mHistoryInfoList;
    public static final int HISTORY_RECV = 1;
    public static final int HISTORY_SEND = 2;
    private LayoutInflater mInflater;
    private List<HistoryInfo> mSelectList = new ArrayList<HistoryInfo>();
    private Cursor mCursor;
    private Context mContext;
    private boolean isCheckMode;
    private ImageWorker mWorker;
    private Bitmap mDefaultIcon;
    private onDataLoadChange mDataLoadChange;

    private LoadMethod mFileIconMethod = new LoadMethod() {

        @Override
        public Bitmap processBitmap(Object obj, Context context) {

            return BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.zapya_history_person_hot);
        }
    };

    public interface onDataLoadChange {
        void onChange();
    }

    public HistoryListAdapter(Context context, List<HistoryInfo> historyList,
            boolean autoRequery) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mWorker = ((LanshareApplication) context.getApplicationContext())
                .getImageWorker();
        mDefaultIcon = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.zapya_history_person_hot);
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

        int type = getItemViewType(getItem(position));
        int typeLayout = HISTORY_RECV == type ? R.layout.history_item_recv
                : R.layout.history_item_send;

        if (convertView == null) {
            convertView = mInflater.inflate(typeLayout, null);
            holder = new ViewHolder(convertView);
            // convertView.setTag(holder);
            convertView.setTag(typeLayout, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(typeLayout);
            // If we don't do this the sender and receiver maybe incorrect.
            if (holder == null) {
                convertView = mInflater.inflate(typeLayout, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(typeLayout, holder);
            }
        }
        holder.setData(getItem(position));
        mWorker.loadImage(getItem(position),
                holder.fileIcon, mDefaultIcon, mFileIconMethod);
        return convertView;
    }

    public void setCheckMode(boolean flag) {
        isCheckMode = flag;
        if (!isCheckMode) {
            mSelectList.clear();
        }
        notifyDataSetChanged();
    }

    public boolean isCheckedMode() {
        return isCheckMode;
    }

    public void setAllChecked(boolean flag) {
        if (flag) {
            mSelectList.clear();
            mSelectList.addAll(mHistoryInfoList);
        } else {
            mSelectList.clear();
        }
        notifyDataSetChanged();
    }

    public void setonDateLoadChangeListener(onDataLoadChange listener) {
        mDataLoadChange = listener;
    }

    /**
     * When we click the item, we set the item checked or not.
     * 
     * @param item
     * @param position
     */
    public void setChecked(View item, int position) {
        HistoryInfo info = getItem(position);
        if (mSelectList.contains(info)) {
            mSelectList.remove(info);
        } else {
            mSelectList.add(info);
        }
        notifyDataSetChanged();
    }

    public void updateFileTransferProgress(HistoryInfo info) {
        if (mHistoryInfoList.contains(info)) {
            notifyDataSetChanged();
        }
    }

    public List<HistoryInfo> getSelectedList() {
        return mSelectList;
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
        public ProgressBar progressBar;

        public ViewHolder(View view) {
            avater = (ImageView) view.findViewById(R.id.item_thumb);
            name = (TextView) view.findViewById(R.id.item_name);
            fileType = (ImageView) view.findViewById(R.id.file_type);
            fileIcon = (ImageView) view.findViewById(R.id.file_icon);
            fileName = (TextView) view.findViewById(R.id.file_name);
            fileSize = (TextView) view.findViewById(R.id.file_size);
            fileOpeate = (TextView) view.findViewById(R.id.file_opeart);
            fileChecked = (ImageView) view.findViewById(R.id.file_checked);
            fileUncheck = (ImageView) view.findViewById(R.id.file_uncheck);
            progressBar = (ProgressBar) view.findViewById(R.id.file_transfer_progress);
        }

        public void setData(HistoryInfo info) {
            setCheckMode();
            // If is check mode then we show the checkbox or not.
            if (isCheckMode) {
                if (mSelectList.contains(info)) {
                    setChecked(true);
                } else {
                    setChecked(false);
                }
            }
            if (HistoryInfo.Status.STATUS_TRANSFERING.equals(info.status)) {
                updateProgress(info);
            }
        }

        private void updateProgress(HistoryInfo info) {
            if (progressBar != null) {
                if (View.VISIBLE != progressBar.getVisibility()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(info.transferProgress);
                if (info.transferProgress == progressBar.getMax()) {
                    progressBar.setVisibility(View.GONE);
                    info.status = HistoryInfo.Status.STATUS_TRANSFERING_FINISH;
                }
            }
        }

        /**
         * If is check mode we should hide the file type icon.
         */
        public void setCheckMode() {
            if (isCheckMode) {
                fileUncheck.setVisibility(View.VISIBLE);
                fileChecked.setVisibility(View.GONE);
                fileType.setVisibility(View.GONE);
            } else {
                fileChecked.setVisibility(View.GONE);
                fileUncheck.setVisibility(View.GONE);
                fileType.setVisibility(View.VISIBLE);
            }
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

    }

    /**
     * Get the header view
     * 
     * @param convertView
     * @return
     */
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
        Button msgBtn = (Button) convertView.findViewById(R.id.show_message);
        msgBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDataLoadChange.onChange();
            }
        });
        return convertView;
    }
}
