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

import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private OpenHelper mDbOpenHelper;
    public static final int LOADER_EXPENSES = 0;
    public List<IncomeInfo> income = IncomeDataManager.getInstance().getmIncome();
    private TextView averageIncomeOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
        Log.d("abc", String.valueOf(income));
        averageIncomeOutput = findViewById(R.id.monthly_average_output);



    }
}
