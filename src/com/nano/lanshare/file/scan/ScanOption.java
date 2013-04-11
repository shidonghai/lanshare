
package com.nano.lanshare.file.scan;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

import android.util.Log;

public class ScanOption {

    private boolean mIncludeHidden = true;

    private boolean mIncludeCantRead;

    private boolean mIncludeCantWrite = true;

    private boolean mDirectoryFirst = true;

    public FileFilter filter;

    public Comparator<File> sorter;

    public ScanOption() {
        filter = new FileFilter() {
            public boolean accept(File file) {
                if (!mIncludeHidden && file.isHidden()) {
                    Log.e("filter", "hidden " + file.getAbsolutePath());
                    return false;
                }
                if (!mIncludeCantRead && !file.canRead()) {
                    Log.e("filter", "cant read " + file.getAbsolutePath());
                    return false;
                }
                if (!mIncludeCantWrite && !file.canWrite()) {
                    Log.e("filter", "cant write " + file.getAbsolutePath());
                    return false;
                }
                return true;
            }
        };

        sorter = new Comparator<File>() {
            public int compare(File lhs, File rhs) {
                if (mIncludeHidden) {
                    boolean isHidden1 = lhs.isHidden();
                    boolean isHidden2 = lhs.isHidden();

                    if (isHidden1 ^ isHidden2) {
                        return isHidden1 ? -1 : 1;
                    }
                }

                boolean isDir1 = lhs.isDirectory();
                boolean isDir2 = rhs.isDirectory();
                if (isDir1 ^ isDir2) {
                    return isDir1 & mDirectoryFirst ? -1 : 1;
                } else {
                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                }
            }
        };
    }
}
