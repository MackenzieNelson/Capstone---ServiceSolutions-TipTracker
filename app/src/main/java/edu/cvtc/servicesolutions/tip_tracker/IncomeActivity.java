package edu.cvtc.servicesolutions.tip_tracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import edu.cvtc.servicesolutions.tip_tracker.R;
import edu.cvtc.servicesolutions.tip_tracker.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class IncomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //Constants
    public static final String ORIGINAL_HOURLY_RATE = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_HOURLY_RATE";
    public static final String ORIGINAL_HOURS_WORKED = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_HOURS_WORKED";
    public static final String ORIGINAL_CASH_TIP = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_CASH_TIP";
    public static final String ORIGINAL_CREDIT_TIP = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_CREDIT_TIP";
    public static final String ORIGINAL_DATE = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_DATE";
    public static final int ID_NOT_SET = -1;
    public static final int LOADER_GAMES = 0;

    //Member Variables
    private boolean mIsNewIncome;
    private String originalHoursWorked;
    private String originalHourlyRate;
    private String originalCashTip;
    private String originalCreditTip;
    private String originalDate;

    //Member objects
    private EditText hourlyRateText;
    private EditText hoursWorkedText;
    private EditText cashTipText;
    private EditText creditTipText;
    private JobOpenHelper mDbOpenHelper;
    private Cursor mCursor;

    // Calendar to pick date to add tips
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    // Navigation drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbOpenHelper = new JobOpenHelper(this);
        readDisplayStateValues();
        // If the bundle is null, save the values. Otherwise restore the original values.
        if (savedInstanceState == null) {
            saveOriginalIncomeValues();
        } else {
            restoreOriginalIncomeValues(savedInstanceState);
        }
        hourlyRateText = findViewById(R.id.hourly_rate_text);
        hoursWorkedText = findViewById(R.id.hours_worked_text);
        cashTipText = findViewById(R.id.add_cash_tip_text);
        creditTipText = findViewById(R.id.add_credit_tip_text);

        // If it is not a new income, load the income data into the layout
        if (!mIsNewIncome) {
            LoaderManager.getInstance(this).initLoader(LOADER_GAMES, null, this);
        }

        setContentView(R.layout.content_main);
        intDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Add arrow to menu to close
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // When user clicks on item get callback
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Log.i("MENU_DRAWER_TAG", "Home item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_track_tips:
                        Log.i("MENU_DRAWER_TAG", "Track Tip item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_record_tips:
                        Log.i("MENU_DRAWER_TAG", "Record Tip item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_budget:
                        Log.i("MENU_DRAWER_TAG", "Budget item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_income_calc:
                        Log.i("MENU_DRAWER_TAG", "Income Calculation item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_settings:
                        Log.i("MENU_DRAWER_TAG", "Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });


    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void restoreOriginalIncomeValues(Bundle savedInstanceState) {
        // Get the original values from the savedInstanceState
        originalHoursWorked = savedInstanceState.getString(ORIGINAL_HOURS_WORKED);
        originalHourlyRate = savedInstanceState.getString(ORIGINAL_HOURLY_RATE);
        originalCashTip = savedInstanceState.getString(ORIGINAL_CASH_TIP);
        originalCreditTip = savedInstanceState.getString(ORIGINAL_CREDIT_TIP);
        originalDate = savedInstanceState.getString(ORIGINAL_DATE);

    }

    private void saveOriginalIncomeValues() {
        // Only save values if you do not have a new game
        if (!mIsNewIncome) {
            originalHoursWorked = mGame.getTitle();
            originalHourlyRate = mGame.getPlatform();
            originalCashTip =
                    originalCreditTip
        }
    }

    private void readDisplayStateValues() {
        // Get the intent passed into the activity
        Intent intent = getIntent();
        // Get the income id passed into the intent
        mGameId = intent.getIntExtra(GAME_ID, ID_NOT_SET);
        // If the game id is not set, create a new game
        mIsNewGame = mGameId == ID_NOT_SET;
        if (mIsNewIncome) {
            createNewGame();
        }
    }

    private void createNewGame() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_TITLE, "");
        values.put(COLUMN_GAME_PLATFORM, "");
        values.put(COLUMN_GAME_RATING, "");
        values.put(COLUMN_GAME_CATEGORY, "");
        values.put(COLUMN_GAME_PLAYERS, "");

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        mGameId = (int)db.insert(JobEntry.TABLE_NAME, null, values);
    }

    private void intDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day );
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APRIL";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUNE";
        if(month == 7)
            return "JULY";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEPT";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        // default should never need it
        return "JAN";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
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