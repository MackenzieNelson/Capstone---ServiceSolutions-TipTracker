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

public class IncomeCalculationsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants
    public static final int ITEM_INCOME = 0;
    public static final int LOADER_INCOME = 0;
    
    // Member Variables
    private OpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mIncomeLayoutManager;
    private TipRecylcerAdapter mIncomeRecyclerAdapter;
    
    // Boolean to check if the 'onCreateLoader' method has run
    private boolean mIsCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_calculations);

        mDbOpenHelper = new OpenHelper(this);
        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        // Retrieve the information from your database
        IncomeDataManager.loadFromDatabase(mDbOpenHelper);
        
        // Set a reference to list of jobs layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_income_by_day);
        mIncomeLayoutManager = new LinearLayoutManager(this);
        
        // No cursor, so pass null
        mIncomeRecyclerAdapter = new TipRecylcerAdapter(this, null);
        
        // Display Income
        displayIncome();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LoaderManager.getInstance(this).restartLoader(LOADER_INCOME, null, this);
    }
    private void displayIncome() {
        mRecyclerItems.setLayoutManager(mIncomeLayoutManager);
        mRecyclerItems.setAdapter(mIncomeRecyclerAdapter);
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

        if (id == LOADER_INCOME) {
            loader = new CursorLoader(this) {
                @Override
                public Cursor loadInBackground () {
                    mIsCreated = true;
                    // open database in read mode
                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                    // create list of columns to be returned
                    String[] incomeColumns = {
                            DatabaseContract.InfoEntry.COLUMN_CASH_TIPS,
                            DatabaseContract.InfoEntry.COLUMN_CREDIT_TIPS,
                            DatabaseContract.InfoEntry.COLUMN_HOURLY_RATE,
                            DatabaseContract.InfoEntry.COLUMN_HOURS_WORKED,
                            DatabaseContract.InfoEntry.COLUMN_DATE,
                            DatabaseContract.InfoEntry._ID };

                    // create an order by field for sorting
                    String incomeOrderBy = DatabaseContract.InfoEntry.COLUMN_DATE;

                    // populate cursor with results
                    return db.query(DatabaseContract.InfoEntry.TABLE_NAME,incomeColumns,
                            null, null, null, null,
                            incomeOrderBy);
                }
            };
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_INCOME && mIsCreated) {
            // associate cursor with RecyclerAdapter
            mIncomeRecyclerAdapter.changeCursor(data);
            mIsCreated = false;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_INCOME) {
            // change the cursor to null (cleanup)
            mIncomeRecyclerAdapter.changeCursor(null);
        }
    }
}
