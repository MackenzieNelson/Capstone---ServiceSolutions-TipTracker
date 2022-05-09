package edu.cvtc.servicesolutions.tip_tracker;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {

    }

    public static final class InfoEntry implements BaseColumns {
        public static final String TABLE_JOB = "job_info";
        public static final String COLUMN_JOB_TITLE = "job_title";
        public static final String COLUMN_JOB_DESCRIPTION = "job_description";

        public static final String TABLE_INCOME = "income";
        public static final String COLUMN_HOURLY_RATE = "hourly_rate";
        public static final String COLUMN_HOURS_WORKED = "hours_worked";
        public static final String COLUMN_CASH_TIPS = "cash_tips";
        public static final String COLUMN_CREDIT_TIPS = "credit_tips";
        public static final String COLUMN_DATE = "date";


        public static final String TABLE_EXPENSES = "expenses";
        public static final String COLUMN_EXPENSE_NAME = "expense_name";
        public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";


        public static final String INDEX1 = TABLE_JOB + "_index1";
        public static final String SQL_CREATE_INDEX1 = "CREATE INDEX " + INDEX1 + " ON " +
                TABLE_JOB + "(" + COLUMN_JOB_TITLE + ")";
        // create job table
        public static final String SQL_CREATE_TABLE_JOBS = "CREATE TABLE " + TABLE_JOB + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_JOB_TITLE + " TEXT NOT NULL, " +
                COLUMN_JOB_DESCRIPTION + " TEXT)";
        // create income table
        public static final String SQL_CREATE_TABLE_INCOME = "CREATE TABLE " + TABLE_INCOME + " (" +
                _ID + " INTEGER PRIMARY KEY, " + COLUMN_HOURLY_RATE + " TEXT, " +
                COLUMN_HOURS_WORKED + " TEXT, " +  COLUMN_CASH_TIPS + " TEXT, " +
                COLUMN_CREDIT_TIPS + " TEXT, " +  COLUMN_DATE + " TEXT)";
        // create expenses table
        public static final String SQL_CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_EXPENSE_NAME + " TEXT NOT NULL, " +
                COLUMN_EXPENSE_AMOUNT + " TEXT NOT NULL)";
    }
}
