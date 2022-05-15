package edu.cvtc.servicesolutions.tip_tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;

public class IncomeCalculationsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants
    public static final int ITEM_INCOME = 0;
    public static final int LOADER_INCOME = 0;
    
    // Member Variables
    private OpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mIncomeLayoutManager;
    private IncomeRecyclerAdapter mIncomeRecyclerAdapter;

    public List<IncomeInfo> income = IncomeDataManager.getInstance().getIncome();
    private String currentMonthString;
    private TextView weeklyIncomeOutput;
    private TextView monthlyIncomeOutput;
    private TextView yearlyIncomeOutput;
    private ToggleButton toggleButton;
    Calendar currentTime = Calendar.getInstance();
    int dayOfWeek;
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMM dd yyyy").toFormatter(Locale.ENGLISH);
    
    // Boolean to check if the 'onCreateLoader' method has run
    private boolean mIsCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_calculations);
        toggleButton = findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View view) {
                calculateIncome();
            }
        });

        mDbOpenHelper = new OpenHelper(this);
        initializeDisplayContent();

        weeklyIncomeOutput = findViewById(R.id.weekly_income_output);
        monthlyIncomeOutput = findViewById(R.id.monthly_income_output);
        yearlyIncomeOutput = findViewById(R.id.yearly_income_output);

        calculateIncome();
    }

    private void initializeDisplayContent() {
        // Retrieve the information from your database
        IncomeDataManager.loadFromDatabase(mDbOpenHelper);
        
        // Set a reference to list of jobs layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_income_by_day);
        mIncomeLayoutManager = new LinearLayoutManager(this);
        
        // No cursor, so pass null
        mIncomeRecyclerAdapter = new IncomeRecyclerAdapter(this, null);
        
        // Display Income
        displayIncome();
    }

    @SuppressLint("DefaultLocale")
    private void calculateIncome() {
        double totalWeeklyIncome = 0;
        double totalMonthlyIncome = 0;
        double totalYearlyIncome = 0;
        for (int i = 0; i < income.size(); i++) {
            // Calender uses 0-11 for months instead on 1-12
            int currentMonth = (currentTime.getTime().getMonth()) + 1;
            int currentYear = currentTime.getTime().getYear() + 1900;
            String date = income.get(i).getDate();
            currentMonthString = getMonthFormat(currentMonth);
//            dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);
//            String dayOfWeekString = getDayFormat(dayOfWeek);
            LocalDate dateDate = LocalDate.parse(date, formatter);
            int numberOfMonth = dateDate.getDayOfMonth();
            int currentNumberOfMonth = currentTime.getTime().getDate();
//            DayOfWeek incomeDayOfWeek = dateDate.getDayOfWeek();
            if (date != null) {
                double hoursWorked = income.get(i).getHoursWorked();
                double hourlyRate = income.get(i).getHourlyWage();
                double cashAmount = income.get(i).getCashTip();
                double creditAmount = income.get(i).getCreditTip();
                double hourlyAmount = hoursWorked * hourlyRate;
                if (toggleButton.isChecked()) {
                    if ((numberOfMonth - (currentNumberOfMonth - 1) < 8)) {
                        totalWeeklyIncome += cashAmount + creditAmount;
                    }
                    if (date.contains(currentMonthString)) {
                        totalMonthlyIncome += cashAmount + creditAmount;
                    }
                    if (date.contains(String.valueOf(currentYear))) {
                        totalYearlyIncome += cashAmount + creditAmount;
                    }
                } else {
                    if ((numberOfMonth - (currentNumberOfMonth - 1) < 8)) {
                        totalWeeklyIncome += cashAmount + creditAmount + hourlyAmount;
                    }
                    if (date.contains(currentMonthString)) {
                        totalMonthlyIncome += cashAmount + creditAmount + hourlyAmount;
                    }
                    if (date.contains(String.valueOf(currentYear))) {
                        totalYearlyIncome += cashAmount + creditAmount + hourlyAmount;
                    }
                }
            }
        }
        weeklyIncomeOutput.setText(MessageFormat.format("${0}", String.format("%.2f", totalWeeklyIncome)));
        monthlyIncomeOutput.setText(MessageFormat.format("${0}", String.format("%.2f", totalMonthlyIncome)));
        yearlyIncomeOutput.setText(MessageFormat.format("${0}", String.format("%.2f", totalYearlyIncome)));
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

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        // default should never need it
        return "JAN";
    }

    private String getDayFormat(int day) {
        if(day == 1)
            return "SUNDAY";
        if(day == 2)
            return "MONDAY";
        if(day == 3)
            return "TUESDAY";
        if(day == 4)
            return "WEDNESDAY";
        if(day == 5)
            return "THURSDAY";
        if(day == 6)
            return "FRIDAY";
        if(day == 7)
            return "SATURDAY";
        // default should never need it
        return "MONDAY";
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
                            InfoEntry.COLUMN_HOURLY_RATE,
                            InfoEntry.COLUMN_HOURS_WORKED,
                            InfoEntry.COLUMN_CASH_TIPS,
                            InfoEntry.COLUMN_CREDIT_TIPS,
                            InfoEntry.COLUMN_DATE,
                            InfoEntry._ID };

                    // create an order by field for sorting
                    String incomeOrderBy = InfoEntry.COLUMN_DATE;

                    // populate cursor with results
                    return db.query(InfoEntry.TABLE_INCOME,incomeColumns,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_income_calc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_track_tips) {
            Intent intent = new Intent(IncomeCalculationsActivity.this, IncomeActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_budget) {
            Intent intent = new Intent(IncomeCalculationsActivity.this, BudgetActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(IncomeCalculationsActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(IncomeCalculationsActivity.this, JobActivityMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_record_tips) {
            Intent intent = new Intent(IncomeCalculationsActivity.this, TipRecordActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
