
package com.nano.lanshare.history.fragment;

import java.io.File;

import com.nano.lanshare.R;
import com.nano.lanshare.history.been.HistoryInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PropertyDialog extends AlertDialog {
    private LayoutInflater mInflater;
    private TextView mName;
    private TextView mType;
    private TextView mPath;
    private TextView mSize;
    private TextView mStatus;
    private TextView mDate;

    protected PropertyDialog(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        initView();

    }

    private void initView() {
        setTitle(R.string.menu_property);

        View view = mInflater.inflate(R.layout.history_file_property, null);
        mName = (TextView) view.findViewById(R.id.property_name);
        mType = (TextView) view.findViewById(R.id.property_type);
        mPath = (TextView) view.findViewById(R.id.property_path);
        mSize = (TextView) view.findViewById(R.id.property_size);
        mStatus = (TextView) view.findViewById(R.id.property_status);
        mDate = (TextView) view.findViewById(R.id.property_date);
        setView(view);
    }

    public void setVal(HistoryInfo info) {
        if (info == null) {
            return;
        }
        File f = new File(info.filePath);
        mName.setText(getContext().getText(R.string.dm_dialog_name)+f.getName());
        mType.setText(getContext().getText(R.string.dm_dialog_type)+(info.fileType+""));
        mPath.setText(getContext().getText(R.string.dm_dialog_location)+info.filePath);
        mSize.setText(getContext().getText(R.string.dm_dialog_totalsize)+info.fileSize);
        mStatus.setText(getContext().getText(R.string.dm_dialog_transstatus)+info.status);
        mDate.setText(getContext().getText(R.string.dm_dialog_transdate)+info.date);
    }
}
