
package com.nano.lanshare.utils;

import java.io.File;
import java.text.DecimalFormat;

import android.os.Environment;
import android.os.StatFs;

public class StorageManager {
    public StorageManager() {
    }

    public static String getTotalSpace() {
        File path = Environment.getExternalStorageDirectory();

        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();

        long availableBlocks = stat.getBlockCount();
        DecimalFormat fnum = new DecimalFormat("##0.00");
        // Unit Conversions to GB
        return fnum.format(availableBlocks * blockSize / 1024 / 1024 / 1024.00f) + "GB";

    }

    public static String getTotalFreeSpace() {
        File path = Environment.getExternalStorageDirectory();

        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();

        long availableBlocks = stat.getAvailableBlocks();
        DecimalFormat fnum = new DecimalFormat("##0.00");
        // Unit Conversions to GB
        return fnum.format(availableBlocks * blockSize / 1024 / 1024 / 1024.00f) + "GB";

    }

}
