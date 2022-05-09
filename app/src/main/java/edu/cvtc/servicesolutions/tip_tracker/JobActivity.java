package edu.cvtc.servicesolutions.tip_tracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;

public class JobActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants
    public static final String JOB_ID = "edu.cvtc.servicesolutions.tip_tracker.JOB_ID";
    public static final String ORIGINAL_JOB_TITLE = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_JOB_TITLE";
    public static final String ORIGINAL_JOB_DESCRIPTION = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_JOB_DESCRIPTION";
    public static final int ID_NOT_SET = -1;
    public static final int LOADER_JOBS = 0;

    // Initialize new JobInfo to empty
    private JobInfo mJob = new JobInfo(0, "", "");

    // Member Variables
    private boolean mIsNewJob;
    private boolean mIsCancelling;
    private int mJobID;
    private int mJobTitlePosition;
    private int mJobDescriptionPosition;
    private String mOriginalJobTitle;
    private String mOriginalJobDescription;

    // Member Objects
    private EditText mTextJobTitle;
    private EditText mTextJobDescription;
    private OpenHelper mDbOpenHelper;
    private Cursor mJobCursor;

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_add_job);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDbOpenHelper = new OpenHelper(this);

        readDisplayStateValues();

        // If the bundle is null, save the values. Otherwise
        // restore the original values.
        if (savedInstanceState == null) {
            saveOriginalJobValues();
        } else {
            restoreOriginalJobValues(savedInstanceState);
        }
        mTextJobTitle = findViewById(R.id.text_job_title);
        mTextJobDescription = findViewById(R.id.text_job_description);

        // If it is not a new job, load the job into the layout
        if (!mIsNewJob) {
            LoaderManager.getInstance(this).initLoader(LOADER_JOBS, null, this);
        }
    }

    private void displayJob() {
        // Retrieve the values from the cursor based upon
        // the position of the columns.
        String jobTitle = mJobCursor.getString(mJobTitlePosition);
        String jobDescription = mJobCursor.getString(mJobDescriptionPosition);

        // Use the information to populate the layout
        mTextJobTitle.setText(jobTitle);
        mTextJobDescription.setText(jobDescription);
    }

    private void saveOriginalJobValues() {
        // Only save values if you do not have a new job
        if (!mIsNewJob) {
            mOriginalJobTitle = mJob.getmJobTitle();
            mOriginalJobDescription = mJob.getmJobDescription();
        }
    }

    private void restoreOriginalJobValues(Bundle savedInstanceState) {
        // Get the original values from the savedInstanceState
        mOriginalJobTitle = savedInstanceState.getString(ORIGINAL_JOB_TITLE);
        mOriginalJobDescription = savedInstanceState.getString(ORIGINAL_JOB_DESCRIPTION);
    }

        private void readDisplayStateValues() {
            // Get the intent passed into the activity
            Intent intent = getIntent();

            // Get the job id passed into the intent
            mJobID = intent.getIntExtra(JOB_ID, ID_NOT_SET);

            // If the job id is not set, create a new job
            mIsNewJob = mJobID == ID_NOT_SET;
            if (mIsNewJob) {
                createNewJob();
            }
        }

        private void createNewJob() {
        // Create ContentValues object to hold our fields
            ContentValues values = new ContentValues();

            // We don't know the values for a new job
            // Set columns to empty
            values.put(InfoEntry.COLUMN_JOB_TITLE, "");
            values.put(InfoEntry.COLUMN_JOB_DESCRIPTION, "");

            // Get connection to the database.
            SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

            // Insert the new row in the database and assign new id
            // return value to an int
            mJobID = (int)db.insert(InfoEntry.TABLE_JOB, null, values);
        }

        @NonNull
        @Override
        // Create the loader
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a local cursor loader
            CursorLoader loader = null;

            // Check to see if the id is for your loader
            if (id == LOADER_JOBS) {
                loader = createLoaderJobs();
            }
            return loader;
        }

        private CursorLoader createLoaderJobs() {
        return new CursorLoader(this) {
            @Override
            public Cursor loadInBackground() {
                // Open a database connection
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                // Build selection criteria
                String selection = InfoEntry._ID + " = ?";
                String[] selectionArgs = {Integer.toString(mJobID)};

                // Create a list of the columns
                String[] jobColumns = {
                        InfoEntry.COLUMN_JOB_TITLE,
                        InfoEntry.COLUMN_JOB_DESCRIPTION,
                };

                // Fill cursor with the info provided
                return db.query(InfoEntry.TABLE_JOB, jobColumns,
                        selection, selectionArgs, null, null, null);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Check to see if this is your cursor
        if (loader.getId() == LOADER_JOBS) {
            loadFinishedJobs(data);
        }
    }

    private void loadFinishedJobs(Cursor data) {
        // Populate your member cursor with the data
        mJobCursor = data;
        // Get the positions of the fields in the cursor so that
        // you are able to retrieve them into your layout.
        mJobTitlePosition = mJobCursor.getColumnIndex(InfoEntry.COLUMN_JOB_TITLE);
        mJobDescriptionPosition = mJobCursor.getColumnIndex(InfoEntry.COLUMN_JOB_DESCRIPTION);

        // Make sure that you have moved to the correct record.
        // The cursor will not have populated any of the
        // fields until you move it.
        mJobCursor.moveToNext();

        // Call the method to display the course.
        displayJob();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Check to see if this is your cursor for your loader
        if (loader.getId() == LOADER_JOBS) {
            // If the cursor is not null, close it
            if (mJobCursor != null ) {
                mJobCursor.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the action bar item clicks here. The action bar
        // will automatically handle clicks on the Home/Up button, so
        // long as you specify a parent activity
        switch (item.getItemId()) {
            case R.id.action_cancel:
                mIsCancelling = true;
                finish();
                break;
            case android.R.id.home:
                if (TextUtils.isEmpty(mTextJobTitle.getText().toString()) && TextUtils.isEmpty(mTextJobDescription.getText().toString())) {
                    Toast.makeText(JobActivity.this, "Job Title and Job Description is Required", Toast.LENGTH_LONG).show();
                    mIsCancelling = true;
                } else if (TextUtils.isEmpty(mTextJobTitle.getText().toString())) {
                    Toast.makeText(JobActivity.this, "Job Title is Required", Toast.LENGTH_LONG).show();
                    mIsCancelling = true;
                } else if (TextUtils.isEmpty(mTextJobDescription.getText().toString())) {
                    Toast.makeText(JobActivity.this, "Job Description is Required", Toast.LENGTH_LONG).show();
                    mIsCancelling = true;
                } else {
                    saveJob();
                    Intent intent = new Intent(this, JobActivityMain.class);
                    startActivity(intent);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveJobToDatabase(String jobTitle, String jobDescription) {
        // Create selection criteria
        String selection = InfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mJobID)};

        // Use a ContentValue object to put our information into.
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.InfoEntry.COLUMN_JOB_TITLE, jobTitle);
        values.put(DatabaseContract.InfoEntry.COLUMN_JOB_DESCRIPTION, jobDescription);

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                // Get connection to the database. Use the writeable
                // method since we are changing the data.
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                // Call the update method
                db.update(InfoEntry.TABLE_JOB, values, selection, selectionArgs);
                return null;
            }
        };
        task.loadInBackground();
    }

    private void storePreviousJobValues() {
        mJob.setmJobTitle(mOriginalJobTitle);
        mJob.setmJobDescription(mOriginalJobDescription);
    }

    private void saveJob() {
        // Get the value from the layout
        String jobTitle = mTextJobTitle.getText().toString();
        String jobDescription = mTextJobDescription.getText().toString();

        // Call the method to write to the database
        saveJobToDatabase(jobTitle, jobDescription);
    }

    private void deleteJobFromDatabase() {
        // Create selection criteria
        final String selection = InfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mJobID)};

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                // Get connection to the database. Use the writable
                // method since we are changing the data.
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                // Call the delete method
                db.delete(InfoEntry.TABLE_JOB, selection, selectionArgs);
                return null;
            }
        };
        task.loadInBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Did the user cancel?
        if (mIsCancelling) {
            // Is this a new job?
            if (mIsNewJob) {
                deleteJobFromDatabase();
            } else {
                // Put the original values on the screen
                storePreviousJobValues();
            }
        } else {
            // save the data when leaving
            saveJob();
        }
    }
}
