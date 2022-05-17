package edu.cvtc.servicesolutions.tip_tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;

import edu.cvtc.servicesolutions.tip_tracker.DatabaseContract.InfoEntry;

public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeRecyclerAdapter.ViewHolder> {
    // Member Variables
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;

    private int mIdPosition;
    private int mOriginalHoursWorkedPosition;
    private int mOriginalHourlyRatePosition;
    private int mOriginalCashTipPosition;
    private int mOriginalCreditTipPosition;
    private int mOriginalDatePosition;

    public IncomeRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);

        // Used to get the positions of the columns we
        // are interested in
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if(mCursor != null) {
            // get column indexes
            mIdPosition = mCursor.getColumnIndex(InfoEntry._ID);
            mOriginalHoursWorkedPosition = mCursor.getColumnIndex(InfoEntry.COLUMN_HOURS_WORKED);
            mOriginalHourlyRatePosition = mCursor.getColumnIndex(InfoEntry.COLUMN_HOURLY_RATE);
            mOriginalCashTipPosition = mCursor.getColumnIndex(InfoEntry.COLUMN_CASH_TIPS);
            mOriginalCreditTipPosition = mCursor.getColumnIndex(InfoEntry.COLUMN_CREDIT_TIPS);
            mOriginalDatePosition = mCursor.getColumnIndex(InfoEntry.COLUMN_DATE);
        }
    }

    public void changeCursor(Cursor cursor) {
        // if the cursor is open, close it
        if (mCursor != null) {
            mCursor.close();
        }

        // Create a new cursor based upon
        // the object passed in
        mCursor = cursor;

        // Get the positions of the columns in
        // your cursor.
        populateColumnPositions();

        // Tell the activity that the data set
        // has changed.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.income_by_day_list, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Move the cursor to the correct row
        mCursor.moveToPosition(position);
        // get the actual values
        int id = mCursor.getInt(mIdPosition);
        double originalHoursWorked = mCursor.getDouble(mOriginalHoursWorkedPosition);
        double originalHourlyRate = mCursor.getDouble(mOriginalHourlyRatePosition);
        double originalCashTip = mCursor.getDouble(mOriginalCashTipPosition);
        double originalCreditTip = mCursor.getDouble(mOriginalCreditTipPosition);
        double calculatedTotalIncome = ((originalHoursWorked * originalHourlyRate) + originalCashTip + originalCreditTip);
        String originalDate = mCursor.getString(mOriginalDatePosition);

        // pass the info
        holder.mId = id;
        holder.mOriginalHoursWorked.setText(String.format("%.2f", originalHoursWorked));
        holder.mOriginalHourlyRate.setText(MessageFormat.format("${0}", String.format("%.2f", originalHourlyRate)));
        holder.mOriginalCashTip.setText(MessageFormat.format("${0}", String.format("%.2f", originalCashTip)));
        holder.mOriginalCreditTip.setText(MessageFormat.format("${0}", String.format("%.2f", originalCreditTip)));
        holder.mOriginalDate.setText(originalDate);
        holder.totalIncome.setText(MessageFormat.format("${0}", String.format("%.2f", calculatedTotalIncome)));
    }

    @Override
    public int getItemCount() {
        // if the cursor is null, return 0. Otherwise
        // return the count of records
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Member variables for inner class
        public int mId;
        public final TextView mOriginalHoursWorked;
        public final TextView mOriginalHourlyRate;
        public final TextView mOriginalCashTip;
        public final TextView mOriginalCreditTip;
        public final TextView mOriginalDate;
        public final TextView totalIncome;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mOriginalHoursWorked = itemView.findViewById(R.id.income_hours_worked);
            mOriginalHourlyRate = itemView.findViewById(R.id.income_hourly_rate);
            mOriginalCashTip = itemView.findViewById(R.id.income_cash_tip);
            mOriginalCreditTip = itemView.findViewById(R.id.income_credit_tip);
            mOriginalDate = itemView.findViewById(R.id.income_tip_date);
            totalIncome = itemView.findViewById(R.id.rV_total_output);
        }
    }
}
