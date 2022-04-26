package edu.cvtc.servicesolutions.tip_tracker;

import android.os.Parcel;
import android.os.Parcelable;

public class JobInfo implements Parcelable {
    // Member Attributes
    private String mJobTitle;
    private String mJobDescription;
    private int mId;

    public JobInfo(String jobTitle, String jobDescription) {
        mJobTitle = jobTitle;
        mJobDescription = jobDescription;
    }

    public JobInfo(int id, String jobTitle, String jobDescription) {
        mId = id;
        mJobTitle = jobTitle;
       mJobDescription = jobDescription;
    }

    // Get and Set

    public int getmId() {
        return mId;
    }

    public String getmJobTitle() {
        return mJobTitle;
    }

    public void setmJobTitle(String mJobTitle) {
        this.mJobTitle = mJobTitle;
    }

    public String getmJobDescription() {
        return mJobDescription;
    }

    public void setmJobDescription(String mJobDescription) {
        this.mJobDescription = mJobDescription;
    }

    private String getCompareKey() { return mJobTitle + "|" + mJobDescription; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobInfo that = (JobInfo) o;
        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() { return getCompareKey().hashCode(); }

    @Override
    public String toString() { return getCompareKey(); }

    protected JobInfo(Parcel parcel) {
        setmJobTitle(parcel.readString());
        setmJobDescription(parcel.readString());
    }

    public static final Creator<JobInfo> CREATOR = new Creator<JobInfo>() {
        @Override
        public JobInfo createFromParcel(Parcel parcel) {
            return new JobInfo(parcel);
        }

        @Override
        public JobInfo[] newArray(int size) { return new JobInfo[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mJobTitle);
        parcel.writeString(mJobDescription);
    }
}
