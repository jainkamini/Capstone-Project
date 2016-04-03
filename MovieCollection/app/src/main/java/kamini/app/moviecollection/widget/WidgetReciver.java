package kamini.app.moviecollection.widget;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kamini.app.moviecollection.R;
import kamini.app.moviecollection.data.MovieContract;

/**
 * Created by Kamini on 4/1/2016.
 */
public class WidgetReciver extends BroadcastReceiver  {
    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
            MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,


    };
    // these indices must match the projection

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_NAME = 1;
    public static final int INDEX_MOVIE_DATE = 2;
    public static final int INDEX_MOVIE_BACKDROP = 3;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(MovieWidgetProvider.ACTION_TOAST)){


            RemoteViews views;
            ComponentName watchWidget;

            views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);
            watchWidget = new ComponentName(context, MovieWidgetProvider.class);


           /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);*/


            Long mMovieid= Long.parseLong(String.valueOf(intent.getExtras().getInt(MovieWidgetProvider.EXTRA_MOVIEID)));




            Log.e(MovieWidgetProvider.LOG_TAG,"widgetdata2 :"+mMovieid);
            Uri movieUri=  MovieContract.MovieEntry.buildMovieUri(mMovieid);



            Cursor data =
                    context. getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(mMovieid), MOVIE_COLUMNS,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " <> " + mMovieid, null,null);
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
            InputStream input;
            try {
                input = new URL("http://image.tmdb.org/t/p/w185"+mMoviePath).openStream();
                Bitmap bit = BitmapFactory.decodeStream(input);
                views.setImageViewBitmap(R.id.img_background, bit);
                //   views.setInt(R.id.layout_widget,"setBackgroundResource", bit.);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(MovieWidgetProvider.LOG_TAG, "widgetdata3:" + mMovieId);


            views.setTextViewText(R.id.txt_moviename, mMovieName);
            views.setTextViewText(R.id.txt_moviedate, mMoviedate);

            views.setOnClickPendingIntent(R.id.btn_next, MovieWidgetProvider. getPendingSelfIntent(context, MovieWidgetProvider.ACTION_TOAST, MovieWidgetProvider.EXTRA_MOVIEID, mMovieId));
           MovieWidgetProvider.pushWidgetUpdate(context ,views);
        }
    }
}
