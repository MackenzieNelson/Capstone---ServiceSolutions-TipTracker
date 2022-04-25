package edu.cvtc.servicesolutions.tip_tracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cvtc.servicesolutions.tip_tracker.JobsDatabaseContract.JobInfoEntry;
import kotlinx.coroutines.Job;


public class JobRecyclerAdapter extends RecyclerView.Adapter<JobRecyclerAdapter.ViewHolder> {

    // Member variables
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;

    private int mJobTitlePosition;
    private int mJobDescriptionPosition;
    private int mIdPosition;

    public JobRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);

        // Used to get the positions of the columns we
        // are interested in
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if (mCursor != null) {
            // get column indexes from mCursor
            mJobTitlePosition = mCursor.getColumnIndex(JobInfoEntry.COLUMN_JOB_TITLE);
            mJobDescriptionPosition = mCursor.getColumnIndex(JobInfoEntry.COLUMN_JOB_DESCRIPTION);
            mIdPosition = mCursor.getColumnIndex(JobInfoEntry._ID);
        }
    }

    public void changeCursor(Cursor cursor) {
        // If the cursor is open, close it
        if (mCursor != null) {
            mCursor.close();
        }

        // Create a new cursor based upon
        // the object passed in.
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
        View itemView = mLayoutInflater.inflate(R.layout.job_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Move the cursor to the correct row
        mCursor.moveToPosition(position);

        // get the actual values
        String jobTitle = mCursor.getString(mJobTitlePosition);
        String jobDescription = mCursor.getString(mJobDescriptionPosition);
        int id = mCursor.getInt(mIdPosition);

        // pass the info
        holder.mJobTitle.setText(jobTitle);
        holder.mJobDescription.setText(jobDescription);
        holder.mId = id;
    }

    @Override
    public int getItemCount() {
        // if the cursor is null, return 0. Otherwise
        // return the count of records
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Member variables for inner class
        public final TextView mJobTitle;
        public final TextView mJobDescription;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mJobTitle = (TextView) itemView.findViewById(R.id.job_title);
            mJobDescription = (TextView) itemView.findViewById(R.id.job_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, JobActivity.class);
                    intent.putExtra(JobActivity.JOB_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
