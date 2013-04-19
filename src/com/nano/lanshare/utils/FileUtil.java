package com.nano.lanshare.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.widget.Toast;

import com.nano.lanshare.R;

public class FileUtil {

	public static final String INBOX_ROOT_PATH = "/lanshare";

	public static final File INBOX_FILE = new File(Environment
			.getExternalStorageDirectory().getAbsolutePath() + INBOX_ROOT_PATH);

	public static final void createInbox(Context context) {
		File sdCrad = Environment.getExternalStorageDirectory();
		if (null == sdCrad) {
			return;
		}

		String[] folders = context.getResources().getStringArray(R.array.inbox);
		for (int i = 0; i < folders.length; i++) {
			File file = new File(INBOX_FILE, File.separator + folders[i]);
			if (!file.exists()) {
				file.mkdirs();
			}
		}

	}

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

	public static void showFileOperationDialog(final Context context,
			final String filePath) {
		if (null == filePath) {
			return;
		}
		File file = new File(filePath);
		if (file.exists()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.menu_operation).setIcon(
					android.R.drawable.ic_dialog_info);
			builder.setItems(
					new String[] { context.getString(R.string.dm_menu_delete),
							context.getString(R.string.dm_menu_rename) },
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								showDeleteDialog(context, filePath);
								break;

							default:
								break;
							}
						}
					});
			builder.create().show();
		}
	}

	public static void showDeleteDialog(final Context context, String filePath) {
		if (null == filePath) {
			return;
		}
		final File file = new File(filePath);
		if (file.exists()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.dm_dialog_delete) + " "
					+ file.getName());
			builder.setMessage(R.string.dm_dialog_delete_confirm);
			builder.setPositiveButton(R.string.dm_dialog_ok,
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (file.delete()) {
								Toast.makeText(
										context,
										String.format(
												context.getString(R.string.dm_data_delete_success),
												file.getName()),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			builder.setNegativeButton(R.string.dm_dialog_cancel, null);
			builder.create().show();
		}
	}
}
