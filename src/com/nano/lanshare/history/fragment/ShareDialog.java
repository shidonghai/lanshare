
package com.nano.lanshare.history.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.lanshare.R;
import com.nano.lanshare.history.been.HistoryInfo;

public class ShareDialog extends DialogFragment implements OnClickListener {
    private ImageView mCloseWin;
    private HistoryInfo mHistoryInfo;
    private ViewGroup mSinaShare;
    private ViewGroup mTencentShare;
    private ViewGroup mRenRenShare;

    public static ShareDialog newInstance(HistoryInfo info) {
        ShareDialog dialog = new ShareDialog();
        Bundle args = new Bundle();
        args.putSerializable("info", info);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryInfo = (HistoryInfo) getArguments().getSerializable("info");
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_share, null);
        mCloseWin =
                (ImageView) view.findViewById(R.id.close_win);
        mCloseWin.setOnClickListener(this);

        mSinaShare = (ViewGroup) view.findViewById(R.id.share_to_sina);
        mSinaShare.setOnClickListener(this);

        mTencentShare = (ViewGroup) view.findViewById(R.id.share_to_tencent);
        mTencentShare.setOnClickListener(this);

        mRenRenShare = (ViewGroup) view.findViewById(R.id.share_to_renren);
        mRenRenShare.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mCloseWin) {
            dismiss();
        } else if (v == mSinaShare) {

        } else if (v == mTencentShare) {

        } else if (v == mRenRenShare) {

        }

    }
}
