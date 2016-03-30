package kamini.app.moviecollection.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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


            int layoutId;
            layoutId = R.layout.widget_movie;
            // Add the data to the RemoteViews
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);
            InputStream input;
            try {
                input = new URL(mMoviePath).openStream();
                Bitmap bit = BitmapFactory.decodeStream(input);
                views.setImageViewBitmap(R.id.layout_widget, bit);
            } catch (IOException e) {
                e.printStackTrace();
            }


            views.setTextViewText(R.id.txt_moviename, mMovieName);
            views.setTextViewText(R.id.txt_moviedate, mMoviedate);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }
}