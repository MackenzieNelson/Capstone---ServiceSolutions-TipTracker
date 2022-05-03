package edu.cvtc.servicesolutions.tip_tracker;

import static edu.cvtc.servicesolutions.tip_tracker.IncomeActivity.LOADER_INCOME;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;

public class BudgetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private JobOpenHelper mDbOpenHelper;
    public static final int LOADER_EXPENSES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new JobOpenHelper(this);

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
