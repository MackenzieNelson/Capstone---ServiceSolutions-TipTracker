package edu.cvtc.servicesolutions.tip_tracker;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class JobActivityViewModel extends ViewModel {
    // Constants
    public static final String ORIGINAL_JOB_TITLE = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_JOB_TITLE";
    public static final String ORIGINAL_JOB_DESCRIPTION = "edu.cvtc.servicesolutions.tip_tracker.ORIGINAL_JOB_DESCRIPTION";

    // Member Variable
    public String mOriginalJobTitle;
    public String mOriginalJobDescription;
    public boolean mIsNewlyCreated = true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_JOB_TITLE, mOriginalJobTitle);
        outState.putString(ORIGINAL_JOB_DESCRIPTION, mOriginalJobDescription);
    }

    public void restoreState(Bundle inState) {
        mOriginalJobTitle = inState.getString(ORIGINAL_JOB_TITLE);
        mOriginalJobDescription = inState.getString(ORIGINAL_JOB_DESCRIPTION);
    }
}
