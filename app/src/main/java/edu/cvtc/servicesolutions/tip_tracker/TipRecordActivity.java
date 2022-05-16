package edu.cvtc.servicesolutions.tip_tracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.TimeZone;

public class TipRecordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Date Range Picker
    private Button mDatePickerBtn;
    private TextView mSelectedDateText;

    // Constants
    public static final int ITEM_TIPS = 0;
    public static final int LOADER_TIPS = 0;

    // Member Variables
    private OpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mTipsLayoutManager;
    private TipRecylcerAdapter mTipRecyclerAdapter;

    // Boolean to check if then 'onCreateLoader' method has run
    private boolean mIsCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tip);

        mDbOpenHelper = new OpenHelper(this);
        initializeDisplayContent();

        mDatePickerBtn = findViewById(R.id.tip_datePickerBtn);
        mSelectedDateText = findViewById(R.id.selected_date);


        // MaterialDatePicker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();


        MaterialDatePicker materialDatePicker = builder.build();

        mDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        mSelectedDateText.setText(materialDatePicker.getHeaderText());

                    }
                });
            }
        });

    }

    private void initializeDisplayContent() {
        // Retrieve the information from your database
        JobsDataManager.loadFromDatabase(mDbOpenHelper);

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
        if (mRecyclerItems != null) {
            mRecyclerItems.setLayoutManager(mTipsLayoutManager);
            mRecyclerItems.setAdapter(mTipRecyclerAdapter);
        }
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
                            DatabaseContract.InfoEntry.COLUMN_HOURLY_RATE,
                            DatabaseContract.InfoEntry.COLUMN_HOURS_WORKED,
                            DatabaseContract.InfoEntry.COLUMN_CASH_TIPS,
                            DatabaseContract.InfoEntry.COLUMN_CREDIT_TIPS,
                            DatabaseContract.InfoEntry.COLUMN_DATE,
                            DatabaseContract.InfoEntry._ID };

                    // create an order by field for sorting
                    String tipOrderBy = DatabaseContract.InfoEntry.COLUMN_DATE;

                    // populate cursor with results
                    return db.query(DatabaseContract.InfoEntry.TABLE_INCOME, tipColumns,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tip_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_track_tips) {
            Intent intent = new Intent(TipRecordActivity.this, IncomeActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_budget) {
            Intent intent = new Intent(TipRecordActivity.this, BudgetActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(TipRecordActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(TipRecordActivity.this, JobActivityMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_income_calc) {
            Intent intent = new Intent(TipRecordActivity.this, IncomeCalculationsActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
