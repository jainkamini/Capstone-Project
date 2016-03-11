package ivan.capstone.com.capstone;

/**
 * Created by Ivan on 16/02/2016.
 */
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.tagmanager.TagManager;
import com.google.android.gms.tagmanager.ContainerHolder;
import java.io.IOException;


public class MyApplication extends Application {

    public Tracker mTracker;
    private static Context mContext;
    public ContainerHolder mContainerHolder;
    public TagManager mTagManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // Get the Tag Manager
    public TagManager getTagManager () {
        if (mTagManager == null) {
            // create the TagManager, save it in mTagManager
            mTagManager = TagManager.getInstance(this);
        }
        return mTagManager;
    }


    // Set the ContainerHolder
    public void setContainerHolder (ContainerHolder containerHolder) {
        mContainerHolder = containerHolder;
    }

    // Get the ContainerHolder
    public ContainerHolder getContainerHolder() {
        return mContainerHolder;
    }


    // Get the tracker associated with this app
    public void startTracking() {

        // Initialize an Analytics tracker using a Google Analytics property ID.

        // Does the Tracker already exist?
        // If not, create it

        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);

            // Get the config data for the tracker
            mTracker = ga.newTracker(R.xml.track_app);

            // Enable tracking of activities
            ga.enableAutoActivityReports(this);

            // Set the log level to verbose.
            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }


    public Tracker getTracker() {
        // Make sure the tracker exists
        startTracking();

        // Then return the tracker
        return mTracker;
    }

}
