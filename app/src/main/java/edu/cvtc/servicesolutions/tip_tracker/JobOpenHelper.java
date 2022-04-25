package edu.cvtc.servicesolutions.tip_tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry;

import androidx.annotation.Nullable;

public class JobOpenHelper extends SQLiteOpenHelper {

    // Constants
    public static final String DATABASE_NAME = "Jobs_ServerSolutions.db";
    public static final int DATABASE_VERSION = 1;

    public JobOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JobInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(JobInfoEntry.SQL_CREATE_INDEX1);

        JobsDataWorker worker = new JobsDataWorker(db);
        worker.insertJobs();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
