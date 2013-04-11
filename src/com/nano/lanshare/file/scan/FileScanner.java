
package com.nano.lanshare.file.scan;

import java.io.File;
import java.util.Stack;

import android.os.Environment;
import android.util.Log;

import com.nano.lanshare.file.FileList;
import com.nano.lanshare.file.scan.ScanThread.ScanState;

public class FileScanner {
    private final File ROOT = Environment.getExternalStorageDirectory();

    private File mCurrentFile;

    private Stack<FileList> mStack;

    private static FileScanner mInstance;

    private ScanThread mThread;

    private FileScanner() {
        mStack = new Stack<FileList>();
        mCurrentFile = ROOT;
    }

    public static FileScanner getInstance() {
        if (mInstance == null) {
            mInstance = new FileScanner();
        }
        return mInstance;
    }

    public void startScanning(File file, FileScanListener listener) {
        boolean refresh = false;
        if (file != null) {
            if (mCurrentFile.equals(file)) {
                refresh = true;
            }
            mCurrentFile = file;
        }
        if (mThread != null && mThread.getState() == ScanState.RUNNING) {
            mThread.stop();
        }
        startScanning(listener, refresh);
    }

    private void startScanning(FileScanListener listener, boolean refresh) {
        mThread = new ScanThread(mCurrentFile, new ScanOption());
        mThread.startScanning(listener, this, refresh);
    }

    public boolean handleBack(FileScanListener listener) {
        if (mCurrentFile == null) {
            return false;
        }
        mCurrentFile = mCurrentFile.getParentFile();
        if (mCurrentFile == null) {
            return false;
        }
        if (mStack.size() > 1) {
            mStack.pop();
            listener.onScanSuccessed();
        } else {
            if (mStack.size() == 1) {
                mStack.pop();
            }
            startScanning(listener, false);
        }
        return true;
    }

    public boolean isEmpty() {
        return mStack.isEmpty();
    }

    public void push(FileList list) {
        mStack.push(list);
    }

    public FileList pop() {
        return mStack.pop();
    }

    public int getLastPosition() {
        FileList list = peek();
        if (list == null) {
            return 0;
        }
        int p = list.getPosition();
        Log.e("file scanner",
                "restore position: " + p + " size: " + mStack.size());
        return p;
    }

    public void saveLastPosition(int p) {
        if (!mStack.isEmpty()) {
            mStack.peek().setPosition(p);
            Log.e("file scanner",
                    "save position: " + p + " size: " + mStack.size());
        }
    }

    public FileList peek() {
        if (mStack.isEmpty()) {
            return null;
        }
        return mStack.peek();
    }

    public static void clear() {
        mInstance = null;
    }

    public File getCurrentFile() {
        return mCurrentFile;
    }
}
