package edu.cvtc.servicesolutions.tip_tracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class DataManager {

    // Member Attributes
    private static DataManager ourInstance = null;
    private List<JobInfo> mJobs = new ArrayList<>();


    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }


    public List<JobInfo> getmJobs(){
        return getmJobs();
    }


    private static void loadJobsFromDatabase(Cursor cursor) {
        int listTitlePosition = cursor.getColumnIndex(JobsDatabaseContract.JobInfoEntry.COLUMN_JOB_TITLE);
        int listDescriptionPosition = cursor.getColumnIndex(JobsDatabaseContract.JobInfoEntry.COLUMN_JOB_DESCRIPTION);
        int idPosition = cursor.getColumnIndex(JobsDatabaseContract.JobInfoEntry._ID);

        DataManager dm = getInstance();
        dm.mJobs.clear();

        while (cursor.moveToNext()) {
            String listTitle = cursor.getString(listTitlePosition);
            String listDescription = cursor.getString(listDescriptionPosition);
            int id = cursor.getInt(idPosition);

            JobInfo list = new JobInfo(id, listTitle, listDescription);

            dm.mJobs.add(list);
        }
        cursor.close();
    }


    public static void loadFromDatabase(JobOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] jobColumns = {
                JobsDatabaseContract.JobInfoEntry.COLUMN_JOB_TITLE,
                JobsDatabaseContract.JobInfoEntry.COLUMN_JOB_DESCRIPTION,
                JobsDatabaseContract.JobInfoEntry._ID };

        String jobOrderBy = JobsDatabaseContract.JobInfoEntry.COLUMN_JOB_TITLE;

        final Cursor jobCursor = db.query(JobsDatabaseContract.JobInfoEntry.TABLE_NAME, jobColumns,
                null, null, null, null, jobOrderBy);

        loadJobsFromDatabase(jobCursor);
    }


    public int createNewJob() {
        JobInfo job = new JobInfo(null, null);
        mJobs.add(job);
        return mJobs.size();
    }


    public void removeJob(int index) {
        mJobs.remove(index);
    }
}
