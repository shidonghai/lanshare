package com.nano.lanshare.components.operation;

import android.content.Context;
import android.view.View;

public class OperationContent {
	public static View CreateContentView(Context context, int type) {
		View view = null;
		switch (type) {
		case OperationDialog.TYPE_VIDEO:
		case OperationDialog.TYPE_MUSIC:
		case OperationDialog.TYPE_FILE: {
			view = new FileOperationContentView().createContentView(context);
			break;
		}
		default: {
			break;
		}
		}

		return view;
	}
}
