package edu.cvtc.servicesolutions.tip_tracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry;

public class JobsDataWorker {

    // Member Attributes
    private SQLiteDatabase mDb;

    public JobsDataWorker(SQLiteDatabase db) { mDb = db; }

    private void insertJob(String jobTitle, String jobDescription) {
        ContentValues values = new ContentValues();
        values.put(JobInfoEntry.COLUMN_JOB_TITLE, jobTitle);
        values.put(JobInfoEntry.COLUMN_JOB_DESCRIPTION, jobDescription);

        long newRowId = mDb.insert(JobInfoEntry.TABLE_NAME, null, values);
    }

    public void insertJobs() {
        insertJob("Server/Bartender", "Typical FOH Duties");
    }

    private void insertIncome(double hourlyRate, double hoursWorked, double cashTip, double creditTip, Date date) {
        ContentValues values = new ContentValues();
        values.put(JobInfoEntry.COLUMN_HOURLY_RATE, hourlyRate);
        values.put(JobInfoEntry.COLUMN_HOURS_WORKED, hoursWorked);
        values.put(JobInfoEntry.COLUMN_CASH_TIPS, cashTip);
        values.put(JobInfoEntry.COLUMN_CREDIT_TIPS, creditTip);
        //TODO: Figure out how to use DATE data type in database
        //values.put(JobInfoEntry.COLUMN_DATE, date);


        long newRowId = mDb.insert(JobInfoEntry.TABLE_INCOME, null, values);
    }

    public void insertIncomes() {
        insertJob("Server/Bartender", "Typical FOH Duties");
    }

    private void insertExpense(String expenseName, String expenseAmount) {
        ContentValues values = new ContentValues();
        values.put(JobInfoEntry.COLUMN_EXPENSE_NAME, expenseName);
        values.put(JobInfoEntry.COLUMN_EXPENSE_AMOUNT, expenseAmount);

        long newRowId = mDb.insert(JobInfoEntry.TABLE_EXPENSES, null, values);
    }

    public void insertExpenses() {
        insertJob("Server/Bartender", "Typical FOH Duties");
    }
}
