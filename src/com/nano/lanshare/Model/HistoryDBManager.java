
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
                mDb.delete(HistoryTable.TABLE_NAME, HistoryTable.Columns.ID + "=?",
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

    public long insert(HistoryInfo hisInfo) {
        mDb.beginTransaction();
        long insertId = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(HistoryTable.Columns.PATH, hisInfo.filePath);
            cv.put(HistoryTable.Columns.DATE, hisInfo.date);
            cv.put(HistoryTable.Columns.SENDER, hisInfo.sender);
            cv.put(HistoryTable.Columns.RECIVER, hisInfo.reciver);
            cv.put(HistoryTable.Columns.TYPE, hisInfo.historyType);
            insertId = mDb.insert(HistoryTable.TABLE_NAME, null, cv);
            mDb.setTransactionSuccessful();
            notifyChange();
        } finally {
            mDb.endTransaction();
        }
        return insertId;
    }

    public int update(HistoryInfo hisInfo) {
        mDb.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(HistoryTable.Columns.PATH, hisInfo.filePath);
            cv.put(HistoryTable.Columns.DATE, hisInfo.date);
            cv.put(HistoryTable.Columns.SENDER, hisInfo.sender);
            cv.put(HistoryTable.Columns.RECIVER, hisInfo.reciver);
            cv.put(HistoryTable.Columns.TYPE, hisInfo.historyType);
            mDb.update(HistoryTable.TABLE_NAME, cv,
                    HistoryTable.Columns.ID + "=?", new String[] {
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
        Cursor cursor = mDb.query(HistoryTable.TABLE_NAME, null, HistoryTable.Columns.ID + "=?",
                new String[] {
                    id + ""
                }, null, null,
                HistoryTable.Columns.DATE);
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
        Cursor cursor = mDb.query(HistoryTable.TABLE_NAME, null, HistoryTable.Columns.PATH + "=?",
                new String[] {
                    path
                }, null, null,
                HistoryTable.Columns.DATE);
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
                HistoryTable.Columns.DATE);
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
            mDb.delete(HistoryTable.TABLE_NAME, HistoryTable.Columns.ID + "=?",
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
