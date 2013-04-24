
package com.nano.lanshare.Model;

public class HistoryTable {
    public static final String TABLE_NAME = "history";
    public static final String CREATE_DB_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(" + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Columns.PATH
            + " VARCHAR, " + Columns.DATE + " INTEGER, " + Columns.SENDER
            + " VARCHAR, " + Columns.RECIVER + " VARCHAR,  " + Columns.TYPE + " INTEGER "
            + Columns.STATUS + " INTEGER, " + Columns.RECV_SIZE + " INTEGER, "
            + Columns.TOTAL_SIZE + " INTEGER " + ")";

    public static class Columns {
        public static final String ID = "_id";
        public static final String PATH = "path";
        public static final String DATE = "date";
        public static final String SENDER = "sender";
        public static final String RECIVER = "reciver";
        public static final String TYPE = "type";
        public static final String STATUS = "status";
        public static final String RECV_SIZE = "recvSize";
        public static final String TOTAL_SIZE = "totalSize";

        public static final String[] CLOMNS_PRJECTION = {
                ID, PATH, DATE, SENDER, RECIVER, TYPE, TYPE, RECV_SIZE, TOTAL_SIZE
        };
    }
}
