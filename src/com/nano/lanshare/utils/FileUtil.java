package com.nano.lanshare.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.nano.lanshare.R;
import com.nano.lanshare.conn.ui.ConnectActivity;
import com.nano.lanshare.main.BaseActivity;

public class FileUtil {

	public static final String INBOX_ROOT_PATH = "/lanshare";

	public static final int REFRESH_UI = 0;

	public static final int DIALOG_DELETE = 0;

	public static final int DIALOG_RENAME = 1;

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
		} else {
			Toast.makeText(context, R.string.dm_data_delete_non_exists,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void showAppOperationDialog(final Context context,
			final ApplicationInfo info) {

		if (null == info) {
			return;
		}
		File file = new File(info.sourceDir);
		if (file.exists()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.menu_operation).setIcon(
					android.R.drawable.ic_dialog_info);
			builder.setItems(
					new String[] {
							context.getString(R.string.dm_zapya_backup_name),
							context.getString(R.string.dm_menu_uninstall) },
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								break;
							case 1:
								try {
									Uri packageURI = Uri.parse("package:"
											+ info.packageName);
									Intent uninstallIntent = new Intent(
											Intent.ACTION_DELETE, packageURI);
									context.startActivity(uninstallIntent);
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							default:
								break;
							}
						}
					});
			builder.create().show();
		}
	}

	public static final void showFileOperationDialog(Context context,
			String filePath) {
		showFileOperationDialog(context, filePath, null);
	}

	public static void showFileOperationDialog(final Context context,
			final String filePath, final Handler handler) {
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
							case DIALOG_DELETE:
								showDeleteDialog(context, filePath, handler);
								break;
							case DIALOG_RENAME:
								showRenameDialog(context, filePath, handler);
								break;
							default:
								break;
							}
						}
					});
			builder.create().show();
		} else {
			Toast.makeText(context, R.string.dm_data_delete_non_exists,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void showDeleteDialog(final Context context, String filePath,
			final Handler handler) {
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
								if (null != handler) {
									handler.sendEmptyMessage(REFRESH_UI);
								}
								scanFileAsync(context, file);
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

	public static void showRenameDialog(final Context context,
			final String filePath, final Handler handler) {
		if (null == filePath) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.dm_dialog_input);
		final EditText editText = new EditText(context);
		editText.setSingleLine(true);
		editText.setMaxWidth(100);
		builder.setView(editText);

		final File file = new File(filePath);
		String name = file.getName();
		editText.setText(name);
		editText.setSelection(0, name.length());
		builder.setPositiveButton(R.string.dm_dialog_ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (TextUtils.isEmpty(editText.getText().toString())) {
					Toast.makeText(context, R.string.dm_toast_emptyname,
							Toast.LENGTH_SHORT).show();
					return;
				}

				// File file = new File(filePath);
				File newFile = new File(file.getParentFile(), editText
						.getText().toString());
				if (newFile.exists()) {
					Toast.makeText(context, R.string.dm_toast_fileexist,
							Toast.LENGTH_SHORT).show();
				} else {
					boolean result = file.renameTo(newFile);
					if (result) {
						if (null != handler) {
							handler.sendEmptyMessage(REFRESH_UI);
						}
						scanFileAsync(context, file);
						scanFileAsync(context, newFile);
						Toast.makeText(context, R.string.dm_toast_rename_done,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		builder.setNegativeButton(R.string.dm_dialog_cancel, null);
		builder.create().show();
	}

	public static void scanFileAsync(Context ctx, File file) {
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		scanIntent.setData(Uri.fromFile(file));
		ctx.sendBroadcast(scanIntent);
	}

	public static void startTransfer(Context context, String path) {
		Intent intent = new Intent();
		intent.setClass(context, ConnectActivity.class);
		intent.setAction(BaseActivity.PICK_A_FRIEND_AND_SEND);
		intent.putExtra("file", path);
		context.startActivity(intent);
	}
}
