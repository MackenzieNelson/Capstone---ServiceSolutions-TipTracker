package edu.cvtc.servicesolutions.tip_tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {

    // Constants
    public static final String DATABASE_NAME = "ServerSolutions.db";
    private static final int DATABASE_VERSION = 1;

    // Variable declaration
    private String table_name = null;


    public OpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the three tables
        db.execSQL(DatabaseContract.InfoEntry.SQL_CREATE_TABLE_JOBS);
        db.execSQL(DatabaseContract.InfoEntry.SQL_CREATE_TABLE_EXPENSES);
        db.execSQL(DatabaseContract.InfoEntry.SQL_CREATE_TABLE_INCOME);
        db.execSQL(InfoEntry.SQL_CREATE_INDEX1);

        DataWorker worker = new DataWorker(db);
        worker.insertJobs();
        worker.insertIncomes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void deleteTable(Integer tableValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        switch (tableValue) {
            case 1:
                table_name = "job_info";
                break;
            case 2:
                table_name = "income";
                break;
            case 3:
                table_name = "expenses";
                break;
        }

        // https://stackoverflow.com/a/9599808
        db.execSQL("delete from "+ table_name);
    }
}
