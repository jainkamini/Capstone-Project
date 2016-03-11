package ivan.capstone.com.capstone.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.bumptech.glide.request.target.AppWidgetTarget;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ivan.capstone.com.capstone.Data.SeriesContract;
import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.R;

/**
 * Created by Ivan on 09/03/2016.
 */
public class SeriesWidgetService extends RemoteViewsService {

    private int id_widget;
    private static final int ID_INDEX = 0;

    private static final String[] SERIES_COLUMNS = {
            SeriesContract.SeriesEntry._ID,
            SeriesContract.SeriesEntry.COLUMN_ID,
            SeriesContract.SeriesEntry.COLUMN_NAME,
            SeriesContract.SeriesEntry.COLUMN_RATING,
            SeriesContract.SeriesEntry.COLUMN_VOTES,
            SeriesContract.SeriesEntry.COLUMN_BANNER_URL,
            SeriesContract.SeriesEntry.COLUMN_POSTER_URL,
            SeriesContract.SeriesEntry.COLUMN_REALSED_DATE,
            SeriesContract.SeriesEntry.COLUMN_OVERVIEW,
            SeriesContract.SeriesEntry.COLUMN_GENRE,
            SeriesContract.SeriesEntry.COLUMN_NETWORK
    };
    static final int COL__ID = 0;
    static final int COL_ID = 1;
    static final int COL_NAME = 2;
    static final int COL_RATING = 3;
    static final int COL_VOTES = 4;
    static final int COL_BANNER = 5;
    static final int COL_POSTER = 6;
    static final int COL_RELEASED_DATE = 7;
    static final int COL_OVERVIEW = 8;
    static final int COL_GENRE = 9;
    static final int COL_NETWORK = 10;


    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        id_widget = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
        return new CollectionSeriesWidgetService();
    }

    class CollectionSeriesWidgetService implements
            RemoteViewsService.RemoteViewsFactory {


        private Cursor data = null;

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (data != null) {
                data.close();
            }

            final long token = Binder.clearCallingIdentity();
            try {
                String sortOrder = SeriesContract.SeriesEntry.COLUMN_NAME + " ASC";
                Uri serieUri = SeriesContract.SeriesEntry.CONTENT_URI;
                data = getContentResolver().query(
                        serieUri,
                        SERIES_COLUMNS,
                        null,
                        null,
                        sortOrder);
            } finally {
                Binder.restoreCallingIdentity(token);
            }

        }



        @Override
        public RemoteViews getViewAt(int position) {

            if (position == AdapterView.INVALID_POSITION ||
                    data == null || !data.moveToPosition(position)) {
                return null;
            }
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_list_myseries_widget);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("EXTRA_ITEM", position);
            remoteViews.setOnClickFillInIntent(R.id.linearlayaout_widget, fillInIntent);

            Serie mySerie = new Serie();
            mySerie.set_id(data.getLong(COL__ID));
            mySerie.setId(data.getString(COL_ID));
            mySerie.setName(data.getString(COL_NAME));
            mySerie.setRating(data.getString(COL_RATING));
            mySerie.setVotes(data.getString(COL_VOTES));
            mySerie.setImage_url(data.getString(COL_BANNER));
            mySerie.setPoster_url(data.getString(COL_POSTER));
            mySerie.setDateReleased(data.getString(COL_RELEASED_DATE));
            mySerie.setOverView(data.getString(COL_OVERVIEW));
            mySerie.setGenre(data.getString(COL_GENRE));
            mySerie.setNetwork(data.getString(COL_NETWORK));


            remoteViews.setTextViewText(R.id.name_serie, mySerie.getName());
            remoteViews.setTextViewText(R.id.date_serie, mySerie.getDateReleased());
            remoteViews.setTextViewText(R.id.network_serie, mySerie.getNetwork());
           // remoteViews.setImageViewUri(R.id.image_serie, mySerie.getPoster_url())

            /*appWidgetTarget = new AppWidgetTarget( MyApplication.getContext(), remoteViews, R.id.image_serie, id_widget );
            Glide
                    .with(SeriesWidgetService.this) // safer!
                    .load(mySerie.getPoster_url())
                    .asBitmap()
                    .into(appWidgetTarget);*/

            InputStream input;
            try {
                input = new URL(mySerie.getPoster_url()).openStream();
                Bitmap bit = BitmapFactory.decodeStream(input);
                remoteViews.setImageViewBitmap(R.id.image_serie, bit);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return remoteViews;

        }



        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }


        @Override
        public int getCount() {
            return data.getCount();
        }





        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            if (data.moveToPosition(i)){
                return data.getLong(ID_INDEX);
            }
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
