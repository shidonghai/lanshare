package com.nano.lanshare.components.operation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;

import com.nano.lanshare.R;
import com.nano.lanshare.utils.FileSizeUtil;

public class PopupMenuUtil {

	public static final int MENU_TRANSPORT = 0;
	public static final int MENU_ACTION = 1;
	public static final int MENU_PROPARTY = 2;
	public static final int MENU_OPERATION = 3;

	public static int[] FILE_POPUP_IAMGES = new int[] {
			R.drawable.zapya_data_downmenu_send,
			R.drawable.zapya_data_downmenu_open,
			R.drawable.zapya_data_downmenu_detail,
			R.drawable.zapya_data_downmenu_operating };

	public static int[] FILE_OPUP_TEXT = new int[] { R.string.menu_transport,
			R.string.menu_play, R.string.menu_property, R.string.menu_operation };

	public static void showPropertyDialog(Context context, String filePath) {
		if (null == filePath) {
			return;
		}

		File file = new File(filePath);
		if (file.exists()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.menu_property).setIcon(
					android.R.drawable.ic_dialog_info);
			Date date = new Date(file.lastModified());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss", Locale.getDefault());

			StringBuilder builder2 = new StringBuilder();
			builder2.append(context.getString(R.string.dm_dialog_name))
					.append(file.getName()).append("\n");
			builder2.append(context.getString(R.string.dm_dialog_location))
					.append(file.getParent()).append("\n");
			builder2.append(context.getString(R.string.dm_dialog_size))
					.append(FileSizeUtil.formatFromByte(file.length()))
					.append("\n");
			builder2.append(context.getString(R.string.dm_dialog_lastdate))
					.append(dateFormat.format(date));
			builder.setMessage(builder2.toString());
			builder.setPositiveButton(R.string.dm_dialog_ok, null);
			builder.create().show();
		}
	}
}
