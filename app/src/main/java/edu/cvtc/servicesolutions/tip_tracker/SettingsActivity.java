package edu.cvtc.servicesolutions.tip_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize variable
        SwitchCompat switchBtn = findViewById(R.id.switchTheme);

        // switch theme mode per user wants
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    buttonView.setText("Night Mode");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    buttonView.setText("Light Mode");
                }

            }
        });

        // is nightmode on?
        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        switchBtn.setChecked(isNightModeOn);

        if (isNightModeOn) {
            switchBtn.setText("Night Mode");
        } else {
            switchBtn.setText("Light Mode");
        }


        // Deleting table data
        // https://www.youtube.com/watch?v=neaCUaHa2Ek

        // Variable declaration
        Context context = SettingsActivity.this;
        int duration = Toast.LENGTH_SHORT;
        OpenHelper myDb = new OpenHelper(this);

        // Creating button variables
        Button btnDeleteJobs = (Button) findViewById(R.id.button_delete_jobs);
        Button btnDeleteTips = (Button)findViewById(R.id.button_delete_tips);
        Button btnDeleteExpenses = (Button)findViewById(R.id.button_delete_expenses);

        // Assigning buttons
        btnDeleteJobs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDb.deleteTable(1);
                        Toast toast = Toast.makeText(context, "The Job Table has been deleted!", duration);
                        toast.show();
                        Log.d("ABC", "Works");
                    }
                }
        );
        btnDeleteTips.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDb.deleteTable(2);
                        Toast toast = Toast.makeText(context, "The Tip Table has been deleted!", duration);
                        toast.show();
                        Log.d("ABC", "Works");
                    }
                }
        );
        btnDeleteExpenses.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDb.deleteTable(3);
                        Toast toast = Toast.makeText(context, "The Expense Table has been deleted!", duration);
                        toast.show();
                        Log.d("ABC", "Works");
                    }

                }

        );

    }

    // remove eye flickers

    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}