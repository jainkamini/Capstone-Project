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
import android.support.annotation.NonNull;
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


    public static final String ACTION_DATA_UPDATED =
            "kamini.app.moviecollection.ACTION_DATA_UPDATED";
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
       context.startService(new Intent(context, MovieWidgetIntentService.class));

        /*int layoutId;
        layoutId = R.layout.widget_movie;*/
        // Add the data to the RemoteViews
        ComponentName watchWidget;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);

       watchWidget = new ComponentName(context, MovieWidgetProvider.class);

        views.setOnClickPendingIntent(R.id.btn_next, getPendingSelfIntent(context, ACTION_DATA_UPDATED));
        appWidgetManager.updateAppWidget(watchWidget, views);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, MovieWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_DATA_UPDATED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views;
            ComponentName watchWidget;

            views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);
            watchWidget = new ComponentName(context, MovieWidgetProvider.class);

            Uri movieUri=  MovieContract.MovieEntry.buildMovieUri(12345);

            Cursor data =
                    context. getContentResolver().query(movieUri, MOVIE_COLUMNS, null,
                            new String[]{"123456"}, null);
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

            //         views.setImageViewResource(R.id.layout_widget,R.drawable.ic_delete_black);
//setBackgroundResource
            //  views.setInt(R.id.layout_widget,"setBackgroundColor", android.graphics.Color.BLACK);
            views.setTextViewText(R.id.txt_moviename, mMovieName);
            views.setTextViewText(R.id.txt_moviedate, mMoviedate);

            appWidgetManager.updateAppWidget(watchWidget, views);

        }
        else {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views;
            ComponentName watchWidget;

            views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);
            watchWidget = new ComponentName(context, MovieWidgetProvider.class);

            Uri movieUri = MovieContract.MovieEntry.buildMovieStatus("U");

            Cursor data =
                    context.getContentResolver().query(movieUri, MOVIE_COLUMNS, null,
                            new String[]{"U"}, null);
            if (data == null) {
                return;
            }
            if (!data.moveToFirst()) {
                data.close();
                return;
            }

            // Extract the Movie data from the Cursor

            int mMovieId = data.getInt(INDEX_MOVIE_ID);
            String mMovieName = data.getString(INDEX_MOVIE_NAME);
            String mMoviePath = data.getString(INDEX_MOVIE_BACKDROP);
            String mMoviedate = data.getString(INDEX_MOVIE_DATE);
            InputStream input;
            try {
                input = new URL("http://image.tmdb.org/t/p/w185" + mMoviePath).openStream();
                Bitmap bit = BitmapFactory.decodeStream(input);
                views.setImageViewBitmap(R.id.img_background, bit);
                //   views.setInt(R.id.layout_widget,"setBackgroundResource", bit.);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //         views.setImageViewResource(R.id.layout_widget,R.drawable.ic_delete_black);
//setBackgroundResource
            //  views.setInt(R.id.layout_widget,"setBackgroundColor", android.graphics.Color.BLACK);
            views.setTextViewText(R.id.txt_moviename, mMovieName);
            views.setTextViewText(R.id.txt_moviedate, mMoviedate);

            appWidgetManager.updateAppWidget(watchWidget, views);
            //    }
       /* if (SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, TodayWidgetIntentService.class));
        }*/
        }
    }
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}