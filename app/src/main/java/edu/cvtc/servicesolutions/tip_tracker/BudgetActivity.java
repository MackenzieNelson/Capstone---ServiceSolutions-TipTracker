package edu.cvtc.servicesolutions.tip_tracker;

import static edu.cvtc.servicesolutions.tip_tracker.IncomeActivity.LOADER_INCOME;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
                    double hourlyAmount = hoursWorked * hourlyRate;
                    totalIncome += cashAmount + creditAmount + hourlyAmount;
                }
            }
        }

        incomeOutput = findViewById(R.id.monthly_average_output);
        currentMonth = findViewById(R.id.current_month);
        currentMonth.setText(currentMonthString);
        incomeOutput.setText((String.valueOf(totalIncome)));

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

}
