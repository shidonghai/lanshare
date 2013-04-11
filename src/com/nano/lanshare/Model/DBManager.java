
package com.nano.lanshare.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBManager {
    protected DBHelper mDbHelper;
    protected SQLiteDatabase mDb;

    public DBManager(Context context) {
        mDbHelper = new DBHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public  boolean closeDB() {
        if (mDb != null && mDb.isOpen()) {
            mDb.close();
        }
        return true;
    }
}
