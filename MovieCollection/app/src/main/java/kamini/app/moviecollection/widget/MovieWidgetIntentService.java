package kamini.app.moviecollection.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kamini.app.moviecollection.R;
import kamini.app.moviecollection.data.MovieContract;

/**
 * Created by Kamini on 3/29/2016.
 */

/**
 * IntentService which handles updating all Movie widgets with the latest data
 */
public class MovieWidgetIntentService extends IntentService {



    private static final String[] MOVIE_COLUMNS = {
               MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
            MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,


    };
    // these indices must match the projection

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_NAME = 1;
    public static final int INDEX_MOVIE_DATE = 2;
    public static final int INDEX_MOVIE_BACKDROP = 3;

    public MovieWidgetIntentService() {
        super("MovieWidgetIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the mOVIE widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                MovieWidgetProvider.class));
//featch data for upcoming movie
        Uri movieUri=  MovieContract.MovieEntry.buildMovieStatus("U");

        Cursor data =
                getContentResolver().query(movieUri, MOVIE_COLUMNS, null,
                        new String[]{"U"}, null);
        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        // Extract the Movie data from the Cursor

        int mMovieId=data.getInt(INDEX_MOVIE_ID);
        String mMovieName=data.getString(INDEX_MOVIE_NAME);
        String mMoviePath=data.getString(INDEX_MOVIE_BACKDROP);
        String mMoviedate=data.getString(INDEX_MOVIE_DATE);
        for (int appWidgetId : appWidgetIds) {
           /* int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);

            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_movie_default_width);
            int largeWidth = getResources().getDimensionPixelSize(R.dimen.widget_movie_large_width);
            int layoutId;
            if (widgetWidth >= largeWidth) {
                layoutId = R.layout.widget_movie_large;
            } else if (widgetWidth >= defaultWidth) {
                layoutId = R.layout.widget_movie;
            } else {
                layoutId = R.layout.widget_movie_small;
            }*/

            int layoutId;
            layoutId = R.layout.widget_movie;
            // Add the data to the RemoteViews
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);
           // Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + mCursor.getString(MovieLoader.Query.poster_path)).into(holder.imgposter);

            InputStream input;
            try {
                input = new URL("http://image.tmdb.org/t/p/w185"+mMoviePath).openStream();
                Bitmap bit = BitmapFactory.decodeStream(input);
                views.setImageViewBitmap(R.id.layout_widget, bit);
            } catch (IOException e) {
                e.printStackTrace();
            }

   //         views.setImageViewResource(R.id.layout_widget,R.drawable.ic_delete_black);

            views.setTextViewText(R.id.txt_moviename, mMovieName);
            views.setTextViewText(R.id.txt_moviedate, mMoviedate);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_movie_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_movie_default_width);
    }

}