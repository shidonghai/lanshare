
package com.nano.lanshare.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TrafficStatusManager extends DBManager {

    public TrafficStatusManager(Context context) {
        super(context);
    }

    public int insert(TrafficInfo info) {
        mDb.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(TrafficStatusTable.Columns.DATE, info.date);
            cv.put(TrafficStatusTable.Columns.SENT, info.sendData);
            cv.put(TrafficStatusTable.Columns.RECEIVED, info.recvData);
            cv.put(TrafficStatusTable.Columns.TYPE, info.type);
            cv.put(TrafficStatusTable.Columns.GPRS, info.gprsData);
            cv.put(TrafficStatusTable.Columns.WIFI, info.wifiData);
            cv.put(TrafficStatusTable.Columns.WIFI_AP, info.wifiApData);
            mDb.insert(TrafficStatusTable.TABLE_NAME, null, cv);
            mDb.setTransactionSuccessful();
            notifyChange();
        } finally {
            mDb.endTransaction();
        }
        return 0;
    }

    public TrafficInfo query() {
        Cursor cursor = mDb
                .query(TrafficStatusTable.TABLE_NAME, null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                TrafficInfo info = new TrafficInfo();
                info.date = cursor.getString(1);
                do {
                    info.recvData += cursor.getInt(3);
                    info.sendData += cursor.getInt(4);
                    info.wifiData += cursor.getInt(5);
                    info.gprsData += cursor.getInt(6);
                    info.wifiApData += cursor.getInt(7);
                } while (cursor.moveToNext());
                return info;
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

}
