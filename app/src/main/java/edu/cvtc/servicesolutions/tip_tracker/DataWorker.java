package edu.cvtc.servicesolutions.tip_tracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;

public class DataWorker {

    // Member Attributes
    private SQLiteDatabase mDb;

    public DataWorker(SQLiteDatabase db) { mDb = db; }

    private void insertJob(String jobTitle, String jobDescription) {
        ContentValues values = new ContentValues();
        values.put(InfoEntry.COLUMN_JOB_TITLE, jobTitle);
        values.put(InfoEntry.COLUMN_JOB_DESCRIPTION, jobDescription);

        long newRowId = mDb.insert(InfoEntry.TABLE_JOB, null, values);
    }

    public void insertJobs() {
        insertJob("Server/Bartender", "Typical FOH Duties");
    }

    private void insertIncome(double hourlyRate, double hoursWorked, double cashTips, double creditTips, String date) {
        ContentValues values = new ContentValues();
        values.put(InfoEntry.COLUMN_HOURLY_RATE, hourlyRate);
        values.put(InfoEntry.COLUMN_HOURS_WORKED, hoursWorked);
        values.put(InfoEntry.COLUMN_CASH_TIPS, cashTips);
        values.put(InfoEntry.COLUMN_CREDIT_TIPS, creditTips);
        values.put(InfoEntry.COLUMN_DATE, String.valueOf(date));


        long newRowId = mDb.insert(InfoEntry.TABLE_INCOME, null, values);
    }

    public void insertIncomes() {
        insertIncome(2.33, 25, 180, 100, "MAY 08 2022");
    }

    private void insertExpense(String expenseName, String expenseAmount) {
        ContentValues values = new ContentValues();
        values.put(InfoEntry.COLUMN_EXPENSE_NAME, expenseName);
        values.put(InfoEntry.COLUMN_EXPENSE_AMOUNT, expenseAmount);

        long newRowId = mDb.insert(InfoEntry.TABLE_EXPENSES, null, values);
    }

    public void insertExpenses() {
        insertJob("Server/Bartender", "Typical FOH Duties");
    }
}
