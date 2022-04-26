package edu.cvtc.servicesolutions.tip_tracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry;

public class JobsDataWorker {

    // Member Attributes
    private SQLiteDatabase mDb;

    public JobsDataWorker(SQLiteDatabase db) { mDb = db; }

    private void insertJob(String jobTitle, String jobDescription) {
        ContentValues values = new ContentValues();
        values.put(JobInfoEntry.COLUMN_JOB_TITLE, jobTitle);
        values.put(JobInfoEntry.COLUMN_JOB_DESCRIPTION, jobDescription);

        long newRowId = mDb.insert(JobInfoEntry.TABLE_NAME, null, values);
    }

    public void insertJobs() {
        insertJob("Server/Bartender", "Typical FOH Duties");
    }
}
