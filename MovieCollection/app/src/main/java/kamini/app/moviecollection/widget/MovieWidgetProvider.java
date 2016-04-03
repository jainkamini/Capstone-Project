package kamini.app.moviecollection.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kamini.app.moviecollection.R;
import kamini.app.moviecollection.data.MovieContract;

/**
 * Created by Kamini on 3/29/2016.
 */
public class MovieWidgetProvider extends AppWidgetProvider {

    public static final String LOG_TAG = MovieWidgetProvider.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED =
            "kamini.app.moviecollection.ACTION_DATA_UPDATED";
    public static final String ACTION_DATA_STARTED =
            "kamini.app.moviecollection.ACTION_DATA_STARTED ";
    public static final String EXTRA_MOVIEID =
            "kamini.app.moviecollection.EXTRA_MOVIEID ";

    public static final String ACTION_TOAST = "kamini.app.moviecollection.ACTION_TOAST";
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
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);



            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);






            Uri movieUri=  MovieContract.MovieEntry.buildMovieStatus("U");

            Cursor data =
                    context.  getContentResolver().query(movieUri, MOVIE_COLUMNS, null,
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




                // Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + mCursor.getString(MovieLoader.Query.poster_path)).into(holder.imgposter);

                InputStream input;
                try {
                    input = new URL("http://image.tmdb.org/t/p/w185"+mMoviePath).openStream();
                    Bitmap bit = BitmapFactory.decodeStream(input);
                    views.setImageViewBitmap(R.id.img_background, bit);
                    //   views.setInt(R.id.layout_widget,"setBackgroundResource", bit.);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            Log.e(LOG_TAG,"widgetdata1:"+mMovieId);
                //         views.setImageViewResource(R.id.layout_widget,R.drawable.ic_delete_black);
//setBackgroundResource
                //  views.setInt(R.id.layout_widget,"setBackgroundColor", android.graphics.Color.BLACK);
                views.setTextViewText(R.id.txt_moviename, mMovieName);
                views.setTextViewText(R.id.txt_moviedate, mMoviedate);



           views.setOnClickPendingIntent(R.id.btn_next, getPendingSelfIntent(context,MovieWidgetProvider.ACTION_TOAST,MovieWidgetProvider.EXTRA_MOVIEID,mMovieId));
            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
      //  pushWidgetUpdate(context, views);

        }

        }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
      //  context.startService(new Intent(context, MovieWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
       if (ACTION_TOAST.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views;
            ComponentName watchWidget;

            views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);
            watchWidget = new ComponentName(context, MovieWidgetProvider.class);


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);


            Long mMovieid= Long.parseLong(String.valueOf(intent.getExtras().getInt(MovieWidgetProvider.EXTRA_MOVIEID)));




            Log.e(LOG_TAG,"widgetdata2 :"+mMovieid);
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
            Log.e(LOG_TAG, "widgetdata3:" + mMovieId);


            views.setTextViewText(R.id.txt_moviename, mMovieName);
            views.setTextViewText(R.id.txt_moviedate, mMoviedate);

            views.setOnClickPendingIntent(R.id.btn_next, getPendingSelfIntent(context, MovieWidgetProvider.ACTION_TOAST, MovieWidgetProvider.EXTRA_MOVIEID, mMovieId));
            appWidgetManager.updateAppWidget(watchWidget, views);

        }

    }
    public static PendingIntent getPendingSelfIntent(Context context, String action,String extra,int mMovieid) {
        Intent intent = new Intent();
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putInt(extra,
                mMovieid);
        intent.putExtras(bundle);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

   /* protected PendingIntent getPendingSelfIntent(Context context, String action,String extra,int mMovieid) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putInt(extra,
                mMovieid);
        intent.putExtras(bundle);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }*/
   /* public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, MovieWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }*/

}