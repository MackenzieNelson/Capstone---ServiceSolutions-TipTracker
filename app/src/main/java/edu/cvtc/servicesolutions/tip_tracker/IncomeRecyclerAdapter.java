package edu.cvtc.servicesolutions.tip_tracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;

public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeRecyclerAdapter.ViewHolder> {

    // Member Variables
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;

    private int mDatePosition;
    private int mCashTipsPosition;
    private int mCreditTipsPosition;
    private int mHourlyRatePosition;
    private int mHoursWorkedPosition;
    private int mIdPosition;

    public IncomeRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);

        // used to get the positions of the columns we want
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if (mCursor != null) {
            // get column indexes from mCursor
            mDatePosition = mCursor.getColumnIndex(InfoEntry.COLUMN_DATE);
            mCashTipsPosition = mCursor.getColumnIndex(InfoEntry.COLUMN_CASH_TIPS);
            mCreditTipsPosition = mCursor.getColumnIndex(InfoEntry.COLUMN_CREDIT_TIPS);
            mHourlyRatePosition = mCursor.getColumnIndex(InfoEntry.COLUMN_HOURLY_RATE);
            mHoursWorkedPosition = mCursor.getColumnIndex(InfoEntry.COLUMN_HOURS_WORKED);
            mIdPosition = mCursor.getColumnIndex(InfoEntry._ID);
        }
    }

    public void changeCursor(Cursor cursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = cursor;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.job_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        //Date incomeDate = mCursor.getDate(mDatePosition);
        double cashTips = mCursor.getDouble(mCashTipsPosition);
        double creditTips = mCursor.getDouble(mCreditTipsPosition);
        double hourlyRate = mCursor.getDouble(mHourlyRatePosition);
        double hoursWorked = mCursor.getDouble(mHoursWorkedPosition);
        int id = mCursor.getInt(mIdPosition);

        holder.mWorkDate.setText(incomeDate);

    }
}
