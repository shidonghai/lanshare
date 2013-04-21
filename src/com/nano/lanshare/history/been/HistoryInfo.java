
package com.nano.lanshare.history.been;

import android.graphics.Bitmap;

public class HistoryInfo {
    public int id;
    public int historyType;
    public int fileType;
    public Bitmap peopleIcon;
    public Bitmap fileIcon;
    public boolean checked;
    public String filePath;
    public String fileSize;
    public String sender;
    public String reciver;
    public String date;
    public String receSize;
    public String costTime;
    public String status;

    public HistoryInfo() {

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HistoryInfo)) {
            return false;
        }
        HistoryInfo anther = (HistoryInfo) obj;
        return id == anther.id;
    }

    @Override
    public String toString() {
        return "HistoryInfo [id=" + id + ", historyType=" + historyType + ", fileType=" + fileType
                + ", peopleIcon=" + peopleIcon + ", fileIcon=" + fileIcon + ", checked=" + checked
                + ", filePath=" + filePath + ", fileSize=" + fileSize + ", sender=" + sender
                + ", reciver=" + reciver + ", date=" + date + "]";
    }
}
