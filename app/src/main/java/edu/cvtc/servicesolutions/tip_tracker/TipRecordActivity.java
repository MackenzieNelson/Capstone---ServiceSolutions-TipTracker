package edu.cvtc.servicesolutions.tip_tracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TipRecordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants
    public static final int ITEM_TIPS = 0;
    public static final int LOADER_TIPS = 0;

    // Member Variables
    private JobOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mTipsLayoutManager;
    private TipRecylcerAdapter mTipRecyclerAdapter;

    // Boolean to check if then 'onCreateLoader' method has run
    private boolean mIsCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tip);

        mDbOpenHelper = new JobOpenHelper(this);
        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        // Retrieve the information from your database
        DataManager.loadFromDatabase(mDbOpenHelper);

        // Set a reference to list of tips layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_tips);
        mTipsLayoutManager = new LinearLayoutManager(this);

        // No cursor yet, so pass null
        mTipRecyclerAdapter = new TipRecylcerAdapter(this, null);

        // Display the tips
        displayTips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // use restartLoader instead of initLoader to make sure
        // you re-query the database each time the activity is
        // loaded in the app
        LoaderManager.getInstance(this).restartLoader(LOADER_TIPS, null, this);
    }

    private void displayTips() {
        mRecyclerItems.setLayoutManager(mTipsLayoutManager);
        mRecyclerItems.setAdapter(mTipRecyclerAdapter);
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

        if (id == LOADER_TIPS) {
            loader = new CursorLoader(this) {
                @Override
                public Cursor loadInBackground () {
                    mIsCreated = true;
                    // open database in read mode
                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                    // create list of columns to be returned
                    String[] tipColumns = {
                            DatabaseContract.JobInfoEntry.COLUMN_HOURLY_RATE,
                            DatabaseContract.JobInfoEntry.COLUMN_HOURS_WORKED,
                            DatabaseContract.JobInfoEntry.COLUMN_CASH_TIPS,
                            DatabaseContract.JobInfoEntry.COLUMN_CREDIT_TIPS,
                            DatabaseContract.JobInfoEntry.COLUMN_DATE,
                            DatabaseContract.JobInfoEntry._ID };

                    // create an order by field for sorting
                    String tipOrderBy = DatabaseContract.JobInfoEntry.COLUMN_DATE;

                    // populate cursor with results
                    return db.query(DatabaseContract.JobInfoEntry.TABLE_NAME,tipColumns,
                            null, null, null, null,
                            tipOrderBy);
                }
            };
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_TIPS && mIsCreated) {
            // associate cursor with RecyclerAdapter
            mTipRecyclerAdapter.changeCursor(data);
            mIsCreated = false;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_TIPS) {
            // change the cursor to null (cleanup)
            mTipRecyclerAdapter.changeCursor(null);
        }
    }

}
