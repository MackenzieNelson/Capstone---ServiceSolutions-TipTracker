package edu.cvtc.servicesolutions.tip_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class JobMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_menu);
    }


    public void openIncomeLog(View view) {
        Intent intent = new Intent(this, IncomeActivity.class);
        startActivity(intent);
    }

    public void showAmountEarned(View view) {
        Intent intent = new Intent(this, IncomeCalculationsActivity.class);
        startActivity(intent);

    }

    public void openViewSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openBudget(View view) {
        Intent intent = new Intent(this, BudgetActivity.class);
        startActivity(intent);
    }
}