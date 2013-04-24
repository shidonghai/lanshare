
package com.nano.lanshare.Model;

public class TrafficStatusTable {
    public static final String TABLE_NAME = "traffic";
    public static final String CREATE_DB_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(" + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Columns.DATE
            + " INTEGER, " + Columns.TYPE + " INTEGER, " + Columns.RECEIVED + " INTEGER, "
            + Columns.SENT + " INTEGER, " + Columns.WIFI + " INTEGER, " + Columns.GPRS
            + " INTEGER, "
            + Columns.WIFI_AP + " INTEGER, "
            + Columns.TOTAL + " INTEGER " + ")";

    public static class Columns {
        public static final String ID = "_id";
        public static final String DATE = "date";
        public static final String TYPE = "type";
        public static final String RECEIVED = "recv";
        public static final String SENT = "sent";
        public static final String WIFI = "wifi";
        public static final String GPRS = "gprs";
        public static final String WIFI_AP = "wifiAp";
        public static final String TOTAL = "totalSize";

        public static final String[] CLOMNS_PRJECTION = {
                ID, TYPE, DATE, RECEIVED, SENT, WIFI, GPRS, WIFI_AP, TOTAL
        };
    }
}
