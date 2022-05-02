package edu.cvtc.servicesolutions.tip_tracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry;

public class JobActivityMain extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants
    public static final int ITEM_JOBS = 0;
    public static final int LOADER_JOBS = 0;

    // Member Variables
    private JobOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mJobsLayoutManager;
    private JobRecyclerAdapter mJobRecyclerAdapter;

    // Boolean to check if then 'onCreateLoader' method has run
    private boolean mIsCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_job);

        Toolbar jobToolbar = (Toolbar) findViewById(R.id.add_job_toolbar);
        jobToolbar.setTitle("Add a Job");
        jobToolbar.setNavigationIcon(R.drawable.ic_add_job);
        setSupportActionBar(jobToolbar);

        jobToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startAddJob();
                    }
                });

        mDbOpenHelper = new JobOpenHelper(this);
        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        // Retrieve the information from your database
        DataManager.loadFromDatabase(mDbOpenHelper);

        // Set a reference to list of jobs layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_jobs);
        mJobsLayoutManager = new LinearLayoutManager(this);

        // No cursor yet, so pass null
        mJobRecyclerAdapter = new JobRecyclerAdapter(this, null);

        // Display the jobs
        displayJobs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // use restartLoader instead of initLoader to make sure
        // you re-query the database each time the activity is
        // loaded in the app
        LoaderManager.getInstance(this).restartLoader(LOADER_JOBS, null, this);
    }

    private void displayJobs() {
        mRecyclerItems.setLayoutManager(mJobsLayoutManager);
        mRecyclerItems.setAdapter(mJobRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader = null;

        if (id == LOADER_JOBS) {
            loader = new CursorLoader(this) {
                @Override
                public Cursor loadInBackground () {
                    mIsCreated = true;
                    // open database in read mode
                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                    // create list of columns to be returned
                    String[] jobColumns = {
                            JobInfoEntry.COLUMN_JOB_TITLE,
                            JobInfoEntry.COLUMN_JOB_DESCRIPTION,
                            JobInfoEntry._ID };

                    // create an order by field for sorting
                    String jobOrderBy = JobInfoEntry.COLUMN_JOB_TITLE;

                    // populate cursor with results
                    return db.query(JobInfoEntry.TABLE_NAME,jobColumns,
                            null, null, null, null,
                            jobOrderBy);
            }
        };
    }
    return loader;
}

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    if (loader.getId() == LOADER_JOBS && mIsCreated) {
        // associate cursor with RecyclerAdapter
        mJobRecyclerAdapter.changeCursor(data);
        mIsCreated = false;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    if (loader.getId() == LOADER_JOBS) {
        // change the cursor to null (cleanup)
        mJobRecyclerAdapter.changeCursor(null);
        }
    }

    private void startAddJob() {
        Intent intent = new Intent(JobActivityMain.this, JobActivity.class);
        startActivity(intent);
    }
}
