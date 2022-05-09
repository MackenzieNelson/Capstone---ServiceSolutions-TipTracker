package edu.cvtc.servicesolutions.tip_tracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;

public class IncomeDataManager {

    // Member Attributes
    private static IncomeDataManager ourInstance = null;
    private List<IncomeInfo> mIncome = new ArrayList<>();

    public static IncomeDataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new IncomeDataManager();
        }
        return ourInstance;
    }

    public List<IncomeInfo> getmIncome() { return mIncome; }

    private static void loadIncomeFromDatabase(Cursor cursor){
        int listHourlyRatePosition = cursor.getColumnIndex(InfoEntry.COLUMN_HOURLY_RATE);
        int listHoursWorkedPosition = cursor.getColumnIndex(InfoEntry.COLUMN_HOURS_WORKED);
        int listCashTipsPosition = cursor.getColumnIndex(InfoEntry.COLUMN_CASH_TIPS);
        int listCreditTipsPosition = cursor.getColumnIndex(InfoEntry.COLUMN_CREDIT_TIPS);
        int listDatePosition = cursor.getColumnIndex(InfoEntry.COLUMN_DATE);
        int idPosition = cursor.getColumnIndex(InfoEntry._ID);

        IncomeDataManager dm = getInstance();
        dm.mIncome.clear();

        while (cursor.moveToNext()) {
            double listHourlyRate = cursor.getDouble(listHourlyRatePosition);
            double listHoursWorked = cursor.getDouble(listHoursWorkedPosition);
            double listCashTips = cursor.getDouble(listCashTipsPosition);
            double listCreditTips = cursor.getDouble(listCreditTipsPosition);
            String listDate = cursor.getString(listDatePosition);
            int id = cursor.getInt(idPosition);


            IncomeInfo list = new IncomeInfo(id, listHourlyRate, listHoursWorked, listCashTips, listCreditTips, listDate);

            dm.mIncome.add(list);
        }
        cursor.close();
    }

    public static void loadFromDatabase(OpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] incomeColumns = {
                InfoEntry.COLUMN_HOURLY_RATE,
                InfoEntry.COLUMN_HOURS_WORKED,
                InfoEntry.COLUMN_CASH_TIPS,
                InfoEntry.COLUMN_CREDIT_TIPS,
                InfoEntry.COLUMN_DATE,
                InfoEntry._ID };

        String incomeOrderBy = InfoEntry.COLUMN_DATE;

        final Cursor incomeCursor = db.query(InfoEntry.TABLE_INCOME, incomeColumns,
                null, null, null, null, incomeOrderBy);

        loadIncomeFromDatabase(incomeCursor);
    }
}
