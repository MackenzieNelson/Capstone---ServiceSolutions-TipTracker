package edu.cvtc.servicesolutions.tip_tracker;

import static edu.cvtc.servicesolutions.tip_tracker.IncomeActivity.LOADER_INCOME;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BudgetActivity extends AppCompatActivity {

    private OpenHelper mDbOpenHelper;
    public static final int LOADER_EXPENSES = 0;
    public List<IncomeInfo> income = IncomeDataManager.getInstance().getIncome();
    private TextView incomeOutput;
    private TextView currentMonth;
    private String currentMonthString;
    private double totalIncome = 0;
    double hourlyAmount = 0;
    double tipsAmount = 0;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    Date currentTime = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        mDbOpenHelper = new OpenHelper(this);
        IncomeDataManager.loadFromDatabase(mDbOpenHelper);

        for (int i = 0; i < income.size(); i++) {
            // Calander uses 0-11 for months instead on 1-12
            int currentMonth = (currentTime.getMonth()) + 1;
            String date = income.get(i).getDate();
            currentMonthString = getMonthFormat(currentMonth);
            if (date != null) {
                if (date.contains(currentMonthString)) {
                    double hoursWorked = income.get(i).getHoursWorked();
                    double hourlyRate = income.get(i).getHourlyWage();
                    double cashAmount = income.get(i).getCashTip();
                    double creditAmount = income.get(i).getCreditTip();
                    tipsAmount = cashAmount + creditAmount;
                    hourlyAmount = hoursWorked * hourlyRate;
                    totalIncome += tipsAmount + hourlyAmount;
                }
            }
        }

        incomeOutput = findViewById(R.id.monthly_average_output);
        currentMonth = findViewById(R.id.current_month);
        currentMonth.setText(currentMonthString);
        incomeOutput.setText("$" + (totalIncome));
        updateChart();
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
        getMenuInflater().inflate(R.menu.menu_budget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_track_tips) {
            Intent intent = new Intent(BudgetActivity.this, IncomeActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_income_calc) {
            Intent intent = new Intent(BudgetActivity.this, IncomeCalculationsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(BudgetActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(BudgetActivity.this, JobActivityMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_record_tips) {
            Intent intent = new Intent(BudgetActivity.this, TipRecordActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateChart() {
        // Update the text in a center of the chart:
        TextView numberOfCals = findViewById(R.id.fraction);
        numberOfCals.setText(String.valueOf(tipsAmount) + " / " + totalIncome);

        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        double d = (double) tipsAmount / (double) totalIncome;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
    }
}
