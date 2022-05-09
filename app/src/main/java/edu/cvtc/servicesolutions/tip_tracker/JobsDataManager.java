package edu.cvtc.servicesolutions.tip_tracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;


public class JobsDataManager {

    // Member Attributes
    private static JobsDataManager ourInstance = null;
    private List<JobInfo> mJobs = new ArrayList<>();


    public static JobsDataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new JobsDataManager();
        }
        return ourInstance;
    }


    public List<JobInfo> getmJobs(){
        return mJobs;
    }


    private static void loadJobsFromDatabase(Cursor cursor) {
        int listTitlePosition = cursor.getColumnIndex(InfoEntry.COLUMN_JOB_TITLE);
        int listDescriptionPosition = cursor.getColumnIndex(InfoEntry.COLUMN_JOB_DESCRIPTION);
        int idPosition = cursor.getColumnIndex(InfoEntry._ID);

        JobsDataManager dm = getInstance();
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


    public static void loadFromDatabase(OpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] jobColumns = {
                InfoEntry.COLUMN_JOB_TITLE,
                InfoEntry.COLUMN_JOB_DESCRIPTION,
                InfoEntry._ID };

        String jobOrderBy = InfoEntry.COLUMN_JOB_TITLE;

        final Cursor jobCursor = db.query(InfoEntry.TABLE_NAME, jobColumns,
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
