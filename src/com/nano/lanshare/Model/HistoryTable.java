
package com.nano.lanshare.Model;

public class HistoryTable {
    public static final String DB_NAME = "lanshare.db";
    public static final String TABLE_NAME = "history";
    public static final String CREATE_DB_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(" + Clomns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Clomns.PATH
            + " VARCHAR, " + Clomns.DATE + " INTEGER, " + Clomns.SENDER
            + " VARCHAR, " + Clomns.RECIVER + " VARCHAR,  "+Clomns.TYPE+ " INTEGER )";

    public static class Clomns {
        public static final String ID = "_id";
        public static final String PATH = "path";
        public static final String DATE = "date";
        public static final String SENDER = "sender";
        public static final String RECIVER = "reciver";
        public static final String TYPE = "type";
        public static final String[] CLOMNS_PRJECTION = {
                ID, PATH, DATE, SENDER, RECIVER, TYPE
        };
    }
}
