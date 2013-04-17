package com.nano.lanshare.components.operation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.utils.FileSizeUtil;

public class FileOperationContentView implements OnClickListener {
	private OnClickListener mAcionClickListener;

	private OnClickListener mOperationClickListener;

	private int mType;

	private String mFilePath;

	private Context mContext;

	private OperationDialog mDialog;

	public FileOperationContentView(int type, String path,
			OperationDialog operationDialog) {
		mType = type;
		mFilePath = path;
		mDialog = operationDialog;
	}

	public View createContentView(Context context) {
		mContext = context;
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.operation_dialog, null);
		View transport = contentView.findViewById(R.id.transport);
		View aciton = contentView.findViewById(R.id.action);
		View property = contentView.findViewById(R.id.property);
		View operation = contentView.findViewById(R.id.operation);

		aciton.setOnClickListener(mAcionClickListener);
		transport.setOnClickListener(this);
		property.setOnClickListener(this);
		operation.setOnClickListener(mOperationClickListener == null ? this
				: mOperationClickListener);

		TextView acitonText = (TextView) contentView
				.findViewById(R.id.action_text);
		setActionText(acitonText);
		return contentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.transport:
			break;
		case R.id.property:
			showPropertyDialog();
			break;
		case R.id.operation:
			break;
		default:
			break;
		}

		mDialog.dismiss();
	}

	private void setActionText(TextView acitonText) {
		int stringId = 0;
		switch (mType) {
		case OperationDialog.TYPE_APP:
		case OperationDialog.TYPE_FILE:
			stringId = R.string.menu_open;
			break;
		case OperationDialog.TYPE_IMAGE:
			stringId = R.string.menu_view;
			break;
		case OperationDialog.TYPE_MUSIC:
		case OperationDialog.TYPE_VIDEO:
			stringId = R.string.menu_play;
			break;
		default:
			break;
		}
		acitonText.setText(stringId);
	}

	public void setActionClickListener(OnClickListener onClickListener) {
		mAcionClickListener = onClickListener;
	}

	public void setOperationClickListener(OnClickListener onClickListener) {
		mOperationClickListener = onClickListener;
	}

	private void showPropertyDialog() {
		if (null == mFilePath) {
			return;
		}

		File file = new File(mFilePath);
		if (file.exists()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(R.string.menu_property).setIcon(
					android.R.drawable.ic_dialog_info);
			Date date = new Date(file.lastModified());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss", Locale.getDefault());

			StringBuilder builder2 = new StringBuilder();
			builder2.append(mContext.getString(R.string.dm_dialog_name))
					.append(file.getName()).append("\n");
			builder2.append(mContext.getString(R.string.dm_dialog_location))
					.append(file.getParent()).append("\n");
			builder2.append(mContext.getString(R.string.dm_dialog_size))
					.append(FileSizeUtil.formatFromByte(file.length()))
					.append("\n");
			builder2.append(mContext.getString(R.string.dm_dialog_lastdate))
					.append(dateFormat.format(date));
			builder.setMessage(builder2.toString());
			builder.setPositiveButton(R.string.dm_dialog_ok, null);
			builder.create().show();
		}
	}

}
