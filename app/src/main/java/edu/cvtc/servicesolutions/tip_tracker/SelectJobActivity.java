package edu.cvtc.servicesolutions.tip_tracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.cvtc.servicesolutions.tip_tracker.R;
import edu.cvtc.servicesolutions.tip_tracker.databinding.ActivitySelectJobBinding;

public class SelectJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_job);

        Toolbar jobToolbar = (Toolbar) findViewById(R.id.add_job_toolbar);
        jobToolbar.setTitle("Add a Job");
        jobToolbar.setNavigationIcon(R.drawable.ic_add_job);
        setSupportActionBar(jobToolbar);

        jobToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startAddJob();
                }
        });
    }

    private void startAddJob() {
        Intent intent = new Intent(SelectJobActivity.this, JobActivity.class);
        startActivity(intent);
    }
}