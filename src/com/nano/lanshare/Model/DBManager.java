
package com.nano.lanshare.Model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBManager {
    protected DBHelper mDbHelper;
    protected SQLiteDatabase mDb;
    List<IDataBaseChange> mList = new ArrayList<IDataBaseChange>();

    public DBManager(Context context) {
        mDbHelper = new DBHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public boolean closeDB() {
        if (mDb != null && mDb.isOpen()) {
            mDb.close();
        }
        return true;
    }

    public void registerContentObserver(IDataBaseChange change) {
        synchronized (mList) {
            if (!mList.contains(change)) {
                mList.add(change);
            }
        }
    }

    public void unregisterContentObserver(IDataBaseChange change) {
        synchronized (mList) {
            if (mList.contains(change)) {
                mList.remove(change);
            }
        }
    }

    public void notifyChange() {
        synchronized (mList) {
            for (IDataBaseChange change : mList) {
                change.onDataChange();
            }
        }
    }
}
