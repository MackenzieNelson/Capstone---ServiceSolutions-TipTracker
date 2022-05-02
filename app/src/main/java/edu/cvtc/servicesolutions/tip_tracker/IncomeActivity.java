package edu.cvtc.servicesolutions.tip_tracker;

import static java.lang.Double.parseDouble;
import static edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry.COLUMN_CASH_TIPS;
import static edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry.COLUMN_CREDIT_TIPS;
import static edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry.COLUMN_DATE;
import static edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry.COLUMN_HOURLY_RATE;
import static edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry.COLUMN_HOURS_WORKED;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
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
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
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
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry;

public class IncomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //Constants
    public static final String INCOME_ID = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_INCOME_ID";
    public static final String ORIGINAL_HOURLY_RATE = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_HOURLY_RATE";
    public static final String ORIGINAL_HOURS_WORKED =  "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_HOURS_WORKED";
    public static final String ORIGINAL_CASH_TIP = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_CASH_TIP";
    public static final String ORIGINAL_CREDIT_TIP = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_CREDIT_TIP";
    public static final String ORIGINAL_DATE = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_DATE";
    public static final int ID_NOT_SET = -1;
    public static final int LOADER_INCOME = 0;

    private IncomeInfo incomeInfo = new IncomeInfo(0, 0, 0, 0, 0, null);

    //Member Variables
    private boolean mIsNewIncome;
    private boolean mIsCancelling;
    private int mIncomeId;
    private double originalHoursWorked;
    private double originalHourlyRate;
    private double originalCashTip;
    private double originalCreditTip;
    private Date originalDate;

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
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new JobOpenHelper(this);
        readDisplayStateValues();
        // If the bundle is null, save the values. Otherwise restore the original values.
        if (savedInstanceState == null) {
            saveOriginalIncomeValues();
        } else {
            try {
                restoreOriginalIncomeValues(savedInstanceState);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        hourlyRateText = findViewById(R.id.hourly_rate_text);
        hoursWorkedText = findViewById(R.id.hours_worked_text);
        cashTipText = findViewById(R.id.add_cash_tip_text);
        creditTipText = findViewById(R.id.add_credit_tip_text);
        Button submitButton = findViewById(R.id.submit_tip_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                Context context = getApplicationContext();

                Toast toast = Toast.makeText(context, "Income Recorded", Toast.LENGTH_SHORT);
                toast.show();

                saveIncome();
            }
        });

        // If it is not a new income, load the income data into the layout
        if (!mIsNewIncome) {
            LoaderManager.getInstance(this).initLoader(LOADER_INCOME, null, this);
        }

        intDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        if (drawerLayout != null) {
            navigationView = findViewById(R.id.navigationView);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_Open, R.string.menu_Close);
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
    }
   @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        super.onBackPressed();
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

    private void restoreOriginalIncomeValues(Bundle savedInstanceState) throws ParseException {
        // Get the original values from the savedInstanceState
        originalHoursWorked = parseDouble(savedInstanceState.getString(ORIGINAL_HOURS_WORKED));
        originalHourlyRate = parseDouble(savedInstanceState.getString(ORIGINAL_HOURLY_RATE));
        originalCashTip = parseDouble(savedInstanceState.getString(ORIGINAL_CASH_TIP));
        originalCreditTip = parseDouble(savedInstanceState.getString(ORIGINAL_CREDIT_TIP));
        originalDate = new SimpleDateFormat("dd/MM/yyyy").parse(savedInstanceState.getString(ORIGINAL_DATE));

    }

    private void saveOriginalIncomeValues() {
        // Only save values if you do not have a new income
        if (!mIsNewIncome) {
            originalHoursWorked = incomeInfo.getHoursWorked();
            originalHourlyRate = incomeInfo.getHourlyWage();
            originalCashTip = incomeInfo.getCashTip();
            originalCreditTip = incomeInfo.getCreditTip();
            originalDate = incomeInfo.getDate();
        }
    }

    private void readDisplayStateValues() {
        // Get the intent passed into the activity
        Intent intent = getIntent();
        // Get the income id passed into the intent
        mIncomeId = intent.getIntExtra(INCOME_ID, ID_NOT_SET);
        // If the Income id is not set, create a new income
        mIsNewIncome = mIncomeId == ID_NOT_SET;
        if (mIsNewIncome) {
            createNewIncome();
        }
    }

    private void createNewIncome() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOURLY_RATE, "");
        values.put(COLUMN_HOURS_WORKED, "");
        values.put(COLUMN_CASH_TIPS, "");
        values.put(COLUMN_CREDIT_TIPS, "");

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        mIncomeId = (int)db.insert(JobInfoEntry.TABLE_INCOME, null, values);
    }

    private void saveIncomeToDatabase(String hoursWorked, String hourlyRate, String creditTips, String cashTips, String date) {
        //Create selection criteria
        String selection = JobInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mIncomeId)};

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOURS_WORKED, hoursWorked);
        values.put(COLUMN_HOURLY_RATE, hourlyRate);
        values.put(COLUMN_CASH_TIPS, cashTips);
        values.put(COLUMN_CREDIT_TIPS, creditTips);
        values.put(JobInfoEntry.COLUMN_DATE, date);

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {

                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
                db.update(JobInfoEntry.TABLE_INCOME, values, selection, selectionArgs);
                return null;
            }
        };
        task.loadInBackground();
    }

    private void deleteIncomeFromDatabase() {
        // Create Selection Criteria
        String selection = JobInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mIncomeId)};

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                //Delete method
                db.delete(JobInfoEntry.TABLE_INCOME, selection, selectionArgs);
                return null;
            }
        };
        task.loadInBackground();
    }

    private void storePreviousIncomeValues() {
        incomeInfo.setHourlyWage(originalHourlyRate);
        incomeInfo.setHoursWorked(originalHoursWorked);
        incomeInfo.setCashTip(originalCashTip);
        incomeInfo.setCreditTip(originalCreditTip);
        incomeInfo.setDate(originalDate);
    }

    private void saveIncome() {
        String hoursWorked = hoursWorkedText.getText().toString();
        String hourlyRate = hourlyRateText.getText().toString();
        String creditTip = creditTipText.getText().toString();
        String cashTip = cashTipText.getText().toString();
        String date = dateButton.getText().toString();

        // Write to the Database
        saveIncomeToDatabase(hoursWorked, hourlyRate, creditTip, cashTip, date);
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
        CursorLoader loader = null;
        if (id == LOADER_INCOME) {
            loader = createLoaderIncome();
        }
        return loader;
    }

    private CursorLoader createLoaderIncome() {
        return new CursorLoader(this) {
            @Override
            public Cursor loadInBackground() {
                // Open a connection to the database
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
                // Build the selection criteria. In this case, you want to set the ID of the Income to the passed-in game id from the Intent.
                String selection = JobsDatabaseContract.JobInfoEntry._ID + " = ?";
                String[] selectionArgs = {Integer.toString(mIncomeId)};
                // Create a list of the columns you are pulling from the database.
                String[] incomeColumns = {
                        COLUMN_HOURLY_RATE,
                        COLUMN_HOURS_WORKED,
                        COLUMN_CASH_TIPS,
                        COLUMN_CREDIT_TIPS,
                        COLUMN_DATE,
                };
                return db.query(JobInfoEntry.TABLE_INCOME, incomeColumns, selection, selectionArgs, null, null, null);
            }
        };
    }

    protected void onPause() {
        super.onPause();
        if (mIsCancelling) {
            if (mIsNewIncome) {
                deleteIncomeFromDatabase();
            } else {
                storePreviousIncomeValues();
            }
        } else {
            saveIncome();
        }
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}