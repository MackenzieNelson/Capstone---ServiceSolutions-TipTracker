package edu.cvtc.servicesolutions.tip_tracker;

import android.provider.BaseColumns;

public class JobsDatabaseContract {

    private JobsDatabaseContract() {

    }

    public static final class JobInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "job_info";
        public static final String COLUMN_JOB_TITLE = "job_title";
        public static final String COLUMN_JOB_DESCRIPTION = "job_description";

        public static final String INDEX1 = TABLE_NAME + "_index1";
        public static final String SQL_CREATE_INDEX1 = "CREATE INDEX " + INDEX1 + " ON " +
                TABLE_NAME + "(" + COLUMN_JOB_TITLE + ")";
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_JOB_TITLE + " TEXT NOT NULL, " +
                COLUMN_JOB_DESCRIPTION + " TEXT)";
    }
}
