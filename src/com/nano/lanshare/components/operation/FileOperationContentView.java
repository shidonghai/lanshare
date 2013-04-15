package com.nano.lanshare.components.operation;

import com.nano.lanshare.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FileOperationContentView extends OperationContentView implements
		OnClickListener {

	@Override
	public View createContentView(Context context) {
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.operation_dialog, null);
		View transport = contentView.findViewById(R.id.transport);
		View aciton = contentView.findViewById(R.id.action);
		View property = contentView.findViewById(R.id.property);
		View operation = contentView.findViewById(R.id.operation);

		transport.setOnClickListener(this);
		aciton.setOnClickListener(this);
		property.setOnClickListener(this);
		operation.setOnClickListener(this);

		TextView acitonText = (TextView) contentView
				.findViewById(R.id.action_text);
		return contentView;
	}

	@Override
	public void onClick(View v) {

	}

}
