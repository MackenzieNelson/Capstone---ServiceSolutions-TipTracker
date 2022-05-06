package edu.cvtc.servicesolutions.tip_tracker;

import static edu.cvtc.servicesolutions.tip_tracker.IncomeActivity.LOADER_INCOME;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class BudgetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private OpenHelper mDbOpenHelper;
    public static final int LOADER_EXPENSES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new OpenHelper(this);

        LoaderManager.getInstance(this).initLoader(LOADER_INCOME, null, this);
        LoaderManager.getInstance(this).initLoader(LOADER_EXPENSES, null, this);


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
