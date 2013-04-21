
package com.nano.lanshare.Model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.nano.lanshare.history.been.HistoryInfo;

public class HistoryDBManager extends DBManager {

    public HistoryDBManager(Context context) {
        super(context);
    }

    public int delete(List<HistoryInfo> hisList) {
        mDb.beginTransaction();
        try {
            for (HistoryInfo info : hisList) {
                mDb.delete(HistoryTable.TABLE_NAME, HistoryTable.Clomns.ID + "=?",
                        new String[] {
                            info.id + ""
                        });
            }
            mDb.setTransactionSuccessful();
            notifyChange();
        } finally {
            mDb.endTransaction();
        }
        return 0;
    }

    public int insert(HistoryInfo hisInfo) {
        mDb.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(HistoryTable.Clomns.PATH, hisInfo.filePath);
            cv.put(HistoryTable.Clomns.DATE, hisInfo.date);
            cv.put(HistoryTable.Clomns.SENDER, hisInfo.sender);
            cv.put(HistoryTable.Clomns.RECIVER, hisInfo.reciver);
            cv.put(HistoryTable.Clomns.TYPE, hisInfo.historyType);
            mDb.insert(HistoryTable.TABLE_NAME, null, cv);
            mDb.setTransactionSuccessful();
            notifyChange();
        } finally {
            mDb.endTransaction();
        }
        return 0;
    }

    public int update(HistoryInfo hisInfo) {
        mDb.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(HistoryTable.Clomns.PATH, hisInfo.filePath);
            cv.put(HistoryTable.Clomns.DATE, hisInfo.date);
            cv.put(HistoryTable.Clomns.SENDER, hisInfo.sender);
            cv.put(HistoryTable.Clomns.RECIVER, hisInfo.reciver);
            cv.put(HistoryTable.Clomns.TYPE, hisInfo.historyType);
            mDb.update(HistoryTable.TABLE_NAME, cv,
                    HistoryTable.Clomns.ID + "=?", new String[] {
                        hisInfo.id + ""
                    });
            mDb.setTransactionSuccessful();
            notifyChange();
        } finally {
            mDb.endTransaction();
        }
        return 0;
    }

    public HistoryInfo queryByID(int id) {
        Cursor cursor = mDb.query(HistoryTable.TABLE_NAME, null, HistoryTable.Clomns.ID + "=?",
                new String[] {
                    id + ""
                }, null, null,
                HistoryTable.Clomns.DATE);
        if (cursor != null && cursor.moveToFirst()) {
            HistoryInfo info = new HistoryInfo();
            info.id = cursor.getInt(0);
            info.filePath = cursor.getString(1);
            info.date = cursor.getString(2);
            info.sender = cursor.getString(3);
            info.reciver = cursor.getString(4);
            info.historyType = cursor.getInt(5);
            return info;
        }
        return null;
    }

    public HistoryInfo queryByPath(String path) {
        Cursor cursor = mDb.query(HistoryTable.TABLE_NAME, null, HistoryTable.Clomns.PATH + "=?",
                new String[] {
                    path
                }, null, null,
                HistoryTable.Clomns.DATE);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                HistoryInfo info = new HistoryInfo();
                info.id = cursor.getInt(0);
                info.filePath = cursor.getString(1);
                info.date = cursor.getString(2);
                info.sender = cursor.getString(3);
                info.reciver = cursor.getString(4);
                info.historyType = cursor.getInt(5);
                return info;
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }

    public List<HistoryInfo> listAll() {
        Cursor cursor = mDb.query(HistoryTable.TABLE_NAME, null, null,
                null, null, null,
                HistoryTable.Clomns.DATE);
        List<HistoryInfo> infos = new ArrayList<HistoryInfo>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    HistoryInfo info = new HistoryInfo();
                    info.id = cursor.getInt(0);
                    info.filePath = cursor.getString(1);
                    info.date = cursor.getString(2);
                    info.sender = cursor.getString(3);
                    info.reciver = cursor.getString(4);
                    info.historyType = cursor.getInt(5);
                    infos.add(info);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return infos;
    }

    public List<HistoryInfo> listByLimit(int form, int to) {
        return null;
    }

    public int deleteByID(int id) {
        mDb.beginTransaction();
        try {
            mDb.delete(HistoryTable.TABLE_NAME, HistoryTable.Clomns.PATH + "=?",
                    new String[] {
                        id + ""
                    });
            mDb.setTransactionSuccessful();
            notifyChange();
        } finally {
            mDb.endTransaction();
        }
        return 0;
    }

}
